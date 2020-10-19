package com.iccard;

import com.kongqw.serialportlibrary.SerialPortFinder;

import java.util.ArrayList;
import java.util.List;

public abstract class ICCardBaseReader {
    protected String TAG = "ICCardReader";
    public static final int FIND_DEVICE = 0;
    public static final int READ_CARD = 1;
    protected byte [] commands = {0x01, 0x08, 0xA1 - 0xff - 1, 0x20, 0, 0, 0, 0x77};
    protected Integer MaxUpdateRetry = 500;
    protected SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    protected int defaultBaudRate = 9600;


    protected static List<ICCardReaderObserver> icCardReaderObservers = new ArrayList<>();

    public static void addObserver(ICCardReaderObserver icCardReaderObserver) {
        if (!icCardReaderObservers.contains(icCardReaderObserver)) {
            icCardReaderObservers.add(icCardReaderObserver);
        }
    }

    public static void removeObserver(ICCardReaderObserver icCardReaderObserver) {
        icCardReaderObservers.remove(icCardReaderObserver);
    }

    protected boolean checkSumIn(byte[] buf) {
        if (buf == null) return false;
        int checkSum = 0;
        for (int i=0;i<buf.length-1;i++) {
            checkSum ^= buf[i];
        }
        return buf[buf.length-1] == ~checkSum;
    }



}
