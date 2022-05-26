package cn.loyx.Jsniffer.capture.Protocols.network;

import cn.loyx.Jsniffer.capture.DisplayColors;
import cn.loyx.Jsniffer.capture.Extractor;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Arp;

import java.awt.*;

public class ArpExtractor extends Extractor {

    private final Arp arp;
    private static final DisplayColors colors = new DisplayColors(
            new Color(0xc9dcdf),
            new Color(0xfaf0d7),
            new Color(0xe1e6db)
    );

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

    @Override
    public DisplayColors getColors() {
        return colors;
    }
}
