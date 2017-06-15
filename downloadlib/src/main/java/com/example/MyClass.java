package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MyClass {

    String path="http://androidxref.com/6.0.0_r1/xref/frameworks/base/core/java/android/view/";
    String savePath="D:/download/";

    public static void main(String[] args){
        new MyClass().run();
    }

    void run(){
        download(path);
    }

    void download(String path){
        String downpath=path.replace("/xref/","/raw/");
        System.out.println("开始下载");
        String s= DownloadUtil.sendGet(path, "");
//        System.out.println(s);
//        FileUtil.write("D:/download/frameworks/av/media/libmedia","Visualizer.cpp",s);
        ArrayList<String> urls=new ArrayList<>();
        ArrayList<String> urlpaths=new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements td = doc.getElementsByTag("tbody");
        for (Element element : td) {
            Elements trs=element.getElementsByTag("tr");
            for (Element tr : trs) {
                Element element2=tr.child(1);
                String element3=element2.getElementsByAttribute("href").get(0).attributes().get("href");
                if(element3.endsWith("/")) {
                    urlpaths.add(path+element3);
                }else if(!"..".equals(element3)){
                    urls.add(downpath+element3);
                }
            }
        }

        String s1,saveTemp;
        String[] split1;
        int split;
        int index=0;
        for (String url : urls) {
            index++;
            System.out.println("下载 "+url+" "+index+"/"+urls.size());
            s1= DownloadUtil.sendGet(url, "");
            split1 = url.split("raw/");
            saveTemp=savePath+split1[1];
            split = saveTemp.lastIndexOf("/");
            FileUtil.write(saveTemp.substring(0,split),saveTemp.substring(split+1),s1);
        }


        System.out.println("下载完毕");


        for (String urlpath : urlpaths) {
            System.out.println("未下载成功 "+urlpath);
            download(urlpath);
        }
    }

}
