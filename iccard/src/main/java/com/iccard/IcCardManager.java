package com.iccard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.iccard.handler.IcCardHandler;
import com.iccard.thread.CheckThread;
import com.iccard.thread.InitdeviceThread;
import com.iccard.thread.ReadCardThread;
import com.mjk.adplayer.utils.HardWareCommunicationUtils;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.LogHelper;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.module.idcard.IDCardReader;
import com.zkteco.android.biometric.module.idcard.IDCardReaderFactory;
import com.zkteco.android.biometric.module.idcard.exception.IDCardReaderException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IcCardManager {
    public static final String ACTION_FIND_CARD_DEVICE = "ACTION_FIND_CARD_DEVICE";
    public static final String ACTION_UN_FIND_CARD_DEVICE = "ACTION_UN_FIND_CARD_DEVICE";
    public static final int TYPE_FINGER_EXHIBITION = 0;
    public static final int TYPE_FINGER_RESTURANT = 1;
    public static final int TYPE_FINGER_DK = 2;
    public static final int TYPE_FINGER_ACCESS = 2;
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
    public static final String READ_MODE1 = "RK30_PIN5_PC2,4,1,test";
    public static final String PATH = "/dev/ttyS1";
    public byte[] path = new byte[1024];
    public int speed = 9600;
    public IcCardHandler icCardHandler;
    public InitdeviceThread initdeviceThread;
    public IDCardReader idCardReader = null;
    public CheckThread checkThread;
    public int type;
    public String cid = "";
    private final String idSerialName = "/dev/ttyS1";
    private final int idBaudrate = 115200;
    public HardWareCommunicationUtils hardWareCommunicationUtils;
    public ReadCardThread readCardThread;
    public ArrayList<GetCardId> getCardIds = new ArrayList<GetCardId>();
    private boolean bopen = false;
    public ICcardView iCcardView;
    public ICCardReader icCardReader;
    public UsbManager usbManager;
    public static IcCardManager init(Context context,int type) {

        if (icCardManager == null) {
            synchronized (IcCardManager.class) {
                if (icCardManager == null) {
                    icCardManager = new IcCardManager(context);
                    icCardManager.type = type;
                    icCardManager.icCardHandler = new IcCardHandler(icCardManager);
                    if(type == IcCardManager.TYPE_FINGER_EXHIBITION)
                    {
                        icCardManager.hardWareCommunicationUtils = new HardWareCommunicationUtils();
                        icCardManager.initdeviceThread = new InitdeviceThread(icCardManager);
                        icCardManager.initdeviceThread.start();

                    }
                    else
                    {
                        ICCardReader.addObserver(icCardManager.icCardReaderObserver);
                        icCardManager.icCardReader = ICCardReader.getInstance();
                        icCardManager.checkNeed();
                    }

                }
                else
                {
                    icCardManager.context = context;
                    icCardManager.type = type;
                    icCardManager.icCardHandler = new IcCardHandler(icCardManager);
                    if(type == IcCardManager.TYPE_FINGER_EXHIBITION)
                    {
                        icCardManager.hardWareCommunicationUtils = new HardWareCommunicationUtils();
                        icCardManager.initdeviceThread = new InitdeviceThread(icCardManager);
                        icCardManager.initdeviceThread.start();

                    }
                    else
                    {
                        ICCardReader.addObserver(icCardManager.icCardReaderObserver);
                        icCardManager.icCardReader = ICCardReader.getInstance();
                        icCardManager.checkNeed();
                    }

                }
            }
        }
        return icCardManager;
    }

    private boolean authenticate() {
        try {
            idCardReader.findCard(0);
            idCardReader.selectCard(0);
            return true;
        }
        catch (IDCardReaderException e)
        {
            return false;
        }
    }


    public void OnBnOpen()
    {
        try {
            startIDCardReader();
            if (bopen) return;
            idCardReader.open(0);
            bopen = true;
        }
        catch (IDCardReaderException e)
        {
            LogHelper.d("连接设备失败, 错误码：" + e.getErrorCode() + "\n错误信息：" + e.getMessage() + "\n 内部错误码=" + e.getInternalErrorCode());
        }
    }
    public String OnBnMFRead(int index)
    {
        try {
            if (!bopen) {
                return "";
            }
            byte mode = (byte)0x01;//写操作模式0或1
            byte blockCount = (byte)0x01;//要读多少块1-4
            byte startAddress = (byte)0x10;//16进制0x00-0x3F 即0到63块
            byte[]key = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};//6字节秘钥

            byte[] dataCard = new byte[16];//读出的卡数据
            byte[] cardNum = new byte[4];//读出卡序列号
            boolean ret=  idCardReader.MF_Read(index, mode, blockCount, startAddress, key,cardNum ,dataCard);

            if (ret) {
                return ToolUtils.bytesToHexString(cardNum);
            }
        } catch (IDCardReaderException e) {
            LogHelper.d("写操作失败, 错误码：" + e.getErrorCode() + "\n错误信息：" + e.getMessage() + "\n 内部错误码=" + e.getInternalErrorCode());
        }
        return "";
    }
