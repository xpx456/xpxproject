package intersky.notice.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.notice.asks.NoticeAsks;
import intersky.notice.prase.NoticePrase;
import intersky.notice.view.activity.NoticeActivity;
import intersky.xpxnet.net.NetObject;

//03
public class NoticeHandler extends Handler {

    public static final int EVENT_NOTICE_UPDATA = 3210300;
    public NoticeActivity theActivity;

    public NoticeHandler(NoticeActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        NetObject netObject;
        if (theActivity != null) {
            switch (msg.what) {
                case NoticeAsks.EVENT_GET_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    netObject = (NetObject) msg.obj;
                    if((int)netObject.item == 0)
                    {
                        NoticePrase.praseList(netObject,theActivity,theActivity.mUnReadeAdapter);
                    }
                    else
                    {
                        NoticePrase.praseList(netObject,theActivity,theActivity.mReadNoticeAdapter);
                    }
                    break;
                case NoticeAsks.EVENT_GET_LIST_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    netObject = (NetObject) msg.obj;
                    if((int)netObject.item == 0)
                    {
                        if( NoticePrase.praseList2(netObject,theActivity,theActivity.mUnReadeAdapter))
                        {
                            if(theActivity.mUnReadeAdapter.totalCount > theActivity.mUnReadNotices.size()
                                    && theActivity.mUnReadeAdapter.endPage > theActivity.mUnReadeAdapter.nowpage) {
                                NoticeAsks.getNoticesListList(theActivity,theActivity.mNoticePresenter.mNoticeHandler,0,theActivity.mUnReadeAdapter.nowpage);
                            }
                        }
                        else
                        {
                            NoticeAsks.getNoticesListList(theActivity,theActivity.mNoticePresenter.mNoticeHandler,0,theActivity.mUnReadeAdapter.nowpage);
                        }
                    }
                    else
                    {
                        if( NoticePrase.praseList2(netObject,theActivity,theActivity.mReadNoticeAdapter))
                        {
                            if(theActivity.mReadNoticeAdapter.totalCount > theActivity.mReadNotices.size()
                                    && theActivity.mReadNoticeAdapter.endPage > theActivity.mReadNoticeAdapter.nowpage) {
                                NoticeAsks.getNoticesListList(theActivity,theActivity.mNoticePresenter.mNoticeHandler,0,theActivity.mReadNoticeAdapter.nowpage);
                            }
                        }
                        else
                        {
                            NoticeAsks.getNoticesListList(theActivity,theActivity.mNoticePresenter.mNoticeHandler,0,theActivity.mReadNoticeAdapter.nowpage);
                        }
                    }
                    break;
                case EVENT_NOTICE_UPDATA:
                    theActivity.waitDialog.hide();
                    theActivity.mNoticePresenter.updataAll();
                    break;

            }
            super.handleMessage(msg);
        }
    }
}
