package com.finger.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;

import jx.vein.javajar.JXFVJavaInterface;

public class Finger{

    public String rid = "0000";
    public String path = "";
    public String gid = "";
    public ArrayList<Bitmap> sampleimg = new ArrayList<Bitmap>();
    public ArrayList<byte[]> feas = new ArrayList<byte[]>();
    public Finger()
    {

    }
}
