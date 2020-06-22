package com.iccard.thread;


import android.os.Message;
import com.iccard.IcCardManager;
import com.mjk.adplayer.utils.HardWareCommunicationUtils;

public class InitdeviceThread extends Thread {

    public static final int INIT_DEVICE_FINISH = 11000;
    public long devHandle;
    public IcCardManager icCardManager;
    public InitdeviceThread(IcCardManager icCardManager)
    {
        this.icCardManager = icCardManager;
    }

    //初始化usb驱动
    private void initCard()
    {
        HardWareCommunicationUtils.initGpio();
        HardWareCommunicationUtils.operateGpio(IcCardManager.READ_MODE.getBytes());
        icCardManager.intmUartHandle = HardWareCommunicationUtils.initUart(icCardManager.path,icCardManager.speed);
        Message msg = new Message();
        msg.what = INIT_DEVICE_FINISH;
        msg.obj = devHandle;
    }




    @Override
    public void run() {
        initCard();
        super.run();
    }
}
