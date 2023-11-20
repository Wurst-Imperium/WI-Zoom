package net.wurstclient.zoom.config;

import eu.midnightdust.lib.config.MidnightConfig;

/**
 * This is the config menu for WI Zoom
 * It won't be loaded without <a href="https://modrinth.com/mod/midnightlib">MidnightLib</a>
 * @see <a href="https://github.com/TeamMidnightDust/MidnightLib/blob/architectury/MidnightConfigExample.java">GitHub|MidnightConfigExample.java</a>
 * To use the config menu UI, <a href="https://modrinth.com/mod/modmenu">Mod Menu</a> is required to be installed.
 * */
public class WiZoomConfig extends MidnightConfig {

    @Entry(min = 25, max = 100, isSlider = true)
    public static int maxZoom = 50;

    @Entry(min = 0.65D, max = 1D, isSlider = true)
    public static double minZoom = 1.0;

    @Entry(min = 1D, max = 10D, isSlider = true)
    public static double mouseScrollZoomSensitivity = 2D;

    @Entry
    public static boolean showFov = false;

}
