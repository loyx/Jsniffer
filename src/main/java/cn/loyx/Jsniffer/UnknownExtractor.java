package cn.loyx.Jsniffer;

import org.jnetpcap.packet.JPacket;

public class UnknownExtractor extends Extractor{
    public UnknownExtractor(JPacket packet) {
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
    public String getInfo() {
        return "Unknown";
    }
}
