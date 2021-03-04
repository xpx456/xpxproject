package com.bigwiner.android.view.adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.ViewHelp;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AttdenceActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import org.jsoup.Jsoup;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import intersky.mywidget.CircleImageView;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.SwipeRevealLayout;
import intersky.talk.GifTextView;



/**
 * Created by xpx on 2016/10/12.
 */

public class ConversationAdapter extends RecyclerView.Adapter {

    private ArrayList<Conversation> mConversations;
    private Context mContext;
    private LayoutInflater mInflater;
    public Handler handler;
    private String keyword = "";

    public ConversationAdapter(ArrayList<Conversation> mConversations, Context mContext,Handler handler, String keyword) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.keyword = keyword;
        this.handler = handler;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ConversationAdapter(ArrayList<Conversation> mConversations, Context mContext,Handler handler) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.handler = handler;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public Conversation getItem(int position) {
        return mConversations.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        String mType = getStringType(viewType);
        View convertView;
        if(mType.equals(Conversation.CONVERSATION_TYPE_NOTICE))
        {
            convertView = mInflater.inflate(R.layout.conversation_notice, null);
            return new NoticeHoder(convertView);
        }
        else if(mType.equals(Conversation.CONVERSATION_TYPE_NEWS))
        {
            convertView = mInflater.inflate(R.layout.conversation_news, null);
            return new NewsHoder(convertView);
        }
        else if(mType.equals(Conversation.CONVERSATION_TYPE_MEETING))
        {
            convertView = mInflater.inflate(R.layout.conversation_meeting, null);
            return new MeetingHoder(convertView);
        }
        else if(mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE))
        {
            convertView = mInflater.inflate(R.layout.conversation_chat, null);
            return new MessageHoder(convertView);
        }
        else
        {
            convertView = mInflater.inflate(R.layout.conversation_title, null);
            return new TitleHolder(convertView);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Conversation conversation, int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    private OnItemClickListener mListener;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Conversation mConversation = getItem(position);
        if(mConversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
            MessageHoder messageHoder = (MessageHoder) holder;
            messageHoder.title.setText(mConversation.mTitle);
            messageHoder.time.setText(mConversation.mTime);
            messageHoder.subject.setSpanText(handler, Jsoup.parse(mConversation.mSubject).text());
            if(keyword.length() == 0)
                messageHoder.subject.setText(mConversation.mSubject);
            else
                messageHoder.subject.setText(AppUtils.highlight(mConversation.mSubject,keyword, Color.rgb(255,187,8)));
            AppUtils.measureHit(messageHoder.hit,mConversation.mHit);

            File file = ChatUtils.getChatUtils().getHead(mConversation.detialId);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override((int) (50* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
            Glide.with(mContext).load(ContactsAsks.getContactIconUrl(mConversation.detialId,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(messageHoder.imageView));
            messageHoder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(conversationFunction != null)
                        conversationFunction.delete(mConversation);
                }
            });
            messageHoder.read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(conversationFunction != null)
                        conversationFunction.read(mConversation);
                }
            });
        }
        else if(mConversation.mType.equals(Conversation.CONVERSATION_TYPE_NOTICE)) {
            NoticeHoder noticeHoder = (NoticeHoder) holder;
            if(mConversation.isRead)
            {
                noticeHoder.title.setTextColor(0xFFB9B9B9);
            }
            else
            {
                noticeHoder.title.setTextColor(0xFF101010);
            }
            if(keyword.length() == 0)
                noticeHoder.title.setText(mConversation.mSubject);
            else
                noticeHoder.title.setText(AppUtils.highlight(mConversation.mSubject,keyword, Color.rgb(255,187,8)));
            noticeHoder.time.setText(mConversation.mTime.substring(0,10));
            String lable[] = mConversation.mTitle.split(",");
            noticeHoder.mMyLinearLayout.removeAllViews();
            if(lable.length > 0)
            {
                for(int i = 0 ; i < lable.length ; i++)
                {
                    if(i == 0)
                        ViewHelp.measureConversationLable(noticeHoder.mMyLinearLayout,"#"+lable[i],R.drawable.conversation_lable_shape_yellow,mInflater);
                    else
                        ViewHelp.measureConversationLable(noticeHoder.mMyLinearLayout,lable[i],R.drawable.conversation_lable_shape_gray,mInflater);
                }
            }
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.noticetemp);
            if(mConversation.sourceId.length() > 0)
                Glide.with(mContext).load(BigwinerApplication.mApp.measureImg(mConversation.sourceId)).apply(options).into(new MySimpleTarget(noticeHoder.imageView));

        }
        else if(mConversation.mType.equals(Conversation.CONVERSATION_TYPE_NEWS)) {
            NewsHoder newsHoder = (NewsHoder) holder;
            if(mConversation.isRead)
            {
                newsHoder.title.setTextColor(0xFFB9B9B9);
            }
            else
            {
                newsHoder.title.setTextColor(0xFF101010);
            }
            if(keyword.length() == 0)
                newsHoder.title.setText(mConversation.mSubject);
            else
                newsHoder.title.setText(AppUtils.highlight(mConversation.mSubject,keyword, Color.rgb(255,187,8)));
            newsHoder.time.setText(mConversation.mTime.substring(0,10));
            newsHoder.count.setText(String.valueOf(mConversation.mHit));
            newsHoder.mMyLinearLayout.removeAllViews();
            String lable[] = mConversation.mTitle.split(",");
            if(lable.length > 0)
            {
                for(int i = 0 ; i < lable.length ; i++)
                {
                    if(i == 0)
                        ViewHelp.measureConversationLable(newsHoder.mMyLinearLayout,"#"+lable[i],R.drawable.conversation_lable_shape_green,mInflater);
                    else
                        ViewHelp.measureConversationLable(newsHoder.mMyLinearLayout,lable[i],R.drawable.conversation_lable_shape_gray,mInflater);
                }
            }
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.newstemp);
            if(mConversation.sourceId.length() > 0)
                Glide.with(mContext).load(BigwinerApplication.mApp.measureImg(mConversation.sourceId)).apply(options).into(new MySimpleTarget(newsHoder.imageView));
        }
        else if(mConversation.mType.equals(Conversation.CONVERSATION_TYPE_MEETING)) {
            MeetingHoder meetingHoder = (MeetingHoder) holder;
            if(mConversation.sourceName.equals("未开始") || mConversation.sourceName.length() == 0)
            {
                if(mConversation.isSendto == false)
                {
                    meetingHoder.join.setText(mContext.getString(R.string.conversation_join));
                    meetingHoder.button.setTag(mConversation);
                    meetingHoder.button.setVisibility(View.VISIBLE);
                    meetingHoder.button.setOnClickListener(joinListener);
                }
                else
                {
                    meetingHoder.join.setText(mContext.getString(R.string.conversation_joined));
                    meetingHoder.button.setVisibility(View.GONE);
                }
            }
            else
            {
                meetingHoder.join.setText(mConversation.sourceName);
                meetingHoder.button.setVisibility(View.GONE);
            }

            if(keyword.length() == 0)
                meetingHoder.title.setText(mConversation.mSubject);
            else
                meetingHoder.title.setText(AppUtils.highlight(mConversation.mSubject,keyword, Color.rgb(255,187,8)));
            meetingHoder.time.setText(mConversation.mTime.substring(0,10));
            meetingHoder.address.setText(mConversation.mTitle);
            meetingHoder.countmin.setText(String.valueOf(mConversation.mHit));
            meetingHoder.countmax.setText(String.valueOf(mConversation.mHit2));
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.meetingtemp);
            if(mConversation.sourceId.length() > 0)
                Glide.with(mContext).load(BigwinerApplication.mApp.measureImg(mConversation.sourceId)).apply(options).into(new MySimpleTarget(meetingHoder.imageView));
        }
        else {
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.title.setText(mConversation.mTitle);
            titleHolder.time.setText(AppUtils.highlight(mContext.getString(R.string.conversation_gong)+String.valueOf(mConversation.mHit)+mContext.getString(R.string.conversation_ge),String.valueOf(mConversation.mHit), Color.rgb(255,187,8)));


        }
        if(!mConversation.mType.equals(Conversation.CONVERSATION_TYPE_TITLE)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onItemClick(mConversation,position,holder.itemView);
                }
            });
            if(mConversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE))
            {
                RelativeLayout relativeLayout = holder.itemView.findViewById(R.id.conversationlayer);
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null)
                            mListener.onItemClick(mConversation,position,holder.itemView);
                    }
                });
            }

            View convertView = holder.itemView;
            View view = convertView.findViewById(R.id.driver);
            view.setVisibility(View.GONE);
            RelativeLayout endline = convertView.findViewById(R.id.line);
            endline.setVisibility(View.VISIBLE);

            if(position != mConversations.size()-1)
            {
                Conversation nextConversation = mConversations.get(position+1);
                if(!nextConversation.mType.equals(mConversation.mType))
                {
                    view.setVisibility(View.VISIBLE);
                    endline.setVisibility(View.INVISIBLE);
                }
                else
                {
                    view.setVisibility(View.INVISIBLE);
                    endline.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                endline.setVisibility(View.INVISIBLE);
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mConversations.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getIntType(getItem(position).mType);
    }

    class TitleHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        public TitleHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.conversation_title);
            time = itemView.findViewById(R.id.conversation_time);
        }
    }

    class NoticeHoder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        CircleImageView imageView;
        MyLinearLayout mMyLinearLayout;
        public NoticeHoder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.conversation_title);
            time = itemView.findViewById(R.id.conversation_time);
            imageView = itemView.findViewById(R.id.conversation_img);
            mMyLinearLayout = itemView.findViewById(R.id.lable);
        }
    }

    class NewsHoder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        TextView count;
        CircleImageView imageView;
        MyLinearLayout mMyLinearLayout;
        public NewsHoder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.conversation_title);
            time = itemView.findViewById(R.id.conversation_time);
            imageView = itemView.findViewById(R.id.conversation_img);
            mMyLinearLayout = itemView.findViewById(R.id.lable);
            count = itemView.findViewById(R.id.conversation_view);
        }
    }

    class MeetingHoder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        CircleImageView imageView;
        TextView countmin;
        TextView countmax;
        TextView address;
        TextView join;
        Button button;
        public MeetingHoder(@NonNull View convertView) {
            super(convertView);
            imageView = convertView.findViewById(R.id.conversation_img);
            title = convertView.findViewById(R.id.conversation_title);
            time = convertView.findViewById(R.id.conversation_time);
            countmin = convertView.findViewById(R.id.count_result);
            countmax = convertView.findViewById(R.id.count_max);
            address = convertView.findViewById(R.id.conversation_address);
            join = convertView.findViewById(R.id.count_join);
            button = convertView.findViewById(R.id.count_join_btn);
        }
    }

    class MessageHoder extends RecyclerView.ViewHolder{
        SwipeRevealLayout swipeLayout;
        TextView title;
        TextView time;
        CircleImageView imageView;
        TextView hit;
        TextView delete;
        TextView read;
        GifTextView subject;
        public MessageHoder(@NonNull View convertView) {
            super(convertView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe);
            imageView = convertView.findViewById(R.id.conversation_img);
            title = convertView.findViewById(R.id.conversation_title);
            time = convertView.findViewById(R.id.conversation_time);
            subject = convertView.findViewById(R.id.conversation_subject);
            delete = convertView.findViewById(R.id.delete);
            read = convertView.findViewById(R.id.read);
            hit = convertView.findViewById(R.id.hit);
        }
    }


    public View.OnClickListener joinListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Conversation conversation = (Conversation) v.getTag();
            if(BigwinerApplication.mApp.checkConfirm(mContext,mContext.getString(R.string.confirm_source_meeting)))
            {
                Meeting meeting = new Meeting();
                meeting.recordid = conversation.detialId;
                Intent intent = new Intent(mContext, AttdenceActivity.class);
                intent.putExtra("meeting",meeting);
                mContext.startActivity(intent);
            }
        }
    };

    public ConversationFunction conversationFunction;
    public void setConversationFunction(ConversationFunction conversationFunction) {
        this.conversationFunction = conversationFunction;
    }

    public interface ConversationFunction {
         void read(Conversation conversation);
         void delete(Conversation conversation);
    }

    public String getStringType(int type) {
        String stype = "";
        switch (type)
        {
            case 0:
                stype = Conversation.CONVERSATION_TYPE_NOTICE;
                break;
            case 1:
                stype = Conversation.CONVERSATION_TYPE_NEWS;
                break;
            case 2:
                stype = Conversation.CONVERSATION_TYPE_MEETING;
                break;
            case 3:
                stype = Conversation.CONVERSATION_TYPE_TITLE;
                break;
            case 4:
                stype = Conversation.CONVERSATION_TYPE_MESSAGE;
                break;
        }
        return stype;
    }


    public int getIntType(String type) {
        if(type.equals(Conversation.CONVERSATION_TYPE_NOTICE))
        {
            return 0;
        }
        else if(type.equals(Conversation.CONVERSATION_TYPE_NEWS))
        {
            return 1;
        }
        else if(type.equals(Conversation.CONVERSATION_TYPE_MEETING))
        {
            return 2;
        }
        else if(type.equals(Conversation.CONVERSATION_TYPE_TITLE))
        {
            return 3;
        }
        else if(type.equals(Conversation.CONVERSATION_TYPE_MESSAGE))
        {
            return 4;
        }
        else
        {
            return 5;
        }
    }

}
