package intersky.vote.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import intersky.appbase.Presenter;
import intersky.mywidget.PullToRefreshView;
import intersky.vote.R;
import intersky.vote.asks.VoteAsks;
import intersky.vote.entity.Vote;
import intersky.vote.handler.VoteListHandler;
import intersky.vote.receicer.VoteListReceiver;
import intersky.vote.view.activity.VoteActivity;
import intersky.vote.view.activity.VoteDetialActivity;
import intersky.vote.view.activity.VoteListActivity;
import intersky.vote.view.adapter.VoteAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class VoteListPresenter implements Presenter {

    private VoteListActivity mVoteListActivity;
    public VoteListHandler mVoteListHandler;

    public VoteListPresenter(VoteListActivity mVoteListActivity) {
        this.mVoteListActivity = mVoteListActivity;
        this.mVoteListHandler = new VoteListHandler(mVoteListActivity);
        mVoteListActivity.setBaseReceiver(new VoteListReceiver(mVoteListHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mVoteListActivity.setContentView(R.layout.activity_vote_list);
        ImageView back = mVoteListActivity.findViewById(R.id.back);
        back.setOnClickListener(mVoteListActivity.mBackListener);
        mVoteListActivity.mCteat = (ImageView) mVoteListActivity.findViewById(R.id.creatvote);
        mVoteListActivity.mCteat.setOnClickListener(mVoteListActivity.mCreatListener);
        mVoteListActivity.mItemList = (ListView) mVoteListActivity.findViewById(R.id.sign_List);
        mVoteListActivity.mVoteAdapter = new VoteAdapter(mVoteListActivity, mVoteListActivity.mVotes);
        mVoteListActivity.mItemList.setAdapter(mVoteListActivity.mVoteAdapter);
        mVoteListActivity.mItemList.setOnItemClickListener(mVoteListActivity.mOnItemClickListener);
        mVoteListActivity.mPullToRefreshView = (PullToRefreshView) mVoteListActivity
                .findViewById(R.id.task_pull_refresh_view);
        mVoteListActivity.mPullToRefreshView.setOnHeaderRefreshListener(mVoteListActivity);
        mVoteListActivity.mPullToRefreshView.setOnFooterRefreshListener(mVoteListActivity);
        mVoteListActivity.mPullToRefreshView.getmFooterView().setVisibility(View.INVISIBLE);
    }


    public void doCreat() {
        Intent intent = new Intent(mVoteListActivity, VoteActivity.class);
        Vote vote = new Vote();
        intent.putExtra("vote",vote);
        mVoteListActivity.startActivity(intent);
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        VoteAsks.getList(mVoteListActivity,mVoteListHandler,mVoteListActivity.mVoteAdapter.startPage);
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
        mVoteListHandler = null;
    }


    public void onItemClick(Vote mVoteModel) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(mVoteListActivity, VoteDetialActivity.class);
        intent.putExtra("vote", mVoteModel);
        mVoteListActivity.startActivity(intent);
    }

    public void updateAll() {
        mVoteListActivity.mVoteAdapter.endPage = mVoteListActivity.mVoteAdapter.startPage;
        mVoteListActivity.mVoteAdapter.startPage = 1;
        mVoteListActivity.mVoteAdapter.totalCount = -1;
        mVoteListActivity.mVotes.clear();
        mVoteListActivity.mVoteAdapter.notifyDataSetChanged();
        VoteAsks.getListList(mVoteListActivity,mVoteListHandler,mVoteListActivity.mVoteAdapter.startPage);
    }

    public void onHead() {
        mVoteListActivity.mVotes.clear();
        mVoteListActivity.mVoteAdapter.startPage = 1;
        mVoteListActivity.mVoteAdapter.isall = false;
        mVoteListActivity.mVoteAdapter.notifyDataSetChanged();
        VoteAsks.getList(mVoteListActivity,mVoteListHandler,mVoteListActivity.mVoteAdapter.startPage);
    }

    public void onFoot() {
        if (mVoteListActivity.isall == false) {
            VoteAsks.getList(mVoteListActivity,mVoteListHandler,mVoteListActivity.mVoteAdapter.startPage);
        } else {

        }
    }
}
