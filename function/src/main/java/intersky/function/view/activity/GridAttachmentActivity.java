package intersky.function.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;
import intersky.function.entity.Function;
import intersky.function.presenter.GridAttachmentPresenter;
import intersky.function.view.adapter.GridAttachmentAdapter;

@SuppressLint("ClickableViewAccessibility")
public class GridAttachmentActivity extends BaseActivity implements OnGestureListener, OnTouchListener
{
	public Function function;
	public String Module_Flag = "";
	public String RecordID = "";
	public GridAttachmentAdapter mGridAttachmentAdapter;
	public ArrayList<Attachment> mAttachments = new ArrayList<Attachment>();
	public ListView mListView;
	public GridAttachmentPresenter mGridAttachmentPresenter = new GridAttachmentPresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mGridAttachmentPresenter.Create();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		mGridAttachmentPresenter.Resume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		mGridAttachmentPresenter.Pause();

	}
	
	@Override
	protected void onDestroy()
	{
		mGridAttachmentPresenter.Destroy();
		super.onDestroy();

	}
	
	public AdapterView.OnItemClickListener mFujianOperListener = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mGridAttachmentPresenter.dodownload((Attachment) parent.getAdapter().getItem(position));
		}

	};

}
