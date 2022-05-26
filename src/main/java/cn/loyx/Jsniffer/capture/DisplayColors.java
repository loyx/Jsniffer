package cn.loyx.Jsniffer.capture;

import java.awt.*;

public class DisplayColors {
    private final Color selectColor;
    private final Color unselectColor;
    private final Color hoverColor;

    public Color getSelectColor() {
        return selectColor;
    }

    public Color getUnselectColor() {
        return unselectColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public DisplayColors(Color selectColor, Color unselectColor, Color hoverColor) {
        this.selectColor = selectColor;
        this.unselectColor = unselectColor;
        this.hoverColor = hoverColor;
    }
}
