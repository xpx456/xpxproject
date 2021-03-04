package com.iccard.thread;


import android.os.Message;
import com.iccard.IcCardManager;
import com.mjk.adplayer.utils.HardWareCommunicationUtils;

import java.io.IOException;

public class InitdeviceThread extends Thread {

    public static final int INIT_DEVICE_FINISH = 11000;
    public int devHandle;
    public IcCardManager icCardManager;
    public InitdeviceThread(IcCardManager icCardManager)
    {
        this.icCardManager = icCardManager;
    }

    //初始化usb驱动
    private void initCard()
    {
//        HardWareCommunicationUtils.initGpio();
//        int res= HardWareCommunicationUtils.operateGpio(IcCardManager.READ_MODE.getBytes());
//        res= HardWareCommunicationUtils.operateGpio(IcCardManager.READ_MODE1.getBytes());
        devHandle = HardWareCommunicationUtils.initUart(IcCardManager.PATH.getBytes(),icCardManager.speed);
        Message msg = new Message();
        msg.what = INIT_DEVICE_FINISH;
        msg.obj = devHandle;
        icCardManager.icCardHandler.sendMessage(msg);
    }

    public void getdriver() {

    }


    @Override
    public void run() {
        if(icCardManager.type == IcCardManager.TYPE_FINGER_EXHIBITION)
        {
            initCard();
        }
        else
        {
            getdriver();
        }
        super.run();
    }
}
