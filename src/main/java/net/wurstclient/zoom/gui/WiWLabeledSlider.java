package net.wurstclient.zoom.gui;

import io.github.cottonmc.cotton.gui.widget.WLabeledSlider;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import net.minecraft.text.Text;

public class WiWLabeledSlider extends WLabeledSlider {
    public WiWLabeledSlider(int min, int max, Axis axis) {
        super(min, max, axis);
    }

    @Override
    protected void onValueChanged(int value) {
        setLabel(Text.of(String.valueOf(value)));
        super.onValueChanged(value);
    }
}
