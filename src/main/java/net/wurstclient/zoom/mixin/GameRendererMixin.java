/*
 * Copyright (C) 2019 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.wurstclient.zoom.WiZoom;

@Mixin(GameRenderer.class)
public class GameRendererMixin
	implements AutoCloseable, SynchronousResourceReloader
{
	@Inject(at = @At(value = "RETURN", ordinal = 1),
		method = {"getFov(Lnet/minecraft/client/render/Camera;FZ)D"},
		cancellable = true)
	private void onGetFov(Camera camera, float tickDelta, boolean changingFov,
		CallbackInfoReturnable<Double> cir)
	{
		cir.setReturnValue(
			WiZoom.INSTANCE.changeFovBasedOnZoom(cir.getReturnValueD()));
	}
	
	@Shadow
	@Override
	public void reload(ResourceManager var1)
	{
		
	}
	
	@Shadow
	@Override
	public void close() throws Exception
	{
		
	}
}
