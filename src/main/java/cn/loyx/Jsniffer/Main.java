package cn.loyx.Jsniffer;

import cn.loyx.Jsniffer.ui.MainForm;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createGUI);
    }

    private static void createGUI() {
        // use flatlaf look & feel
        FlatLightLaf.setup();

        JFrame frame = new JFrame("Jsniffer");
        Image logo = new ImageIcon("src/main/resources/logo.png").getImage();
        frame.setIconImage(logo);
        frame.setContentPane(new MainForm().getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}