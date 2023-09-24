/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.util.Mth;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;

public enum WiZoom
{
	INSTANCE;
	
	public static final String MODID = "wi_zoom";
	public static final Minecraft MC = Minecraft.getInstance();
	
	private static final Lazy<KeyMapping> ZOOM_KEY =
		Lazy.of(() -> new KeyMapping("key.wi_zoom.zoom",
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "WI Zoom"));
	
	private final double defaultLevel = 3;
	private Double currentLevel;
	private Double defaultMouseSensitivity;
	
	public void initialize()
	{
		IModFileInfo modInfo = ModList.get().getModFileById(WiZoom.MODID);
		String version = modInfo.versionString();
		System.out.println("Starting WI Zoom v" + version);
	}
	
	public double changeFovBasedOnZoom(double fov)
	{
		OptionInstance<Double> mouseSensitivitySetting =
			MC.options.sensitivity();
		
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(!getZoomKey().isDown())
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
	
	public void onMouseScroll(double amount)
	{
		if(!getZoomKey().isDown())
			return;
		
		if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(amount > 0)
			currentLevel *= 1.1;
		else if(amount < 0)
			currentLevel *= 0.9;
		
		currentLevel = Mth.clamp(currentLevel, 1, 50);
	}
	
	public KeyMapping getZoomKey()
	{
		return ZOOM_KEY.get();
	}
}
