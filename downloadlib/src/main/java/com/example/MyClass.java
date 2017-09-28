package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyClass {

    String path="http://basic.10jqka.com.cn/002011/operate.html";
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
        try {
            download(path);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        lasttime=System.currentTimeMillis();
//        System.out.println("总数量="+allUrls.size());
//        MultiDownload multiDownload=new MultiDownload(allUrls);
//        multiDownload.start();
    }

    void download(String path) throws UnsupportedEncodingException {
        String downpath=path;

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
        Document doc = null;
        try {
            doc = Jsoup.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinkedHashMap<String,String> map=getIndustry(doc);

        for (Map.Entry<String,String> entry:map.entrySet()) {
            System.out.println(entry.getKey()+"="+entry.getValue());
        }
    }

    


    LinkedHashMap<String,String> getSummary(Document doc){
        LinkedHashMap<String,String> map=new LinkedHashMap<>();
        Elements trs = doc.select("div[class=bd]").select("table[class=m_table]").select("tr");
        Elements trs1 = doc.select("div[class=m_tab_content2]").select("table[class=m_table ggintro]").select("tr");
        trs.addAll(trs1);
        int i;
        for ( i=0; i<trs.size(); i++){
            Elements tds = trs.get(i).select("td");
            for (int j=0; j<tds.size(); j++){
                String txt = tds.get(j).text();
                String[] split = txt.split("：");
                if(split.length==2) {
                    if(split[1].contains("展开 ▼收起")){
                        map.put(split[0], split[1].split("展开 ▼收起")[0]);
                    }else {
                        map.put(split[0], split[1]);
                    }
                }else if(split.length==3){
                    map.put(split[0], split[1]+split[2]);
                }
            }
        }
        return map;
    }


    LinkedHashMap<String,String> getIndustry(Document doc){
        LinkedHashMap<String,String> map=new LinkedHashMap<>();
        Elements trs = doc.select("div[class=m_tab_content]").select("table[class=m_table m_hl]").select("tbody");
//        Elements trs1 = doc.select("div[class=m_tab_content2]").select("table[class=m_table ggintro]").select("tr");
//        trs.addAll(trs1);
        int i;
        for ( i=0; i<trs.size(); i++){
            String txt = trs.get(i).text();
            if(txt.contains("按行业")||txt.contains("按产品")||txt.contains("按地区")){
                Elements tds1 = trs.get(i).select("tr");
                for (int j=0; j<tds1.size(); j++){
                    String head = tds1.get(j).select("th").text();
                    if(!head.equals("")) {
                        map.put(head, head);
                    }
                    Elements tds2 = tds1.get(j).select("td");
                    for (int k=0; k<3; k++){
                        if(tds2.size()>k) {
                            String txt1 = tds2.get(k).text();
                            map.put(txt1, txt1);
                        }
                    }
                }
                break;
            }
        }
        return map;
    }
}
