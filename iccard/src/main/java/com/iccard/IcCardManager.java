package com.iccard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.iccard.entity.SerialPort;
import com.iccard.handler.IcCardHandler;
import com.iccard.thread.InitdeviceThread;
import com.iccard.thread.ReadThread;
import com.mjk.adplayer.utils.HardWareCommunicationUtils;
import com.mjk.adplayer.utils.HardwareInterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.PortUnreachableException;
import java.security.InvalidParameterException;

public class IcCardManager {
    public static final int TYPE_FINGER_EXHIBITION = 0;
    public static final int TYPE_FINGER_RESTURANT = 1;
    public final byte COM_PKT_CMD_QUERY_MODE = 0xD; //自动读取卡号
    public final byte COM_PKT_CMD_CARD_TYPE = 0xF; //数据类型为卡号
    public final byte  COM_PKT_CMD_INIT_TYPEA	= 0x1E;
    public final byte  COM_PKT_CMD_INIT_TYPEB	 =	0x1F;
    public final byte  COM_PKT_CMD_REQA  = 	0x20; // A类卡
    public final byte  COM_PKT_CMD_TYPEA_HALT   =	0x21;
    public final byte  COM_PKT_CMD_TYPEA_MF1_READ  =	0x22;//M1 卡 读
    public final byte  COM_PKT_CMD_TYPEA_MF1_WRITE   =	0x23;//M1 卡 写
    public final byte  COM_PKT_CMD_TYPEA_MF1_WALLET_INIT  = 	0x24;
    public final byte  COM_PKT_CMD_TYPEA_MF1_WALLET_INCREMENT = 0x25;
    public final byte  COM_PKT_CMD_TYPEA_MF1_WALLET_DECREMENT = 0x26;
    public final byte  COM_PKT_CMD_TYPEA_MF1_WALLET_READ   =    0x27;
    public final byte  COM_PKT_CMD_TYPEA_MF0_READ       =       0x28;
    public final byte  COM_PKT_CMD_TYPEA_MF0_WRITE     =        0x29;
    public final byte  COM_PKT_CMD_TYPEA_RATS			=		0x2A;
    public final byte  COM_PKT_CMD_EXCHANGE				=	0x2B;
    public final byte  COM_PKT_CMD_DESELECT				=	0x2C;
    public final byte  COM_PKT_CMD_MULTI_EXCHANGE_TEST	=		0x2D;
    public final byte  COM_PKT_CMD_TEST_STOP			=		0x2E;
    public final byte COM_PKT_CMD_OUTPUT_MODE 	=	0x38;
    public final byte COM_PKT_CMD_BEER_ON_OFF	=	 0x39;
    public final byte COM_PKT_CMD_DATA_POLAR	=	 0x3a;
    public final byte COM_PKT_CMD_DATA_FORMAT	=	 0x3b;
    public final byte COM_PKT_CMD_CPU_RST       =    0x3c; //CPU 卡复位
    public final byte COM_PKT_CMD_CPU_COS       =    0x3d;//CPU 卡发送COS指令
    public static final byte  CAR_NUM			=		1;
    public static final byte  READ_CAR_NO			=		2;
    public static final byte  WRITE_CARD_STATUS			=		3;
    public static final byte  CAR_TYPE             =       4;
    public static final byte  CAR_AST             = 5;
    public static final byte  CAR_COS             = 6;
    public static int butrate = 115200;
    public volatile static IcCardManager icCardManager = null;
    public Context context;
    public int intmUartHandle = -1;
    public static final String READ_MODE = "RK30_PIN5_PC2,2,1,test";
    public byte[] path = new byte[1024];
    public int speed;
    public IcCardHandler icCardHandler;
    public InitdeviceThread initdeviceThread;
    public SerialPortFinder mSerialPortFinder;
    public SerialPort mSerialPort;
    public int type;
    public OutputStream mOutputStream;
    public InputStream mInputStream;
    public ReadThread readThread;
    public static IcCardManager init(Context context,int type) {

        if (icCardManager == null) {
            synchronized (IcCardManager.class) {
                if (icCardManager == null) {
                    icCardManager = new IcCardManager(context);
                    icCardManager.type = type;
                    icCardManager.icCardHandler = new IcCardHandler(icCardManager);
                    icCardManager.initdeviceThread = new InitdeviceThread(icCardManager);
                    icCardManager.initdeviceThread.start();
                    if(icCardManager.type == TYPE_FINGER_EXHIBITION)
                    {


                    }
                    else
                    {
//                        icCardManager.mSerialPortFinder = new SerialPortFinder();
                    }
                }
                else
                {
                    icCardManager.context = context;
                    icCardManager.type = type;
                    icCardManager.icCardHandler = new IcCardHandler(icCardManager);
                    icCardManager.initdeviceThread = new InitdeviceThread(icCardManager);
                    icCardManager.initdeviceThread.start();
                    if(icCardManager.type == TYPE_FINGER_EXHIBITION)
                    {
//                        icCardManager.initdeviceThread = new InitdeviceThread(icCardManager);
//                        icCardManager.initdeviceThread.start();
                    }
                    else
                    {
//                        icCardManager.mSerialPortFinder = new SerialPortFinder();

                    }
                }
            }
        }
        return icCardManager;
    }

    public IcCardManager(Context context) {
        this.context = context;
    }

    public void readDate() {
        if(type == TYPE_FINGER_EXHIBITION)
        {
            byte[] buff = new byte[1024];
            int a = HardWareCommunicationUtils.readUart(icCardManager.intmUartHandle,buff,0,1024);
            int b = a + 1024;
            icCardManager.icCardHandler.sendEmptyMessageDelayed(IcCardHandler.CHECK_ICCARD_READ,1000);
        }
        else
        {
            icCardManager.readThread = new ReadThread(icCardManager);
            icCardManager.readThread.start();
        }

    }


