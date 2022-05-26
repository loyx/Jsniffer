package cn.loyx.Jsniffer.capture.Protocols.tcpip;

import cn.loyx.Jsniffer.capture.DisplayColors;
import cn.loyx.Jsniffer.capture.Extractor;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Udp;

import java.awt.*;

public class UdpExtractor extends Extractor {
    private final Udp udp;
    private final String source;
    private final String destination;
    public UdpExtractor(PcapPacket packet) {
        super(packet);
        udp = packet.getHeader(new Udp());
        if (packet.hasHeader(Ip4.ID)){
            Ip4 ip4 = packet.getHeader(new Ip4());
            source = FormatUtils.ip(ip4.source());
            destination = FormatUtils.ip(ip4.destination());
        }else if (packet.hasHeader(Ip6.ID)){
            Ip6 ip6 = packet.getHeader(new Ip6());
            source = FormatUtils.ip(ip6.source());
            destination = FormatUtils.ip(ip6.destination());
        }else {
            source = "Unknown";
            destination = "Unknown";
        }
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public String getProtocol() {
        return udp.getName();
    }

    @Override
    public DisplayColors getColors() {
        return new DisplayColors(
                new Color(0xafdaff),
                new Color(0xdaeeff),
                new Color(0xc4e4ff)
        );
    }

    @Override
    public String getInfo() {
        return String.format("%d -> %d Len=%d", udp.source(), udp.destination(), udp.length()-8);
    }
}
