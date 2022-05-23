package cn.loyx.Jsniffer.ui;

import cn.loyx.Jsniffer.service.DevicesService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;

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

    private final CardLayout contentPanelLayout;

    private final DevicesService devicesService;

    public MainForm() {
        // initial services
        devicesService = new DevicesService();

        // initial field
        contentPanelLayout = (CardLayout) contentPanel.getLayout();

        // set GUI
        createButtons();
        createInitialCardPanel();
        createContentCardPanel();

    }

    private void createButtons() {
        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        saveButton.setEnabled(false);

        devicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                CardLayout layout = (CardLayout) contentPanel.getLayout();
//                layout.show(contentPanel, initialPanel.getName());
                contentPanelLayout.show(contentPanel, initialPanel.getName());
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (devicesTable.getSelectedRow() != -1){
                    // switch card
//                    CardLayout layout = (CardLayout)contentPanel.getLayout();
//                    layout.show(contentPanel, capturePanel.getName());
                    contentPanelLayout.show(contentPanel, capturePanel.getName());

                    // enable stop and save
                    stopButton.setEnabled(true);
                    saveButton.setEnabled(true);

                }
            }
        });
    }

    private void createContentCardPanel() {
        createPacketTable();

    }

    private void createInitialCardPanel() {
        createDivcesTable();
    }

    private void createDivcesTable() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(getDeviceData(), new Object[]{"Devices"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        devicesTable.setModel(defaultTableModel);

        devicesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (devicesTable.getSelectedRow() != -1){
                    startButton.setEnabled(true);
                }
                if (e.getClickCount() == 2){
                    contentPanelLayout.show(contentPanel, capturePanel.getName());
                }
                super.mouseClicked(e);
            }
        });
    }

    private Object[][] getDeviceData(){
        String[] devicesName = devicesService.getDevicesDescription();
        Object[][] data = new Object[devicesName.length][];
        for (int i = 0; i < devicesName.length; i++) {
            data[i] = new Object[]{devicesName[i]};
        }
        return data;
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
