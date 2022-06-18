package cn.loyx.Jsniffer.service;

import cn.loyx.Jsniffer.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;

public class LoadingService {

    private final String[] libs = {
            "jnetpcap.dll"
    };
    public void loading(){
        for (String lib : libs) {
            loadLibrary(lib);
        }
    }

    private void loadLibrary(String libName) {
        try {
            String location = "/lib/" + libName;
            InputStream in = Main.class.getResourceAsStream(location);
            File outFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "JSniffer"+  location);
            boolean mkdirs = outFile.getParentFile().mkdirs();
            assert mkdirs;
            FileOutputStream out  = new FileOutputStream(outFile);
            int i;
            byte[] buf = new byte[1024];
            assert in != null;
            while ((i = in.read(buf)) != -1){
                out.write(buf, 0, i);
            }
            in.close();
            out.close();
            System.load(outFile.getPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable<String> loadingStatus(){
        return () -> new Iterator<String>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                if (index < libs.length) loadLibrary(libs[index]);
                return index < libs.length;
            }

            @Override
            public String next() {
                String status = "Loading: " + libs[index];
                index ++;
                return status;
            }
        };
    }
}
