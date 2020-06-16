package intersky.xpxnet.net.nettask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;

public class PostJsonListNetTask extends NetTask {

    private String formBody;
    private ArrayList<String> urls = new ArrayList<String>();
    public int finalevent;
    public ArrayList<String> jsons = new ArrayList<String>();
    public ArrayList<Object> items = new ArrayList<Object>();
    public PostJsonListNetTask(Handler mHandler, int successEvent, int finalEvent, Context mContext, ArrayList<String> urls, ArrayList<String> formBodys, ArrayList<Object> items) {
        super(urls.get(0), mHandler, successEvent, mContext,formBodys.get(0));
        // TODO Auto-generated constructor stub
        this.finalevent = finalEvent;
        this.jsons.addAll(formBodys);
        this.urls.addAll(urls);
        this.items.addAll(items);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        if (NetUtils.checkNetWorkState(NetUtils.mContext)) {
            for(int i = 0 ; i < urls.size() ; i++)
            {
                ResposeResult request = NetUtils.getInstance().postJson(urls.get(i), jsons.get(i));
                if (request == null) {
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
                    if(request.isSuccess) {
                        NetObject netObject = new NetObject();
                        netObject.result = request.result;
                        netObject.item = item;
                        Message message = new Message();
                        message.what = successEvent;
                        message.obj = netObject;
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
            }
            mHandler.sendEmptyMessage(finalevent);

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

    }
}
