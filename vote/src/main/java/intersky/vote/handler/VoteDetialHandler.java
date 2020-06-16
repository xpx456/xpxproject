package intersky.vote.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.appbase.ReplyUtils;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Reply;
import intersky.oa.OaAsks;
import intersky.vote.VoteManager;
import intersky.vote.asks.VoteAsks;
import intersky.vote.entity.Vote;
import intersky.vote.entity.VoteSelect;
import intersky.vote.prase.VotePrase;
import intersky.vote.view.activity.VoteDetialActivity;
import intersky.xpxnet.net.NetObject;
//01
public class VoteDetialHandler extends Handler {

    public static final int EVENT_VOTE_UPDATA = 3260100;
    public static final int EVENT_VOTE_DELTE = 3260101;
    public VoteDetialActivity theActivity;

    public VoteDetialHandler(VoteDetialActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case OaAsks.EVENT_GET_ATTCHMENT_SUCCESS:
                    VotePrase.praseAddtchment((NetObject) msg.obj);
                    NetObject netObject = (NetObject) msg.obj;
                    VoteSelect voteSelect = (VoteSelect) netObject.item;
                    theActivity.mVoteDetialPresenter.addPicView(voteSelect);
                    theActivity.waitDialog.hide();
                    break;
                case VoteAsks.EVENT_GET_DETIAL_SUCCESS:
                    theActivity.waitDialog.hide();
                    VotePrase.praseDetial((NetObject) msg.obj,theActivity);
                    VoteManager.getInstance().register.conversationFunctions.Read(VoteManager.getInstance().register.typeName
                            ,theActivity.vote.voteid);
                    theActivity.mVoteDetialPresenter.praseDetial();
                    break;
                case VoteAsks.EVENT_GET_CLOSE_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(VotePrase.praseData((NetObject) msg.obj,theActivity)) {
                        Vote vote = (Vote) ((NetObject) msg.obj).item;
                        VoteManager.getInstance().sendVoteUpdate(vote.voteid);
                        theActivity.finish();
                    }
                    break;
                case VoteAsks.EVENT_ADD_REPLY_SUCCESS:
                    theActivity.waitDialog.hide();
                    Reply reply = VotePrase.prasseReply((NetObject) msg.obj,theActivity.mReplys,theActivity.vote.voteid);
                    if (reply != null)
                    {
                        theActivity.mEditTextContent.setText("");
                        ReplyUtils.praseReplyViews(theActivity.mReplys,theActivity,theActivity.mAnswer
                                ,theActivity.mAnswerlayer,theActivity.mDeleteReplyListenter,theActivity.mVoteDetialPresenter.mVoteDetialHandler,reply);
                    }
                    break;
                case VoteAsks.EVENT_DELETE_REPLY_SUCCESS:
                    theActivity.waitDialog.hide();
                    int pos = VotePrase.praseReplyDelete((NetObject) msg.obj,theActivity.mReplys);
                    ReplyUtils.removeReplyView(pos,theActivity.mReplys,theActivity.mAnswer,theActivity.mAnswerlayer,theActivity);
                    break;
                case VoteAsks.EVENT_GET_DELETE_SUCCESS:
                    if(VotePrase.praseData((NetObject) msg.obj,theActivity)) {
                        Vote vote = (Vote) ((NetObject) msg.obj).item;
                        theActivity.waitDialog.hide();
                        VoteManager.getInstance().sendVoteDelete(vote.voteid);
                        Intent intent = new Intent();
                        intent.putExtra("type", Conversation.CONVERSATION_TYPE_VOTE);
                        intent.putExtra("detialid",vote.voteid);
                        Bus.callData(theActivity,"function/updateOahit");
                        Bus.callData(theActivity,"conversation/setdetialdelete",intent);
                        theActivity.finish();
                    }
                    break;
                case VoteAsks.EVENT_VOTE_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(VotePrase.praseData((NetObject) msg.obj,theActivity)) {
                        Vote vote = (Vote) ((NetObject) msg.obj).item;
                        VoteManager.getInstance().sendVoteUpdate(vote.voteid);
                    }
                    Bus.callData(theActivity,"function/updateOahit");
                    break;
                case EVENT_VOTE_UPDATA:
                    theActivity.waitDialog.show();
                    Intent intent = (Intent) msg.obj;
                    String id = intent.getStringExtra("voteid");
                    if(id.equals(theActivity.vote.voteid))
                    VoteAsks.getDetial(theActivity,theActivity.mVoteDetialPresenter.mVoteDetialHandler,theActivity.vote);
                    break;
                case EVENT_VOTE_DELTE:
                    theActivity.waitDialog.show();
                    Intent intent1 = (Intent) msg.obj;
                    String id1 = intent1.getStringExtra("voteid");
                    if(id1.equals(theActivity.vote.voteid))
                        theActivity.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
