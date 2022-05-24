package cn.loyx.Jsniffer.service;

import cn.loyx.Jsniffer.capture.Extractor;
import cn.loyx.Jsniffer.capture.Extractors;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.winpcap.WinPcap;

import javax.swing.table.DefaultTableModel;
import java.util.*;

public class CaptureService {
    private final DefaultTableModel model;
    private final PcapBpfProgram filter;
    private Pcap pcap;
    private final Deque<Extractor> bufQueue;
    private final List<Extractor> showList;


    private String filerExpress = "";
    private boolean validFilterExpress;


    public CaptureService(DefaultTableModel model) {
        this.model = model;
        bufQueue = new LinkedList<>();
        showList = new ArrayList<>();
        filter = new PcapBpfProgram();
    }

    public void setFilerExpress(String filerExpress) {
        this.filerExpress = filerExpress;
    }

    public void startCapture(String devName){
        System.out.println("start capture:" + devName);
        StringBuilder errBuf = new StringBuilder();
        pcap = Pcap.openLive(devName, Pcap.DEFAULT_SNAPLEN, Pcap.DEFAULT_PROMISC, 60*1000, errBuf);
        checkFilter();

        PcapPacketHandler<Object> handler = (packet, user) -> captureNewPacket(Extractors.createExtractor(packet));
        Thread thread = new Thread(() -> pcap.loop(Pcap.LOOP_INFINITE, handler, model));
        thread.start();

    }

    private void checkFilter() {
        validFilterExpress = pcap.compile(filter, filerExpress, 1, 0) == 0;
    }

    private void captureNewPacket(Extractor extractor) {
        bufQueue.add(extractor);
        if (validFilterExpress){
            int s = WinPcap.offlineFilter(filter, extractor.getPacket().getCaptureHeader(), extractor.getPacket());
            if (s != 0){
                showList.add(extractor);
                model.addRow(new Object[]{
                        extractor.getNo(),
                        extractor.getTimeStamp(),
                        extractor.getSource(),
                        extractor.getDestination(),
                        extractor.getProtocol(),
                        extractor.getLength(),
                        extractor.getInfo()
                });
            }
        }
    }

    public void stopCapture(){
        System.out.println("stop capture");
        pcap.breakloop();
        pcap.close();
    }

    public String getPacketDetail(int index){
        return showList.get(index).toTextFormatterDump();
    }
    public String getPacketHex(int index){
        return showList.get(index).toHexDump();
    }

    public void clearHistory() {
        bufQueue.clear();
        showList.clear();
        model.setColumnCount(0);
    }
}
