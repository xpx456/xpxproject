package com.bigwiner.android.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Contacts;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import intersky.mywidget.CircleImageView;


/**
 * Created by xpx on 2016/10/12.
 */

public class ContactsAdapter extends RecyclerView.Adapter {

    private ArrayList<Contacts> mContacts;
    private Context mContext;
    private LayoutInflater mInflater;
    private int type = 0;
    public Handler handler;
    public Meeting meeting;
    public ContactsAdapter(ArrayList<Contacts> mContacts, Context mContext, int type,Handler handler,Meeting meeting) {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.type = type;
        this.handler = handler;
        this.meeting = meeting;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ContactsAdapter(ArrayList<Contacts> mContacts, Context mContext) {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.type = 0;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.contacts, null);
        ViewHoder viewHolder = new ViewHoder(convertView);
        return viewHolder;
    }


    public interface OnItemClickListener {
        void onItemClick(Contacts contacts,int position,View view);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Contacts mContact = getItem(position);
        ViewHoder mview = (ViewHoder) holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                mListener.onItemClick(mContact,position,holder.itemView);
            }
        });
        mview.mtitle.setText(mContact.getmRName());
        mview.mtitle2.setText(mContext.getString(R.string.my_des)+mContact.des);
        if(mContact.typevalue.length() == 0 || mContact.typearea.length() == 0)
        {
            mview.gang.setVisibility(View.GONE);
        }

        mview.type1.setText(mContact.typevalue);
        mview.type2.setText(mContact.typearea);
        mview.item1.setVisibility(View.VISIBLE);
        mview.confrim.setText(mContact.confrim);
        if(position == 0)
        {
            mview.t1.setVisibility(View.VISIBLE);
        }
        else
        {
            mview.t1.setVisibility(View.GONE);
        }
        if(mContact.location.length() > 0)
        {
            mview.item2.setVisibility(View.VISIBLE);
            mview.location.setText(mContact.location);
        }
        else
        {
            mview.item2.setVisibility(View.GONE);
        }
        if(mContact.sex == 0)
        {
            mview.seximg.setImageResource(R.drawable.male);
        }
        else
        {
            mview.seximg.setImageResource(R.drawable.female);
        }
        if(!mContact.confrim.equals(mContext.getString(R.string.contacts_confirm)))
        {
            mview.confrim.setText(mContext.getString(R.string.contacts_unconfirm));
            mview.confrim.setTextColor(Color.rgb(185,185,185));
            mview.cimg.setImageResource(R.drawable.confirm2);
            mview.item1.setBackgroundResource(R.drawable.contact_btn_shape_gray_empty);
        }
        if(type == 0)
        {
            mview.btn.setVisibility(View.INVISIBLE);
            mview.btn2.setVisibility(View.INVISIBLE);
            mview.array.setVisibility(View.VISIBLE);
        }
        else if(type == 1)
        {
            mview.btn.setVisibility(View.VISIBLE);
            mview.btn2.setVisibility(View.INVISIBLE);
            mview.array.setVisibility(View.INVISIBLE);
            mview.btn.setBackgroundResource(R.drawable.shape_login_bg_btn);
            mview.btn.setTag(mContact);
            mview.btn.setOnClickListener(dateListener);
            mview.btn.setText(mContext.getString(R.string.meeting_can_date));
        }
        else if(type == 2)
        {
            mview.btn.setVisibility(View.VISIBLE);
            mview.btn2.setVisibility(View.VISIBLE);
            mview.array.setVisibility(View.INVISIBLE);
            mview.btn.setBackgroundResource(R.drawable.shape_refous_bg_btn);
            mview.btn.setTag(mContact);
            mview.btn.setOnClickListener(refousListener);
            mview.btn.setText(mContext.getString(R.string.button_word_refouse));
            mview.btn2.setBackgroundResource(R.drawable.shape_access_bg_btn);
            mview.btn2.setTag(mContact);
            mview.btn2.setOnClickListener(accessListener);
            mview.btn2.setText(mContext.getString(R.string.button_word_accept));
        }
        else if(type == 3)
        {
            mview.btn.setVisibility(View.VISIBLE);
            mview.btn2.setVisibility(View.INVISIBLE);
            mview.array.setVisibility(View.INVISIBLE);
            mview.btn.setBackgroundColor(Color.argb(0,0,0,0));
            mview.btn.setTextColor(Color.GRAY);
            mview.btn.setText(mContact.staue);
        }
        else
        {
            mview.btn.setVisibility(View.INVISIBLE);
            mview.btn2.setVisibility(View.INVISIBLE);
            mview.array.setVisibility(View.VISIBLE);
        }

