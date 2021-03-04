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

public class PostNetListTask extends NetTask {

	public ArrayList<ArrayList<NameValuePair>> listdata = new ArrayList<ArrayList<NameValuePair>>();
	public ArrayList<String> urls = new ArrayList<String>();
	public ArrayList<Object> objects = new ArrayList<Object>();
	public ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	public PostNetListTask(ArrayList<String> urls, Handler mHandler, int successEvent, Context mContext, ArrayList<ArrayList<NameValuePair>> listdata) {
		super(urls.get(0), mHandler, successEvent, mContext);
		// TODO Auto-generated constructor stub
		this.listdata.addAll(listdata);
		this.urls.addAll(urls) ;
		this.nameValuePairs.addAll(nameValuePairs);
	}

	public PostNetListTask(ArrayList<String> urls, Handler mHandler, int successEvent, Context mContext, ArrayList<ArrayList<NameValuePair>> listdata, ArrayList<Object> objects) {
		super(urls.get(0), mHandler, successEvent, mContext,objects.get(0));
		// TODO Auto-generated constructor stub
		this.listdata.addAll(listdata);
		this.urls.addAll(urls) ;
		this.objects.add(objects);
		this.nameValuePairs.addAll(nameValuePairs);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (NetUtils.checkNetWorkState(NetUtils.mContext)) {
			for(int i = 0 ; i < urls.size() ; i++)
			{
				ResposeResult request = NetUtils.getInstance().post(urls.get(i), NetUtils.initRepuestBody(listdata.get(i)));
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
//						NetObject netObject = new NetObject();
//						netObject.result = request.result;
//						netObject.item = objects.get(i);
//						Message message = new Message();
//						message.what = successEvent;
//						message.obj = netObject;
//						if(mHandler != null)
//							mHandler.sendMessage(message);
					}
					else
					{
						if (mHandler != null)
						{
							NetObject netObject = new NetObject();
							netObject.result = urls.get(i);
							netObject.item = item;
							Message message = new Message();
							message.what = NetUtils.NO_INTERFACE;
							message.obj = urls.get(i);
							//mHandler.sendMessage(message);
						}
					}
				}
				if(i == urls.size()-1)
				{
					mHandler.sendEmptyMessage(successEvent);
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
