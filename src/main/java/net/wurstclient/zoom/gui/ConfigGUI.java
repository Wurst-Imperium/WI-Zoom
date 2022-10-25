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
        root.setSize(360, 100);
        root.setInsets(Insets.NONE);

        WLabel defaultLevelText = new WLabel(Text.translatable("gui.wi_zoom.label"), 0x000000);
        defaultLevelText.setVerticalAlignment(VerticalAlignment.CENTER);
        
        WiWLabeledSlider defaultLevelLabelSlider = new WiWLabeledSlider(1, 50, Axis.HORIZONTAL);
        defaultLevelLabelSlider.setValue((int) WiZoom.INSTANCE.getDefaultLevel());
        defaultLevelLabelSlider.setLabel(Text.of(String.valueOf((int) WiZoom.INSTANCE.getDefaultLevel())));
        
        WButton saveButton = new WButton(Text.translatable("gui.wi_zoom.button"));
        saveButton.setAlignment(HorizontalAlignment.CENTER);
        saveButton.setOnClick(() -> {
            WiZoom.INSTANCE.setDefaultLevel(defaultLevelLabelSlider.getValue());
            WiZoom.saveConfig();
            MinecraftClient.getInstance().setScreen(previous);
        });

        System.out.println(MinecraftClient.getInstance().getLanguageManager().getLanguage().getName());
        System.out.println(MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode());
        System.out.println(MinecraftClient.getInstance().getLanguageManager().getLanguage().getRegion());
//        root.add(defaultLevelText, 1, 1, 1 ,1);
//        root.add(defaultLevelLabelSlider, 1, 1, 1,1);
//        root.add(saveButton, 1, 1, 1 ,1);

        if (MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode().equals("fr_fr")) {
            root.add(defaultLevelText, 1, 1, 5 ,1);
            root.add(defaultLevelLabelSlider, 9, 1, 10 ,1);
            root.add(saveButton, 6, 3, 8 ,4);
        } else if (MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode().equals("zh_hk")) {
            root.add(defaultLevelText, 1, 1, 5 ,1);
            root.add(defaultLevelLabelSlider, 5, 1, 14,1);
            root.add(saveButton, 7, 3, 6 ,4);
        } else if (MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode().equals("zh_cn")) {
            root.add(defaultLevelText, 1, 1, 5 ,1);
            root.add(defaultLevelLabelSlider, 5, 1, 14,1);
            root.add(saveButton, 7, 3, 6 ,4);
        } else if (MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode().equals("zh_tw")) {
            root.add(defaultLevelText, 1, 1, 5 ,1);
            root.add(defaultLevelLabelSlider, 5, 1, 14,1);
            root.add(saveButton, 7, 3, 6 ,4);
        } else if (MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode().equals("lzh")) {
            root.add(defaultLevelText, 1, 1, 5 ,1);
            root.add(defaultLevelLabelSlider, 5, 1, 14,1);
            root.add(saveButton, 7, 3, 6 ,4);
        } else if (MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode().equals("ru_ru")) {
            root.add(defaultLevelText, 1, 1, 5 ,1);
            root.add(defaultLevelLabelSlider, 13, 1, 6,1);
            root.add(saveButton, 7, 3, 6 ,4);
        } else if (MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode().equals("et_ee")) {
            root.add(defaultLevelText, 1, 1, 5 ,1);
            root.add(defaultLevelLabelSlider, 7, 1, 12,1);
            root.add(saveButton, 7, 3, 6 ,4);
        } else {
            root.add(defaultLevelText, 2, 1, 5 ,1);
            root.add(defaultLevelLabelSlider, 8, 1, 11,1);
            root.add(saveButton, 7, 3, 6 ,4);
        }

        root.validate(this);
    }
}
