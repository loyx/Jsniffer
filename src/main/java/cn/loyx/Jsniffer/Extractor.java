package cn.loyx.Jsniffer;

import org.jnetpcap.packet.JHeader;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.TextFormatter;
import org.jnetpcap.packet.format.XmlFormatter;
import org.jnetpcap.protocol.JProtocol;

import java.io.IOException;
import java.sql.Timestamp;

public abstract class Extractor implements ModelAccessor {

    private static final int PROTOCOL_HEADER_OFFSET = 2;
    private static final ThreadLocal<TextFormatter> textFormatterPool =
            ThreadLocal.withInitial(() -> new TextFormatter(new StringBuilder()));

    private static final ThreadLocal<XmlFormatter> XmlFormatterPool =
            ThreadLocal.withInitial(() -> new XmlFormatter(new StringBuilder()));
    private final JPacket packet;

    public Extractor(JPacket packet){
        this.packet = packet;
    }

    @Override
    public long getNo() {
        return packet.getState().getFrameNumber();
    }

    @Override
    public Timestamp getTimeStamp() {
        return new Timestamp(packet.getCaptureHeader().timestampInMillis());
    }

    public abstract String getSource();

    public abstract String getDestination();

    @Override
    public String getProtocol() {
        JHeader jHeader;
        try {
            jHeader = JProtocol.valueOf(
                    packet.getHeaderIdByIndex(packet.getHeaderCount() - PROTOCOL_HEADER_OFFSET)
            ).getClazz().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return packet.getHeader(jHeader).getName();
    }

    @Override
    public int getLength() {
        return packet.getCaptureHeader().wirelen();
    }

    public abstract String getInfo();

    @Override
    public String toHexDump() {
        return packet.toHexdump();
    }

    @Override
    public String toTextFormatterDump() {
        TextFormatter out = textFormatterPool.get();
        out.reset();
        try {
            out.format(packet);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(out.toString(), e);
        }
    }

    @Override
    public String toXmlFormatterDump() {
        XmlFormatter out = XmlFormatterPool.get();
        out.reset();
        try {
            out.format(packet);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(out.toString(), e);
        }
    }

    @Override
    public String toString() {
        return getNo() + " " +
                getTimeStamp() + " " +
                getSource() +  " " +
                getDestination() + " " +
                getProtocol() + " " +
                getLength() + " " +
                getInfo();
    }
}
