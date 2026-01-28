/*
 * Copyright (c) 2019-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.zoom;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public enum WiZoom
{
	INSTANCE;
	
	public static final Minecraft MC = Minecraft.getInstance();
	
	private KeyMapping zoomKey;
	private final double defaultLevel = 3;
	private Double currentLevel;
	private Double defaultMouseSensitivity;
	
	public void initialize()
	{
		FabricLoader fabricLoader = FabricLoader.getInstance();
		ModContainer modContainer =
			fabricLoader.getModContainer("wi_zoom").get();
		Version version = modContainer.getMetadata().getVersion();
		System.out.println("Starting WI Zoom v" + version.getFriendlyString());
		
		Category zoomCategory = KeyMapping.Category
			.register(Identifier.fromNamespaceAndPath("wi_zoom", "wi_zoom"));
		zoomKey = new KeyMapping("key.wi_zoom.zoom", InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_V, zoomCategory);
		KeyBindingHelper.registerKeyBinding(zoomKey);
	}

    private void updateZoomText()
    {
        if(currentLevel == null)
            return;

        if(MC.player == null)
            return;

        if(ZoomConfig.INSTANCE.actionBarMode == ActionBarMode.DISABLED)
            return;

        String msg;

        if(ZoomConfig.INSTANCE.actionBarMode == ActionBarMode.PERCENT)
        {
            int pct = (int) Math.round(currentLevel * 100.0);
            msg = "Zoom " + pct + "%";
        }
        else
        {
            int zoom = (int) Math.round(currentLevel);
            msg = "Zoom " + zoom + "x";
        }


        MC.gui.setOverlayMessage(net.minecraft.network.chat.Component.literal(msg), false);
    }

    public float changeFovBasedOnZoom(float fov)
	{
		OptionInstance<Double> mouseSensitivitySetting =
			MC.options.sensitivity();
		
		if(currentLevel == null)

			currentLevel = defaultLevel;
		
		if(!zoomKey.isDown())
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

        updateZoomText();
		return (float)(fov / currentLevel);
	}
	
	public void onMouseScroll(double amount)
	{
        if(!zoomKey.isDown() && amount != 0)
            return;

        if(currentLevel == null)
			currentLevel = defaultLevel;
		
		if(amount > 0)
			currentLevel *= 1.1;
		else if(amount < 0)
			currentLevel /= 1.1;

        currentLevel = Mth.clamp(currentLevel, 1, ZoomConfig.INSTANCE.maxZoom);
        updateZoomText();

    }

	public KeyMapping getZoomKey()
	{
		return zoomKey;
	}
}
