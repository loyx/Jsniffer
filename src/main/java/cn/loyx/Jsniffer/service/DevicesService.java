package cn.loyx.Jsniffer.service;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import java.util.ArrayList;
import java.util.List;

public class DevicesService {

    private List<PcapIf> devs;

    private int selectDevIndex;

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

    public void refreshDevices(){
        devs = new ArrayList<>();
        StringBuilder errBuf = new StringBuilder();
        if (Pcap.findAllDevs(devs, errBuf) != Pcap.OK){
            throw new RuntimeException(errBuf.toString());
        }
    }


    public String[] getDevicesDescription(){
        String[] desc = new String[devs.size()];
        for (int i = 0; i < devs.size(); i++) {
            desc[i] = devs.get(i).getDescription();
        }
        return desc;
    }
    public String[] getDevicesNames(){
        String[] names = new String[devs.size()];
        for (int i = 0; i < devs.size(); i++) {
            names[i] = devs.get(i).getName();
        }
        return names;
    }
}
