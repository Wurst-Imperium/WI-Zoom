/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(WiZoom.MODID)
@Mod.EventBusSubscriber(modid = WiZoom.MODID,
	bus = Mod.EventBusSubscriber.Bus.MOD,
	value = Dist.CLIENT)
public final class WiZoomInitializer
{
	private static boolean initialized;
	
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		if(initialized)
			throw new RuntimeException(
				"WiZoomInitializer.onClientSetup() ran twice!");
		
		WiZoom.INSTANCE.initialize();
		initialized = true;
	}
	
	@SubscribeEvent
	public static void registerBindings(RegisterKeyMappingsEvent event)
	{
		event.register(WiZoom.INSTANCE.getZoomKey());
	}
}
