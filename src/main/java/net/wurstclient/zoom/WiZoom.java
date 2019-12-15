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
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public enum WiZoom
{
	INSTANCE;
	
	public static final String VERSION = "1.0.1";
	
	private FabricKeyBinding zoomKey;
	
	public void initialize()
	{
		System.out.println("Starting WI Zoom...");
		
		zoomKey =
			FabricKeyBinding.Builder.create(new Identifier("wi-zoom", "zoom"),
				InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "WI Zoom").build();
		
		KeyBindingRegistry.INSTANCE.register(zoomKey);
	}
	
	private final double defaultLevel = 3;
	private Double currentLevel;
	
	public double changeFovBasedOnZoom(double fov)
	{
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(!zoomKey.isPressed())
		{
			currentLevel = defaultLevel;
			return fov;
		}
		
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
