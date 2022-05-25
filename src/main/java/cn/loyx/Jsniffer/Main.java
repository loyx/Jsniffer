package cn.loyx.Jsniffer;

import cn.loyx.Jsniffer.ui.MainForm;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createGUI);
    }

    private static void createGUI() {
        // use flatlaf look & feel
        FlatDarkLaf.setup();

        JFrame frame = new JFrame("Jsniffer");
        frame.setContentPane(new MainForm().getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}