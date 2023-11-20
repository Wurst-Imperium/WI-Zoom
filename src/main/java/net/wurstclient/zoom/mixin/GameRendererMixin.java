/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom.mixin;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.wurstclient.zoom.WiZoom;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements AutoCloseable
{
	@Shadow @Final private LightmapTextureManager lightmapTextureManager;
	@Shadow @Final MinecraftClient client;
	@Shadow public abstract void updateTargetedEntity(float tickDelta);
	@Shadow protected abstract boolean shouldRenderBlockOutline();
	@Shadow @Final private Camera camera;
	@Shadow private float viewDistance;
	@Shadow private int ticks;
	@Shadow private boolean renderHand;
	@Shadow protected abstract double getFov(Camera camera, float tickDelta, boolean changingFov);
	@Shadow public abstract Matrix4f getBasicProjectionMatrix(double fov);
	@Shadow protected abstract void tiltViewWhenHurt(MatrixStack matrices, float tickDelta);
	@Shadow protected abstract void bobView(MatrixStack matrices, float tickDelta);
	@Shadow public abstract void loadProjectionMatrix(Matrix4f projectionMatrix);
	@Shadow protected abstract void renderHand(MatrixStack matrices, Camera camera, float tickDelta);


	/**
	 * @author Jaffe2718
	 * @reason Zoom the field of view without affecting the first view hand zoom
	 */
	@Overwrite
	public void renderWorld(float tickDelta, long limitTime, MatrixStack matrices) {
		this.lightmapTextureManager.update(tickDelta);
		if (this.client.getCameraEntity() == null) {
			this.client.setCameraEntity(this.client.player);
		}
		this.updateTargetedEntity(tickDelta);
		this.client.getProfiler().push("center");
		boolean bl = this.shouldRenderBlockOutline();
		this.client.getProfiler().swap("camera");
		Camera camera = this.camera;
		this.viewDistance = this.client.options.getClampedViewDistance() * 16;
		MatrixStack matrixStack = new MatrixStack();
		double d = WiZoom.INSTANCE.changeFovBasedOnZoom(this.getFov(camera, tickDelta, true));
		matrixStack.multiplyPositionMatrix(this.getBasicProjectionMatrix(d));
		this.tiltViewWhenHurt(matrixStack, tickDelta);
		if (this.client.options.getBobView().getValue()) {
			this.bobView(matrixStack, tickDelta);
		}
		float f = this.client.options.getDistortionEffectScale().getValue().floatValue();
		float g = MathHelper.lerp(tickDelta, this.client.player.prevNauseaIntensity, this.client.player.nauseaIntensity) * (f * f);
		if (g > 0.0f) {
			int i = this.client.player.hasStatusEffect(StatusEffects.NAUSEA) ? 7 : 20;
			float h = 5.0f / (g * g + 5.0f) - g * 0.04f;
			h *= h;
			RotationAxis rotationAxis = RotationAxis.of(new Vector3f(0.0f, MathHelper.SQUARE_ROOT_OF_TWO / 2.0f, MathHelper.SQUARE_ROOT_OF_TWO / 2.0f));
			matrixStack.multiply(rotationAxis.rotationDegrees(((float)this.ticks + tickDelta) * (float)i));
			matrixStack.scale(1.0f / h, 1.0f, 1.0f);
			float j = -((float)this.ticks + tickDelta) * (float)i;
			matrixStack.multiply(rotationAxis.rotationDegrees(j));
		}
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		this.loadProjectionMatrix(matrix4f);
		camera.update(this.client.world, this.client.getCameraEntity() == null ? this.client.player : this.client.getCameraEntity(), !this.client.options.getPerspective().isFirstPerson(), this.client.options.getPerspective().isFrontView(), tickDelta);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
		Matrix3f matrix3f = new Matrix3f(matrices.peek().getNormalMatrix()).invert();
		RenderSystem.setInverseViewRotationMatrix(matrix3f);
		this.client.worldRenderer.setupFrustum(matrices, camera.getPos(), this.getBasicProjectionMatrix(Math.max(d, this.client.options.getFov().getValue())));
		this.client.worldRenderer.render(matrices, tickDelta, limitTime, bl, camera, (GameRenderer) (Object)this, this.lightmapTextureManager, matrix4f);
		this.client.getProfiler().swap("hand");
		if (this.renderHand) {
			RenderSystem.clear(GlConst.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
			this.renderHand(matrices, camera, tickDelta);
		}
		this.client.getProfiler().pop();
	}


}
