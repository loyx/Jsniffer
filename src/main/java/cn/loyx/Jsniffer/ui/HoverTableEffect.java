package cn.loyx.Jsniffer.ui;

import cn.loyx.Jsniffer.capture.DisplayColors;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class HoverTableEffect extends DefaultTableCellRenderer implements MouseListener, MouseMotionListener {


    protected int row=-1;
    protected static final DisplayColors defaultColors = new DisplayColors(
            new Color(0xcce8ff),
            Color.white,
            new Color(0xe5f3ff)
    );

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        row = -1;
        JTable source = (JTable) e.getSource();
        source.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

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
        setColor(isSelected, row, defaultColors);
        setValue(value);
        return this;
    }

    protected void setColor(boolean isSelected, int row, DisplayColors colors) {
        if (isSelected){
            super.setBackground(colors.getSelectColor());
        }else {
            if (this.row == row){
                super.setBackground(colors.getHoverColor());
            }else
                super.setBackground(colors.getUnselectColor());
        }
    }
}
