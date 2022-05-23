package cn.loyx.Jsniffer.service;

import cn.loyx.Jsniffer.capture.Extractor;
import cn.loyx.Jsniffer.capture.Extractors;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CaptureService {
    private final DefaultTableModel model;
    private Pcap pcap;
    private List<Extractor> extractors;

    public CaptureService(DefaultTableModel model) {
        this.model = model;
        extractors = new ArrayList<>();
    }

    public void startCapture(String devName){
        System.out.println("start capture:" + devName);
        StringBuilder errBuf = new StringBuilder();
        pcap = Pcap.openLive(devName, Pcap.DEFAULT_SNAPLEN, Pcap.DEFAULT_PROMISC, 60*1000, errBuf);
        JPacketHandler<DefaultTableModel> handler = new JPacketHandler<DefaultTableModel>() {
            @Override
            public void nextPacket(JPacket packet, DefaultTableModel user) {
                Extractor extractor = Extractors.createExtractor(packet);
                Object[] rowData = {
                        extractor.getNo(),
                        extractor.getTimeStamp(),
                        extractor.getSource(),
                        extractor.getDestination(),
                        extractor.getProtocol(),
                        extractor.getLength(),
                        extractor.getInfo()
                };
                model.addRow(rowData);
                extractors.add(extractor);
            }
        };
        Thread thread = new Thread(() -> pcap.loop(Pcap.LOOP_INFINITE, handler, model));
        thread.start();

    }
    public void stopCapture(){
        System.out.println("stop capture");
        pcap.close();
    }

    public String getPacketDetail(int index){
        return extractors.get(index).toTextFormatterDump();
    }
    public String getPacketHex(int index){
        return extractors.get(index).toHexDump();
    }
}
