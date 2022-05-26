package cn.loyx.Jsniffer.capture;

import cn.loyx.Jsniffer.capture.Protocols.UnknownExtractor;
import cn.loyx.Jsniffer.capture.Protocols.application.HttpExtractor;
import cn.loyx.Jsniffer.capture.Protocols.lan.EthernetExtractor;
import cn.loyx.Jsniffer.capture.Protocols.network.ArpExtractor;
import cn.loyx.Jsniffer.capture.Protocols.network.IcmpExtractor;
import cn.loyx.Jsniffer.capture.Protocols.network.Ip4Extractor;
import cn.loyx.Jsniffer.capture.Protocols.network.Ip6Extractor;
import cn.loyx.Jsniffer.capture.Protocols.tcpip.TCPExtractor;
import cn.loyx.Jsniffer.capture.Protocols.tcpip.UdpExtractor;
import cn.loyx.Jsniffer.capture.Protocols.wan.PppExtractor;
import org.jnetpcap.packet.PcapPacket;
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

    public static Extractor createExtractor(PcapPacket packet){
        for (int i = 0; i < protocolsHeaderId.length; i++) {
            if (packet.hasHeader(protocolsHeaderId[i])){
                Class<? extends Extractor> extractorClass = protocolsExtractor.get(i);
                try {
                    Constructor<? extends Extractor> constructor = extractorClass.getConstructor(PcapPacket.class);
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
