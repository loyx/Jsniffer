package cn.loyx.Jsniffer.kernel.Protocols.network;

import cn.loyx.Jsniffer.kernel.Extractor;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;

public class IcmpExtractor extends Extractor {
    private final Icmp icmp;
    private final Ip4 ip4;
    public IcmpExtractor(JPacket packet) {
        super(packet);
        icmp = packet.getHeader(new Icmp());
        ip4 = packet.getHeader(new Ip4());
    }

    @Override
    public String getSource() {
        return FormatUtils.ip(ip4.source());
    }

    @Override
    public String getDestination() {
        return FormatUtils.ip(ip4.destination());
    }

    @Override
    public String getProtocol() {
        return icmp.getName();
    }

    @Override
    public String getInfo() {
        return "ICMP";
    }
}
