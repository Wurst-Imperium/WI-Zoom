/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.zoom.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.render.GameRenderer;
import net.wimods.zoom.WiZoom;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements AutoCloseable
{
	@ModifyReturnValue(at = @At("RETURN"),
		method = "getFov(Lnet/minecraft/client/render/Camera;FZ)F")
	private float onGetFov(float original)
	{
		return WiZoom.INSTANCE.changeFovBasedOnZoom(original);
	}
}
