/*
 * Copyright (C) 2019 - 2020 | Alexander01998 | All rights reserved.
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

import net.minecraft.client.Mouse;
import net.wurstclient.zoom.WiZoom;

@Mixin(Mouse.class)
public class MouseMixin
{
	@Inject(at = {@At("RETURN")}, method = {"onMouseScroll(JDD)V"})
	private void onOnMouseScroll(long long_1, double double_1, double double_2,
		CallbackInfo ci)
	{
		WiZoom.INSTANCE.onMouseScroll(double_2);
	}
}
