package cn.loyx.Jsniffer.kernel.Protocols.wan;

import cn.loyx.Jsniffer.kernel.Extractor;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.wan.PPP;

public class PppExtractor extends Extractor {
    private final PPP ppp;
    public PppExtractor(JPacket packet) {
        super(packet);
        ppp = packet.getHeader(new PPP());
    }

    @Override
    public String getSource() {
        return null;
    }

    @Override
    public String getDestination() {
        return null;
    }

    @Override
    public String getProtocol() {
        return ppp.getName();
    }

    @Override
    public String getInfo() {
        return ppp.getName();
    }
}
