package intersky.vote.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Register;
import intersky.vote.VoteManager;
import intersky.vote.entity.Vote;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class VoteBusObject extends BusObject {
    public VoteBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
        if(TextUtils.equals(bizName, "vote/startVoteMain")) {
            VoteManager.getInstance().startVoteMain(context);
            return null;
        }
        else if(TextUtils.equals(bizName, "vote/startVoteNew")) {
            VoteManager.getInstance().startVoteNew(context);
            return null;
        }
        else if(TextUtils.equals(bizName, "vote/startDetialConversation")) {
            VoteManager.getInstance().startDetial(context,(String)var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "vote/startDetial")) {
            VoteManager.getInstance().startDetial(context,(Vote)var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "vote/sendUpdate")) {
            VoteManager.getInstance().sendVoteUpdate((String)var3[0]);
            return null;
        }
        return null;
    }
}
