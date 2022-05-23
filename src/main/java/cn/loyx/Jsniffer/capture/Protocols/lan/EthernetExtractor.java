package cn.loyx.Jsniffer.capture.Protocols.lan;

import cn.loyx.Jsniffer.capture.Extractor;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;

public class EthernetExtractor extends Extractor {
    private final Ethernet eth;

    public EthernetExtractor(JPacket packet) {
        super(packet);
        eth = packet.getHeader(new Ethernet());
    }

    @Override
    public String getSource() {
        return FormatUtils.mac(eth.source());
    }

    @Override
    public String getDestination() {
        return FormatUtils.mac(eth.destination());
    }

    @Override
    public String getProtocol() {
        return eth.getName();
    }

    @Override
    public String getInfo() {
        return "Ethernet Frame";
    }
}
