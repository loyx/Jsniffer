package cn.loyx.Jsniffer.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class TableHoverEffect extends DefaultTableCellRenderer implements MouseMotionListener {
    int row=-1;
    Color hoverColor = new Color(0xe5f3ff);
    Color selectColor = new Color(0xcce8ff);

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
        if (isSelected){
            super.setBackground(selectColor);
        }else {
            if (this.row == row){
                super.setBackground(hoverColor);
            }else
                super.setBackground(Color.white);
        }
        setValue(value);
        return this;
    }
}
