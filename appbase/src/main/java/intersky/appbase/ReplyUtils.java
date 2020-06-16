package intersky.appbase;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.mywidget.GifTextView;

public class ReplyUtils {

    public static void addReply(Reply mReplyModel, boolean isnew, final Context context, View.OnClickListener deltetelistener, LinearLayout answerLayers, Handler handler) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview = mInflater.inflate(intersky.appbase.R.layout.reply_item, null);
        ImageView delete = (ImageView) mview.findViewById(intersky.appbase.R.id.close_image);
        if (mReplyModel.mUserId.equals(Bus.callData(context,"chat/getAccountId",""))) {
            delete.setVisibility(View.VISIBLE);
            delete.setTag(mReplyModel);
            delete.setOnClickListener(deltetelistener);
        } else {
            delete.setVisibility(View.INVISIBLE);
        }
        TextView mhead = (TextView) mview.findViewById(intersky.appbase.R.id.conversation_img);
        Contacts contacts = (Contacts) Bus.callData(context,"chat/getContactItem",mReplyModel.mUserId);
        mhead.setTag(contacts);
        mhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.callData(context,"chat/startContactDetial",(Contacts) v.getTag());
            }
        });
        Bus.callData(context,"chat/setContactCycleHead",mhead,contacts);
        TextView text = (TextView) mview.findViewById(intersky.appbase.R.id.conversation_title);
        text.setText(contacts.mName);
        text = (TextView) mview.findViewById(intersky.appbase.R.id.conversation_time);
        text.setText(mReplyModel.mCreatTime.substring(0, mReplyModel.mCreatTime.length() - 3));
        GifTextView text1 = (GifTextView) mview.findViewById(intersky.appbase.R.id.conversation_subject);
        text1.setSpanText(handler, mReplyModel.mConetent);
        if (isnew == true)
            answerLayers.addView(mview, 0);
        else
            answerLayers.addView(mview);
    }

    public static class DeleteReplyDialogListener implements DialogInterface.OnClickListener {

        public Reply reply;
        public DoDelete doDelete;
        public DeleteReplyDialogListener(Reply reply,DoDelete doDelete) {
            this.reply = reply;
            this.doDelete = doDelete;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            doDelete.doDeltet(reply);
        }
    }

    public interface DoDelete{
        void doDeltet(Reply reply);
    }

    public static void praseReplyViews(ArrayList<Reply> replies,Context context,TextView answertitle,LinearLayout answerLayers,View.OnClickListener deltetelistener,Handler handler) {
//        if(replies.size() == 0)
//        {
//            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View mview = mInflater.inflate(intersky.appbase.R.layout.reply_item_none, null);
//            answerLayers.removeAllViews();
//            answerLayers.addView(mview);
//            if(answertitle != null)
//            answertitle.setText(context.getString(intersky.appbase.R.string.xml_workreport_replay));
//        }
//        else
//        {
//            if(answerLayers.getChildCount() > 0)
//            {
//                View view = answerLayers.getChildAt(0);
//                RelativeLayout relativeLayout = view.findViewById(R.id.conversationlayer);
//                if(relativeLayout != null)
//                {
//                    answerLayers.removeViewAt(0);
//                }
//            }
//        }
//        if (replies.size() != 0)
//        {
//            if(answerLayers.getChildCount() != 0 )
//            {
//                if(answertitle != null)
//                answertitle.setText(context.getString(intersky.appbase.R.string.xml_workreport_replay) + "(" + String.valueOf(replies.size()) + ")");
//            }
//        }
        if(answertitle != null)
        answertitle.setText(context.getString(R.string.xml_workreport_replay) + "(" + String.valueOf(replies.size()) + ")");
        for(int i = 0 ; i < replies.size() ; i++) {
            Reply mReplyModel = replies.get(i);
            ReplyUtils.addReply(mReplyModel, false,context,deltetelistener,answerLayers,handler);
        }
    }

    public static void praseReplyViews(ArrayList<Reply> replies,Context context,TextView answertitle,LinearLayout answerLayers,View.OnClickListener deltetelistener,Handler handler,Reply reply) {
        if(answertitle != null)
            answertitle.setText(context.getString(intersky.appbase.R.string.xml_workreport_replay) + "(" + String.valueOf(replies.size()) + ")");
        ReplyUtils.addReply(reply, true,context,deltetelistener,answerLayers,handler);
    }

    public static void removeReplyView(int pos,ArrayList<Reply> replies,TextView answertitle,LinearLayout answerLayers,Context context)
    {
        if(pos != -1)
        {
            View mView = answerLayers.getChildAt(pos);
            answerLayers.removeView(mView);
            if(answertitle != null)
            answertitle.setText(context.getString(intersky.appbase.R.string.xml_workreport_replay) + "(" + String.valueOf(replies.size()) + ")");

//            if (replies.size() == 0) {
//                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View mview = mInflater.inflate(intersky.appbase.R.layout.reply_item_none, null);
//                answerLayers.addView(mview);
//                if(answertitle != null)
//                answertitle.setText(context.getString(intersky.appbase.R.string.xml_workreport_replay));
//            }

        }
    }

}
