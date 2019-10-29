/*
 * Copyright (C) 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom.mixin;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.wurstclient.zoom.WiZoom;

@SuppressWarnings("deprecation")
@Mixin(EntityRenderer.class)
public class EntityRendererMixin implements IResourceManagerReloadListener
{
	@Redirect(
		at = @At(value = "FIELD",
			target = "Lnet/minecraft/client/settings/GameSettings;fovSetting:F",
			opcode = Opcodes.GETFIELD,
			ordinal = 0),
		method = {"getFOVModifier(FZ)F"})
	private float getFov(GameSettings settings)
	{
		return WiZoom.INSTANCE.changeFovBasedOnZoom(settings.fovSetting);
	}
	
	@Shadow
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		
	}
}
