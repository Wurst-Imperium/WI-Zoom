/*
 * Copyright (C) 2019 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public enum WiZoom
{
	INSTANCE;
	
	public static final String VERSION = "1.1";
	
	private KeyBinding zoomKey;
	private final float defaultLevel = 3;
	private Float currentLevel;
	private Float defaultMouseSensitivity;
	
	public void initialize()
	{
		System.out.println("Starting WI Zoom...");
		
		zoomKey = new KeyBinding("wi-zoom.zoom", Keyboard.KEY_V, "WI Zoom");
		ClientRegistry.registerKeyBinding(zoomKey);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public float changeFovBasedOnZoom(float fov)
	{
		GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
		
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(!isZooming())
		{
			currentLevel = defaultLevel;
			
			if(defaultMouseSensitivity != null)
			{
				gameSettings.mouseSensitivity = defaultMouseSensitivity;
				defaultMouseSensitivity = null;
			}
			
			return fov;
		}
		
		if(defaultMouseSensitivity == null)
			defaultMouseSensitivity = gameSettings.mouseSensitivity;
			
		// Adjust mouse sensitivity in relation to zoom level.
		// (fov / currentLevel) / fov is a value between 0.02 (50x zoom)
		// and 1 (no zoom).
		gameSettings.mouseSensitivity =
			defaultMouseSensitivity * (fov / currentLevel / fov);
		
		return fov / currentLevel;
	}
	
	@SubscribeEvent
	public void onMouseScroll(InputEvent.MouseInputEvent event)
	{
		if(!isZooming())
			return;
		
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		int amount = Mouse.getDWheel();
		if(amount > 0)
			currentLevel *= 1.1F;
		else if(amount < 0)
			currentLevel *= 0.9F;
		
		currentLevel = MathHelper.clamp(currentLevel, 1, 50);
	}
	
	public boolean isZooming()
	{
		return zoomKey.isKeyDown();
	}
}
