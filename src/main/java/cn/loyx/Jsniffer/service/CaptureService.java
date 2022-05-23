package cn.loyx.Jsniffer.service;

import javax.swing.table.DefaultTableModel;

public class CaptureService {
    private final DefaultTableModel model;

    public CaptureService(DefaultTableModel model) {
        this.model = model;
    }

    public void startCapture(String devName){
        System.out.println("start capture:" + devName);
    }
    public void stopCapture(){
        System.out.println("stop capture");
    }
}
