/*
 * Copyright (C) 2019 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = WiZoomInitializer.MODID,
	name = WiZoomInitializer.NAME,
	version = WiZoomInitializer.VERSION)
public final class WiZoomInitializer
{
	private static boolean initialized;
	
	public static final String MODID = "wi-zoom";
	public static final String NAME = "WI Zoom";
	public static final String VERSION = "1.0";
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if(initialized)
			throw new RuntimeException(
				"WiZoomInitializer.onInitialize() ran twice!");
		
		WiZoom.INSTANCE.initialize();
		initialized = true;
	}
}
