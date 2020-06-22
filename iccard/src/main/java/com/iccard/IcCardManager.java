package com.iccard;

import android.content.Context;

import com.iccard.handler.IcCardHandler;
import com.iccard.thread.InitdeviceThread;
import com.mjk.adplayer.utils.HardWareCommunicationUtils;
import com.mjk.adplayer.utils.HardwareInterface;

import java.net.PortUnreachableException;

public class IcCardManager {
    public volatile static IcCardManager icCardManager = null;
    public Context context;
    public int intmUartHandle = -1;
    public static final String READ_MODE = "RK30_PIN5_PC2,2,1,test";
    public byte[] path = new byte[1024];
    public int speed;
    public IcCardHandler icCardHandler;
    public InitdeviceThread initdeviceThread;
    public static IcCardManager init(Context context) {

        if (icCardManager == null) {
            synchronized (IcCardManager.class) {
                if (icCardManager == null) {
                    icCardManager = new IcCardManager(context);
                    icCardManager.icCardHandler = new IcCardHandler(icCardManager);
                    icCardManager.initdeviceThread = new InitdeviceThread(icCardManager);
                    icCardManager.initdeviceThread.start();

                }
                else
                {
                    icCardManager.context = context;
                    icCardManager.icCardHandler = new IcCardHandler(icCardManager);
                    icCardManager.initdeviceThread = new InitdeviceThread(icCardManager);
                    icCardManager.initdeviceThread.start();
                }
            }
        }
        return icCardManager;
    }

    public IcCardManager(Context context) {
        this.context = context;
//        System.loadLibrary("libuart-jni");
    }

    public void readDate() {
        byte[] buff = new byte[1024];
        int a = HardWareCommunicationUtils.readUart(icCardManager.intmUartHandle,buff,0,1024);
        int b = a + 1024;
        icCardManager.icCardHandler.sendEmptyMessageDelayed(IcCardHandler.CHECK_ICCARD_READ,1000);
    }
}
