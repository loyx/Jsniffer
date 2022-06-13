package cn.loyx.Jsniffer.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.ui.RectangleInsets;
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
            JFreeChart chart = ChartFactory.createXYLineChart("", "xxx", "yyy", data);
            chart.removeLegend();
            XYPlot plot = (XYPlot) chart.getPlot();
            // set line color
            XYItemRenderer renderer = plot.getRenderer();
            renderer.setSeriesPaint(0, Color.BLACK);
            // remove outline
            plot.setOutlineVisible(false);
            // remove axis
            plot.getDomainAxis().setVisible(false);
            plot.getRangeAxis().setVisible(false);
            // remove grid line
            plot.setDomainGridlinesVisible(false);
            plot.setRangeGridlinesVisible(false);
//             set backgroundColor
//            plot.setBackgroundPaint(new Color(0xe5f3ff));
            plot.setBackgroundAlpha(0f);

            // set offset
            plot.setAxisOffset(new RectangleInsets(-10,-10,-10,-10));


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
