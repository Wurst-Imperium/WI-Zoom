package net.wurstclient.zoom.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.wurstclient.zoom.WiZoom;

public class ConfigGUI extends LightweightGuiDescription {
    public ConfigGUI(Screen previous) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(340, 90);
        root.setInsets(Insets.NONE);

        WLabel defaultLevelText = new WLabel(Text.translatable("gui.wi_zoom.label"), 0x000000);
        defaultLevelText.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(defaultLevelText, 1, 1, 5, 1);
        
        WiWLabeledSlider defaultLevelLabelSlider = new WiWLabeledSlider(1, 50, Axis.HORIZONTAL);
        defaultLevelLabelSlider.setValue((int) WiZoom.INSTANCE.getDefaultLevel());
        defaultLevelLabelSlider.setLabel(Text.of(String.valueOf((int) WiZoom.INSTANCE.getDefaultLevel())));
        root.add(defaultLevelLabelSlider, 10, 1, 8, 1);
        
        WButton saveButton = new WButton(Text.translatable("gui.wi_zoom.button"));
        saveButton.setOnClick(() -> {
            WiZoom.INSTANCE.setDefaultLevel(defaultLevelLabelSlider.getValue());
            WiZoom.saveConfig();
            MinecraftClient.getInstance().setScreen(previous);
        });
        saveButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(saveButton, 5, 3, 10, 2);

        root.validate(this);
    }
}
