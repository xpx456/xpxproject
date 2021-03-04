package com.dk.dkhome.entity;

import com.dk.dkhome.utils.TestManager;

import intersky.filetools.PathUtils;

public class Device {

    public static final int DEVICE_LEAVECHANGE = 1;
    public static final int DEVICE_NOMAL = 0;
    public static final int DEVICE_ERROR = 2;
    public String typeName = "";
    public String deviceName = "";
    public String deviceMac = "";
    public int deviceForm = EquipData.DKTEST;
    public int maxleave = 32;
    public int statetype = DEVICE_NOMAL;
    public int nowrpm = 0;
    public double nowwork = 0;
    public int nowleavel = 0;

    public void updataData(String[] data) {

        if(TestManager.testManager.perleave == -1 )
        {
            nowleavel = Integer.valueOf(data[2]);
        }
        else
        {
            if(TestManager.testManager.perleave == Integer.valueOf(data[2]))
            {
                nowleavel = Integer.valueOf(data[2]);
                TestManager.testManager.perleave = -1;
            }
        }
        nowrpm = Integer.valueOf(data[3]);
        if(data[5].equals("1"))
        {
            statetype = Device.DEVICE_ERROR;
        }
        else
        {
            if(data[7].startsWith("1"))
            {
                statetype = Device.DEVICE_LEAVECHANGE;
            }
            else
            {
                statetype = Device.DEVICE_NOMAL;
            }
        }
        nowwork = EquipData.getWork(nowleavel,nowrpm,this);
    }
}
