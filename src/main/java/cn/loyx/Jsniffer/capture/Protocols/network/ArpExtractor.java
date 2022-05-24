package cn.loyx.Jsniffer.capture.Protocols.network;

import cn.loyx.Jsniffer.capture.Extractor;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Arp;

public class ArpExtractor extends Extractor {

    private final Arp arp;

    public ArpExtractor(PcapPacket packet) {
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
    public String getProtocol() {
        return arp.getName();
    }

    @Override
    public String getInfo() {
        return String.format("who has %s? Tell %s", FormatUtils.ip(arp.tpa()), FormatUtils.ip(arp.spa()));
    }
}
