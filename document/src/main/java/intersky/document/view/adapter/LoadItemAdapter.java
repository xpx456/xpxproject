package intersky.document.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Actions;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Attachment;
import intersky.apputils.TimeUtils;
import intersky.document.DocumentManager;
import intersky.document.R;
import intersky.document.entity.DocumentNet;
import intersky.document.handler.DocumentHandler;
import intersky.document.handler.DownUpHandler;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.mywidget.RoundProgressBar;

@SuppressLint({ "InflateParams", "ViewHolder", "CutPasteId" })
public class LoadItemAdapter extends BaseAdapter {
	private ArrayList<DocumentNet> mDocumentNets;
	private Context mContext;
	private LayoutInflater mInflater;
	private Handler mHandler;
	public int type = 0;
	public LoadItemAdapter(Context context, ArrayList<DocumentNet> mDocumentNets, Handler mHandler, int type) {
		this.mContext = context;
		this.mDocumentNets = mDocumentNets;
		this.mHandler = mHandler;
		this.type = type;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDocumentNets.size();
	}

	@Override
	public DocumentNet getItem(int position) {
		// TODO Auto-generated method stub
		return mDocumentNets.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DocumentNet mLoadItem = getItem(position);

		switch (mLoadItem.mType) {
		case DocumentManager.TYPE_DOWNLOAD_NOMAL:
		case DocumentManager.TYPE_UPLOAD_NOMAL:
			if(mLoadItem.mState == DocumentManager.STATE_FINISH)
			{
				convertView = mInflater.inflate(R.layout.item_loadfinish, null);
			}
			else
			{
				convertView = mInflater.inflate(R.layout.item_load, null);
			}
			ImageView imageView = convertView.findViewById(R.id.conversation_img);
			Bus.callData(mContext,"filetools/setfileimg",imageView,mLoadItem.mPath);
			if((int)Bus.callData(mContext,"filetools/getFileType",mLoadItem.mPath) == Actions.FILE_TYPE_PICTURE)
			{
				RequestOptions options = new RequestOptions()
						.placeholder(R.drawable.plugin_camera_no_pictures);
				if(mLoadItem.mState == DocumentManager.STATE_FINISH)
				Glide.with(mContext).load(new File(mLoadItem.mPath)).apply(options).into(imageView);
				else
					imageView.setImageResource(R.drawable.plugin_camera_no_pictures);
			}
			TextView size = (TextView) convertView.findViewById(R.id.size);
			TextView mName = (TextView) convertView.findViewById(R.id.item_name);
			ImageView mStateIcon = (ImageView) convertView.findViewById(R.id.item_state_icon);
			RelativeLayout mRoundProgressBarLayer = (RelativeLayout) convertView.findViewById(R.id.item_ProgressBar_layer);
			RoundProgressBar mRoundProgressBar = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBar);
			RelativeLayout mSelectIconLayer = (RelativeLayout) convertView.findViewById(R.id.item_selecticon_layer);
			ImageView mSelectIcon = (ImageView) convertView.findViewById(R.id.item_selecticon);
			TextView mDate = (TextView) convertView.findViewById(R.id.item_date);
			mSelectIconLayer.setOnClickListener(new DocumentListener(mLoadItem));
			mName.setText(mLoadItem.mName);
			mSelectIconLayer.setVisibility(View.VISIBLE);
			mSelectIcon.setVisibility(View.VISIBLE);
			if (mLoadItem.isSelect == true) {
				mSelectIcon.setImageResource(R.drawable.bselect);
			}
			else {
				mSelectIcon.setImageResource(R.drawable.bunselect);
			}
			if (mLoadItem.mState == DocumentManager.STATE_FINISH) {
				mRoundProgressBarLayer.setVisibility(View.INVISIBLE);
				mDate.setText(TimeUtils.getDateAndTime2());
				size.setText(AppUtils.getSizeText(mLoadItem.mSize) );
			}
			else {
				mRoundProgressBarLayer.setVisibility(View.VISIBLE);
				mStateIcon.setOnClickListener(new LoadListener(mLoadItem));
				if (mLoadItem.mState == DocumentManager.STATA_DOWNLOADING) {
					mStateIcon.setImageResource(R.drawable.dpause);
				}
				else {
					mStateIcon.setImageResource(R.drawable.ding);
				}
				mRoundProgressBar.setMax(1000);
				if (mLoadItem.mSize > 0) {
					long a = mLoadItem.mFinishSize;
					long b = mLoadItem.mSize;
					long c = 1000 * a / b;
					Log.d("progresss", String.valueOf(a) + ":" + String.valueOf(b) + ":" + String.valueOf(c));
					mRoundProgressBar.setProgress((int) c);
				}
				else {
					mRoundProgressBar.setProgress(0);
				}

				mDate.setText(AppUtils.getSizeText(mLoadItem.mFinishSize) + "/"
						+ AppUtils.getSizeText(mLoadItem.mSize));
				size.setText(AppUtils.getSizeText(mLoadItem.speed)+"/s" );

			}
			break;
		case DocumentManager.TYPE_DOWNLOAD_UNFINISH:
		case DocumentManager.TYPE_DOWNLOAD_FINISH:
		case DocumentManager.TYPE_UPLOAD_UNFINISH:
		case DocumentManager.TYPE_UPLOAD_FINISH:
			convertView = mInflater.inflate(R.layout.loadtitle, null);
			TextView mTitle1 = (TextView) convertView.findViewById(R.id.titlecount);
			if(DocumentManager.TYPE_DOWNLOAD_UNFINISH == mLoadItem.mType)
				mTitle1.setText(mContext.getString(R.string.document_downloading)+"  (" + mLoadItem.mState + ")");
			if(DocumentManager.TYPE_DOWNLOAD_FINISH == mLoadItem.mType)
				mTitle1.setText(mContext.getString(R.string.document_downloadfinish)+"  (" + mLoadItem.mState + ")");
			if(DocumentManager.TYPE_UPLOAD_UNFINISH == mLoadItem.mType)
				mTitle1.setText(mContext.getString(R.string.document_uploading)+"  (" + mLoadItem.mState + ")");
			if(DocumentManager.TYPE_UPLOAD_FINISH == mLoadItem.mType)
				mTitle1.setText(mContext.getString(R.string.document_uploadfinish)+"  (" + mLoadItem.mState + ")");
			break;
		}
		return convertView;
	}

	private class DocumentListener implements OnClickListener {

		DocumentNet mLoadItem;

		public DocumentListener(DocumentNet mtLoadItem) {
			mLoadItem = mtLoadItem;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mLoadItem.isSelect == false) {
				mLoadItem.isSelect  = true;
				if(type == 0) {
					if(DocumentManager.getInstance().downSelectCount == 0)
					{
						mHandler.sendEmptyMessage(DocumentHandler.EVENT_SHOW_DOWNLOAD_EDIT);
					}
					DocumentManager.getInstance().downSelectCount++;
				}
				else {
					if(DocumentManager.getInstance().upSelectCount == 0)
					{
						mHandler.sendEmptyMessage(DocumentHandler.EVENT_SHOW_UPLOAD_EDIT);
					}
					DocumentManager.getInstance().upSelectCount++;
				}

			}
			else {
				mLoadItem.isSelect = false;
				if(type == 0) {
					DocumentManager.getInstance().downSelectCount--;
					if(DocumentManager.getInstance().downSelectCount == 0)
					{
						mHandler.sendEmptyMessage(DocumentHandler.EVENT_HIT_DOWNLOAD_EDIT);
					}
				}
				else {
					DocumentManager.getInstance().upSelectCount--;
					if(DocumentManager.getInstance().upSelectCount == 0)
					{
						mHandler.sendEmptyMessage(DocumentHandler.EVENT_HIT_UPLOAD_EDIT);
					}
				}
			}
			if(mHandler!= null)
			mHandler.sendEmptyMessage(DocumentHandler.EVENT_UPDATA_LOAD_LIST);
		}

	}

	private class LoadListener implements OnClickListener {

		DocumentNet mLoadItem;

		@SuppressWarnings("unused")
		public LoadListener(DocumentNet mtDownLoadModel) {
			mLoadItem = mtDownLoadModel;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mLoadItem.mState == DocumentManager.STATA_DOWNLOADING || mLoadItem.mState == DocumentManager.STATA_PAUSE )
			{
				Message message = new Message();
				message.what = DownUpHandler.EVETN_DOWNLOAD_PAUSE;
				message.obj = mLoadItem;
				DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
			}
			else
			{
				Message message = new Message();
				message.what = DownUpHandler.EVETN_DOWNLOAD_START;
				message.obj = mLoadItem;
				DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
			}
//			if(mHandler!= null)
//			mHandler.sendEmptyMessage(DocumentHandler.EVENT_UPDATA_DOWNLOAD_LIST);
		}

	}
}