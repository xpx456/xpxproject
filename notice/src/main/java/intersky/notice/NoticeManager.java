package intersky.notice;

import android.content.Context;
import android.content.Intent;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Register;
import intersky.notice.asks.NoticeAsks;
import intersky.notice.entity.Notice;
import intersky.notice.handler.HitHandler;
import intersky.notice.view.activity.NewNoticeActivity;
import intersky.notice.view.activity.NoticeActivity;
import intersky.notice.view.activity.NoticeDetialActivity;
import intersky.oa.OaUtils;

public class NoticeManager {

    public static final int MAX_PIC_COUNT = 9;
    public static final String ACTION_NOTICE_ADD= "ACTION_NOTICE_ADD";
    public static final String ACTION_NOTICE_UPDATE= "ACTION_NOTICE_UPDATE";
    public static final String ACTION_NOTICE_DELETE= "ACTION_NOTICE_DELETE";
    public static final String ACTION_NOTICE_UPATE_SENDER = "ACTION_NOTICE_UPATE_SENDER";
    public static final String ACTION_NOTICE_ADDPICTORE = "ACTION_NOTICE_ADDPICTORE";
    public static final String ACTION_SET_NOTICE_CONTENT = "ACTION_SET_NOTICE_CONTENT";
    public static volatile NoticeManager mNoticeManager;
    public Context context;
    public HitHandler hitHandler;
    public Register register;
    public OaUtils oaUtils;
    public static NoticeManager init(OaUtils oaUtils,Context context) {
        if (mNoticeManager == null) {
            synchronized (NoticeManager.class) {
                if (mNoticeManager == null) {
                    mNoticeManager = new NoticeManager(oaUtils,context);
                }
                else
                {
                    mNoticeManager.oaUtils = oaUtils;
                    mNoticeManager.context = context;
                    mNoticeManager.hitHandler = new HitHandler(context);
                }
            }
        }
        return mNoticeManager;
    }

    public static NoticeManager getInstance() {
        return mNoticeManager;
    }

    public NoticeManager(OaUtils oaUtils,Context context){
        this.oaUtils = oaUtils;
        this.context = context;
        this.hitHandler = new HitHandler(context);
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public void sendNoticeAdd() {
        Intent intent = new Intent(ACTION_NOTICE_ADD);
        context.sendBroadcast(intent);
    }

    public void sendNoticeUpdate(String noticeid) {
        Intent intent = new Intent(ACTION_NOTICE_UPDATE);
        intent.putExtra("noticeid",noticeid);
        context.sendBroadcast(intent);
    }

    public void sendNoticeDelete(String noticeid) {
        Intent intent = new Intent(ACTION_NOTICE_DELETE);
        intent.putExtra("noticeid",noticeid);
        context.sendBroadcast(intent);
    }

    public void startNotice(Context context) {
        Intent intent = new Intent(context, NoticeActivity.class);
        context.startActivity(intent);
    }

    public void startDetial(Context context,String recordid) {
        BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
        baseActivity.waitDialog.show();
        Notice notice = new Notice();
        notice.nid = recordid;
        NoticeAsks.getDetial(context,hitHandler,notice);
    }

    public void startDetial(Context context,Notice notice) {
        Intent intent = new Intent(context, NoticeDetialActivity.class);
        intent.putExtra("notice",notice);
        context.startActivity(intent);
    }

    public void newNotice(Context context) {
        Intent intent = new Intent(context, NewNoticeActivity.class);
        Notice notice = new Notice();
        intent.putExtra("notice",notice);
        context.startActivity(intent);
    }

    public void sendRead() {

    }
}
