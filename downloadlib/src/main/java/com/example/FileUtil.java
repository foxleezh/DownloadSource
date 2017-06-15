package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/10/12.
 */

public class FileUtil {

    public static void write(String path,String filename,String content) {
        File filepath=new File(path);
        File file=new File(path,filename);
        if(!filepath.exists()){
            filepath.mkdirs();
        }
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(file);
            fos.write(content.getBytes("utf-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if(fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean exists(String path,String filename){
        File file=new File(path,filename);
        return file.exists();
    }
}
