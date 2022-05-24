package cn.loyx.Jsniffer.capture.Protocols.application;

import cn.loyx.Jsniffer.capture.Extractor;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;

public class HttpExtractor extends Extractor {
    private final Http http;
    private final Ip4 ip4;

    public HttpExtractor(PcapPacket packet) {
        super(packet);
        http = packet.getHeader(new Http());
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
        return http.getName();
    }

    @Override
    public String getInfo() {
        return http.contentType();
    }
}
