package cn.loyx.Jsniffer.service;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DevicesService {

    private List<PcapIf> devs;

    private int selectDevIndex;
    private int[] activePcapPacketCnt;
    private List<Pcap> activePcap;

    private List<LinkedList<Integer>> packetsHistory;

    public DevicesService() {
        refreshDevices();
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
    }

    public void refreshDevices(){
        clearPacketCnt();
        devs = new ArrayList<>();
        StringBuilder errBuf = new StringBuilder();
        if (Pcap.findAllDevs(devs, errBuf) != Pcap.OK){
            throw new RuntimeException(errBuf.toString());
        }
        activePcapPacketCnt = new int[devs.size()];
        activePcap = new ArrayList<>(devs.size());
        packetsHistory = new ArrayList<LinkedList<Integer>>(devs.size()){{for (int i = 0; i < devs.size(); i++) add(new LinkedList<>());}};
        ExecutorService packetCntService = Executors.newCachedThreadPool();
        for (int i = 0; i < devs.size(); i++) {
            PcapIf pcapIf = devs.get(i);
            Pcap pcap = Pcap.openLive(pcapIf.getName(), Pcap.DEFAULT_SNAPLEN, Pcap.DEFAULT_PROMISC, 60 * 1000, new StringBuilder());

            PcapPacketHandler<Integer> handler = new PcapPacketHandler<Integer>() {
                int cnt = 0;
                Long pre;
                @Override
                public void nextPacket(PcapPacket packet, Integer user) {
                    cnt++;
                    long l = new Date().getTime();
                    if (pre == null || l - pre > 1_000){
                        System.out.println(user + "cnt: " + cnt);
                        activePcapPacketCnt[user] = cnt;
                        cnt = 0;
                        pre = l;
                    }
                }
            };

            int pcapId = i;
            packetCntService.submit(() -> pcap.loop(Pcap.LOOP_INFINITE, handler, pcapId));

            activePcap.add(pcap);
        }
    }


    public String[] getDevicesDescription(){
        String[] desc = new String[devs.size()];
        for (int i = 0; i < devs.size(); i++) {
            desc[i] = devs.get(i).getDescription();
        }
        return desc;
    }
    public String getPacketCntValue(int devId) {
        LinkedList<Integer> list = packetsHistory.get(devId);
        if (list.size() > 10) list.removeFirst();
        list.add(activePcapPacketCnt[devId]);
        return historyToString(list);
    }

    private String historyToString(LinkedList<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) stringBuilder.append(" ");
            stringBuilder.append(String.format("%5d", list.get(i)));
        }
        return stringBuilder.toString();
    }
}
