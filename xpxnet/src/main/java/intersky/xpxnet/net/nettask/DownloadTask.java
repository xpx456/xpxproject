package intersky.xpxnet.net.nettask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import intersky.xpxnet.net.Contral;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;

//import com.intersky.android.view.activity.MainFuncsActivity;

public class DownloadTask extends NetTask {

	public RandomAccessFile randomAccessFile;
	public Contral contral = new Contral();
	public DownloadTask(String url, Handler mHandler, int successEvent, Context mContext, File file) {
		super(url, mHandler, successEvent, mContext);
		try {
			if(file.exists())
			file.delete();
			randomAccessFile = new RandomAccessFile(file.getPath(),"rwd");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}

	public DownloadTask(String url, Handler mHandler, int successEvent, Context mContext,Object item) {
		super(url, mHandler, successEvent, mContext,item);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (NetUtils.checkNetWorkState(NetUtils.mContext)) {

			int request;
			try {
				request = NetUtils.getInstance().doDownload3(mUrl, randomAccessFile, contral);
				Message message = new Message();
				message.what = successEvent;
				message.arg1 = request;
				if (mHandler != null) {
					mHandler.sendMessage(message);

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
