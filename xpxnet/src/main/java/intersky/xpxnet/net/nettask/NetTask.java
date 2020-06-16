package intersky.xpxnet.net.nettask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.Contral;
import intersky.xpxnet.net.FinishItem;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;
import okhttp3.Call;


//import com.intersky.android.view.activity.MainFuncsActivity;

public class NetTask implements Runnable {

	public String mUrl;
	public Handler mHandler;
	public int successEvent;
	public Context mContext;
	public Call mCall;
	public String mRecordId = "";
	public Object item;
	public FinishItem finishItem;
	public checkToken checkToken;

	public NetTask(String url, Handler mHandler, int successEvent,  Context mContext) {
		this.mUrl = url;
		this.mHandler = mHandler;
		this.successEvent = successEvent;
		this.mContext = mContext;
		this.mRecordId = AppUtils.getguid();
	}

	public NetTask(String url, Handler mHandler, int successEvent,  Context mContext,Object item) {
		this.mUrl = url;
		this.mHandler = mHandler;
		this.successEvent = successEvent;
		this.mContext = mContext;
		this.item = item;
		this.mRecordId = AppUtils.getguid();
	}

	public NetTask(String url, Handler mHandler, int successEvent,  Context mContext,Object item,FinishItem finishItem) {
		this.mUrl = url;
		this.mHandler = mHandler;
		this.successEvent = successEvent;
		this.mContext = mContext;
		this.item = item;
		this.finishItem = finishItem;
		this.mRecordId = AppUtils.getguid();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (NetUtils.checkNetWorkState(NetUtils.mContext)) {
			mCall = NetUtils.getInstance().get(mUrl);
			ResposeResult request = NetUtils.getInstance().getUrl(mCall);


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
				if(request.isSuccess) {
					NetObject netObject = new NetObject();
					netObject.result = request.result;
					netObject.item = item;
					Message message = new Message();
					message.what = successEvent;
					message.obj = netObject;
					if (mHandler != null)
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

	}

	public interface checkToken{
		boolean checkToken(String result,String url);
	}
}
