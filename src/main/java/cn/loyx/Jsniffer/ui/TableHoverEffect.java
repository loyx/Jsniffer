package cn.loyx.Jsniffer.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class TableHoverEffect extends DefaultTableCellRenderer implements MouseMotionListener {
    int row=-1;
    Color hoverColor = new Color(0x55A3EE);

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        int crow = table.rowAtPoint(e.getPoint());
        if (crow != row){
            row = crow;
            System.out.println(row);
            table.repaint();
        }

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (this.row == row && !isSelected){
            super.setForeground(hoverColor);
            return this;
        }else {
            System.out.println("repaint 2");
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
