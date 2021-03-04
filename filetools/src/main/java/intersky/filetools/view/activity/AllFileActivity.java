package intersky.filetools.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.filetools.entity.LocalDocument;
import intersky.filetools.presenter.AllFilePresenter;
import intersky.filetools.view.adapter.DocumentAdapter;
import intersky.filetools.view.adapter.LocalPathAdapter;
import intersky.mywidget.HorizontalListView;

@SuppressLint("ClickableViewAccessibility")
public class AllFileActivity extends BaseActivity
{
	public ListView mListView;
	public HorizontalListView mPathList;
	public ArrayList<LocalDocument> mDocumentItems = new ArrayList<LocalDocument>();
	public LocalPathAdapter mSelectFoladerListAdapter;
	public DocumentAdapter mDocumentAdapter;
	public RelativeLayout layer;
	public RelativeLayout layer2;
	public RelativeLayout listlayer;
	public TextView mbtnright2;
	public TextView mFilePath;
	public TextView mBtnleft;
	public TextView mBtnRight;
	private AllFilePresenter mAllFilePresenter = new AllFilePresenter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mAllFilePresenter.Create();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mAllFilePresenter.Pause();

	}

	@Override
	protected void onStart()
	{
		super.onStart();
		mAllFilePresenter.Start();
	}

	@Override
	protected void onDestroy()
	{
		mAllFilePresenter.Destroy();
		super.onDestroy();

	}

	@Override
	protected void onResume()
	{
		mAllFilePresenter.Resume();
		super.onResume();

	}

	public View.OnClickListener mBackListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mAllFilePresenter.doBack();
		}
	};

	public View.OnClickListener mSelectAllListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mAllFilePresenter.selectAll();
		}
	};

	public View.OnClickListener mOkListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mAllFilePresenter.addMailAttachmenFinish();
		}
	};

	public AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
		{
			// TODO Auto-generated method stub
			mAllFilePresenter.doItemClick((LocalDocument) parent.getAdapter().getItem(position));

		}
	};

	public AdapterView.OnItemClickListener mPathItemClick = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
		{
			// TODO Auto-generated method stub
			mAllFilePresenter.doPathItemClick((LocalDocument) parent.getAdapter().getItem(position));
		}
	};


}
