package intersky.filetools.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.filetools.entity.Video;
import intersky.filetools.presenter.VideoPresenter;
import intersky.filetools.provider.AbstructProvider;
import intersky.filetools.view.adapter.VideoAdapter;

@SuppressLint("ClickableViewAccessibility")
public class VideoActivity extends BaseActivity
{
	public RelativeLayout mButtomLayer;
	public AbstructProvider provider;
	public ArrayList<Video> listVideos = new ArrayList<Video>();
	public ListView mListView;
	public VideoAdapter mVideoAdapter;
	public TextView mbtnleft;
	public TextView mbtnright;
	public TextView mbtnright2;
	public RelativeLayout layer2;
	private VideoPresenter mVideoPresenter = new VideoPresenter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mVideoPresenter.Create();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();


	}


	@Override
	public void onResume()
	{
		mVideoPresenter.Resume();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		mVideoPresenter.Destroy();
		super.onDestroy();

	}
	
	public View.OnClickListener mFinishListener = new View.OnClickListener() {
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mVideoPresenter.addMailAttachmenFinish();
		}
	};

	public AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
		{
			// TODO Auto-generated method stub
			mVideoPresenter.doItemClick(position);
		}
	};

	public View.OnClickListener mTakePhotoListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mVideoPresenter.takePhoto();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mVideoPresenter.takePhotoResult(requestCode, resultCode, data);
	}

}
