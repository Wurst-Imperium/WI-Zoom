/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;

public enum WiZoom
{
	INSTANCE;
	
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	
	private KeyBinding zoomKey;
	private final double defaultLevel = 3;
	private Double currentLevel;
	private Double defaultMouseSensitivity;
	
	public void initialize()
	{
		FabricLoader fabricLoader = FabricLoader.getInstance();
		ModContainer modContainer =
			fabricLoader.getModContainer("wi_zoom").get();
		Version version = modContainer.getMetadata().getVersion();
		System.out.println("Starting WI Zoom v" + version.getFriendlyString());
		
		zoomKey = new KeyBinding("key.wi_zoom.zoom", InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_V, KeyBinding.class_11900.MISC);
		KeyBindingHelper.registerKeyBinding(zoomKey);
	}
	
	public float changeFovBasedOnZoom(float fov)
	{
		SimpleOption<Double> mouseSensitivitySetting =
			MC.options.getMouseSensitivity();
		
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(!zoomKey.isPressed())
		{
			currentLevel = defaultLevel;
			
			if(defaultMouseSensitivity != null)
			{
				mouseSensitivitySetting.setValue(defaultMouseSensitivity);
				defaultMouseSensitivity = null;
			}
			
			return fov;
		}
		
		if(defaultMouseSensitivity == null)
			defaultMouseSensitivity = mouseSensitivitySetting.getValue();
			
		// Adjust mouse sensitivity in relation to zoom level.
		// 1.0 / currentLevel is a value between 0.02 (50x zoom)
		// and 1 (no zoom).
		mouseSensitivitySetting
			.setValue(defaultMouseSensitivity * (1.0 / currentLevel));
		
		return (float)(fov / currentLevel);
	}
	
	public void onMouseScroll(double amount)
	{
		if(!zoomKey.isPressed())
			return;
		
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(amount > 0)
			currentLevel *= 1.1;
		else if(amount < 0)
			currentLevel *= 0.9;
		
		currentLevel = MathHelper.clamp(currentLevel, 1, 50);
	}
	
	public KeyBinding getZoomKey()
	{
		return zoomKey;
	}
}
