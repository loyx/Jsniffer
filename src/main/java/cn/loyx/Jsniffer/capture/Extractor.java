package cn.loyx.Jsniffer.capture;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.TextFormatter;
import org.jnetpcap.packet.format.XmlFormatter;

import java.awt.*;
import java.io.IOException;
import java.sql.Timestamp;

public abstract class Extractor implements ModelAccessor {
    private static final ThreadLocal<TextFormatter> textFormatterPool =
            ThreadLocal.withInitial(() -> new TextFormatter(new StringBuilder()));

    private static final ThreadLocal<XmlFormatter> XmlFormatterPool =
            ThreadLocal.withInitial(() -> new XmlFormatter(new StringBuilder()));
    private final PcapPacket packet;

    public Extractor(PcapPacket packet){
        this.packet = packet;
    }

    public PcapPacket getPacket() {
        return packet;
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

    public abstract String getProtocol();

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
    public DisplayColors getColors() {
        return new DisplayColors(
                new Color(0xcce8ff),
                Color.white,
                new Color(0xe5f3ff)
        );
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
