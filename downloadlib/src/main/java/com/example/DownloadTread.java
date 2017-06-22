package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.example.MyClass.lasttime;

/**
 * Created by Administrator on 2016/10/12.
 */

public class DownloadTread extends Thread {

    public boolean isfinish;

    @Override
    public void run() {
        super.run();
        while (!isfinish) {
            DownloadInfo info=MultiDownload.getUrl();
            if(info==null){
                isfinish=true;
                MultiDownload.aliveThread--;
                if(MultiDownload.aliveThread==0) {
                    System.out.println("已下载完毕 耗时="+(System.currentTimeMillis()-lasttime));
                }
                return;
            }
            if (!FileUtil.exists(info.filePath, info.fileName)) {

                String result = "";
                BufferedReader in = null;
                try {
                    String urlNameString = info.url;
                    URL realUrl = new URL(urlNameString);
                    URLConnection connection;
                    // 打开和URL之间的连接
                    connection = realUrl.openConnection();
                    // 设置通用的请求属性
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    connection.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    connection.setConnectTimeout(10 * 1000);
                    connection.setReadTimeout(20 * 1000);
                    // 建立实际的连接
                    connection.connect();
                    // 定义 BufferedReader输入流来读取URL的响应
                    in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += (line+"\r\n");
                    }
                } catch (Exception e) {
                    System.out.println("发送GET请求出现异常！失败" + info.toString());
                    MultiDownload.putInfo(info);
                    e.printStackTrace();
                    return;
                }
                // 使用finally块来关闭输入流
                finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                info.content=result;
                if(info.fileName.endsWith("java")||info.fileName.endsWith("aidl")) {
                    String name = info.fileName.substring(0, info.fileName.lastIndexOf("."));
                    if (!result.contains(name)) {
                        MultiDownload.putInfo(info);
                        return;
                    }
                }
                write(info);
            } else {
                MultiDownload.size++;
                System.out.println("已下载 " + info.url + " " + MultiDownload.size + "/" + MultiDownload.Totalsize);
            }
        }
    }

    public void write(DownloadInfo info) {
        String path=info.filePath;
        String filename=info.fileName;
        File filepath = new File(path);
        File file = new File(path, filename);
        synchronized (file) {

            if (!filepath.exists()) {
                filepath.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(info.content.getBytes("utf-8"));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            MultiDownload.size++;
            System.out.println("下载 " + info.url + " "+MultiDownload.size+ "/" + MultiDownload.Totalsize);
        }
    }
}
