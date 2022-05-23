package cn.loyx.Jsniffer.ui;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private JPanel root;
    private JTextField filter;
    private JTable packetTable;
    private JButton devicesButton;
    private JButton startButton;
    private JButton stopButton;
    private JButton saveButton;
    private JButton loadButton;
    private JTextField filterField;
    private JPanel toolPanel;
    private JPanel contentPanel;
    private JPanel capturePanel;
    private JPanel initialPanel;
    private JTextField initialFilter;
    private JTable devicesTable;
    private JComboBox<String> deviceTypes;

    public MainForm() {
        createInitialCardPanel();
        createContentCardPanel();
    }

    private void createContentCardPanel() {
        createPacketTable();
        devicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) contentPanel.getLayout();
                layout.show(contentPanel, initialPanel.getName());
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout)contentPanel.getLayout();
                layout.show(contentPanel, capturePanel.getName());
            }
        });

    }

    private void createInitialCardPanel() {
        createDivesTable();
    }

    private void createDivesTable() {
        devicesTable.setModel(new DefaultTableModel(
                new Object[][]{
                        {"WLAN"},
                        {"local"}
                },
                new String[]{"Devices"}
        ));
    }

    public JPanel getRoot() {
        return root;
    }
    private void createPacketTable() {
        packetTable.setModel(new DefaultTableModel(
                new Object[][]{
                        {1, "2022", "0.0.0.0", "1.1.1.1", "TCP", "62", "test info"},
                        {2, "2023", "0.0.0.1", "1.1.1.2", "ICMP", "62", "test info"},
                        {3, "2024", "0.0.0.2", "1.1.1.3", "Eth", "62", "test info"}
                },
                new String[]{"No.", "Time", "Source", "Destination", "Protocol", "Length", "Info"}
        ));
        TableColumnModel columnModel = packetTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        packetTable.getTableHeader().setReorderingAllowed(false);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}
