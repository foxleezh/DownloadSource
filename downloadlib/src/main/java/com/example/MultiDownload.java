package com.example;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by foxleezh on 2017/6/15.
 */

public class MultiDownload {

    public static ArrayList<DownloadTread> threads = new ArrayList<>();

    private static PriorityQueue<DownloadInfo> request = new PriorityQueue<>();
    public static LinkedBlockingQueue<DownloadInfo> result = new LinkedBlockingQueue<>();

    public static volatile int size;
    public static volatile int Totalsize;
    public static volatile int aliveThread;


    public MultiDownload(ArrayList<DownloadInfo> urls) {
        for (int i = 0; i < 100; i++) {
            threads.add(new DownloadTread());
        }
        for (int i = 0; i < urls.size(); i++) {
            request.add(urls.get(i));
        }
        aliveThread = threads.size();
        Totalsize = request.size();
    }

    public static void start() {
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).start();
        }
    }

    public static DownloadInfo getUrl() {
        synchronized (request) {
            if (!request.isEmpty()) {
                return request.poll();
            }
        }
        return null;
    }

    public static void putInfo(DownloadInfo info) {
        synchronized (request) {
            request.add(info);
            new DownloadTread().start();
        }
    }

}
