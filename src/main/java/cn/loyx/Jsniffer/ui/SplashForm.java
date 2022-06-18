package cn.loyx.Jsniffer.ui;

import cn.loyx.Jsniffer.service.LoadingService;

import java.awt.*;

public class SplashForm{

    private final SplashScreen splashScreen;
    LoadingService loadingService;
    public SplashForm(){
        loadingService = new LoadingService();
        splashScreen = SplashScreen.getSplashScreen();
        if (splashScreen == null){
            System.out.println("splash load error");
            loadingService.loading();
            return;
        }
        Graphics2D g = splashScreen.createGraphics();
        for (String msg : loadingService.loadingStatus()) {
            renderSplashFrom(g, msg);
            splashScreen.update();
        }
    }

    private void renderSplashFrom(Graphics2D g, String msg){
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.WHITE);
        g.drawString(msg, 500, 350);
    }

    public void close(){
        if (splashScreen != null)splashScreen.close();
    }
}
