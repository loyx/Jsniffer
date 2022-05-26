package cn.loyx.Jsniffer;

import cn.loyx.Jsniffer.ui.MainForm;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        loadLibrary();
        SwingUtilities.invokeLater(Main::createGUI);
    }

    private static void loadLibrary() {
        try {
            String location = "/lib/" + "jnetpcap.dll";
            InputStream in = Main.class.getResourceAsStream(location);
            File outFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "JSniffer"+  location);
            boolean mkdirs = outFile.getParentFile().mkdirs();
            assert mkdirs;
            FileOutputStream out  = new FileOutputStream(outFile);
            int i;
            byte[] buf = new byte[1024];
            assert in != null;
            while ((i = in.read(buf)) != -1){
                out.write(buf, 0, i);
            }
            in.close();
            out.close();
            System.load(outFile.getPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void createGUI() {
        // use flatlaf look & feel
        FlatLightLaf.setup();

        JFrame frame = new JFrame("Jsniffer");
        URL resource = Main.class.getResource("/logo.png");
        assert resource != null;
        Image logo = new ImageIcon(resource).getImage();
        frame.setIconImage(logo);
        frame.setContentPane(new MainForm().getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 900);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}