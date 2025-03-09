/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Inventory;
import net.wurstclient.zoom.WiZoom;

@Mixin(Inventory.class)
public class PlayerInventoryMixin
{
	@Inject(at = @At("HEAD"), method = "swapPaint(D)V", cancellable = true)
	private void onScrollInHotbar(double scrollAmount, CallbackInfo ci)
	{
		if(WiZoom.zoomKey.get().isDown())
			ci.cancel();
	}
}
