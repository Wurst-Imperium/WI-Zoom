/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public final class WiZoomInitializer implements ModInitializer
{
	private static boolean initialized;
	
	@Override
	public void onInitialize()
	{
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		if(initialized)
			throw new RuntimeException(
				"WiZoomInitializer.onInitialize() ran twice!");

		try{   // @Jaffe2718: Initialize MidnightConfig for WiZoom
			Class<?> configBaseClass = Class.forName("eu.midnightdust.lib.config.MidnightConfig");
			configBaseClass
					.getMethod("init", String.class, Class.class)
					.invoke(null,"wi_zoom", WiZoomInitializer.getConfigClass());
		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			System.err.println("WiZoomInitializer.onInitialize(): MidnightLib not found. Configuration UI will not be available.");
		}

        WiZoom.INSTANCE.initialize();
		initialized = true;
	}

	/**
	 * Gets the config class for WiZoom.
	 * @return The config class or <code>null</code> if
	 * @author Jaffe2718
	 * @since 1.5.1-MC1.20.2
	 * @see net.wurstclient.zoom.config.WiZoomConfig
	 * */
	private static @Nullable Class<?> getConfigClass()
	{
		try {
			Class.forName("eu.midnightdust.lib.config.MidnightConfig");   // Check if MidnightConfig is loaded
			return Class.forName("net.wurstclient.zoom.config.WiZoomConfig");
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Gets the maximum zoom level that can be set in the config.
	 * @return The maximum zoom level, 50 for default config or if MidnightConfig is not loaded
	 * @since 1.5.1-MC1.20.2
	 * @see net.wurstclient.zoom.config.WiZoomConfig#maxZoom
	 * @author Jaffe2718
	 */
	public static int getCfgMaxZoom() {
		Class<?> configClass = getConfigClass();
		if (configClass == null) return 50;
		else {
			try {
				return (int) configClass.getField("maxZoom").get(null);
			} catch (IllegalAccessException | NoSuchFieldException e) {
				return 50;
			}
		}
	}

	/**
	 * Gets the minimum zoom level that can be set in the config.
	 * @return The minimum zoom level, 1.0 for default config or if MidnightConfig is not loaded
	 * @since 1.5.1-MC1.20.2
	 * @see net.wurstclient.zoom.config.WiZoomConfig#minZoom
	 * @author Jaffe2718
	 * */
	public static double getCfgMinZoom() {
		Class<?> configClass = getConfigClass();
		if (configClass == null) return 1.0;
		else {
			try {
				return (double) configClass.getField("minZoom").get(null);
			} catch (IllegalAccessException | NoSuchFieldException e) {
				return 1.0;
			}
		}
	}
	
	/**
	 * Gets the mouse scroll sensitivity that can be set in the config.
	 * ScrollFactor = 1 + MouseScrollSensitivity / 20
	 * @return The mouse scroll sensitivity, 2.0 for default config or if MidnightConfig is not loaded
	 * @since 1.5.1-MC1.20.2
	 * @see net.wurstclient.zoom.config.WiZoomConfig#mouseScrollZoomSensitivity
	 * @see net.wurstclient.zoom.WiZoom#onMouseScroll(double) 
	 * */
	public static double getCfgMouseScrollZoomSensitivity() {
		Class<?> configClass = getConfigClass();
		if (configClass == null) return 2;
		else {
			try {
				return (double) configClass.getField("mouseScrollSensitivity").get(null);
			} catch (IllegalAccessException | NoSuchFieldException e) {
				return 2D;
			}
		}
	}
}
