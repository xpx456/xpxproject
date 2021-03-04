package com.iccard;

import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ICCardReader extends ICCardBaseReader {

    private SerialPortManager mSerialPortManager;
    private List<Device> workableDevices = new ArrayList<>();
    private AtomicBoolean foundICReader = new AtomicBoolean(false);
    private ExecutorService executorService;
    private Integer workMode = FIND_DEVICE;

    private static ICCardReader icCardReader = new ICCardReader();

    public static ICCardReader getInstance() {
        return icCardReader;
    }

    private ICCardReader() {
        initThreads();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> getDeviceNames() {
        ArrayList<Device> devices = mSerialPortFinder.getDevices();
        return devices.stream().map(device -> device.getName()).collect(Collectors.toList());
    }


    private Long cnt = 0L;

    public boolean openDevice(String deviceFD, Integer baudRate) {
        ArrayList<Device> devices = mSerialPortFinder.getDevices();

        cnt = System.currentTimeMillis();

        mSerialPortManager = new SerialPortManager();
        mSerialPortManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {

                Log.w("test", "time:"+(System.currentTimeMillis() - cnt));

                modeSelect(bytes);
                workMode = READ_CARD;
            }

            @Override
            public void onDataSent(byte[] bytes) {

            }
        });

        for (Device device : devices) {
            if (device.getFile().getName().equals(deviceFD)) {
                if (mSerialPortManager.openSerialPort(device.getFile(), baudRate) && mSerialPortManager.sendBytes(commands)) {
                    workableDevices.add(device);
                    return true;
                }
                mSerialPortManager.closeSerialPort();
            }
        }
        return false;
    }





    public void searchDeviceOnThread() {
        ArrayList<Device> devices = mSerialPortFinder.getDevices();
        foundICReader.set(false);
        workMode = FIND_DEVICE;

        for (Device device : devices) {
            executorService.execute(() -> openDevice(device));
        }
    }



    private synchronized void openDevice(Device device) {

        if (foundICReader.get()) return;

        SerialPortManager serialPortManager = new SerialPortManager();

        cnt = System.currentTimeMillis();
        String root = device.getRoot();
        serialPortManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {

                Log.w("test", "time:"+(System.currentTimeMillis() - cnt));

                modeSelect(bytes);

            }

            @Override
            public void onDataSent(byte[] bytes) {

            }
        });

        if (serialPortManager.openSerialPort(device.getFile(), defaultBaudRate) && serialPortManager.sendBytes(commands)) {
            SystemClock.sleep(2000);
        }

        if (foundICReader.get()) {
            workMode = READ_CARD;

            for (ICCardReaderObserver icCardReaderObserver: icCardReaderObservers) {

                if (icCardReaderObserver != null) {
                    icCardReaderObserver.findICReader(foundICReader.get());
                }
            }

        } else {
            serialPortManager.closeSerialPort();
        }

    }

    private void modeSelect(byte[] bytes) {
        switch (workMode) {
            case READ_CARD:{
                String icCard = null;

                if (bytes.length < 11) return;

                if (checkSumIn(bytes)) {
                    icCard = "";
                    for (int i=5;i<11;i++) {
                        icCard +=  String.format("%02x", bytes[i]);
                    }
                }

                for (ICCardReaderObserver observer: icCardReaderObservers) {
                    if (observer != null) {
                        observer.findCard(icCard);
                    }
                }
            }break;
            case FIND_DEVICE: {
                int i=0;
                while (!foundICReader.compareAndSet(false, true)
                && (MaxUpdateRetry > i++));
            }break;
        }
    }

    private void initThreads() {
        if (executorService == null || executorService.isTerminated()) {
            executorService = Executors.newCachedThreadPool();
        }
    }

    public synchronized void close() {
        if (mSerialPortManager != null) {
            mSerialPortManager.closeSerialPort();
        }
    }

    public void release() {
        close();
        executorService.shutdown();
    }

}
