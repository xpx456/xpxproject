package intersky.xpxnet.net.nettask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;

//import com.intersky.android.view.activity.MainFuncsActivity;

public class PostNetTask extends NetTask {

	public ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	public PostNetTask(String url, Handler mHandler, int successEvent,  Context mContext, ArrayList<NameValuePair> nameValuePairs) {
		super(url, mHandler, successEvent, mContext);
		// TODO Auto-generated constructor stub
		this.nameValuePairs.addAll(nameValuePairs);
	}

	public PostNetTask(String url, Handler mHandler, int successEvent,Context mContext, ArrayList<NameValuePair> nameValuePairs,Object item) {
		super(url, mHandler, successEvent, mContext,item);
		// TODO Auto-generated constructor stub
		this.nameValuePairs.addAll(nameValuePairs);
	}

	public PostNetTask(String url, Handler mHandler, int successEvent,  Context mContext, ArrayList<NameValuePair> nameValuePairs,checkToken checkToken) {
		super(url, mHandler, successEvent, mContext);
		// TODO Auto-generated constructor stub
		this.checkToken = checkToken;
		this.nameValuePairs.addAll(nameValuePairs);
	}

	public PostNetTask(String url, Handler mHandler, int successEvent,Context mContext, ArrayList<NameValuePair> nameValuePairs,Object item,checkToken checkToken) {
		super(url, mHandler, successEvent, mContext,item);
		// TODO Auto-generated constructor stub
		this.checkToken = checkToken;
		this.nameValuePairs.addAll(nameValuePairs);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (NetUtils.checkNetWorkState(NetUtils.mContext)) {
			ResposeResult request = NetUtils.getInstance().post(mUrl, NetUtils.initRepuestBody(nameValuePairs));

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
		}
		else {
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
