package com.boot.security.service.Util;

import org.springframework.stereotype.Component;

import java.io.*;


public class FileUtil {
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if(file.exists())
        {
            return file.delete();
        }
        return false;
    }

    public static boolean copyFile(String oldpath,String newpath){
        File newfile = new File(newpath);
        File oldfile = new File(oldpath);
        FileInputStream in = null;
        FileOutputStream os=null;
        try {
            in = new FileInputStream(oldfile);
            os= new FileOutputStream(newfile);
            byte[] bytes = new byte[1024];
            int count = 0;
            while ((count = in.read(bytes)) != -1)
            {
                os.write(bytes,0,count);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {

            if(in != null)
            {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
