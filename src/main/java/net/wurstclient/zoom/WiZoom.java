/*
 * Copyright (C) 2019 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public enum WiZoom
{
	INSTANCE;
	
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	
	private KeyBinding zoomKey;
	private static double defaultLevel = 3;
	private static Double zoomScrollAmount = 0.1;
	private Double currentLevel;
	private Double defaultMouseSensitivity;
	private static String configPath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "WiZoom.json";
	
	public void initialize()
	{
		FabricLoader fabricLoader = FabricLoader.getInstance();
		ModContainer modContainer =
			fabricLoader.getModContainer("wi_zoom").get();
		Version version = modContainer.getMetadata().getVersion();
		System.out.println("Starting WI Zoom v" + version.getFriendlyString());
		
		zoomKey = new KeyBinding("key.wi_zoom.zoom", InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_V, "WI Zoom");
		KeyBindingHelper.registerKeyBinding(zoomKey);
		loadConfig();
	}
	
	public double changeFovBasedOnZoom(double fov)
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

	//save the changes made to the config
	public static void saveConfig() {
		//convert the configs to a json string
		Gson gson = new Gson();
		String config = gson.toJson(new Config(defaultLevel));
		try {
			//delete the config file if it exists
			File f = new File(configPath);
			if (f.exists()) {
				f.delete();
			}
			//save the config
			FileWriter file = new FileWriter(configPath);
//			file.write(config.replace(",", ",\n"));
			file.write(config);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadConfig() {
		//default config if there is no config file
		Config config = new Config(3);
		//load the config file and replace the default configs
		try {
			//read the contents of the file
			Scanner scanner = new Scanner(new FileReader(configPath));
			String content = "";
			while (scanner.hasNextLine()) {
				content += scanner.nextLine();
			}
			//convert the json string to the config class
			Gson gson = new Gson();
			config = gson.fromJson(content, Config.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//override the configs settings
		defaultLevel = config.defaultLevel;
	}
	
	public KeyBinding getZoomKey()
	{
		return zoomKey;
	}
	public double getDefaultLevel() {return defaultLevel;}
	public void setDefaultLevel(double DefaultLevel) {defaultLevel = DefaultLevel;}
	public Double getcurrentLevel() {return currentLevel;}
}

//class that is just use to store the config options
class Config {
	double defaultLevel = 3;
	Config(double defaultLevel) {
		this.defaultLevel = defaultLevel;
	}
}