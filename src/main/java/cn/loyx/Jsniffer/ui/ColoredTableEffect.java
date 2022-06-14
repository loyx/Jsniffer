package cn.loyx.Jsniffer.ui;

import cn.loyx.Jsniffer.capture.DisplayColors;
import cn.loyx.Jsniffer.service.CaptureService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ColoredTableEffect extends DefaultTableCellRenderer implements MouseMotionListener, MouseListener {
    int row=-1;
    private static final DisplayColors defaultColors = new DisplayColors(
            new Color(0xcce8ff),
            Color.white,
            new Color(0xe5f3ff)
    );
    private boolean colored;
    private CaptureService service;

    public ColoredTableEffect(){
        super();
        colored = false;
    }

    public ColoredTableEffect(CaptureService service){
        super();
        colored = false;
        this.service = service;
    }

    public void setColored(boolean colored) {
        this.colored = colored;
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        int crow = table.rowAtPoint(e.getPoint());
        if (crow != row){
            row = crow;
            table.repaint(); // todo ui performance
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Color unselectColor, selectColor, hoverColor;
        if (colored){
            DisplayColors colors = service.getColors(row);
            unselectColor = colors.getUnselectColor();
            selectColor = colors.getSelectColor();
            hoverColor = colors.getHoverColor();
        }else {
            unselectColor = defaultColors.getUnselectColor();
            selectColor = defaultColors.getSelectColor();
            hoverColor = defaultColors.getHoverColor();
        }
        if (isSelected){
            super.setBackground(selectColor);
        }else {
            if (this.row == row){
                super.setBackground(hoverColor);
            }else
                super.setBackground(unselectColor);
        }
        setValue(value);
        return this;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        row = -1;
        JTable source = (JTable) e.getSource();
        source.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}
}
