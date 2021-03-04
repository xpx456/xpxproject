package intersky.function.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.function.handler.WebAppHandler;
import intersky.function.presenter.WebAppPresenter;
import intersky.function.view.activity.WebAppActivity;
import intersky.scan.ScanUtils;


@SuppressLint("NewApi")
public class WebAppReceiver extends BaseReceiver {

	public static final String GET_PICTURE_PATH= "get_picture_path";
	public Handler mHandler;


	public WebAppReceiver(Handler mHandler)
	{

		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(GET_PICTURE_PATH);
		intentFilter.addAction(ScanUtils.ACTION_SCAN_FINISH);
		intentFilter.addAction(WebAppActivity.ACTION_WEBAPP_ADDPICTORE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(GET_PICTURE_PATH))
		{
			Message msg = new Message();
			msg.what = WebAppActivity.GET_PIC_PATH;
			msg.obj = intent.getStringExtra("path");
			if(mHandler != null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(ScanUtils.ACTION_SCAN_FINISH))
		{
			Message msg = new Message();
			msg.what = WebAppActivity.SCANNIN_GREQUEST_CODE_WITH_JSON;
			msg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(WebAppActivity.ACTION_WEBAPP_ADDPICTORE))
		{
			Message msg = new Message();
			msg.what = WebAppHandler.ADD_PICTURE;
			msg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(msg);
		}
	}

}
