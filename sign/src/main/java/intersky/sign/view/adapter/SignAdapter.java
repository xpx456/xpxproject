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
			TextView mRemark = (TextView) convertView.findViewById(R.id.remark_text);
		String[] address = mAttendanceModel.mAddress.split(",");
		mTime.setText(mAttendanceModel.mDate.substring(5,10).replace("-",mContext.getString(R.string.month))
				.replace("-",mContext.getString(R.string.day))+" "+mAttendanceModel.mTime.substring(0,5));
		mName.setText(mAttendanceModel.mAddressName);
		if(address.length >=1)
		mRemark.setText(address[1]);
		else
			mRemark.setText(mAttendanceModel.mAddress);
		return convertView;
	}



}