//        BigwinerApplication.mApp.setContactHead(mContext,mContact.icon,mview.imageView);
        if(mContact.icon.length() == 0)
        {
            if(mContact.sex == 0)
            {
                mview.imageView.setImageResource(R.drawable.default_user);
            }
            else
            {
                mview.imageView.setImageResource(R.drawable.default_wuser);
            }
        }
        else
        {
            File file = ChatUtils.getChatUtils().getHead(mContact.mRecordid);

            if(mContact.sex == 0)
            {
                if(file != null)
                {
                    BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (55*ChatUtils.getChatUtils().mScreenDefine.density)
                            ,(int) (55*ChatUtils.getChatUtils().mScreenDefine.density),file);
                    RequestOptions options = new RequestOptions().override(bitmapSize.width,bitmapSize.height)
                            .placeholder(R.drawable.default_user);
                    Glide.with(mContext).load(file).apply(options).into(new MySimpleTarget(mview.imageView));
                }
                else
                {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override((int) (55* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
                    Glide.with(mContext).load(ContactsAsks.getContactIconUrl(mContact.mRecordid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mview.imageView));
                }
            }
            else
            {
                if(file != null)
                {
                    BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (55*ChatUtils.getChatUtils().mScreenDefine.density)
                            ,(int) (55*ChatUtils.getChatUtils().mScreenDefine.density),file);
                    RequestOptions options = new RequestOptions().override(bitmapSize.width,bitmapSize.height)
                            .placeholder(R.drawable.default_wuser);
                    Glide.with(mContext).load(file).apply(options).into(new MySimpleTarget(mview.imageView));
                }
                else
                {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.default_wuser).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override((int) (55* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
                    Glide.with(mContext).load(ContactsAsks.getContactIconUrl(mContact.mRecordid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mview.imageView));
                }
            }

        }

        if(getItemCount()-1 != position)
        {
            mview.driver.setVisibility(View.VISIBLE);
        }
        else
        {
            mview.driver.setVisibility(View.GONE);
        }
    }


    class ViewHoder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView mtitle;
        TextView mtitle2;
        TextView type1;
        TextView type2;
        RelativeLayout item1;
        TextView confrim;
        RelativeLayout item2;
        RelativeLayout sex;
        View t1;
        ImageView cimg;
        ImageView seximg;
        ImageView array;
        TextView btn;
        TextView btn2;
        TextView location;
        TextView gang;
        RelativeLayout driver;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            mtitle = (TextView) itemView.findViewById(R.id.conversation_title);
            mtitle2 = (TextView) itemView.findViewById(R.id.des);
            imageView = (CircleImageView) itemView.findViewById(R.id.conversation_img);
            type1 = (TextView) itemView.findViewById(R.id.typetitle);
            type2 = (TextView) itemView.findViewById(R.id.typearea);
            item1 = (RelativeLayout) itemView.findViewById(R.id.confirm);
            cimg = itemView.findViewById(R.id.confirmimg);
            confrim = itemView.findViewById(R.id.cinfirmsubject);
            item2 = (RelativeLayout) itemView.findViewById(R.id.location);
            location = (TextView) itemView.findViewById(R.id.locationsubject);
            sex = itemView.findViewById(R.id.sexlayer);
            seximg = itemView.findViewById(R.id.sex);
            t1 = itemView.findViewById(R.id.t1);
            array = itemView.findViewById(R.id.array);
            btn = itemView.findViewById(R.id.btn);
            btn2 = itemView.findViewById(R.id.btn2);
            gang = itemView.findViewById(R.id.typegang);
            driver = itemView.findViewById(R.id.driver);
        }
    }

    public Contacts getItem(int position) {
        return mContacts.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    public View.OnClickListener dateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContactsAsks.doDate(mContext,handler,meeting, (Contacts) v.getTag());
        }
    };
    public View.OnClickListener refousListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContactsAsks.doSetApply(mContext,handler,meeting, (Contacts) v.getTag(),"refused");
        }
    };

    public View.OnClickListener accessListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContactsAsks.doSetApply(mContext,handler,meeting, (Contacts) v.getTag(),"agreed");
        }
    };

}
