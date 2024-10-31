/*
 * Copyright (c) 2019-2024 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.util.Mth;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;

@Mod(WiZoom.MODID)
@EventBusSubscriber(modid = WiZoom.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class WiZoom
{
	public static final String MODID = "wi_zoom";
	private static boolean initialized;
	
	public static final Minecraft MC = Minecraft.getInstance();
	
	public static final Lazy<KeyMapping> zoomKey =
		Lazy.of(() -> new KeyMapping("key.wi_zoom.zoom",
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "WI Zoom"));
	
	private static final double defaultLevel = 3;
	private static Double currentLevel;
	private static Double defaultMouseSensitivity;
	
	public WiZoom(IEventBus modBus, ModContainer container)
	{
		if(initialized)
			throw new RuntimeException("WiZoom constructor ran twice!");
		
		ArtifactVersion version = container.getModInfo().getVersion();
		System.out.println("Starting WI Zoom v" + version.toString());
	}
	
	@SubscribeEvent
	public static void registerKeyBindings(RegisterKeyMappingsEvent event)
	{
		event.register(zoomKey.get());
	}
	
	public static double changeFovBasedOnZoom(double fov)
	{
		OptionInstance<Double> mouseSensitivitySetting =
			MC.options.sensitivity();
		
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(!zoomKey.get().isDown())
		{
			currentLevel = defaultLevel;
			
			if(defaultMouseSensitivity != null)
			{
				mouseSensitivitySetting.set(defaultMouseSensitivity);
				defaultMouseSensitivity = null;
			}
			
			return fov;
		}
		
		if(defaultMouseSensitivity == null)
			defaultMouseSensitivity = mouseSensitivitySetting.get();
			
		// Adjust mouse sensitivity in relation to zoom level.
		// 1.0 / currentLevel is a value between 0.02 (50x zoom)
		// and 1 (no zoom).
		mouseSensitivitySetting
			.set(defaultMouseSensitivity * (1.0 / currentLevel));
		
		return fov / currentLevel;
	}
	
	public static void onMouseScroll(double amount)
	{
		if(!zoomKey.get().isDown())
			return;
		
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(amount > 0)
			currentLevel *= 1.1;
		else if(amount < 0)
			currentLevel *= 0.9;
		
		currentLevel = Mth.clamp(currentLevel, 1, 50);
	}
}
