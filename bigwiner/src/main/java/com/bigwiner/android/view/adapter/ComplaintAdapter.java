package com.bigwiner.android.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.bigwiner.android.asks.SailAsks;
import com.bigwiner.android.entity.Complaint;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.ShareItem;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import intersky.mywidget.CircleImageView;


/**
 * Created by xpx on 2016/10/12.
 */

public class ComplaintAdapter extends RecyclerView.Adapter {

    private ArrayList<Complaint> mComplaints;
    private Activity mContext;
    private LayoutInflater mInflater;
    public Handler handler;

    public ComplaintAdapter(ArrayList<Complaint> mContacts, Activity mContext) {
        this.mContext = mContext;
        this.mComplaints = mContacts;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Complaint getItem(int position) {
        return mComplaints.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.complaint, null);
        return new ComplaintViewHoder(convertView);
    }

    public interface OnItemClickListener{
        void onItemClick(Complaint complaint,int position,View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    private OnItemClickListener mListener;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Complaint mContact = getItem(position);
        ComplaintViewHoder mview = (ComplaintViewHoder) holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onItemClick(mContact,position,holder.itemView);
            }
        });
        mview.mtitle.setText(mContact.cname1);
        mview.requset.setText(mContact.request);
        mview.result.setText(mContact.result);
        mview.confrim.setText(mContact.statue);
        ShareItem shareItem = new ShareItem();
        shareItem.title = mContact.cname1;
        shareItem.des = mContact.request;
        shareItem.picurl = ContactsAsks.getCompanyIconUrl(mContact.cid1,BigwinerApplication.mApp.contactManager.updataKey);
        mview.share.setOnClickListener(new BigwinerApplication.DoshareListener(mContext,shareItem));
        mview.btnComplaint.setOnClickListener(doComplaintListener);
        mview.btnComplaint.setTag(mContact);
        if(position == 0)
        {
            mview.t1.setVisibility(View.VISIBLE);
        }
        else
        {
            mview.t1.setVisibility(View.GONE);
        }
        RequestOptions options = new RequestOptions()
                .placeholder(com.bigwiner.R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL)
                .override((int) (50* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
        GlideApp.with(mContext).load(ContactsAsks.getCompanyIconUrl(mContact.cid1,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(mview.imageView));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mComplaints.size();
    }

    class ComplaintViewHoder extends  RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView mtitle;
        TextView requset;
        TextView result;
        TextView confrim;
        View t1;
        ArrayList<ImageView> stars = new ArrayList<ImageView>();
        RelativeLayout btnComplaint;
        ImageView share;

        public ComplaintViewHoder(@NonNull View convertView) {
            super(convertView);
            mtitle = (TextView) convertView.findViewById(R.id.conversation_title);
            requset = (TextView) convertView.findViewById(R.id.requestvalue);
            result = (TextView) convertView.findViewById(R.id.resultvalue);
            imageView = (CircleImageView) convertView.findViewById(R.id.conversation_img);
            confrim = convertView.findViewById(R.id.approvetitle);
            share = convertView.findViewById(R.id.share);
            btnComplaint = convertView.findViewById(R.id.complaint);
            t1 = convertView.findViewById(R.id.t1);
        }
    };

    public View.OnClickListener doComplaintListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Complaint complaint = (Complaint) v.getTag();
            Intent intent = new Intent(mContext, WebMessageActivity.class);
            intent.putExtra("url", SailAsks.praseComplantDetialUrl(complaint.id));
            mContext.startActivity(intent);
        }
    };



}
