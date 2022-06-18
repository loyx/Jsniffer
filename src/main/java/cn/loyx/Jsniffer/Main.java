package cn.loyx.Jsniffer;

import cn.loyx.Jsniffer.ui.MainForm;
import cn.loyx.Jsniffer.ui.SplashForm;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashForm splashForm = new SplashForm();

            // use flatlaf look & feel
            FlatLightLaf.setup();
            JFrame mainGUI = createMainGUI();
            splashForm.close();
            mainGUI.setVisible(true);
            mainGUI.toFront();
        });
    }

    private static JFrame createMainGUI() {
        JFrame frame = new JFrame("Jsniffer");
        URL resource = Main.class.getResource("/logo.png");
        assert resource != null;
        Image logo = new ImageIcon(resource).getImage();
        frame.setIconImage(logo);
        frame.setContentPane(new MainForm().getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 900);
        frame.setLocationRelativeTo(null);
        return frame;
    }
}