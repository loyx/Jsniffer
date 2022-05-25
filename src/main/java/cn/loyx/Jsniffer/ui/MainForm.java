package cn.loyx.Jsniffer.ui;

import cn.loyx.Jsniffer.service.CaptureService;
import cn.loyx.Jsniffer.service.DevicesService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MainForm {
    private JPanel root;
    private JTable packetTable;
    private JButton devicesButton;
    private JButton startButton;
    private JButton stopButton;
    private JButton saveButton;
    private JButton loadButton;
    private JTextField filterBar;
    private JPanel toolPanel;
    private JPanel contentPanel;
    private JPanel capturePanel;
    private JPanel initialPanel;
    private JTextField initialFilterBar;
    private JTable devicesTable;
    private JComboBox<String> deviceTypes;
    private JTextArea packetDetailArea;
    private JTextArea packetHexArea;
    private JButton clearButton;
    private JPanel statusBar;
    private JLabel statusBarDevName;
    private JLabel statusBarCaptureStatus;
    private JComboBox<String> disStyleComboBox;
    private JLabel filterBarLabel;
    private JLabel statusBarButtonStatusIcon;

    // field
    private final CardLayout contentPanelLayout;
    private final DefaultTableModel packetTableModel;

    // services
    private final DevicesService devicesService;
    private final CaptureService captureService;
    private String packetDisplayStyle;
    private boolean uiStatusCapturing;

    public MainForm() {
        // initial field
        contentPanelLayout = (CardLayout) contentPanel.getLayout();
        String[] columnNames = {"No.", "Time", "Source", "Destination", "Protocol", "Length", "Info"};
        packetTableModel = new DefaultTableModel(columnNames, 0);

        // initial services
        devicesService = new DevicesService();
        captureService = new CaptureService(packetTableModel);

        // initial ui status
        uiStatusCapturing = false;


        // set GUI
        createStatusBar();
        createButtons();
        createFilerBar();
        createInitialCardPanel();
        createContentCardPanel();


    }

    private void createStatusBar() {
        // set status Bar
        statusBarCaptureStatus.setText("stop");
        statusBarDevName.setText("Dev: None");
        packetDisplayStyle = (String) disStyleComboBox.getSelectedItem();

        disStyleComboBox.addItemListener(e -> {
            packetDisplayStyle = (String) disStyleComboBox.getSelectedItem();
            packetDetailArea.setText(captureService.getPacketDetail(packetTable.getSelectedRow(), packetDisplayStyle));
        });
    }

    private void createButtons() {
        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        clearButton.setEnabled(false);
        saveButton.setEnabled(false);

        devicesButton.setIcon(resizeIcon("src/main/resources/icons/devs.png"));
        startButton.setIcon(resizeIcon("src/main/resources/icons/start.png"));
        stopButton.setIcon(resizeIcon("src/main/resources/icons/stop.png"));
        clearButton.setIcon(resizeIcon("src/main/resources/icons/clear.png"));
        saveButton.setIcon(resizeIcon("src/main/resources/icons/save.png"));
        loadButton.setIcon(resizeIcon("src/main/resources/icons/load.png"));

        devicesButton.addActionListener(e -> {
            contentPanelLayout.show(contentPanel, initialPanel.getName());
            if (uiStatusCapturing){
                captureService.stopCapture();
                statusBarCaptureStatus.setText(null);
                statusBarButtonStatusIcon.setIcon(null);
            }
            stopButton.setEnabled(false);
            clearButton.setEnabled(false);
            saveButton.setEnabled(false);
        });
        startButton.addActionListener(e -> {
            if (devicesTable.getSelectedRow() != -1){
                // switch card
                contentPanelLayout.show(contentPanel, capturePanel.getName());

                // enable stop and save
                stopButton.setEnabled(true);
                clearButton.setEnabled(true);
                saveButton.setEnabled(true);

                // update status bar
                statusBarDevName.setText("Dev: " + devicesService.getSelectDevName());
                statusBarCaptureStatus.setText("Capturing...");
                statusBarButtonStatusIcon.setIcon(resizeIcon("src/main/resources/icons/capturing.png"));

                // start capture
                captureService.startCapture(devicesService.getSelectDev());

                // set ui status
                uiStatusCapturing = true;
            }
        });
        stopButton.addActionListener(e -> {
            captureService.stopCapture();

            // update status bar
            statusBarCaptureStatus.setText("stop");
            statusBarButtonStatusIcon.setIcon(resizeIcon("src/main/resources/icons/stopCapturing.png"));

            // update ui status
            uiStatusCapturing = false;
        });
        clearButton.addActionListener(e -> {
            captureService.clearHistory();
            packetHexArea.setText("");
            packetDetailArea.setText("");
        });
        clearButton.addMouseListener(new MouseAdapter() {

            private final Icon clearIcon = resizeIcon("src/main/resources/icons/clearStatus.png");
            private Icon preIcon;

            @Override
            public void mousePressed(MouseEvent e) {
                preIcon = statusBarButtonStatusIcon.getIcon();
                statusBarButtonStatusIcon.setIcon(clearIcon);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                statusBarButtonStatusIcon.setIcon(preIcon);
            }
        });

        File currentDir = new File("."); // current directory
        FileFilter jSnifferFileType = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                else return f.getName().toLowerCase().endsWith(".jsn");
            }
            @Override
            public String getDescription() {
                return "JSniffer file (*.jsn)";
            }
        };

        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(currentDir);
            fileChooser.setFileFilter(jSnifferFileType);
            if (fileChooser.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION){
                System.out.println("save file to " + fileChooser.getSelectedFile().getPath());
                captureService.dumpToFile(fileChooser.getSelectedFile().getPath());
            }
        });
        loadButton.addActionListener(e -> {
            // switch to content panel
            contentPanelLayout.show(contentPanel, capturePanel.getName());

            // choose load file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(currentDir);
            fileChooser.setFileFilter(jSnifferFileType);
            if (fileChooser.showOpenDialog(contentPanel) == JFileChooser.APPROVE_OPTION){
                System.out.println("load file from " + fileChooser.getSelectedFile().getPath());
                captureService.loadFromFile(fileChooser.getSelectedFile().getPath());
                statusBarCaptureStatus.setText("Load file form " + fileChooser.getSelectedFile().getName());
            }
        });
    }

    private Icon resizeIcon(String path) {
        return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    }

    private void createContentCardPanel() {
        createPacketTable();
    }

    private void createFilerBar() {
        filterBarLabel.setIcon(resizeIcon("src/main/resources/icons/search.png"));
        filterBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                captureService.setNewFilterExpression(filterBar.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                captureService.setNewFilterExpression(filterBar.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                captureService.setNewFilterExpression(filterBar.getText());
            }
        });
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

                    // update status bar
                    statusBarDevName.setText("Dev: " + devicesService.getSelectDevName());
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

        packetTable.setModel(packetTableModel);
        TableColumnModel columnModel = packetTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        packetTable.getTableHeader().setReorderingAllowed(false);

        packetTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = packetTable.getSelectedRow();
                if (selectedRow != -1){
                    packetDetailArea.setText(captureService.getPacketDetail(selectedRow, packetDisplayStyle));
                    packetHexArea.setText(captureService.getPacketHex(selectedRow));
                    packetDetailArea.repaint();
                    packetHexArea.repaint();
                }
            }
        });
    }

}
