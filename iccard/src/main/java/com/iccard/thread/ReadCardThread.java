package com.iccard.thread;

import android.os.Message;
import android.util.Log;

import com.iccard.IcCardManager;
import com.iccard.handler.IcCardHandler;
import com.mjk.adplayer.utils.HardWareCommunicationUtils;
import com.zkteco.android.biometric.module.idcard.exception.IDCardReaderException;

import java.io.IOException;
import java.nio.ByteBuffer;

import intersky.apputils.AppUtils;

public class ReadCardThread extends Thread {

    public static final int GET_DATA = 10500;
    public IcCardManager icCardManager;
    public boolean stop = false;
    public ReadCardThread(IcCardManager icCardManager) {
        this.icCardManager = icCardManager;
    }

    @Override
    public void run() {
        super.run();
        Log.i("chuan2", "----string");

        while(true) {
            if(icCardManager.type == IcCardManager.TYPE_FINGER_EXHIBITION) {
                ByteBuffer mUartDirectBuffer= ByteBuffer.allocateDirect(1024);
                int a = HardWareCommunicationUtils.readUart(icCardManager.intmUartHandle,mUartDirectBuffer,1024,0);
                String sss = "";
                sss = new String(AppUtils.decodeValue(mUartDirectBuffer));
                String b = "";
                if(a > 0)
                {
                    int start = sss.indexOf("$")+1;
                    int end = sss.indexOf("\r\n");
                    if(start != -1 && end != -1 && start < end && end < sss.length())
                        b = sss.substring(start,end);
                }
                //1269748972 //A148,7315//11457772//4b ae d4 ec//aed4ec
                Message message = new Message();
                message.obj =b;
                message.what = GET_DATA;
                if(icCardManager != null)
                {
                    if(icCardManager.icCardHandler != null)
                    {
                        if(b.length() > 0)
                            icCardManager.icCardHandler.sendMessage(message);
                    }
                }
                try {
                    if(b.length() == 0)
                        sleep(100);
                    else
                        sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                String card = icCardManager.OnBnMFRead(0);
                Message message = new Message();
                message.obj =card;
                message.what = GET_DATA;
                if(icCardManager != null)
                {
                    if(icCardManager.icCardHandler != null)
                    {
                        if(card.length() > 0)
                            icCardManager.icCardHandler.sendMessage(message);
                    }
                }
                try {
                    if(card.length() == 0)
                        sleep(100);
                    else
                        sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(stop)
            {
                return;
            }
        }
    }
}
