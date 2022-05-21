package cn.loyx.Jsniffer;

import org.jnetpcap.packet.JHeader;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.JProtocol;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Extractors {
    private static final Map<String, Class<? extends Extractor>> extractors;
    private static final int PROTOCOL_HEADER_OFFSET = 2;
    static {
        extractors = new HashMap<>();
        extractors.put("Arp", ArpExtractor.class);
        extractors.put("Ethernet", EthernetExtractor.class);
        extractors.put("Unknown", UnknownExtractor.class);
    }

    public static Extractor createExtractor(JPacket packet){
        try {
            int headerId = packet.getHeaderIdByIndex(packet.getHeaderCount() - PROTOCOL_HEADER_OFFSET);
            JHeader header = JProtocol.valueOf(headerId).getClazz().newInstance();
            String ProtocolName = packet.getHeader(header).getName();
            Class<? extends Extractor> extractorClazz = extractors.getOrDefault(ProtocolName, UnknownExtractor.class);
            return extractorClazz.getConstructor(JPacket.class).newInstance(packet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
