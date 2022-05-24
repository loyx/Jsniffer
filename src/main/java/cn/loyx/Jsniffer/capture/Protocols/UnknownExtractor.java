package cn.loyx.Jsniffer.capture.Protocols;

import cn.loyx.Jsniffer.capture.Extractor;
import org.jnetpcap.packet.PcapPacket;

public class UnknownExtractor extends Extractor {
    public UnknownExtractor(PcapPacket packet) {
        super(packet);
    }

    @Override
    public String getSource() {
        return "Unknown";
    }

    @Override
    public String getDestination() {
        return "Unknown";
    }

    @Override
    public String getProtocol() {
        return "Unknown";
    }

    @Override
    public String getInfo() {
        return "Unknown";
    }
}
