package cn.loyx.Jsniffer.service;

import org.jfree.data.xy.XYSeries;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacketHandler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DevicesService {

    private List<PcapIf> devs;

    private int selectDevIndex;
    private int[] packetCounter;
    private List<Pcap> activePcap;

    private List<XYSeries> packetsSeries;

    private int seriesLenCnt = 0;
    private ScheduledExecutorService plotService;

    public DevicesService() {
        refreshDevices();
    }

    public List<XYSeries> getPacketsSeries() {
        return packetsSeries;
    }

    public void setSeriesLength(int length){
        for (XYSeries series : packetsSeries) {
            series.setMaximumItemCount(length);
        }
    }

    public void setSelectDevIndex(int selectDevIndex) {
        System.out.println("select: " + selectDevIndex);
        this.selectDevIndex = selectDevIndex;
    }

    public String getSelectDev(){
        return devs.get(selectDevIndex).getName();
    }

    public String getSelectDevName(){
        return devs.get(selectDevIndex).getDescription();
    }

    public void clearPacketCnt(){
        if (activePcap != null){
            for (Pcap pcap : activePcap) if (pcap != null){
                pcap.breakloop();
                pcap.close();
            }
        }
        activePcap = null;
        if (plotService != null){
            plotService.shutdownNow();
        }
        plotService = null;
    }

    public void refreshDevices(){
        // clear last cnt
        clearPacketCnt();

        // get new devs
        devs = new ArrayList<>();
        StringBuilder errBuf = new StringBuilder();
        if (Pcap.findAllDevs(devs, errBuf) != Pcap.OK){
            throw new RuntimeException(errBuf.toString());
        }

        // init packet cnt function
        packetCounter = new int[devs.size()];
        activePcap = new ArrayList<>(devs.size());
        packetsSeries = new ArrayList<>(devs.size());
        for (int i = 0; i < devs.size(); i++) {
            packetsSeries.add(new XYSeries("dev"+i));
        }

        // set counters service
        ExecutorService packetCntService = Executors.newCachedThreadPool();
        for (int i = 0; i < devs.size(); i++) {
            PcapIf pcapIf = devs.get(i);
            Pcap pcap = Pcap.openLive(pcapIf.getName(), Pcap.DEFAULT_SNAPLEN, Pcap.DEFAULT_PROMISC, 60 * 1000, new StringBuilder());

            PcapPacketHandler<Integer> handler = (packet, user) -> packetCounter[user] ++;

            int pcapId = i;
            packetCntService.submit(() -> pcap.loop(Pcap.LOOP_INFINITE, handler, pcapId));
            activePcap.add(pcap);
        }
    }

    public void setPlotService(JTable table){
        plotService = Executors.newSingleThreadScheduledExecutor();
        plotService.scheduleAtFixedRate(() -> {
            int x = seriesLenCnt++;
            for (int i = 0; i < devs.size(); i++) {
                packetsSeries.get(i).add(x, packetCounter[i]);
                packetCounter[i] = 0;
            }
            table.repaint();
        }, 1, 1, TimeUnit.SECONDS);
    }


    public String[] getDevicesDescription(){
        String[] desc = new String[devs.size()];
        for (int i = 0; i < devs.size(); i++) {
            desc[i] = devs.get(i).getDescription();
        }
        return desc;
    }
}