    public SerialPort getSerialPort(String ptah) throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            String path = ptah;
            int baudrate = butrate ;

            Log.i("chuan", "path="+path);
            Log.i("chuan", "baudrate="+baudrate);

            /* Check parameters */
            if ( (path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }



    public void make_packet(byte cmd, byte[] buf, byte len)
    {
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();

        byte sum = 0;
        byte[] send_buff = new byte[len+4];
        sum += 0x68;
        //sendc(HEAD);
        send_buff[0] = 0x68;

        try {
            mOutputStream.write(0x68);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sum += len + 4;
        //sendc(len + 4);
        send_buff[1] = (byte)(len+4);
        try {
            mOutputStream.write(len+4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sum += cmd;
        //sendc(cmd);
        try {
            mOutputStream.write(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send_buff[2] = cmd;
        for(int i=0;i<len;i++)
        {
            sum +=buf[i];
            send_buff[3+i] = buf[i];
            try {
                mOutputStream.write(buf[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        send_buff[len+3] = sum;
        try {
            mOutputStream.write(sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sendc(sum);
    }

    public
    void parase_pkg(byte[] buffer)
    {
        if((buffer[0] == 0x68)&&(buffer[1] > 0 )&&(buffer[1] < 64 ))
        {
            byte len = buffer[1];
            byte sum = check_sum(buffer,len-1);
            if(sum == buffer[len-1])
                parase_cmd(buffer);
        }
    }

    byte check_sum(byte[]  buf, int len)
    {
        byte sum = 0;
        int i=0;
        for(i=0;i<len;i++)
            sum += buf[i];
        return sum;
    }

    public static String BytetoHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<b.length;i++) {
            sb.append(String.format("%02x ", b[i]));
        }
        return sb.toString();
    }

    void parase_cmd(byte[] buffer)
    {
        byte cmd = buffer[2];
        byte len = buffer[1];
        Message msg = new Message();
        Bundle bundle = new Bundle();
        Log.i("hgt", " cmd:0x"+ String.format("%02x ", cmd));
        switch(cmd)
        {
            /*                0x4400 = Mifare_UltraLight
             *                0x4400 = Mifare_One(S50_0)
             *                0x0400 = Mifare_One(S50_3)
             *                0x0200 = Mifare_One(S70_0)
             *                0x4200 = Mifare_One(S70_3)
             *                0x0800 = Mifare_Pro
             *                0x0403 = Mifare_ProX
             *                0x4403 = Mifare_DESFire
             */
            case COM_PKT_CMD_CARD_TYPE:

                byte[] card_type = new byte[2];
                for(int i = 0;i < 2;i++)
                {
                    card_type[i]=buffer[i+3];
                }

                msg.what = CAR_TYPE;
                msg.obj = "传递的内容";
                bundle.putString("card_type",BytetoHex(card_type));
                msg.setData(bundle);
                icCardHandler.sendMessage(msg);
                break;
            case COM_PKT_CMD_REQA:
                byte[] card_num = new byte[buffer[3]];
                for(int i = 0;i < buffer[3];i++)
                {
                    card_num[i]=buffer[i+4];
                }
                //Log.i("hgt", " card_num:"+ BytetoHex(card_num));
                //mCardNum.setText(card_num.toString());

                msg.what = CAR_NUM;
                msg.obj = "传递的内容";
                bundle.putString("card_num",BytetoHex(card_num));
                msg.setData(bundle);
                icCardHandler.sendMessage(msg);

                break;
            case COM_PKT_CMD_TYPEA_MF1_READ:
                byte[] buf = new byte[len-5];
                for(int i = 0;i < len-5;i++)
                {
                    buf[i]=buffer[i+4];
                }

                msg.what = READ_CAR_NO;
                msg.obj = "传递的内容";
                bundle.putString("read_card_no",BytetoHex(buf));
                msg.setData(bundle);
                icCardHandler.sendMessage(msg);
                break;

            case COM_PKT_CMD_TYPEA_MF1_WRITE:
                byte write_status = buffer[3];

                msg.what = WRITE_CARD_STATUS;
                msg.obj = "传递的内容";
                if(write_status == 0)
                    bundle.putString("write_status","写卡成功");
                else
                    bundle.putString("write_status","写卡失败");
                msg.setData(bundle);
                icCardHandler.sendMessage(msg);
                break;

            case COM_PKT_CMD_CPU_RST:
                byte[] ast = new byte[buffer[1]-4];
                for(int i = 0;i < buffer[1]-4;i++)
                {
                    ast[i]=buffer[i+3];
                }
                //Log.i("hgt", " card_num:"+ BytetoHex(card_num));
                //mCardNum.setText(card_num.toString());

                msg.what = CAR_AST;
                msg.obj = "传递的内容";
                bundle.putString("ast",BytetoHex(ast));
                msg.setData(bundle);
                icCardHandler.sendMessage(msg);
                break;

            case COM_PKT_CMD_CPU_COS:
                byte[] cpu_cos = new byte[buffer[1]-4];
                for(int i = 0;i < buffer[1]-4;i++)
                {
                    cpu_cos[i]=buffer[i+3];
                }
                //Log.i("hgt", " card_num:"+ BytetoHex(card_num));
                //mCardNum.setText(card_num.toString());

                msg.what = CAR_COS;
                msg.obj = "传递的内容";
                bundle.putString("cpu_cos",BytetoHex(cpu_cos));
                msg.setData(bundle);
                icCardHandler.sendMessage(msg);
                break;
        }
    }
}
