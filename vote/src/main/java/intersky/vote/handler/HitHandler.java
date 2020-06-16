package intersky.vote.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.vote.VoteManager;
import intersky.vote.asks.VoteAsks;
import intersky.vote.entity.Vote;
import intersky.vote.prase.VotePrase;
import intersky.xpxnet.net.NetObject;
//00
public class HitHandler extends Handler {




    public Context context;

    public HitHandler(Context context){
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {

            switch (msg.what) {
                case VoteAsks.EVENT_GET_DETIAL_SUCCESS:
                    BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
                    baseActivity.waitDialog.hide();
                    boolean exist =  VotePrase.praseDetial((NetObject) msg.obj,context);
                    Vote notice = (Vote) ((NetObject) msg.obj).item;
                    if(exist == true)
                        VoteManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),notice);
                    break;
            }
            super.handleMessage(msg);

    }
}
