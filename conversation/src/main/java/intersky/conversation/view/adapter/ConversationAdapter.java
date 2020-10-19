package intersky.conversation.view.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.StringUtils;
import intersky.apputils.TimeUtils;
import intersky.conversation.ConversationManager;
import intersky.conversation.R;
import intersky.appbase.entity.Conversation;

/**
 * Created by xpx on 2016/10/12.
 */

public class ConversationAdapter extends BaseAdapter {

    private ArrayList<Conversation> mConversations;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean type = false;

    public ConversationAdapter(ArrayList<Conversation> mConversations, Context mContext, boolean type) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.type = type;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ConversationAdapter(ArrayList<Conversation> mConversations, Context mContext) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.type = type;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mConversations.size();
    }

    @Override
    public Conversation getItem(int position) {
        return mConversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Conversation mConversation = getItem(position);
        ViewHoder mview = new ViewHoder();
        convertView = mInflater.inflate(R.layout.conversation_item, null);
        mview = new ViewHoder();
        mview.mtitle = (TextView) convertView.findViewById(R.id.conversation_title);
        mview.mhead = (TextView) convertView.findViewById(R.id.head_title);
        mview.imageView = (ImageView) convertView.findViewById(R.id.conversation_img);
        mview.msubject = (TextView) convertView.findViewById(R.id.conversation_subject);
        mview.mtime = (TextView) convertView.findViewById(R.id.conversation_time);
        mview.mhit = (TextView) convertView.findViewById(R.id.hit);
        mview.buttom = (TextView) convertView.findViewById(R.id.bottom);
        convertView.setTag(mview);

        String name = mConversation.mTitle;
        mview.mtitle.setText(name);
        mview.msubject.setText(StringUtils.htmlToString(mConversation.mSubject));

        mview.mtime.setText(TimeUtils.measureDeteForm(mConversation.mTime));
        if(type == true)
        {
            if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_NOTICE))
                mview.imageView.setImageResource(R.drawable.huihua_msglist_gonggaoicn);
            else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
                mview.imageView.setImageResource(R.drawable.contact_head);
                ConversationManager.setContactCycleHead(mview.mhead,mConversation.mTitle);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_TASK)) {
                mview.imageView.setImageResource(R.drawable.huihuatask);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_LEAVE)) {
                mview.imageView.setImageResource(R.drawable.huihua_msglist_leave);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_REPORT)) {
                mview.imageView.setImageResource(R.drawable.huihua_msglist_work_report);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_SCHDULE)) {
                mview.imageView.setImageResource(R.drawable.huihua_msglist_sche);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_VOTE)) {
                mview.imageView.setImageResource(R.drawable.huihua_msglist_vote);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_IWEB_MAIL)) {
                mview.imageView.setImageResource(R.drawable.huihua_msglist_mail);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_IWEB_APPROVE)) {
                mview.imageView.setImageResource(R.drawable.huihua_msglist_rwsp);
            }
            else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_IWEB_REMIND)) {
                mview.imageView.setImageResource(R.drawable.huihua_msglist_remind);
            }else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_GROP_MESSAGE)) {
                mview.imageView.setImageResource(R.drawable.huihua_msglist_mess);
            }
        }
        else
        {
            mview.imageView.setImageResource(R.drawable.huihuamessage);
        }

        if(type == true)
        AppUtils.measureHit(mview.mhit,mConversation.mHit);

        if(position == getCount()-1)
        {
            mview.buttom.setVisibility(View.VISIBLE);
        }
        else
        {
            mview.buttom.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHoder {
        ImageView imageView;
        View buttom;
        TextView mtitle;
        TextView mhead;
        TextView msubject;
        TextView mtime;
        TextView mhit;
    }

    ;
}
