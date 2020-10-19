package com.iccard.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iccard.ICCardReader;
import com.iccard.IcCardManager;
import com.iccard.thread.InitdeviceThread;
import com.iccard.thread.ReadCardThread;

public class IcCardHandler extends Handler {

    public static final int CHECK_ICCARD_READ = 10000;
    public static final int CHECK_ICCARD_SUCCESS = 10001;
    public static final int CHECK_ICCARD_FAIL = 10002;
    public IcCardManager icCardManager;

    public IcCardHandler(IcCardManager icCardManager)
    {
        this.icCardManager = icCardManager;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case InitdeviceThread.INIT_DEVICE_FINISH:
                icCardManager.intmUartHandle = (int) msg.obj;
                icCardManager.readDate();
                break;
            case ReadCardThread.GET_DATA:
                icCardManager.getCardData((String) msg.obj);
                break;
            case IcCardManager.CAR_NUM:
                String resultMsg = (String) msg.obj;
                Bundle bundle = msg.getData();
                String card_num = bundle.getString("card_num");
                Log.i("hgt", " card_num:"+ card_num);
//                mCardNum.setText(card_num);
//                mWriteCardStatus.setText("读取卡号成功");
                break;
            case IcCardManager.READ_CAR_NO:
//                String resultMsg = (String) msg.obj;
//                Bundle bundle = msg.getData();
//                String read_card_no = bundle.getString("read_card_no");
//                Log.i("hgt", " read_card_no:"+ read_card_no);
//                mReadCardNum.setText(read_card_no);
//                mWriteCardStatus.setText("读卡成功");
                break;
            case IcCardManager.WRITE_CARD_STATUS:
//                String resultMsg = (String) msg.obj;
//                Bundle bundle = msg.getData();
//                String write_status = bundle.getString("write_status");
//                mWriteCardStatus.setText(write_status);
                break;
            case IcCardManager.CAR_TYPE:
//                String resultMsg = (String) msg.obj;
//                Bundle bundle = msg.getData();
//                String card_type = bundle.getString("card_type");
//                Log.i("hgt", " card_type_name:"+ card_type +"a");
//                if(card_type.equals("08 00 "))//这个要根据自己定制的CPU卡类型而定，不一定所有的CPU卡都是0x800
//                    card_type = "CPU卡";
//                else
//                    card_type = "M1卡";
//                Log.i("hgt", " card_type:"+ card_type);
//                mCardType.setText(card_type);
                break;
            case IcCardManager.CAR_AST:
//                String resultMsg = (String) msg.obj;
//                Bundle bundle = msg.getData();
//                String card_ast = bundle.getString("ast");
//                Log.i("hgt", " card_ast:"+ card_ast);
//                mCpuCardCmdResult.setText(card_ast);
                break;
            case IcCardManager.CAR_COS:
//                String resultMsg = (String) msg.obj;
//                Bundle bundle = msg.getData();
//                String cpu_cos = bundle.getString("cpu_cos");
//                Log.i("hgt", " cpu_cos:"+ cpu_cos);
//                mCpuCardCmdResult.setText(cpu_cos);
                break;
            case CHECK_ICCARD_SUCCESS:
                icCardManager.getNameSuccess();
                break;
            case CHECK_ICCARD_FAIL:
                icCardManager.unFind();
                break;
            case CHECK_ICCARD_READ:
                icCardManager.setGetCardIds((String) msg.obj);
                break;
        }
    }


}
