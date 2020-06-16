package intersky.notice.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.lang.ref.WeakReference;

import intersky.appbase.ReplyUtils;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Reply;
import intersky.notice.NoticeManager;
import intersky.notice.asks.NoticeAsks;
import intersky.notice.entity.Notice;
import intersky.notice.prase.NoticePrase;
import intersky.notice.view.activity.NoticeDetialActivity;
import intersky.oa.OaAsks;
import intersky.xpxnet.net.NetObject;

//02
public class NoticeDetialHandler extends Handler {

    public static final int EVENT_DETIAL_UPDATA = 3210200;
    public static final int EVENT_DETIAL_DELETE = 3210201;
    public NoticeDetialActivity theActivity;

    public NoticeDetialHandler(NoticeDetialActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case NoticeAsks.EVENT_GET_DETIAL_SUCCESS:
                    NoticePrase.praseDetial((NetObject) msg.obj,theActivity);
                    theActivity.mNoticeDetialPresenter.praseDetial();
                    NoticeManager.getInstance().register.conversationFunctions.Read(NoticeManager.getInstance().register.typeName
                            ,theActivity.notice.nid);
                    theActivity.waitDialog.hide();
                    break;
                case OaAsks.EVENT_GET_ATTCHMENT_SUCCESS:
                    theActivity.mAttachmentlayer.setVisibility(View.VISIBLE);
                    NoticePrase.praseAddtchment((NetObject) msg.obj,theActivity.mPics,theActivity.notice);
                    theActivity.mNoticeDetialPresenter.praseAttahcmentViews();
                    theActivity.waitDialog.hide();
                    break;
                case NoticeAsks.EVENT_ADD_REPLY_SUCCESS:
                    theActivity.waitDialog.hide();
                    Reply reply = NoticePrase.prasseReply((NetObject) msg.obj,theActivity.mReplys,theActivity.notice.nid);
                    if(reply != null)
                    {
                        theActivity.mEditTextContent.setText("");
                        ReplyUtils.praseReplyViews(theActivity.mReplys,theActivity,theActivity.mAnswer
                                ,theActivity.mAnswerlayer,theActivity.mDeleteReplyListenter,theActivity.mNoticeDetialPresenter.mNoticeDetialHandler,reply);
                    }
                    break;
                case NoticeAsks.EVENT_GET_DELETE_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(NoticePrase.praseData((NetObject) msg.obj,theActivity)) {
                        Notice notice = (Notice) ((NetObject) msg.obj).item;
                        NoticeManager.getInstance().sendNoticeDelete(notice.nid);
                        Intent intent = new Intent();
                        intent.putExtra("type", Conversation.CONVERSATION_TYPE_NOTICE);
                        intent.putExtra("detialid",notice.nid);
                        Bus.callData(theActivity,"function/updateOahit");
                        Bus.callData(theActivity,"conversation/setdetialdelete",intent);
                        theActivity.finish();
                    }
                    break;
                case NoticeAsks.EVENT_DELETE_REPLY_SUCCESS:
                    theActivity.waitDialog.hide();
                    int pos = NoticePrase.praseReplyDelete((NetObject) msg.obj,theActivity.mReplys);
                    ReplyUtils.removeReplyView(pos,theActivity.mReplys,theActivity.mAnswer,theActivity.mAnswerlayer,theActivity);
                    break;
                case EVENT_DETIAL_UPDATA:
                    theActivity.waitDialog.show();
                    Intent intent = (Intent) msg.obj;
                    String id = intent.getStringExtra("noticeid");
                    if(id.equals(theActivity.notice.nid))
                    NoticeAsks.getDetial(theActivity,theActivity.mNoticeDetialPresenter.mNoticeDetialHandler,theActivity.notice);
                    break;
                case EVENT_DETIAL_DELETE:
                    theActivity.waitDialog.show();
                    Intent intent1 = (Intent) msg.obj;
                    String id1 = intent1.getStringExtra("noticeid");
                    if(id1.equals(theActivity.notice.nid))
                        theActivity.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
