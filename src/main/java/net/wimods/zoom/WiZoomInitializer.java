/*
 * Copyright (c) 2019-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.zoom;

import net.fabricmc.api.ModInitializer;

public final class WiZoomInitializer implements ModInitializer
{
	private static boolean initialized;

	@Override
	public void onInitialize()
	{
        if(initialized)
			throw new RuntimeException("WiZoomInitializer.onInitialize() ran twice!");

        ZoomConfig.load();
        WiZoom.INSTANCE.initialize();

		initialized = true;
	}
}
