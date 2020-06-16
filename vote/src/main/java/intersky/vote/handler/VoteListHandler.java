package intersky.vote.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.vote.asks.VoteAsks;
import intersky.vote.prase.VotePrase;
import intersky.vote.view.activity.VoteListActivity;
import intersky.xpxnet.net.NetObject;
//03
public class VoteListHandler extends Handler {

    public static final int EVENT_VOTE_UPDATA = 3260303;

    public VoteListActivity theActivity;

    public VoteListHandler(VoteListActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        NetObject netObject;
        if (theActivity != null) {
            switch (msg.what) {
                case VoteAsks.EVENT_GET_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    VotePrase.praseList((NetObject) msg.obj,theActivity,theActivity.mVoteAdapter);
                    break;
                case VoteAsks.EVENT_GET_LIST_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    netObject = (NetObject) msg.obj;
                    if( VotePrase.praseList2(netObject,theActivity,theActivity.mVoteAdapter))
                    {
                        if(theActivity.mVoteAdapter.totalCount > theActivity.mVotes.size()
                                && theActivity.mVoteAdapter.endPage > theActivity.mVoteAdapter.startPage) {
                            VoteAsks.getListList(theActivity,theActivity.mVoteListPresenter.mVoteListHandler,theActivity.mVoteAdapter.startPage);
                        }
                    }
                    else
                    {
                        VoteAsks.getListList(theActivity,theActivity.mVoteListPresenter.mVoteListHandler,theActivity.mVoteAdapter.startPage);
                    }
                    break;
                case EVENT_VOTE_UPDATA:
                    theActivity.waitDialog.hide();
                    theActivity.mVoteListPresenter.updateAll();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
