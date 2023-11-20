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
import org.slf4j.Logger;
import java.lang.reflect.InvocationTargetException;

import static org.slf4j.LoggerFactory.getLogger;

public final class WiZoomInitializer implements ModInitializer {

    public static final String MOD_ID = "wi_zoom";
    public static final Logger LOGGER = getLogger(MOD_ID);
    private static boolean initialized;

    /**
     * Gets the config class for WiZoom.
     * This method is invoked by {@link WiZoomInitializer#getCfgMaxZoom()}, {@link WiZoomInitializer#getCfgMinZoom()}
     * and {@link WiZoomInitializer#getCfgMouseScrollZoomSensitivity()}, and these methods will be used inside event loop
     * in {@link WiZoom#onMouseScroll(double)}
     *
     * @return The config class or <code>null</code> if
     * @author Jaffe2718
     * @see net.wurstclient.zoom.config.WiZoomConfig
     * @since 1.5.1-MC1.20.2
     * @author Jaffe2718
     */
    private static @Nullable Class<?> getConfigClass() {
        try {
            Class.forName("eu.midnightdust.lib.config.MidnightConfig");   // Check if MidnightConfig is loaded
            return Class.forName("net.wurstclient.zoom.config.WiZoomConfig");
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    /**
     * Gets the maximum zoom level that can be set in the config.
     *
     * @return The maximum zoom level, 50 for default config or if MidnightConfig is not loaded
     * @author Jaffe2718
     * @see net.wurstclient.zoom.config.WiZoomConfig#maxZoom
     * @since 1.5.1-MC1.20.2
     * @author Jaffe2718
     */
    public static int getCfgMaxZoom() {
        Class<?> configClass = getConfigClass();
        if (configClass == null) return 50;
        else {
            try {
                return (int) configClass.getField("maxZoom").get(null);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
                return 50;
            }
        }
    }

    /**
     * Gets the minimum zoom level that can be set in the config.
     *
     * @return The minimum zoom level, 1.0 for default config or if MidnightConfig is not loaded
     * @author Jaffe2718
     * @see net.wurstclient.zoom.config.WiZoomConfig#minZoom
     * @since 1.5.1-MC1.20.2
     * @author Jaffe2718
     */
    public static double getCfgMinZoom() {
        Class<?> configClass = getConfigClass();
        if (configClass == null) return 1.0;
        else {
            try {
                return (double) configClass.getField("minZoom").get(null);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
                return 1.0;
            }
        }
    }

    /**
     * Gets the mouse scroll sensitivity that can be set in the config.
     * ScrollFactor = 1 + MouseScrollSensitivity / 20
     *
     * @return The mouse scroll sensitivity, 2.0 for default config or if MidnightConfig is not loaded
     * @see net.wurstclient.zoom.config.WiZoomConfig#mouseScrollZoomSensitivity
     * @see net.wurstclient.zoom.WiZoom#onMouseScroll(double)
     * @since 1.5.1-MC1.20.2
     * @author Jaffe2718
     */
    public static double getCfgMouseScrollZoomSensitivity() {
        Class<?> configClass = getConfigClass();
        if (configClass == null) return 2;
        else {
            try {
                return (double) configClass.getField("mouseScrollSensitivity").get(null);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
                return 2D;
            }
        }
    }

    /**
     * Gets whether to show the current FOV in the config.
     *
     * @return Whether to show the current FOV, false for default config or if MidnightConfig is not loaded
     * @see net.wurstclient.zoom.config.WiZoomConfig#showFov
     * @since 1.6.1-rc.1-MC1.20.2
     * @author Jaffe2718
     */
    public static boolean getCfgShowFov() {
        Class<?> configClass = getConfigClass();
        if (configClass == null) return false;
        else {
            try {
                return (boolean) configClass.getField("showFov").get(null);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
                return false;
            }
        }
    }

    @Override
    public void onInitialize() {
        if (initialized)
            throw new RuntimeException(
                    "WiZoomInitializer.onInitialize() ran twice!");

        try {   // @Jaffe2718: Initialize MidnightConfig for WiZoom
            Class<?> configBaseClass = Class.forName("eu.midnightdust.lib.config.MidnightConfig");
            configBaseClass
                    .getMethod("init", String.class, Class.class)
                    .invoke(null, "wi_zoom", WiZoomInitializer.getConfigClass());
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
            LOGGER.warn("WiZoomInitializer.onInitialize(): MidnightLib not found. Configuration UI will not be available.");
        }

        WiZoom.INSTANCE.initialize();
        initialized = true;
    }
}
