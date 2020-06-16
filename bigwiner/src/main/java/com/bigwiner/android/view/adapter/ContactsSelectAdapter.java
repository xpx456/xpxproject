package com.bigwiner.android.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.view.BigwinerApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.mywidget.CircleImageView;
import intersky.talk.GifTextView;


/**
 * Created by xpx on 2016/10/12.
 */

public class ContactsSelectAdapter extends RecyclerView.Adapter {

    private ArrayList<Contacts> mContacts;
    private Context mContext;
    private LayoutInflater mInflater;
    public Handler handler;
    public Meeting meeting;

    public ContactsSelectAdapter(ArrayList<Contacts> mContacts, Context mContext) {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View convertView;
        if(viewType == Contacts.TYPE_PERSON)
        {
            convertView = mInflater.inflate(R.layout.contact_select, null);
            PersonViewHolder personViewHolder = new PersonViewHolder(convertView);
            return personViewHolder;
        }
        else
        {
            convertView = mInflater.inflate(R.layout.contact_select_head, null);
            HeadViewHolder mHeadViewHolder = new HeadViewHolder(convertView);
            return mHeadViewHolder;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Contacts contacts,int position,View view);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    class PersonViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView title;
        RelativeLayout line;
        public PersonViewHolder(@NonNull View convertView) {
            super(convertView);
            imageView = convertView.findViewById(R.id.conversation_img);
            title = convertView.findViewById(R.id.conversation_title);
            line = convertView.findViewById(R.id.line);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public HeadViewHolder(@NonNull View convertView) {
            super(convertView);
            title = convertView.findViewById(R.id.conversation_title);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Contacts mContact = mContacts.get(position);


        if(mContact.mType == Contacts.TYPE_PERSON)
        {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                    mListener.onItemClick(mContact,position,holder.itemView);
                }
            });

            PersonViewHolder personViewHolder = (PersonViewHolder) holder;
            personViewHolder.title.setText(mContact.getmRName());
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override((int) (50* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
            Glide.with(mContext).load(ContactsAsks.getContactIconUrl(mContact.mRecordid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(personViewHolder.imageView));
            if(position != getItemCount()-1)
            {
                Contacts contacts = mContacts.get(position+1);
                if(contacts.mType != Contacts.TYPE_PERSON)
                {
                    personViewHolder.line.setVisibility(View.INVISIBLE);
                }
                else
                {
                    personViewHolder.line.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                personViewHolder.line.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            headViewHolder.title.setText(mContact.getmRName());
        }
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mContacts.get(position).mType;
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

}
