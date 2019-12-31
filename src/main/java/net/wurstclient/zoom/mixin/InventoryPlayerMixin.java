/*
 * Copyright (C) 2019 | Wurst-Imperium | All rights reserved.
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

import net.minecraft.entity.player.InventoryPlayer;
import net.wurstclient.zoom.WiZoom;

@Mixin(InventoryPlayer.class)
public class InventoryPlayerMixin
{
	@Inject(at = {@At("HEAD")},
		method = {"changeCurrentItem(I)V"},
		cancellable = true)
	private void onChangeCurrentItem(int direction, CallbackInfo ci)
	{
		if(WiZoom.INSTANCE.isZooming())
			ci.cancel();
	}
}
