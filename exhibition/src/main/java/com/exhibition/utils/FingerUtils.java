package com.exhibition.utils;

import com.exhibition.entity.Guest;
import com.exhibition.view.ExhibitionApplication;
import com.finger.entity.Finger;

import java.io.File;
import java.util.ArrayList;

public class FingerUtils {


    public static void getFingerImage(Finger finger) {
        File fpath = new File(ExhibitionApplication.mApp.fingerbase.getPath()+"/"+finger.rid+"/"+finger.gid);
        ExhibitionApplication.mApp.fingerManger.getFingerImage(finger,fpath);
    }

    public static String  getFingerGudid(int size)
    {
        return "0000000000000000000"+String.valueOf(size);
    }

    public static String  getFingerrid(Guest guest)
    {
        return "00000000000000"+guest.rid;
    }
}
