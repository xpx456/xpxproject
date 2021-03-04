package com.intersky.android.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.intersky.R;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Contacts;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.chat.ChatUtils;
import intersky.function.FunctionUtils;
import intersky.function.entity.Function;
import intersky.function.view.activity.FunctionModuleActivity;
import intersky.mywidget.CircleImageView;


/**
 * Created by xpx on 2016/10/12.
 */

public class FunctionAddAdapter extends RecyclerView.Adapter {

    private ArrayList<Function> mFunctions;
    private Context mContext;
    private LayoutInflater mInflater;
    private int type = 0;
    public Handler handler;
    public boolean search = false;

    public FunctionAddAdapter(ArrayList<Function> mFunctions, Context mContext) {
        this.mContext = mContext;
        this.mFunctions = mFunctions;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FunctionAddAdapter(ArrayList<Function> mFunctions, Context mContext,boolean search) {
        this.mContext = mContext;
        this.mFunctions = mFunctions;
        this.search = search;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mFunctions.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView;
        if(search)
        {
            convertView = mInflater.inflate(R.layout.functionselect2, null);
        }
        else
        {
            convertView = mInflater.inflate(R.layout.functionselect, null);
        }

        ViewHoder viewHolder = new ViewHoder(convertView);
        return viewHolder;
    }


    public interface OnItemClickListener {
        void onItemClick(Function function, int position, View view);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Function mFunction = getItem(position);
        ViewHoder mview = (ViewHoder) holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                mListener.onItemClick(mFunction,position,holder.itemView);
            }
        });
        mview.mtitle.setText(mFunction.mCaption);
        mview.mtitle2.setText(mFunction.des);
        if(!mFunction.typeName.equals(Function.BLACK))
        {
            Bitmap bmap =getBitmap(mFunction);
            if(bmap == null)
            {
                if(mFunction.mCatalogueName.equals("web"))
                {
                    String url = createIconURLStringWeb(mFunction.iconName, new String());
                    Glide.with(mContext).load(url).into(mview.icon);
                }
                else
                {
                    String url = createIconURLString(mFunction.iconName, new String());
                    Glide.with(mContext).load(url).into(mview.icon);
                }

            }
            else
            {
                mview.icon.setImageBitmap(bmap);
            }
        }
        if(search == false)
        {
            if(mFunction.select)
            {
                mview.select.setImageResource(R.drawable.selects);
            }
            else
            {
                mview.select.setImageResource(R.drawable.select);
            }
        }

    }
    public final String createIconURLString(String path, String params) {
        String urlString = null;
        if(FunctionUtils.getInstance().service.https)
        {
            if(path.startsWith("/"))
            {
                if(params.length() != 0)
                    urlString = "https://" + FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // PORT
                            + "" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path;
            }
            else
            {
                if(params.length() != 0)
                    urlString = "https://" + FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // PORT
                            + "/" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "/" + path;
            }
        }
        else
        {
            if(path.startsWith("/"))
            {
                if(params.length() != 0)
                    urlString = "http://" + FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // PORT
                            + "" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path;
            }
            else
            {
                if(params.length() != 0)
                    urlString = "http://" + FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // PORT
                            + "/" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "/" + path;
            }
        }


        return urlString;
    }



    public final String createIconURLStringWeb(String path, String params) {
        String urlString = null;
        if(FunctionUtils.getInstance().service.https)
        {
            if(path.startsWith("/"))
            {
                if(params.length() != 0)
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path;
            }
            else
            {
                if(params.length() != 0)
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "/" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort// HOST
                            + "" + path;
            }
        }
        else
        {
            if(path.startsWith("/"))
            {
                if(params.length() != 0)
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path;
            }
            else
            {
                if(params.length() != 0)
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "/" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort// HOST
                            + "" + path;
            }
        }



        return urlString;
    }

    public Bitmap getBitmap(Function func) {
        Bitmap bmap = null;

        if (func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_businessWarn))) {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.remind);
        } else if (func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_taskExamine))) {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.apporve);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_businesscard)))
        {

            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.cardmamager);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_system)))
        {
            if(func.typeName.equals(Function.NOTICE))
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.notice);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.noticec);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_newtask)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.taskc);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_newproject)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.projectc);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_newmail)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.mailc);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_workreport)))
        {
            if(func.typeName.equals(Function.WORKREPORT))
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.workreport);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.workreportc);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_sign)))
        {
            if(func.typeName.equals(Function.SIGN))
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.sign);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.signc);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_leave)))
        {
            if(func.typeName.equals(Function.LEAVE))
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.leave);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.leavec);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_leave_n)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.leavec);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_schedule)))
        {
            if(func.typeName.equals(Function.DATE))
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.schdule);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.schdulec);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_date_n)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.schdulec);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_attdence)))
        {
            if(func.typeName.equals(Function.WORKATTDENCE))
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.attdence);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.attdencec);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_systemmesage_n)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.noticec);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_vote)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.vote);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_task)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.task);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(intersky.function.R.string.function_vote_n)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.votec);
        }
        else if(func.mCaption.equals(mContext.getString(R.string.add_comm)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.funadd);
        }
        else if(func.typeName.equals(Function.MAIL))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.mail);
        }
        else if(func.typeName.equals(Function.DOCUMENT))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.document);
        }
        else if(func.typeName.equals(Function.SEARCH))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), intersky.function.R.drawable.smartsearch);
        }
        return bmap;
    }




    class ViewHoder extends RecyclerView.ViewHolder{

        ImageView icon;
        ImageView select;
        TextView mtitle;
        TextView mtitle2;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            mtitle = (TextView) itemView.findViewById(R.id.function_title);
            mtitle2 = (TextView) itemView.findViewById(R.id.function_sub);
            icon = (ImageView) itemView.findViewById(R.id.function_img);
            if(search == false)
            select = (ImageView) itemView.findViewById(R.id.select);

        }
    }

    public Function getItem(int position) {
        return mFunctions.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


}
