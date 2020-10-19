package com.iccard.thread;

import android.os.Message;

import com.iccard.ICCardReader;
import com.iccard.IcCardManager;
import com.iccard.handler.IcCardHandler;
import com.kongqw.serialportlibrary.Device;


public class CheckThread extends Thread {

    public ICCardReader icCardReader;
    public long current = 0;
    public boolean stop = false;
    public IcCardManager icCardManager;
    public CheckThread(ICCardReader icCardReader,IcCardManager icCardManager) {
        this.icCardReader = icCardReader;
        this.icCardManager = icCardManager;

    }


    @Override
    public void run() {
//        boolean find = false;
//        int n = 0;
//        int size = icCardReader.devices.size();
//        while (icCardReader.finddevice == null)
//        {
//
//            try {
//                int index = n%size;
//                Device device = icCardReader.devices.get(index);
//                icCardReader.openDevice(device);
//                sleep(200);
//                icCardReader.closeDevice(device);
//                sleep(200);
//                n++;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//        Message msg = new Message();
//        msg.what = IcCardHandler.CHECK_ICCARD_SUCCESS;
//        msg.obj = icCardReader;
//        icCardManager.icCardHandler.sendMessage(msg);
    }
}
