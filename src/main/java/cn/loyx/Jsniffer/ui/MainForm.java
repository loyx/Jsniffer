package cn.loyx.Jsniffer.ui;

import cn.loyx.Jsniffer.service.CaptureService;
import cn.loyx.Jsniffer.service.DevicesService;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

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
    private JTextArea packetDetailArea;
    private JTextArea packetHexArea;
    private JButton clearButton;

    // field
    private final CardLayout contentPanelLayout;
    private final DefaultTableModel packetTableModel;

    // services
    private final DevicesService devicesService;
    private final CaptureService captureService;

    public MainForm() {
        // initial field
        contentPanelLayout = (CardLayout) contentPanel.getLayout();
        String[] columnNames = {"No.", "Time", "Source", "Destination", "Protocol", "Length", "Info"};
        packetTableModel = new DefaultTableModel(columnNames, 0);

        // initial services
        devicesService = new DevicesService();
        captureService = new CaptureService(packetTableModel);


        // set GUI
        createButtons();
        createInitialCardPanel();
        createContentCardPanel();

        packetTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = packetTable.getSelectedRow();
                if (selectedRow != -1){
                    packetDetailArea.setText(captureService.getPacketDetail(selectedRow));
                    packetHexArea.setText(captureService.getPacketHex(selectedRow));
                    packetDetailArea.repaint();
                    packetHexArea.repaint();
                }
            }
        });
    }

    private void createButtons() {
        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        saveButton.setEnabled(false);

        devicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanelLayout.show(contentPanel, initialPanel.getName());
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (devicesTable.getSelectedRow() != -1){
                    // switch card
                    contentPanelLayout.show(contentPanel, capturePanel.getName());

                    // enable stop and save
                    stopButton.setEnabled(true);
                    saveButton.setEnabled(true);

                    // start capture
                    captureService.startCapture(devicesService.getSelectDev());
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                captureService.stopCapture();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                captureService.clearHistory();
            }
        });

        File currentDir = new File("."); // current directory
        FileFilter jSnifferFileType = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                else return f.getName().toLowerCase().equals(".jsn");
            }
            @Override
            public String getDescription() {
                return "JSniffer file (*.jsn)";
            }
        };

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(currentDir);
                fileChooser.setFileFilter(jSnifferFileType);
                if (fileChooser.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION){
                    System.out.println("save file to " + fileChooser.getSelectedFile());
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(currentDir);
                fileChooser.setFileFilter(jSnifferFileType);
                if (fileChooser.showOpenDialog(contentPanel) == JFileChooser.APPROVE_OPTION){
                    System.out.println("load file from " + fileChooser.getSelectedFile());
                }
            }
        });
    }

    private void createContentCardPanel() {
        createPacketTable();

    }

    private void createInitialCardPanel() {
        createDevicesTable();
    }

    private void createDevicesTable() {
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
                    devicesService.setSelectDevIndex(devicesTable.getSelectedRow());
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

    public TableModel getPacketTableModel() {
        return packetTableModel;
    }

    private void createPacketTable() {
        packetTable.setModel(packetTableModel);
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
