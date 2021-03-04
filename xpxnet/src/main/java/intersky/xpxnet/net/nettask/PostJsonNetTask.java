package intersky.xpxnet.net.nettask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;

public class PostJsonNetTask extends NetTask {

    public String json = "";


    public PostJsonNetTask(String url, Handler mHandler, int successEvent,Context mContext, String json) {
        super(url, mHandler, successEvent, mContext);
        // TODO Auto-generated constructor stub
        this.json = json;
    }

    public PostJsonNetTask(String url, Handler mHandler, int successEvent,Context mContext, String json,Object item) {
        super(url, mHandler, successEvent, mContext,item);
        // TODO Auto-generated constructor stub
        this.json = json;
    }


    public PostJsonNetTask(String url, Handler mHandler, int successEvent,Context mContext, String json,checkToken checkToken) {
        super(url, mHandler, successEvent, mContext);
        // TODO Auto-generated constructor stub
        this.checkToken = checkToken;
        this.json = json;
    }

    public PostJsonNetTask(String url, Handler mHandler, int successEvent,Context mContext, String json,Object item,checkToken checkToken) {
        super(url, mHandler, successEvent, mContext,item);
        // TODO Auto-generated constructor stub
        this.checkToken = checkToken;
        this.json = json;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        if (NetUtils.checkNetWorkState(NetUtils.mContext)) {
            ResposeResult request = NetUtils.getInstance().postJson(mUrl,json);
            if(request == null)
            {
                if (mHandler != null) {
                    NetObject netObject = new NetObject();
                    netObject.result = mUrl;
                    netObject.item = item;
                    Message msg = new Message();
                    msg.what = NetUtils.NO_NET_WORK;
                    msg.obj = netObject;
                    mHandler.sendMessage(msg);
                }
            }
            else
            {
                if(checkToken != null)
                {
                    if(checkToken.checkToken(request.result,this.mUrl) == false)
                    {
                        NetObject netObject = new NetObject();
                        netObject.result = mUrl;
                        netObject.item = item;
                        Message msg = new Message();
                        msg.what = NetUtils.TOKEN_ERROR;
                        msg.obj = netObject;
                        mHandler.sendMessage(msg);
                        return;
                    }
                }


                if(request.isSuccess) {
                    NetObject netObject = new NetObject();
                    netObject.result = request.result;
                    netObject.item = item;
                    Message message = new Message();
                    message.what = successEvent;
                    message.obj = netObject;
                    if(mHandler != null)
                    mHandler.sendMessage(message);
                }
                else
                {
                    if (mHandler != null)
                    {
                        NetObject netObject = new NetObject();
                        netObject.result = mUrl;
                        netObject.item = item;
                        Message message = new Message();
                        message.what = NetUtils.NO_INTERFACE;
                        message.obj = netObject;
                        mHandler.sendMessage(message);
                    }
                }
            }
        } else {
            if (mHandler != null) {
                NetObject netObject = new NetObject();
                netObject.result = mUrl;
                netObject.item = item;
                Message msg = new Message();
                msg.what = NetUtils.NO_NET_WORK;
                msg.obj = netObject;
                mHandler.sendMessage(msg);
            }

        }
        if(endCallback != null)
        {
            endCallback.doremove(mRecordId);
        }
    }
}
