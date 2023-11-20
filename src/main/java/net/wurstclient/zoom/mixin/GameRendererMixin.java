/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom.mixin;

import net.minecraft.client.render.GameRenderer;
import net.wurstclient.zoom.WiZoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements AutoCloseable {

	/**
	 * Change the fov based on the zoom level.
	 * @since 1.6-MC1.20.2
	 * @author Jaffe2718
	 * {@link net.minecraft.client.render.GameRenderer#renderWorld}
	 * */
	@ModifyVariable(method = "renderWorld", at = @At(value = "STORE"), ordinal = 0)
	private double injectD(double d) {
		return WiZoom.INSTANCE.changeFovBasedOnZoom(d);
	}

}
