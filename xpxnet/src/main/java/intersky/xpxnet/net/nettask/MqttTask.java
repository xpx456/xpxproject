package intersky.xpxnet.net.nettask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;

public class MqttTask extends NetTask {

    public String json = "";
    public String messageid = "";
    public Public send;
    public long time = 0;
    public int trycount = 1;
    public static final int MAX_TRYCOUNT = 3;
    public static final long TIME_OUT = 10000;

    public MqttTask(String url, Handler mHandler, int successEvent, Context mContext, String json,String messageid,Public send) {
        super(url, mHandler, successEvent, mContext);
        // TODO Auto-generated constructor stub
        this.json = json;
        this.send = send;
        this.messageid = messageid;
    }

    public MqttTask(String url, Handler mHandler, int successEvent, Context mContext, String json,String messageid, Object item,Public send) {
        super(url, mHandler, successEvent, mContext,item);
        // TODO Auto-generated constructor stub
        this.json = json;
        this.send = send;
        this.messageid = messageid;
    }


    public MqttTask(String url, Handler mHandler, int successEvent, Context mContext, String json,String messageid, checkToken checkToken,Public send) {
        super(url, mHandler, successEvent, mContext);
        // TODO Auto-generated constructor stub
        this.checkToken = checkToken;
        this.json = json;
        this.send = send;
        this.messageid = messageid;
    }

    public MqttTask(String url, Handler mHandler, int successEvent, Context mContext, String json,String messageid, Object item, checkToken checkToken,Public send) {
        super(url, mHandler, successEvent, mContext,item);
        // TODO Auto-generated constructor stub
        this.checkToken = checkToken;
        this.json = json;
        this.send = send;
        this.messageid = messageid;
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        if (NetUtils.checkNetWorkState(NetUtils.mContext)) {
            send.doPublc(json, this.mUrl,messageid);
            time = System.currentTimeMillis();
            while (true)
            {
                if(System.currentTimeMillis()-time > TIME_OUT)
                {
                    if(trycount > MAX_TRYCOUNT)
                    {
                        break;
                    }
                    else
                    {
                        trycount++;
                        send.doPublc(json, this.mUrl,messageid);
                        time = System.currentTimeMillis();
                    }

                }
            }
        }
    }

    public interface Public{
        public void doPublc(String json,String topic,String id);
    }
}
