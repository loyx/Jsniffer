package cn.loyx.Jsniffer.kernel;

import java.sql.Timestamp;

public interface ModelAccessor {
    long getNo();
    Timestamp getTimeStamp();
    String getSource();
    String getDestination();
    String getProtocol();
    int getLength();
    String getInfo();
    String toHexDump();
    String toTextFormatterDump();
    String toXmlFormatterDump();
}
