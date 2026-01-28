package net.wimods.zoom;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class ModMenuApiImpl implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return this::buildScreen;
    }

    private Screen buildScreen(Screen parent)
    {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("WI Zoom"));

        builder.setSavingRunnable(ZoomConfig::save);

        ConfigEntryBuilder eb = builder.entryBuilder();

        builder.getOrCreateCategory(Component.literal("General")).addEntry(
                eb.startEnumSelector(Component.literal("Action Bar Display"), ActionBarMode.class, ZoomConfig.INSTANCE.actionBarMode)
                        .setDefaultValue(ActionBarMode.DISABLED)
                        .setSaveConsumer(v -> {
                            ZoomConfig.INSTANCE.actionBarMode = v;
                            WiZoom.INSTANCE.onMouseScroll(0);
                        })
                        .build()
        );

        builder.getOrCreateCategory(Component.literal("General")).addEntry(
                eb.startDoubleField(Component.literal("Max Zoom"), ZoomConfig.INSTANCE.maxZoom)
                        .setMin(1.0)
                        .setMax(5000.0)
                        .setDefaultValue(50.0)
                        .setSaveConsumer(v -> {
                            ZoomConfig.INSTANCE.maxZoom = v;
                            WiZoom.INSTANCE.onMouseScroll(0);
                        })
                        .build()
        );

        return builder.build();
    }
}
