/*
 * Copyright (c) 2019-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.zoom.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.Camera;
import net.wimods.zoom.WiZoom;

@Mixin(Camera.class)
public abstract class CameraMixin
{
	/**
	 * Makes the zoom work.
	 */
	@ModifyReturnValue(at = @At("RETURN"), method = "calculateFov(F)F")
	private float onCalculateFov(float original)
	{
		return WiZoom.INSTANCE.changeFovBasedOnZoom(original);
	}
	
	/**
	 * Moves the hand in first person mode out of the way as you zoom in
	 * further.
	 */
	@ModifyReturnValue(at = @At("RETURN"), method = "calculateHudFov(F)F")
	private float onCalculateHudFov(float original)
	{
		return WiZoom.INSTANCE.changeFovBasedOnZoom(original);
	}
}
