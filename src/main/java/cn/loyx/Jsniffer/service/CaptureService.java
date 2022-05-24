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
    private final LinkedList<Extractor> bufList;
    private final List<Extractor> showList;


    private String filerExpression = "";
    private boolean validFilterExpression;


    public CaptureService(DefaultTableModel model) {
        this.model = model;
        bufList = new LinkedList<>();
        showList = new ArrayList<>();
        filter = new PcapBpfProgram();
    }

    public void startCapture(String devName){
        System.out.println("start capture:" + devName);
        StringBuilder errBuf = new StringBuilder();
        pcap = Pcap.openLive(devName, Pcap.DEFAULT_SNAPLEN, Pcap.DEFAULT_PROMISC, 60*1000, errBuf);
        checkFilter();

        PcapPacketHandler<Object> handler = (packet, user) -> {
            Extractor extractor = Extractors.createExtractor(packet);
            bufListAdd(extractor);
            if (validFilterExpression){
                filerAdd(extractor);
            }
        };
        Thread thread = new Thread(() -> pcap.loop(Pcap.LOOP_INFINITE, handler, model));
        thread.start();

    }

    private void checkFilter() {
        validFilterExpression = Pcap.compileNoPcap(Pcap.DEFAULT_SNAPLEN, 1, filter, filerExpression, 1, 0) != -1;
    }

    public void setNewFilterExpression(String exp){
        filerExpression = exp;
        checkFilter();
        if (!validFilterExpression) return;
        synchronized (this){
            showList.clear();
            model.setRowCount(0);
            for (Extractor extractor : bufList) {
                filerAdd(extractor);
            }
        } // need repaint
    }

    private void filerAdd(Extractor extractor) {
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

    synchronized private void bufListAdd(Extractor extractor) {
        bufList.add(extractor);
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
        bufList.clear();
        showList.clear();
        model.setRowCount(0);
    }
}
