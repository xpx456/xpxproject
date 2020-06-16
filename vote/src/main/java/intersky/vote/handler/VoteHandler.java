package intersky.vote.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.apputils.AppUtils;
import intersky.oa.OaUtils;
import intersky.vote.VoteManager;
import intersky.vote.asks.VoteAsks;
import intersky.vote.entity.Vote;
import intersky.vote.prase.VotePrase;
import intersky.vote.view.activity.VoteActivity;
import intersky.xpxnet.net.NetObject;
//02
public class VoteHandler extends Handler {
    public VoteActivity theActivity;

    public static final int EVENT_SET_VOTE_TYPE = 3260200;
    public static final int EVENT_ADD_PHOTO = 3260201;
    public static final int EVENT_DELETE_PIC = 3260202;
    public static final int EVENT_SET_VOTER = 3260203;
    public VoteHandler(VoteActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case VoteAsks.EVENT_GET_SAVE_SUCCESS:
                    theActivity.waitDialog.hide();
                    theActivity.issub = false;
                    if(VotePrase.praseData((NetObject) msg.obj,theActivity)) {
                        NetObject netObject = (NetObject) msg.obj;
                        Vote vote = (Vote) netObject.item;
                        if(vote.voteid.length() == 0)
                        {
                            VoteManager.getInstance().sendVoteAdd();
                        }
                        else
                        {
                            VoteManager.getInstance().sendVoteUpdate(vote.voteid);
                        }
                        theActivity.finish();
                    }
                    break;
                case OaUtils.EVENT_UPLOAD_FILE_RESULT:
                    theActivity.waitDialog.hide();
                    theActivity.mVotePresenter.saveVote((String) msg.obj);
                    break;
                case OaUtils.EVENT_UPLOAD_FILE_RESULT_FAIL:
                    theActivity.waitDialog.hide();
                    AppUtils.showMessage(theActivity, (String) msg.obj);
                    break;
                case EVENT_SET_VOTE_TYPE:
                    theActivity.mVotePresenter.setVotetype();
                    break;
                case EVENT_SET_VOTER:
                    theActivity.mVotePresenter.setSender((Intent) msg.obj);
                    break;
                case EVENT_ADD_PHOTO:
                    theActivity.mVotePresenter.addpic((Intent) msg.obj);
                    break;
                case EVENT_DELETE_PIC:
                    theActivity.mVotePresenter.deletePic((Intent) msg.obj);
                    break;

            }
            super.handleMessage(msg);
        }
    }
}
