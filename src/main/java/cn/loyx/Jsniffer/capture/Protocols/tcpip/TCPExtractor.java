package cn.loyx.Jsniffer.capture.Protocols.tcpip;

import cn.loyx.Jsniffer.capture.DisplayColors;
import cn.loyx.Jsniffer.capture.Extractor;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import java.awt.*;

public class TCPExtractor extends Extractor {
    private final Tcp tcp;
    private final Ip4 ip4;
    private final String[] flags = {"CWR", "ECE", "URG", "ACK", "PSH", "RST", "SYN", "FIN"};
    public TCPExtractor(PcapPacket packet) {
        super(packet);
        tcp = packet.getHeader(new Tcp());
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
        return tcp.getName();
    }

    @Override
    public DisplayColors getColors() {
        if (tcp.flags_SYN() || tcp.flags_FIN()){
            return new DisplayColors(
                    new Color(0x909eaa),
                    new Color(0x809cb3),
                    new Color(0x809cb3)
            );
        }
        if (tcp.flags_RST()){
            return new DisplayColors(
                    new Color(0xa40000),
                    new Color(0x831c33),
                    new Color(0x930e1a)
            );
        }
        return new DisplayColors(
                new Color(0xb9d4ff),
                new Color(0xe7e6ff),
                new Color(0xcfddff)
        );
    }

    @Override
    public String getInfo() {
        StringBuilder flagStr = new StringBuilder();
        int fMask = tcp.flags();
        for (int i = 0; i < 8; i++) {
            if ((fMask & (1 << 7-i)) != 0){
                if (flagStr.length() != 0) flagStr.append(", ");
                flagStr.append(flags[i]);
            }
        }
        return String.format("%d -> %d [%s] Seq=%d %s Win=%d Len=%d",
                tcp.source(), tcp.destination(), flagStr, tcp.seq() ,
                (tcp.flags_ACK()?" Ack="+tcp.ack():""), tcp.window(), tcp.getPayloadLength());
    }
}
