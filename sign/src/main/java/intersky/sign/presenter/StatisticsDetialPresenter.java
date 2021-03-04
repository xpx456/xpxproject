package intersky.sign.presenter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.TileOverlayOptions;
import com.amap.api.maps2d.model.TileProvider;
import com.amap.api.maps2d.model.UrlTileProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.mywidget.MyLinearLayout;
import intersky.sign.R;
import intersky.sign.SignManager;
import intersky.sign.view.activity.StatisticsDetialActivity;
import intersky.sign.view.adapter.SignAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class StatisticsDetialPresenter implements Presenter {

	private StatisticsDetialActivity mStatisticsDetialActivity;
	
	public StatisticsDetialPresenter(StatisticsDetialActivity mStatisticsDetialActivity)
	{
		this.mStatisticsDetialActivity = mStatisticsDetialActivity;
	
	}


	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mStatisticsDetialActivity.setContentView(R.layout.activity_statistics_detial);
		ImageView back = mStatisticsDetialActivity.findViewById(R.id.back);
		back.setOnClickListener(mStatisticsDetialActivity.mBackListener);

		mStatisticsDetialActivity.mSign = mStatisticsDetialActivity.getIntent().getParcelableExtra("sign");
		mStatisticsDetialActivity.mRelativeLayout = (RelativeLayout) mStatisticsDetialActivity.findViewById(R.id.shade);
		mStatisticsDetialActivity.headImage = mStatisticsDetialActivity.findViewById(R.id.headimg);
		mStatisticsDetialActivity.mAddressName = mStatisticsDetialActivity.findViewById(R.id.address_title);
		mStatisticsDetialActivity.mAddress = (TextView) mStatisticsDetialActivity.findViewById(R.id.address_text);
		mStatisticsDetialActivity.mTime = (TextView) mStatisticsDetialActivity.findViewById(R.id.time_text);
		mStatisticsDetialActivity.mSave = (TextView) mStatisticsDetialActivity.findViewById(R.id.save_text);
		mStatisticsDetialActivity.editText = (TextView) mStatisticsDetialActivity.findViewById(R.id.edit_text);
		mStatisticsDetialActivity.reason = (TextView) mStatisticsDetialActivity.findViewById(R.id.reason_edit_text);
		mStatisticsDetialActivity.mImageLayer = (LinearLayout) mStatisticsDetialActivity.findViewById(R.id.image_content);
		AppUtils.setContactCycleHead(mStatisticsDetialActivity.headImage,mStatisticsDetialActivity.mSign.mName);
		String[] addresss = mStatisticsDetialActivity.mSign.mAddress.split(",");
		if(addresss.length >=1)
			mStatisticsDetialActivity.mAddress.setText(addresss[1]);
		else
			mStatisticsDetialActivity.mAddress.setText(mStatisticsDetialActivity.mSign.mAddress);
		mStatisticsDetialActivity.mAddressName.setText(mStatisticsDetialActivity.mSign.mAddressName);
		mStatisticsDetialActivity.editText.setText(mStatisticsDetialActivity.mSign.mRemark);
		mStatisticsDetialActivity.reason.setText(mStatisticsDetialActivity.mSign.mReason);
		String time = mStatisticsDetialActivity.mSign.mDate.substring(0,10).replace("-",mStatisticsDetialActivity.getString(R.string.year)).
				replace("-",mStatisticsDetialActivity.getString(R.string.month)).replace("-",mStatisticsDetialActivity.getString(R.string.day))+" "+mStatisticsDetialActivity.mSign.mTime.substring(0,5);
		mStatisticsDetialActivity.mTime.setText(time);
		getAttachments(mStatisticsDetialActivity.mSign.mImage);
		initImage(mStatisticsDetialActivity.mPics,mStatisticsDetialActivity.mImageLayer);
	}

	
	@Override
	public void Create() {
		// TODO Auto-generated method stub
		initView();
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		
	}

	public void getAttachments( String hashs) {
		if (hashs.length() != 0) {
			String[] strs = hashs.split(",");

			for (int j = 0; j < strs.length; j++) {
				mStatisticsDetialActivity.mPics.add(new Attachment(strs[j],strs[j]+".png", Bus.callData(mStatisticsDetialActivity,"filetools/getfilePath", "/sign/image")+ "/" + strs[j] + ".png"
						, SignManager.getInstance().oaUtils.praseClodAttchmentUrl(strs[j]), 0, 0, "", SignManager.getInstance().oaUtils.praseClodAttchmentUrl(strs[j],60)));

			}
		}

	}

	public void initImage(ArrayList<Attachment> iamges, LinearLayout imageView)
	{
		LayoutInflater mInflater=(LayoutInflater)mStatisticsDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
						Bus.callData(mStatisticsDetialActivity,"filetools/setfileimg",image,mCircleImageModel.mPath);
						RequestOptions options = new RequestOptions()
								.placeholder(R.drawable.plugin_camera_no_pictures);
						Glide.with(mStatisticsDetialActivity).load(SignManager.getInstance().oaUtils.praseClodAttchmentUrl(mCircleImageModel.mRecordid, (int) (60* mStatisticsDetialActivity.mBasePresenter.mScreenDefine.density))).apply(options).into(new MySimpleTarget(image));
					}
					else if(j %3 == 1)
					{
						ImageView image = (ImageView) mview.findViewById(R.id.image2);
						RelativeLayout layout1 = (RelativeLayout) mview.findViewById(R.id.Support);
						layout1.setOnClickListener(new imageclick(iamges,mCircleImageModel));
						image.setVisibility(View.VISIBLE);
						Bus.callData(mStatisticsDetialActivity,"filetools/setfileimg",image,mCircleImageModel.mPath);
						RequestOptions options = new RequestOptions()
								.placeholder(R.drawable.plugin_camera_no_pictures);
						Glide.with(mStatisticsDetialActivity).load(SignManager.getInstance().oaUtils.praseClodAttchmentUrl(mCircleImageModel.mRecordid, (int) (60* mStatisticsDetialActivity.mBasePresenter.mScreenDefine.density))).apply(options).into(new MySimpleTarget(image));
					}
					else if(j %3 == 2)
					{
						ImageView image = (ImageView) mview.findViewById(R.id.image3);
						RelativeLayout layout1 = (RelativeLayout) mview.findViewById(R.id.share);
						layout1.setOnClickListener(new imageclick(iamges,mCircleImageModel));
						image.setVisibility(View.VISIBLE);
						Bus.callData(mStatisticsDetialActivity,"filetools/setfileimg",image,mCircleImageModel.mPath);
						RequestOptions options = new RequestOptions()
								.placeholder(R.drawable.plugin_camera_no_pictures);
						Glide.with(mStatisticsDetialActivity).load(SignManager.getInstance().oaUtils.praseClodAttchmentUrl(mCircleImageModel.mRecordid, (int) (60* mStatisticsDetialActivity.mBasePresenter.mScreenDefine.density))).apply(options).into(new MySimpleTarget(image));
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

	public void showPic(ArrayList<Attachment> iamges, Attachment image)
	{
		Bus.callData(mStatisticsDetialActivity,"filetools/showSeriasPic",iamges,image,false,false,"");
	}

}