//0501320181105000910177-1796894707
    public void OnBnGetSamID()
    {
        try {
            if (!bopen) {
                return;
            }
            String samid = idCardReader.getSAMID(0);
           String card = samid;

        }
        catch (IDCardReaderException e)
        {
            LogHelper.d("获取SAM模块失败, 错误码：" + e.getErrorCode() + "\n错误信息：" + e.getMessage() + "\n 内部错误码=" + e.getInternalErrorCode());
        }
    }

    private void startIDCardReader() {
        // Define output log level
        LogHelper.setLevel(Log.VERBOSE);
        // Start fingerprint sensor
        Map idrparams = new HashMap();
        String strSerialName = "";
        if (strSerialName != null && strSerialName.length() > 0)
        {
            idrparams.put(ParameterHelper.PARAM_SERIAL_SERIALNAME, strSerialName);
        }
        else
        {
            idrparams.put(ParameterHelper.PARAM_SERIAL_SERIALNAME, idSerialName);
        }

        idrparams.put(ParameterHelper.PARAM_SERIAL_BAUDRATE, idBaudrate);
        idCardReader = IDCardReaderFactory.createIDCardReader(context, TransportType.SERIALPORT, idrparams);

    }

    public IcCardManager(Context context) {
        this.context = context;
    }


    //1269748972   1487315
    public void readDate() {

        {
            if(readCardThread == null)
            {
                readCardThread = new ReadCardThread(icCardManager);
                readCardThread.start();
            }
            else
            {
                readCardThread.stop = true;
                readCardThread = new ReadCardThread(icCardManager);
                readCardThread.start();
            }
        }

    }

    public static String BytetoHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<b.length;i++) {
            sb.append(String.format("%02x ", b[i]));
        }
        return sb.toString();
    }


    public void getCardData(String b)
    {
        for(int i = 0 ; i < icCardManager.getCardIds.size() ; i++)
        {
            if(b.length() > 0)
                icCardManager.getCardIds.get(i).getGardId(b);
        }
    }


    public String praseIcCardIs(String iput)
    {
        if(iput.length() > 0)
        {
            String sixteen = Integer.toHexString(Integer.valueOf(iput));
            if(sixteen.length() >= 8)
            {
                int ten = Integer.parseInt(sixteen.substring(2,8), 16);
                return String.valueOf(ten);
            }
        }
        return "";
    }

    public interface GetCardId{
        public void getGardId(String id);
    }


    public void checkDevice(Context context,ICCardReader icCardManager,View location)
    {
//        iCcardView = new ICcardView(context);
//        iCcardView.creatView(location);
//        iCcardView.tip.setText(context.getString(R.string.first_ic_set));
        if(checkThread != null)
        {
            checkThread.stop = true;
        }
        checkThread = new CheckThread(icCardManager,this);
        checkThread.start();
    }


    public ICCardReaderObserver icCardReaderObserver = new ICCardReaderObserver()
    {

        @Override
        public void findCard(String id) {
            Message msg = new Message();
            msg.what = IcCardHandler.CHECK_ICCARD_READ;
            msg.obj = id;
            icCardHandler.sendMessage(msg);
        }

        @Override
        public void findICReader(Boolean isFound) {
            if(isFound == true)
            {
                Message msg = new Message();
                msg.what = IcCardHandler.CHECK_ICCARD_SUCCESS;
                icCardHandler.sendMessage(msg);
            }
            else
            {
                Message msg = new Message();
                msg.what = IcCardHandler.CHECK_ICCARD_FAIL;
                icCardHandler.sendMessage(msg);
            }
        }
    };

    public void getNameSuccess()
    {

        Intent intent = new Intent(IcCardManager.ACTION_FIND_CARD_DEVICE);
        context.sendBroadcast(intent);
    }

    public void unFind() {
        Intent intent = new Intent(IcCardManager.ACTION_UN_FIND_CARD_DEVICE);
        context.sendBroadcast(intent);
        icCardManager.icCardReader.searchDeviceOnThread();
    }

    public void checkNeed()
    {
//        SharedPreferences sharedPre = context.getSharedPreferences("device", 0);
//        String name = sharedPre.getString("devicename","");
//        if(name.length() == 0)
        {
            icCardManager.icCardReader.searchDeviceOnThread();
        }
//        else
//        {
//
//            icCardManager.icCardReader.openDevice(name,9600);
//        }
    }


    public void setGetCardIds(String id)
    {
        for(int i = 0 ; i < getCardIds.size() ; i++)
        {
            if(getCardIds.get(i) != null)
            getCardIds.get(i).getGardId(id);
        }
    }
}
