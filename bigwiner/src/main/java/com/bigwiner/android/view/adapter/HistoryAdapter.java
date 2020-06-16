package com.bigwiner.android.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AttdenceActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import intersky.mywidget.CircleImageView;


/**
 * Created by xpx on 2016/10/12.
 */

public class HistoryAdapter extends RecyclerView.Adapter {

    private ArrayList<Conversation> mConversations;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean type = false;

    public HistoryAdapter(ArrayList<Conversation> mConversations, Context mContext, boolean type) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.type = type;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public HistoryAdapter(ArrayList<Conversation> mConversations, Context mContext) {
        this.mContext = mContext;
        this.mConversations = mConversations;
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
        if(mType.equals(Conversation.CONVERSATION_TYPE_TITLE))
        {
            convertView = mInflater.inflate(R.layout.history_title, null);
            return new TitleHolder(convertView);
        }
        else if(mType.equals(Conversation.CONVERSATION_TYPE_MEETING))
        {
            convertView = mInflater.inflate(R.layout.conversation_meeting, null);
            return new MeetingHoder(convertView);
        }
        else
        {
            convertView = mInflater.inflate(R.layout.history, null);
            return new HistoryHolder(convertView);
        }
    }

    class TitleHolder extends RecyclerView.ViewHolder{
        TextView title;
        public TitleHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.conversation_title);
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

    class HistoryHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        CircleImageView imageView;
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.conversation_title);
            time = itemView.findViewById(R.id.conversation_time);
            imageView = itemView.findViewById(R.id.conversation_img);
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
        if(mConversation.mType.equals(Conversation.CONVERSATION_TYPE_TITLE) ) {
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.title.setText(TimeUtils.praseHistoryDate(mContext,mConversation.mTime));
        }
        else if(mConversation.mType.equals(Conversation.CONVERSATION_TYPE_MEETING)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onItemClick(mConversation,position,holder.itemView);
                }
            });
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
            if(meetingHoder.time.length() > 10)
                meetingHoder.time.setText(mConversation.mTime.substring(0,10));
            meetingHoder.address.setText(mConversation.mTitle);
            meetingHoder.countmin.setText(String.valueOf(mConversation.mHit));
            meetingHoder.countmax.setText(String.valueOf(mConversation.mHit2));
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.meetingtemp);
            meetingHoder.title.setText(mConversation.mSubject);
            if(mConversation.sourceId.length() > 0)
                GlideApp.with(mContext).load(BigwinerApplication.mApp.measureImg(mConversation.sourceId)).apply(options).into(new MySimpleTarget(meetingHoder.imageView));

            View line = meetingHoder.itemView.findViewById(R.id.line);
            line.setVisibility(View.VISIBLE);

            if(position == mConversations.size()-1) {
                line.setVisibility(View.INVISIBLE);
            }
        }
        else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onItemClick(mConversation,position,holder.itemView);
                }
            });
            HistoryHolder historyHolder = (HistoryHolder) holder;
            historyHolder.title.setText(mConversation.mSubject);
            historyHolder.time.setText(mConversation.mTime.substring(0,10));
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.plugin_camera_no_pictures);
            GlideApp.with(mContext).load(BigwinerApplication.mApp.measureImg(mConversation.sourceId)).apply(options).into(new MySimpleTarget(historyHolder.imageView));

            View line = historyHolder.itemView.findViewById(R.id.line);
            line.setVisibility(View.VISIBLE);

            if(position == mConversations.size()-1)
            {
                line.setVisibility(View.INVISIBLE);
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
