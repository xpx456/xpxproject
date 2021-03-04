package com.exhibition.utils;

import com.exhibition.entity.Guest;
import com.exhibition.view.ExhibitionApplication;
import com.finger.entity.Finger;

import java.io.File;
import java.util.ArrayList;

import intersky.apputils.BitmapCache;

public class FingerUtils {


    public static void getFingerImage(Finger finger) {
        File fpath = new File(ExhibitionApplication.mApp.fingerbase.getPath()+"/"+finger.rid+"/"+finger.gid);
        if(fpath.exists())
        ExhibitionApplication.mApp.fingerManger.getFingerImage(finger,fpath);
    }

    public static String  getFingerGudid(int size)
    {
        return String.valueOf(size+1)+"\0\0\0\0\0,\0\0\0\0\0,\0\0\0\0\0,\0\0\0\0";
    }

    public static String  getFingerrid(Guest guest)
    {
        return guest.rid+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
    }

    public static void cleanFingers(Guest guest) {
        File fpath2 = new File(ExhibitionApplication.mApp.fingerbase.getPath()+"/"
                +guest.rid);
        ExhibitionApplication.mApp.fingerManger.deleteGuestFea(fpath2);
        if(fpath2.exists())
        {
            fpath2.delete();
        }
        fpath2.mkdirs();
    }

    public static void saveFingerImage(Finger finger)
    {
        File fpath = new File(ExhibitionApplication.mApp.fingerbase.getPath()+"/"
                +finger.rid+"/"+finger.gid);
        if(fpath.exists() == false)
        fpath.mkdirs();
        for(int i = 0 ; i < finger.sampleimg.size();i++)
        {
            File file = new File(fpath.getPath()+"/"+String.valueOf(i+1)+".png");
            BitmapCache.saveBitmap(finger.sampleimg.get(i),file.getPath());
        }
    }
}
