/*
 * Copyright (C) 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public enum WiZoom
{
	INSTANCE;
	
	public static final String VERSION = "1.0.1";
	
	private FabricKeyBinding zoomKey;
	private final double defaultLevel = 3;
	private Double currentLevel;
	private Double defaultMouseSensitivity;
	
	public void initialize()
	{
		System.out.println("Starting WI Zoom...");
		
		zoomKey =
			FabricKeyBinding.Builder.create(new Identifier("wi-zoom", "zoom"),
				InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "WI Zoom").build();
		
		KeyBindingRegistry.INSTANCE.register(zoomKey);
	}
	
	public double changeFovBasedOnZoom(double fov)
	{
		GameOptions gameOptions = MinecraftClient.getInstance().options;
		
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(!zoomKey.isPressed())
		{
			currentLevel = defaultLevel;
			
			if(defaultMouseSensitivity != null)
			{
				gameOptions.mouseSensitivity = defaultMouseSensitivity;
				defaultMouseSensitivity = null;
			}
			
			return fov;
		}
		
		if(defaultMouseSensitivity == null)
			defaultMouseSensitivity = gameOptions.mouseSensitivity;
			
		// Adjust mouse sensitivity in relation to zoom level.
		// (fov / currentLevel) / fov is a value between 0.02 (50x zoom)
		// and 1 (no zoom).
		gameOptions.mouseSensitivity =
			defaultMouseSensitivity * (fov / currentLevel / fov);
		
		return fov / currentLevel;
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
	
	public FabricKeyBinding getZoomKey()
	{
		return zoomKey;
	}
}
