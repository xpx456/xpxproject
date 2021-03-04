package intersky.vote.prase;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.oa.OaUtils;
import intersky.vote.VoteManager;
import intersky.vote.entity.Vote;
import intersky.vote.entity.VoteSelect;
import intersky.vote.view.adapter.VoteAdapter;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class VotePrase {

    public static void praseList(NetObject net, Context context, VoteAdapter mVoteAdapter) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return;
            }

            JSONObject mjson = new JSONObject(json);
            JSONArray array = mjson.getJSONArray("data");

            for (int i = 0; i < array.length() - 1; i++) {
                JSONObject item = array.getJSONObject(i);
                Vote temp = new Vote(item.getString("vote_id"), item.getString("user_id")
                        , item.getString("create_time"), item.getString("end_time"), item.getString("content")
                        , item.getInt("is_incognito"), item.getInt("is_close"), item.getInt("type"));
                temp.userName = (String) Bus.callData(VoteManager.getInstance().context,"chat/getContactName",item.getString("user_id"));

                if (item.has("has_one_notice")) {
                    JSONObject jo = item.getJSONObject("has_one_notice");

//                    if (jo.getString("vote_item_id").equals("0")) {
//                        temp.hasjoin = true;
//                        temp.hasvote = false;
//                    }else  if(jo.getString("vote_item_id").equals("-1"))
//                    {
//                        temp.hasjoin = false;
//                        temp.hasvote = false;
//                    }
//                    else {
//                        temp.hasjoin = true;
//                        temp.hasvote = true;
//                    }

                }
                mVoteAdapter.mVotes.add(temp);
            }
            mVoteAdapter.notifyDataSetChanged();
            JSONObject jo = array.getJSONObject(array.length() - 1);
            if (jo.getInt("total_results") > mVoteAdapter.mVotes.size()) {
                mVoteAdapter.startPage++;
            } else {
                mVoteAdapter.isall = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseList2(NetObject net, Context context, VoteAdapter mVoteAdapter) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }

            JSONObject mjson = new JSONObject(json);
            JSONArray array = mjson.getJSONArray("data");

            for (int i = 0; i < array.length() - 1; i++) {
                JSONObject item = array.getJSONObject(i);
                Vote temp = new Vote(item.getString("vote_id"), item.getString("user_id")
                        , item.getString("create_time"), item.getString("end_time"), item.getString("content")
                        , item.getInt("is_incognito"), item.getInt("is_close"), item.getInt("type"));
                temp.userName = (String) Bus.callData(VoteManager.getInstance().context,"chat/getContactName",item.getString("user_id"));

                if (item.has("has_one_notice")) {
                    JSONObject jo = item.getJSONObject("has_one_notice");

//                    if (jo.getString("vote_item_id").equals("0")) {
//                        temp.hasjoin = true;
//                        temp.hasvote = false;
//                    }else  if(jo.getString("vote_item_id").equals("-1"))
//                    {
//                        temp.hasjoin = false;
//                        temp.hasvote = false;
//                    }
//                    else {
//                        temp.hasjoin = true;
//                        temp.hasvote = true;
//                    }

                }
                mVoteAdapter.mVotes.add(temp);
            }
            mVoteAdapter.notifyDataSetChanged();
            JSONObject jo = array.getJSONObject(array.length() - 1);
            if (jo.getInt("total_results") > mVoteAdapter.mVotes.size()) {
                mVoteAdapter.startPage++;
            } else {
                mVoteAdapter.isall = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean praseDetial(NetObject net,Context context) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }
            Vote vote = (Vote) net.item;
            Bus.callData(context,"conversation/setdetialread", Conversation.CONVERSATION_TYPE_VOTE,vote.voteid);
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            vote.mContent = jo.getString("content");
            vote.personCount = jo.getInt("total_sender");
            vote.userid = jo.getString("user_id");
            Contacts user = (Contacts) Bus.callData(context,"chat/getContactItem",vote.userid);
            vote.userName = user.mName;
            vote.creatTime = jo.getString("create_time");
            vote.endTime = jo.getString("end_time");
            vote.is_close = jo.getInt("is_close");
            vote.is_incognito = jo.getInt("is_incognito");
            vote.type = jo.getInt("type");
            vote.voterid = jo.getString("sender_id");
            if (jo.has("my_vote_item")) {
                JSONObject ja = jo.getJSONObject("my_vote_item");
                if (!ja.getString("vote_item_id").equals("0")) {
                    vote.visiable = true;
                }
                else
                {
                    vote.visiable = false;
                }
                vote.myvoteid = ja.getString("vote_item_id");
            }
            else
            {
                if(!VoteManager.getInstance().oaUtils.mAccount.mRecordId.equals(jo.getString("user_id")))
                    vote.visiable = false;
                else
                    vote.visiable = true;
            }

            if (jo.has("has_many_tiems")) {
                vote.selectJson = jo.getJSONArray("has_many_tiems").toString();

            }
            if (jo.has("has_many_notice")) {
                vote.voteJson = jo.getJSONArray("has_many_notice").toString();

            }
            if (jo.has("has_many_reply")) {
                vote.replyJson = jo.getJSONArray("has_many_reply").toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void praseAddtchment(NetObject net) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return;
            }
            VoteSelect voteSelect = (VoteSelect) net.item;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            String hash = voteSelect.hash;
            String[] hashs = hash.split(",");
            for (int j = 0; j < hashs.length; j++) {
                String temp = hashs[j];
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getString("hash").equals(temp)) {
                        Attachment mAttachmentModel = new Attachment(jo.getString("hash"),jo.getString("name"),
                                Bus.callData(VoteManager.getInstance().context,"filetools/getfilePath","/" + "intersky" + "/" + "vote")+"/"+jo.getString("hash")+jo.getString("ext"),
                                VoteManager.getInstance().oaUtils.praseClodAttchmentUrl(jo.getString("hash")),jo.getLong("size"),0,"");
                        mAttachmentModel.mPath2 = Bus.callData(VoteManager.getInstance().context,"filetools/getfilePath","/" + "intersky" + "/" + "vote")+"/"+jo.getString("hash")+"2"+jo.getString("ext");
                        voteSelect.mPics.add(mAttachmentModel);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Reply prasseReply(NetObject net,ArrayList<Reply> replies,String voteid) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return null;
            }
            int count = replies.size();
            JSONObject object = new JSONObject(json);
            JSONObject jo2 = object.getJSONObject("data");
            Reply mReplyModel = new Reply(jo2.getString("vote_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), voteid, jo2.getString("create_time"));
            replies.add(0, mReplyModel);
            return mReplyModel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int praseReplyDelete(NetObject net,ArrayList<Reply> replies) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            return -1;
        }

        Reply mreply = (Reply) net.item;
        int pos = replies.indexOf(mreply);
        replies.remove(mreply);
        return  pos;
    }

    public static boolean praseData(NetObject net, Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return  true;
    }
}
