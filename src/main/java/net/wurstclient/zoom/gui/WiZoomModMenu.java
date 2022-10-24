package net.wurstclient.zoom.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class WiZoomModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new ConfigScreen(new ConfigGUI(screen)) {
            @Override
            public void close() {
                this.client.setScreen(screen);
            }
        };
    }
}