package cn.loyx.Jsniffer;

import cn.loyx.Jsniffer.kernel.Extractor;
import cn.loyx.Jsniffer.kernel.Extractors;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
        pcap.loop(1000, handler, System.out);
    }
}