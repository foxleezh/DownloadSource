package com.example;

/**
 * Created by foxleezh on 2017/6/21.
 */

public class DownloadInfo implements Comparable<DownloadInfo>{
    public String url;
    public String filePath;
    public String fileName;
    public String content;
    public int index;

    @Override
    public int compareTo(DownloadInfo o) {
        if(o.index>index){
            return -1;
        }else if(o.index<index){
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return " url="+url+" index="+index;
    }
}
