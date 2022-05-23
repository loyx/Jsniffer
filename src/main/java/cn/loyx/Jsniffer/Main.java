package cn.loyx.Jsniffer;

import cn.loyx.Jsniffer.capture.Extractor;
import cn.loyx.Jsniffer.capture.Extractors;
import cn.loyx.Jsniffer.ui.MainForm;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;

import javax.swing.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createGUI);
    }

    private static void createGUI() {
        JFrame frame = new JFrame("Jsniffer");
        frame.setContentPane(new MainForm().getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }


    public static void Cap() {
        List<PcapIf> devs = new ArrayList<>();
        StringBuilder errBuf = new StringBuilder();
        int s = Pcap.findAllDevs(devs, errBuf);
        if (s != Pcap.OK){
            return;
        }

        for (int i = 0; i < devs.size(); i++) {
            PcapIf pcapIf = devs.get(i);
            System.out.printf("#%d: %s %s\n", i, pcapIf.getName(), pcapIf.getDescription());
        }

        int select = new Scanner(System.in).nextInt();

        PcapIf dev = devs.get(select);
        JPacketHandler<PrintStream> handler = new JPacketHandler<PrintStream>() {
            @Override
            public void nextPacket(JPacket packet, PrintStream user) {
                Extractor extractor = Extractors.createExtractor(packet);
                System.out.println(extractor);
//                System.out.println(extractor.toTextFormatterDump());
            }
        };

        Pcap pcap = Pcap.openLive(dev.getName(), Pcap.DEFAULT_JPACKET_BUFFER_SIZE, Pcap.DEFAULT_PROMISC, 60*1000, errBuf);

        // set filter
        PcapBpfProgram filter = new PcapBpfProgram();
        int r = pcap.compile(filter, "udp", 1, 0);
        if (r != Pcap.OK){
            throw new RuntimeException("filter error!");
        }
        pcap.setFilter(filter);
        pcap.loop(Pcap.LOOP_INFINITE, handler, System.out);
        pcap.close();
    }
}