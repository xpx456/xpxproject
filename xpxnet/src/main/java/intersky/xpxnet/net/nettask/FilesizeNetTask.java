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

public class FilesizeNetTask extends NetTask {

	public ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	public FilesizeNetTask(String url, Handler mHandler, int successEvent, Context mContext) {
		super(url, mHandler, successEvent, mContext);
		// TODO Auto-generated constructor stub
	}

	public FilesizeNetTask(String url, Handler mHandler, int successEvent, Context mContext, Object item) {
		super(url, mHandler, successEvent, mContext,item);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (NetUtils.checkNetWorkState(NetUtils.mContext)) {
			long size = NetUtils.getInstance().getfile(mUrl);
			if (size == 0) {
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
				NetObject netObject = new NetObject();
				netObject.result = String.valueOf(size);
				netObject.item = item;
				Message message = new Message();
				message.what = successEvent;
				message.obj = netObject;
				if(mHandler != null)
					mHandler.sendMessage(message);
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
