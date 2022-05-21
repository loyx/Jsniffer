package cn.loyx.Jsniffer;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip4;

public class Ip4Extractor extends Extractor{
    private final Ip4 ip4;

    public Ip4Extractor(JPacket packet) {
        super(packet);
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
    public String getInfo() {
        return "IPv4";
    }
}
