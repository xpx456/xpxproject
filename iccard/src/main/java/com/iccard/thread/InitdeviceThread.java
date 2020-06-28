package com.iccard.thread;


import android.os.Message;
import com.iccard.IcCardManager;
import com.iccard.SerialPortFinder;
import com.mjk.adplayer.utils.HardWareCommunicationUtils;

import java.io.IOException;

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

    public void getdriver() {
        icCardManager.mSerialPortFinder = new SerialPortFinder();
        String[] entries = icCardManager.mSerialPortFinder.getAllDevices();
        String[] entryValues = icCardManager.mSerialPortFinder.getAllDevicesPath();
        String path = "";
        if(entries.length > 0)
        {
            for(int i = 0 ; i < entries.length ; i++)
            {
                if(entries[i].equals("ttyS1"))
                {
                    path = entryValues[i];
                }
            }
        }
        try {
            icCardManager.mSerialPort = icCardManager.getSerialPort(path);
            icCardManager.mOutputStream = icCardManager.mSerialPort.getOutputStream();
            icCardManager.mInputStream = icCardManager.mSerialPort.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
