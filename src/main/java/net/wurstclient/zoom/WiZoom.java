/*
 * Copyright (c) 2019-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom;

import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;

import static net.wurstclient.zoom.WiZoomInitializer.getCfgMaxZoom;
import static net.wurstclient.zoom.WiZoomInitializer.getCfgMinZoom;
import static net.wurstclient.zoom.WiZoomInitializer.getCfgMouseScrollZoomSensitivity;
import static net.wurstclient.zoom.WiZoomInitializer.getCfgShowFov;
import static net.wurstclient.zoom.WiZoomInitializer.LOGGER;

public enum WiZoom {
    INSTANCE;

    public static final MinecraftClient MC = MinecraftClient.getInstance();
    private final double defaultLevel = 3;
    private KeyBinding zoomKey;
    private Double currentLevel;   // zoom level factor
    private Double defaultMouseSensitivity;

    public void initialize() {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        ModContainer modContainer =
                fabricLoader.getModContainer("wi_zoom").get();
        Version version = modContainer.getMetadata().getVersion();

        LOGGER.info("Starting WI Zoom v" + version.getFriendlyString());

        zoomKey = new KeyBinding("key.wi_zoom.zoom", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V, "WI Zoom");
        KeyBindingHelper.registerKeyBinding(zoomKey);
    }

    public double changeFovBasedOnZoom(double fov) {
        SimpleOption<Double> mouseSensitivitySetting =
                MC.options.getMouseSensitivity();

        if (currentLevel == null)
            currentLevel = defaultLevel;

        if (!zoomKey.isPressed() || isAdventureOrSurvival()) {  // forbid zooming in survival mode or adventure mode
            currentLevel = defaultLevel;

            if (defaultMouseSensitivity != null) {
                mouseSensitivitySetting.setValue(defaultMouseSensitivity);
                defaultMouseSensitivity = null;
            }

            return fov;
        }

        if (defaultMouseSensitivity == null)
            defaultMouseSensitivity = mouseSensitivitySetting.getValue();

        // Adjust mouse sensitivity in relation to zoom level.
        mouseSensitivitySetting
                .setValue(defaultMouseSensitivity * (1.0 / currentLevel));
        if (MC.player != null && getCfgShowFov())
            MC.player.sendMessage(Text.of("§bFOV: §f" + (fov / currentLevel) + " deg"), true);
        return fov / currentLevel;
    }

    public void onMouseScroll(double amount) {
        if (!zoomKey.isPressed())
            return;

        if (currentLevel == null)
            currentLevel = defaultLevel;

        if (isAdventureOrSurvival())
            MC.player.sendMessage(Text.of("§cYou can only zoom in creative or spectator mode."), true);

        double scrollFactor = 1 + getCfgMouseScrollZoomSensitivity() / 20D;
        if (amount > 0)
            currentLevel *= scrollFactor;
        else if (amount < 0)
            currentLevel /= scrollFactor;
        currentLevel = MathHelper.clamp(currentLevel, getCfgMinZoom(), getCfgMaxZoom());
    }

    public KeyBinding getZoomKey() {
        return zoomKey;
    }

    /**
     * Checks if the player is in creative or spectator mode.
     * To forbid zooming in survival mode or adventure mode, we define the zoom action is <b>CHEAT</b>.
     *
     * @return <code>true</code> if the player is in creative or spectator mode,
     * <code>false</code> otherwise
     */
    private boolean isAdventureOrSurvival() {
        return MC.player != null && !(MC.player.isCreative() || MC.player.isSpectator());
    }
}
