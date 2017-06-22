package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MyClass {

    String path="http://androidxref.com/6.0.0_r1/xref/frameworks/base/core/java/android/content/pm/";
    public static String savePath="D:\\stock\\MyApplication\\app\\src\\main\\java\\";
    public static String tempPath="D:\\temp\\";

    ArrayList<DownloadInfo> allUrls=new ArrayList<>();

    public static long lasttime;

    public static void main(String[] args){
        new MyClass().run();
    }

    void run(){
        System.out.println("开始下载");
        lasttime=System.currentTimeMillis();
        download(path);
        lasttime=System.currentTimeMillis();
        System.out.println("总数量="+allUrls.size());
        MultiDownload multiDownload=new MultiDownload(allUrls);
        multiDownload.start();
    }

    void download(String path){
        String downpath=path.replace("/xref/","/raw/");

        String s="";
//        if(path.endsWith("/")){
        DownloadInfo info = DownloadUtil.getPathInfo(path);
        if (!FileUtil.exists(info.filePath, info.fileName)) {
            s = DownloadUtil.sendGet(path, "");
            FileUtil.write(info.filePath, info.fileName, s);
        } else {
            s = FileUtil.read(info.filePath, info.fileName);
        }
//        }
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
                    allUrls.add(DownloadUtil.getInfo(downpath+element3,allUrls.size()+1));
                }
            }
        }
        for (String urlpath : urlpaths) {
            System.out.println("解析地址 "+urlpath);
            download(urlpath);
        }

        System.out.println("解析地址耗时="+(System.currentTimeMillis()-lasttime));

    }



}
