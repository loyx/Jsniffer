package cn.loyx.Jsniffer.kernel;

import cn.loyx.Jsniffer.kernel.Protocols.UnknownExtractor;
import cn.loyx.Jsniffer.kernel.Protocols.application.HttpExtractor;
import cn.loyx.Jsniffer.kernel.Protocols.lan.EthernetExtractor;
import cn.loyx.Jsniffer.kernel.Protocols.network.ArpExtractor;
import cn.loyx.Jsniffer.kernel.Protocols.network.IcmpExtractor;
import cn.loyx.Jsniffer.kernel.Protocols.network.Ip4Extractor;
import cn.loyx.Jsniffer.kernel.Protocols.network.Ip6Extractor;
import cn.loyx.Jsniffer.kernel.Protocols.tcpip.TCPExtractor;
import cn.loyx.Jsniffer.kernel.Protocols.tcpip.UdpExtractor;
import cn.loyx.Jsniffer.kernel.Protocols.wan.PppExtractor;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.protocol.wan.PPP;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Extractors {
    private static final int[] protocolsHeaderId = {
            Http.ID,
            Tcp.ID, Udp.ID,
            Ip4.ID, Ip6.ID, JProtocol.ARP_ID, Icmp.ID,
            PPP.ID,
            Ethernet.ID
    };
    private static final List<Class<? extends Extractor>> protocolsExtractor = new ArrayList<Class<? extends Extractor>>(){{
        add(HttpExtractor.class);
        add(TCPExtractor.class); add(UdpExtractor.class);
        add(Ip4Extractor.class); add(Ip6Extractor.class); add(ArpExtractor.class); add(IcmpExtractor.class);
        add(PppExtractor.class);
        add(EthernetExtractor.class);
    }};
    private static final int PROTOCOL_HEADER_OFFSET = 2;

    public static Extractor createExtractor(JPacket packet){
        for (int i = 0; i < protocolsHeaderId.length; i++) {
            if (packet.hasHeader(protocolsHeaderId[i])){
                Class<? extends Extractor> extractorClass = protocolsExtractor.get(i);
                try {
                    Constructor<? extends Extractor> constructor = extractorClass.getConstructor(JPacket.class);
                    return constructor.newInstance(packet);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                         InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new UnknownExtractor(packet);
    }
}
