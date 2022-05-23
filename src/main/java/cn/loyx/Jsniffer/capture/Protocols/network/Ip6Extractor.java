package cn.loyx.Jsniffer.capture.Protocols.network;

import cn.loyx.Jsniffer.capture.Extractor;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip6;

public class Ip6Extractor extends Extractor {
    private final Ip6 ip6;
    public Ip6Extractor(JPacket packet) {
        super(packet);
        ip6 = packet.getHeader(new Ip6());
    }

    @Override
    public String getSource() {
        return FormatUtils.ip(ip6.source());
    }

    @Override
    public String getDestination() {
        return FormatUtils.ip(ip6.destination());
    }

    @Override
    public String getProtocol() {
        return ip6.getName();
    }

    @Override
    public String getInfo() {
        return "IPv6";
    }
}
