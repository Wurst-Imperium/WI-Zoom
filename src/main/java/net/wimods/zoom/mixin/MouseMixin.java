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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerInventory;
import net.wimods.zoom.WiZoom;

@Mixin(Mouse.class)
public class MouseMixin
{
	@Inject(at = @At("RETURN"), method = "onMouseScroll(JDD)V")
	private void onOnMouseScroll(long window, double horizontal,
		double vertical, CallbackInfo ci)
	{
		WiZoom.INSTANCE.onMouseScroll(vertical);
	}
	
	@WrapWithCondition(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/entity/player/PlayerInventory;setSelectedSlot(I)V"),
		method = "onMouseScroll(JDD)V")
	private boolean wrapOnMouseScroll(PlayerInventory inventory, int slot)
	{
		return !WiZoom.INSTANCE.getZoomKey().isPressed();
	}
}
