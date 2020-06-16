package intersky.vote.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.mywidget.PullToRefreshView;
import intersky.vote.entity.Vote;
import intersky.vote.presenter.VoteListPresenter;
import intersky.vote.view.adapter.VoteAdapter;

@SuppressLint("ClickableViewAccessibility")
public class VoteListActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

	public static final int EVENT_UPDATA_DATA = 1000;
	public static final int EVENT_GETDATA_DATA_SUCCESS = 1001;
	public static final int EVENT_GETDATA_DATA_FAIL = 1002;
	public static final int EVENT_UPDATA_LIST= 1004;
	public PopupWindow popupWindow1;
	public ListView mItemList;
	public ArrayList<Vote> mVotes = new ArrayList<Vote>();
	public VoteAdapter mVoteAdapter;
	public PullToRefreshView mPullToRefreshView;
	public VoteListPresenter mVoteListPresenter = new VoteListPresenter(this);
	public boolean isall = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mVoteListPresenter.Create();
	}
	
	public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			mVoteListPresenter.onItemClick(mVotes.get(arg2));
		}

	};


	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mVoteListPresenter.onFoot();
		mPullToRefreshView.onFooterRefreshComplete();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mVoteListPresenter.onHead();
		mPullToRefreshView.onHeaderRefreshComplete();

	}


	public View.OnClickListener mCreatListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVoteListPresenter.doCreat();
		}

	};
}
