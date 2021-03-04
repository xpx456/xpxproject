package intersky.conversation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.StringUtils;
import intersky.apputils.TimeUtils;
import intersky.conversation.ConversationManager;
import intersky.conversation.R;
import intersky.mywidget.SwipeRevealLayout;

/**
 * Created by xpx on 2016/10/12.
 */

public class ConversationAdapter2 extends RecyclerView.Adapter {

    private ArrayList<Conversation> mConversations;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean type = false;
    public SwapFunction swapFunction;

    public ConversationAdapter2(ArrayList<Conversation> mConversations, Context mContext, boolean type) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.type = type;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ConversationAdapter2(ArrayList<Conversation> mConversations, Context mContext) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.type = type;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ConversationAdapter2(ArrayList<Conversation> mConversations, Context mContext, SwapFunction swapFunction) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.type = type;
        this.swapFunction = swapFunction;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ConversationAdapter2(ArrayList<Conversation> mConversations, Context mContext, boolean type, SwapFunction swapFunction) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.type = type;
        this.swapFunction = swapFunction;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Conversation getItem(int position) {
        return mConversations.get(position);
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        ViewHoder mview = (ViewHoder) viewHolder;

        final Conversation mConversation = getItem(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onItemClick(mConversation,position,viewHolder.itemView);
                }
            }
        });
        mview.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onItemClick(mConversation,position,viewHolder.itemView);
                }
            }
        });
        String name = mConversation.mTitle;
        mview.mtitle.setText(name);
        mview.msubject.setText(StringUtils.htmlToString(mConversation.mSubject));
        mview.delete.setTag(mConversation);
        mview.delete.setOnClickListener(onDeleteListener);
        mview.mtime.setText(TimeUtils.measureDeteForm(mContext,mConversation.mTime));
        if(type == true)
        {
            if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_NOTICE))
                mview.imageView.setImageResource(R.drawable.noticeh);
            else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
                mview.imageView.setImageResource(R.drawable.contact_head);
                ConversationManager.setContactCycleHead(mview.mhead,mConversation.mTitle);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_TASK)) {
                mview.imageView.setImageResource(R.drawable.taskh);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_LEAVE)) {
                mview.imageView.setImageResource(R.drawable.leaveh);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_REPORT)) {
                mview.imageView.setImageResource(R.drawable.workreporth);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_SCHDULE)) {
                mview.imageView.setImageResource(R.drawable.schduleh);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_VOTE)) {
                mview.imageView.setImageResource(R.drawable.voteh);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_IWEB_MAIL)) {
                mview.imageView.setImageResource(R.drawable.mailh);
            } else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_IWEB_APPROVE)) {
                mview.imageView.setImageResource(R.drawable.apporveh);
            }
            else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_IWEB_REMIND)) {
                mview.imageView.setImageResource(R.drawable.remindh);
            }else if (mConversation.mType.equals(Conversation.CONVERSATION_TYPE_GROP_MESSAGE)) {
                mview.imageView.setImageResource(R.drawable.gropmessageh);
            }
        }
        else
        {
            mview.imageView.setImageResource(R.drawable.gropmessageh);
        }

        if(type == true)
        AppUtils.measureHit(mview.mhit,mConversation.mHit);
    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.conversation_item, null);
        return new ViewHoder(convertView);
    }


    public View.OnClickListener onDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            swapFunction.delete((Conversation) v.getTag());
        }
    };

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface SwapFunction {
        void read(Conversation conversation);
        void delete(Conversation conversation);
    }

    public interface OnItemClickListener{
        void onItemClick(Conversation conversation, int position, View view);
    }

    private OnItemClickListener mListener;

    public class ViewHoder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView mtitle;
        TextView mhead;
        TextView msubject;
        TextView mtime;
        TextView mhit;
        TextView delete;
        Button button;
        SwipeRevealLayout swipeLayout;
        public ViewHoder(@NonNull View mview) {
            super(mview);
            swipeLayout = (SwipeRevealLayout) mview.findViewById(R.id.swipe);
            button = (Button) mview.findViewById(R.id.btnlayer);
            mtitle = (TextView) mview.findViewById(R.id.conversation_title);
            mhead = (TextView) mview.findViewById(R.id.head_title);
            imageView = (ImageView) mview.findViewById(R.id.conversation_img);
            msubject = (TextView) mview.findViewById(R.id.conversation_subject);
            mtime = (TextView) mview.findViewById(R.id.conversation_time);
            mhit = (TextView) mview.findViewById(R.id.hit);
            delete = mview.findViewById(R.id.delete);
        }
    };


    public interface ConversationFunction {
        void read(Conversation conversation);
        void delete(Conversation conversation);
    }
}
