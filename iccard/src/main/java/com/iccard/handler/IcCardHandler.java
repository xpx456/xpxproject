package com.iccard.handler;

import android.os.Handler;
import android.os.Message;

import com.iccard.IcCardManager;
import com.iccard.thread.InitdeviceThread;

public class IcCardHandler extends Handler {

    public static final int CHECK_ICCARD_READ = 10000;

    public IcCardManager icCardManager;

    public IcCardHandler(IcCardManager icCardManager)
    {
        this.icCardManager = icCardManager;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case CHECK_ICCARD_READ:
                icCardManager.readDate();
                break;
            case InitdeviceThread.INIT_DEVICE_FINISH:
                icCardManager.intmUartHandle = (int) msg.obj;
                icCardManager.readDate();
                break;

        }
    }


}
