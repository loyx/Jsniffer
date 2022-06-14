package cn.loyx.Jsniffer.ui;

import cn.loyx.Jsniffer.capture.DisplayColors;
import cn.loyx.Jsniffer.service.CaptureService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class PacketTableEffect extends HoverTableEffect {
    private final CaptureService service;

    private boolean colored;
    public PacketTableEffect(CaptureService service){
        this.service = service;
        this.colored = false;
    }

    public void setColored(boolean colored) {
        this.colored = colored;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (colored){
            setColor(isSelected, row, service.getColors(row));
        }else {
            setColor(isSelected, row, defaultColors);
        }
        setValue(value);
        return this;
    }

}
