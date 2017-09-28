package com.example;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/10/12.
 */

public class FileUtil {

    public static FileUtil ins;

    private FileUtil(){

    }

    public static FileUtil getIns(){
        if(ins==null){
            ins=new FileUtil();
        }
        return ins;
    }

    public static void write(String path,String filename,String content) {
            File file = new File(path, filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
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
                fos.write(content.getBytes("GBK"));
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
    }


    public static String read(String path,String filename) throws UnsupportedEncodingException {
        File file=new File(path,filename);
        if(!file.exists()){
            return "";
        }
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final int bufferSize = 4096;
        try {
            final FileInputStream in = new FileInputStream(file);
            final BufferedInputStream bIn = new BufferedInputStream(in);
            int length = 0;
            byte[] buffer = new byte[bufferSize];
            byte[] bufferCopy = new byte[bufferSize];
            while ((length = bIn.read(buffer, 0, bufferSize)) != -1) {
                bufferCopy = new byte[length];
                System.arraycopy(buffer, 0, bufferCopy, 0, length);
                output.write(bufferCopy);
            }
            bIn.close();
        } catch (Exception e){

        }finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new String(output.toByteArray(),"GBK");
    }

    public static boolean exists(String path,String filename){
        File file=new File(path,filename);
        return file.exists();
    }
}
