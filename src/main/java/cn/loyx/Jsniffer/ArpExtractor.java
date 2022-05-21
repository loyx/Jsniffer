package cn.loyx.Jsniffer;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;

public class ArpExtractor extends Extractor {

    private final Arp arp;

    public ArpExtractor(JPacket packet) {
        super(packet);
        arp = packet.getHeader(new Arp());
    }

    @Override
    public String getSource() {
        return FormatUtils.mac(arp.sha());
    }

    @Override
    public String getDestination() {
        return "Broadcast";
    }

    @Override
    public String getInfo() {
        return String.format("who has %s? Tell %s", FormatUtils.ip(arp.tpa()), FormatUtils.ip(arp.spa()));
    }
}
