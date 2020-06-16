package intersky.sign.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.sign.R;
import intersky.sign.SignManager;
import intersky.sign.entity.Sign;


public class SignAdapter extends BaseAdapter
{

	public ArrayList<Sign> mAttendanceModels;
	public int page = 1;
	private Context mContext;
	private LayoutInflater mInflater;
	private Handler mStatisticsHandler;
	public boolean isall = false;
	public DisplayMetrics metric;
	public SignAdapter(Context context, ArrayList<Sign> mAttendanceModels, Handler mStatisticsHandler)
	{
		this.mContext = context;
		this.mAttendanceModels = mAttendanceModels;
		this.mStatisticsHandler = mStatisticsHandler;
		this.metric = AppUtils.getWindowInfo(context);
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mAttendanceModels.size();
	}

	@Override
	public Sign getItem(int position)
	{
		// TODO Auto-generated method stub
		return mAttendanceModels.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		Sign mAttendanceModel = mAttendanceModels.get(position);

			convertView = mInflater.inflate(R.layout.sign_item, null);
			TextView mName = (TextView) convertView.findViewById(R.id.item_name);
			TextView mTime = (TextView) convertView.findViewById(R.id.item_time);
			TextView mAddress = (TextView) convertView.findViewById(R.id.address_text);
			TextView mRemark = (TextView) convertView.findViewById(R.id.remark_text);
			TextView mReason = (TextView) convertView.findViewById(R.id.reason_text);
		LinearLayout mImageView = (LinearLayout) convertView.findViewById(R.id.image_content);

		mAddress.setText(mAttendanceModel.mTitle);
		mTime.setText(mAttendanceModel.mTime.substring(0,5));
		mName.setText(mAttendanceModel.mName);
		if(mAttendanceModel.mRemark.length() == 0)
		{
			mRemark.setVisibility(View.GONE);
		}
		else
		{
			mRemark.setText(mAttendanceModel.mRemark);
		}

		if(mAttendanceModel.mReason.length() == 0)
		{
			mReason.setVisibility(View.GONE);
		}
		else
		{
			mReason.setText(mContext.getString(R.string.xml_attendanceat)+":"+mAttendanceModel.mReason);
		}


		if(mAttendanceModel.mImage.length() > 0)
		{
			ArrayList<Attachment> mimages = new ArrayList<Attachment>();
			getAttachments(mimages,mAttendanceModel.mImage);
			initImage(mimages,mImageView);
		}
		else
		{
			mImageView.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	public void showPic(ArrayList<Attachment> iamges, Attachment image)
	{
        Bus.callData(mContext,"filetools/showSeriasPic",iamges,image,false,false,"");
	}

	public class imageclick implements View.OnClickListener {

		public ArrayList<Attachment> attaches;
		public Attachment nowattache;

		public imageclick(ArrayList<Attachment> items , Attachment item)
		{
			this.attaches = items;
			this.nowattache = item;
		}

		@Override
		public void onClick(View v) {

			showPic(attaches,nowattache);
		}
	}

	public void initImage(ArrayList<Attachment> iamges, LinearLayout imageView)
	{
		for(int i = 0 ; i < (iamges.size()+2)/3 ; i++)
		{
			View mview = mInflater.inflate(R.layout.circle_image_item, null);
			imageView.addView(mview);
			for(int j = i*3 ; j < i*3+3 ; j++)
			{
				if(j <iamges.size())
				{
					Attachment mCircleImageModel = iamges.get(j);
					if(j %3 == 0)
					{
						ImageView image = (ImageView) mview.findViewById(R.id.image1);
						RelativeLayout layout1 = (RelativeLayout) mview.findViewById(R.id.answer);
						layout1.setOnClickListener(new imageclick(iamges,mCircleImageModel));
						image.setVisibility(View.VISIBLE);
						Bus.callData(mContext,"filetools/setfileimg",image,mCircleImageModel.mPath);
						RequestOptions options = new RequestOptions()
								.placeholder(R.drawable.plugin_camera_no_pictures);
						Glide.with(mContext).load(SignManager.getInstance().oaUtils.praseClodAttchmentUrl(mCircleImageModel.mRecordid, (int) (60* metric.density))).apply(options).into(new MySimpleTarget(image));
					}
					else if(j %3 == 1)
					{
						ImageView image = (ImageView) mview.findViewById(R.id.image2);
						RelativeLayout layout1 = (RelativeLayout) mview.findViewById(R.id.Support);
						layout1.setOnClickListener(new imageclick(iamges,mCircleImageModel));
						image.setVisibility(View.VISIBLE);
						Bus.callData(mContext,"filetools/setfileimg",image,mCircleImageModel.mPath);
						RequestOptions options = new RequestOptions()
								.placeholder(R.drawable.plugin_camera_no_pictures);
						Glide.with(mContext).load(SignManager.getInstance().oaUtils.praseClodAttchmentUrl(mCircleImageModel.mRecordid, (int) (60* metric.density))).apply(options).into(new MySimpleTarget(image));
					}
					else if(j %3 == 2)
					{
						ImageView image = (ImageView) mview.findViewById(R.id.image3);
						RelativeLayout layout1 = (RelativeLayout) mview.findViewById(R.id.share);
						layout1.setOnClickListener(new imageclick(iamges,mCircleImageModel));
						image.setVisibility(View.VISIBLE);
						Bus.callData(mContext,"filetools/setfileimg",image,mCircleImageModel.mPath);
						RequestOptions options = new RequestOptions()
								.placeholder(R.drawable.plugin_camera_no_pictures);
						Glide.with(mContext).load(SignManager.getInstance().oaUtils.praseClodAttchmentUrl(mCircleImageModel.mRecordid, (int) (60* metric.density))).apply(options).into(new MySimpleTarget(image));
					}
				}
			}
			if(i != (iamges.size()+2)/3 )
			{
				View emview = mInflater.inflate(R.layout.emptylayer, null);
				imageView.addView(emview);
			}

		}
	}

	public void getAttachments(ArrayList<Attachment> mimages, String hashs) {
		if (hashs.length() != 0) {
			String[] strs = hashs.split(",");

			for (int j = 0; j < strs.length; j++) {
				mimages.add(new Attachment(strs[j],strs[j]+".png", Bus.callData(mContext,"filetools/getfilePath", "/sign/image")+ "/" + strs[j] + ".png"
						, SignManager.getInstance().oaUtils.praseClodAttchmentUrl(strs[j]), 0, 0, "", SignManager.getInstance().oaUtils.praseClodAttchmentUrl(strs[j],60)));

			}
		}

	}

}
