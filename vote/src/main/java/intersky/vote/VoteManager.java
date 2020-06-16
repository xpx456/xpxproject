package intersky.vote;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Register;
import intersky.oa.OaUtils;
import intersky.select.entity.Select;
import intersky.vote.asks.VoteAsks;
import intersky.vote.entity.Vote;
import intersky.vote.handler.HitHandler;
import intersky.vote.view.activity.VoteActivity;
import intersky.vote.view.activity.VoteDetialActivity;
import intersky.vote.view.activity.VoteListActivity;

public class VoteManager {

    public static final String ACTION_SET_VOTE_TYPE = "ACTION_SET_VOTE_TYPE";
    public static final String ACTION_SET_VOTER = "ACTION_SET_VOTER";
    public static final String ACTION_SET_PIC = "ACTION_SET_PIC";
    public static final String ACTION_DELETE_PIC = "ACTION_DELETE_PIC";


    public static final String ACTION_VOTE_ADD= "ACTION_NOTICE_ADD";
    public static final String ACTION_VOTE_UPDATE= "ACTION_NOTICE_UPDATE";
    public static final String ACTION_VOTE_DELETE= "ACTION_NOTICE_DELETE";

    public static final int MAX_PIC_COUNT = 4;
    public static volatile VoteManager mVoteManager;
    public Context context;
    public HitHandler hitHandler;
    public ArrayList<Select> mVoteTypes = new ArrayList<Select>();
    public Register register;
    public OaUtils oaUtils;
    public static VoteManager init(OaUtils oaUtils,Context context) {
        if (mVoteManager == null) {
            synchronized (VoteManager.class) {
                if (mVoteManager == null) {
                    mVoteManager = new VoteManager(oaUtils,context);
                }
                else
                {
                    mVoteManager.oaUtils = oaUtils;
                    mVoteManager.context = context;
                    mVoteManager.hitHandler = new HitHandler(context);
                }
            }
        }
        return mVoteManager;
    }

    public static VoteManager getInstance() {
        return mVoteManager;
    }

    public VoteManager(OaUtils oaUtils,Context context) {
        this.oaUtils = oaUtils;
        this.context = context;
        hitHandler = new HitHandler(context);
    }

    public void setRegister(Register register)
    {
        this.register = register;
    }

    public void startVoteMain(Context context) {
        Intent intent = new Intent(context, VoteListActivity.class);
        context.startActivity(intent);
    }

    public void startVoteNew(Context context) {
        Intent intent = new Intent(context, VoteActivity.class);
        Vote vote = new Vote();
        intent.putExtra("vote",vote);
        context.startActivity(intent);
    }

    public void startDetial(Context context,String recordid) {
        BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
        baseActivity.waitDialog.show();
        Vote vote = new Vote();
        vote.voteid = recordid;
        VoteAsks.getDetial(context,hitHandler,vote);
    }

    public void startDetial(Context context,Vote vote) {
        Intent intent = new Intent(context, VoteDetialActivity.class);
        intent.putExtra("vote",vote);
        context.startActivity(intent);
    }

    public void sendVoteAdd() {
        Intent intent = new Intent(ACTION_VOTE_ADD);
        context.sendBroadcast(intent);
    }

    public void sendVoteUpdate(String vote) {
        Intent intent = new Intent(ACTION_VOTE_UPDATE);
        intent.putExtra("voteid",vote);
        context.sendBroadcast(intent);
    }

    public void sendVoteDelete(String vote) {
        Intent intent = new Intent(ACTION_VOTE_DELETE);
        intent.putExtra("voteid",vote);
        context.sendBroadcast(intent);
    }

}
