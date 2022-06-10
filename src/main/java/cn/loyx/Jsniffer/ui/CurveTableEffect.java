package cn.loyx.Jsniffer.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CurveTableEffect implements TableCellRenderer {

    List<ChartPanel> cells;

    public CurveTableEffect(List<XYSeries> seriesList){
        cells = new ArrayList<>(seriesList.size());
        for (XYSeries series : seriesList) {
            XYSeriesCollection data = new XYSeriesCollection(series);
            JFreeChart chart = ChartFactory.createXYLineChart("", "", "", data);
            chart.removeLegend();
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.getDomainAxis().setVisible(false);
            plot.getRangeAxis().setVisible(false);
            plot.setDomainGridlinesVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setBackgroundAlpha(0f);
            ChartPanel panel = new ChartPanel(chart);
            cells.add(panel);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        try {
            return cells.get(row);
        } catch (Exception e) {
            return new JLabel("Inactivity connection");
        }
    }
}
