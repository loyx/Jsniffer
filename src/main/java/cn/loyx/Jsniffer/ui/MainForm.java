package cn.loyx.Jsniffer.ui;

import cn.loyx.Jsniffer.service.CaptureService;
import cn.loyx.Jsniffer.service.DevicesService;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.List;

public class MainForm {
    private final Icon stopCapturingIcon = resizeIcon("/icons/stopCapturing.png");
    private final Icon capturingIcon = resizeIcon("/icons/capturing.png");
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
    private JLabel JSnifferLabel;
    private JScrollPane packetScrollPane;
    private JButton colorTableButton;
    private JButton scrollEndButton;

    // field
    private final CardLayout contentPanelLayout;
    private final DefaultTableModel packetTableModel;
    private String packetDisplayStyle;
    private boolean uiStatusCapturing;
    private boolean coloredPacketTable;

    // services
    private final DevicesService devicesService;
    private final CaptureService captureService;
    private final ColoredTableEffect coloredPacketTableEffect;

    public MainForm() {
        // initial field
        contentPanelLayout = (CardLayout) contentPanel.getLayout();
        String[] columnNames = {"No.", "Time", "Source", "Destination", "Protocol", "Length", "Info"};
        packetTableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5) return Integer.class;
                else return String.class;
            }
        };
        URL resource = this.getClass().getResource("/logo.png");
        assert resource != null;
        JSnifferLabel.setIcon(new ImageIcon(new ImageIcon(resource)
                .getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)));

        // initial services
        devicesService = new DevicesService();
        captureService = new CaptureService(packetTableModel);
        coloredPacketTableEffect = new ColoredTableEffect(captureService);

        // initial ui status
        uiStatusCapturing = false;
        coloredPacketTable = true;


        // enable auto scroll
        new SmartScroller(packetScrollPane);


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
        statusBarButtonStatusIcon.setIcon(stopCapturingIcon);
        statusBarDevName.setText("Dev: None");
        packetDisplayStyle = (String) disStyleComboBox.getSelectedItem();

        disStyleComboBox.addItemListener(e -> {
            packetDisplayStyle = (String) disStyleComboBox.getSelectedItem();
            int selectedRow = packetTable.getSelectedRow();
            if (selectedRow != -1)packetDetailArea.setText(captureService.getPacketDetail(selectedRow, packetDisplayStyle));
        });
    }

    private void createButtons() {
        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        clearButton.setEnabled(false);
        saveButton.setEnabled(false);

        devicesButton.setIcon(resizeIcon("/icons/devs.png"));
        startButton.setIcon(resizeIcon("/icons/start.png"));
        stopButton.setIcon(resizeIcon("/icons/stop.png"));
        clearButton.setIcon(resizeIcon("/icons/clear.png"));
        saveButton.setIcon(resizeIcon("/icons/save.png"));
        loadButton.setIcon(resizeIcon("/icons/load.png"));
        colorTableButton.setIcon(resizeIcon("/icons/table_color.png"));
        colorTableButton.setToolTipText("Colored table");
        scrollEndButton.setIcon(resizeIcon("/icons/scroll_down.png"));
        scrollEndButton.setToolTipText("Scroll to end");

        devicesButton.addActionListener(e -> {
            contentPanelLayout.show(contentPanel, initialPanel.getName());
            DefaultTableModel model = (DefaultTableModel) devicesTable.getModel();
            if (model != null){
                model.setRowCount(0);
                model.setDataVector(getDeviceData(), new Object[]{"devices", "packets"});
            }
            devicesService.refreshDevices();
            setChartPanel();
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
                devicesService.clearPacketCnt();

                // enable stop and save
                stopButton.setEnabled(true);
                clearButton.setEnabled(true);
                saveButton.setEnabled(true);

                // update status bar
                statusBarDevName.setText("Dev: " + devicesService.getSelectDevName());
                statusBarCaptureStatus.setText("Capturing...");
                statusBarButtonStatusIcon.setIcon(capturingIcon);

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
            statusBarButtonStatusIcon.setIcon(stopCapturingIcon);

            // update ui status
            uiStatusCapturing = false;
        });
        clearButton.addActionListener(e -> {
            captureService.clearHistory();
            packetHexArea.setText("");
            packetDetailArea.setText("");
        });
        clearButton.addMouseListener(new MouseAdapter() {

            private final Icon clearIcon = resizeIcon("/icons/clearStatus.png");
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
        colorTableButton.addActionListener(e -> {
            coloredPacketTable = !coloredPacketTable;
            if (coloredPacketTable) colorTableButton.setIcon(resizeIcon("/icons/table_color.png"));
            else colorTableButton.setIcon(resizeIcon("/icons/table_fill.png"));
            coloredPacketTableEffect.setColored(coloredPacketTable);
            packetTable.repaint();
        });
        scrollEndButton.addActionListener(e -> packetTable.scrollRectToVisible(packetTable.getCellRect(packetTable.getRowCount()-1, 0, true)));
    }

    private Icon resizeIcon(String path) {
        URL resource = this.getClass().getResource(path);
        assert resource != null;
        return new ImageIcon(new ImageIcon(resource).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    }

    private void createContentCardPanel() {
        createPacketTable();
    }

    private void createFilerBar() {
        filterBarLabel.setIcon(resizeIcon("/icons/search.png"));
        filterBar.getDocument().addDocumentListener(new DocumentListener() {

            private void setFilter() {
                if (captureService.setNewFilterExpression(filterBar.getText()))
                    filterBar.putClientProperty("JComponent.outline", "");
                else
                    filterBar.putClientProperty("JComponent.outline", "error");
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                setFilter();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                setFilter();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                setFilter();
            }
        });
    }

    private void createInitialCardPanel() {
        createDevicesTable();
    }

    private void createDevicesTable() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(getDeviceData(), new Object[]{"Devices", "flow"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        devicesTable.setModel(defaultTableModel);
        setChartPanel();

        ColoredTableEffect coloredTableEffect = new ColoredTableEffect();
        devicesTable.setDefaultRenderer(Object.class, coloredTableEffect);
        devicesTable.addMouseMotionListener(coloredTableEffect);

        devicesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (devicesTable.getSelectedRow() != -1){
                    startButton.setEnabled(true);
                    devicesService.setSelectDevIndex(devicesTable.getSelectedRow());
                }
                if (e.getClickCount() == 2){
                    contentPanelLayout.show(contentPanel, capturePanel.getName());
                    devicesService.clearPacketCnt();

                    // update status bar
                    statusBarDevName.setText("Dev: " + devicesService.getSelectDevName());
                }
                super.mouseClicked(e);
            }
        });
    }

    private void setChartPanel() {
        List<XYSeries> packetsSeries = devicesService.getPacketsSeries();
        devicesService.setSeriesLength(30);
        CurveTableEffect curveTableEffect = new CurveTableEffect(packetsSeries);
        devicesTable.getColumnModel().getColumn(1).setCellRenderer(curveTableEffect);
        devicesService.setPlotService(devicesTable);
    }

    private Object[][] getDeviceData(){
        String[] devicesName = devicesService.getDevicesDescription();
        Object[][] data = new Object[devicesName.length][];
        for (int i = 0; i < devicesName.length; i++) {
            data[i] = new Object[]{devicesName[i], 0};
        }
        return data;
    }

    public JPanel getRoot() {
        return root;
    }

    private void createPacketTable() {

        // set data model
        packetTable.setModel(packetTableModel);

        // set color hover effect
        coloredPacketTableEffect.setColored(coloredPacketTable);
        packetTable.setDefaultRenderer(Object.class, coloredPacketTableEffect);
        packetTable.addMouseMotionListener(coloredPacketTableEffect);

        // set table header alignment
        ((DefaultTableCellRenderer) packetTable.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(JLabel.LEFT);

        // set columns style
        TableColumnModel columnModel = packetTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(0).setCellRenderer(coloredPacketTableEffect);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(100);
        columnModel.getColumn(5).setCellRenderer(coloredPacketTableEffect);
        columnModel.getColumn(6).setPreferredWidth(800);

        // disable drag
        packetTable.getTableHeader().setReorderingAllowed(false);

        // set MouserListener
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
