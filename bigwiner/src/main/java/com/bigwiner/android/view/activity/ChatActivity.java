package com.bigwiner.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.bigwiner.R;
import com.bigwiner.android.presenter.ChatPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.Actions;
import intersky.appbase.BaseActivity;
import intersky.appbase.Downloadobject;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.GlideApp;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.ProgressInterceptor;
import intersky.chat.ChatUtils;
import intersky.chat.entity.ChatPager;
import intersky.chat.entity.UPlayer;
import intersky.filetools.FileUtils;
import intersky.mywidget.CustomScrollView;
import intersky.mywidget.CustomerLinearLayout;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.RoundProgressBar;
import intersky.talk.AudioLayout;
import intersky.talk.ImFaceRelativeLayout;

/**
 * Created by xpx on 2017/8/18.
 */

public class ChatActivity extends BaseActivity implements  CustomScrollView.OnScrollChangeListener {
    public ImageView backBtn;
    public Contacts mContacts;
    public UPlayer mUPlayer;
    public LinearLayout chatView;
    public CustomScrollView scrollView;
    public ImFaceRelativeLayout impuArer;
//    public ChatAdapter mOWAdapter;
    public ChatPager chatPager;
    public AudioLayout audioLayout;
    public Conversation selectmsg;
    public Uri fileUri;
    public Conversation mUserMessageModel;
    public PopupMenu popup;
//    public HashMap<String,Downloadobject> mDownloadThreads = new HashMap<String,Downloadobject>();
    public HashMap<String, Attachment> attachmentHashMap = new HashMap<String,Attachment>();
    public ArrayList<Attachment> attachments = new ArrayList<Attachment>();
    public ChatPresenter mChatPresenter = new ChatPresenter(this);
    public String coderesult;
    public TextView textView;
    public RelativeLayout shade;
    public EditText editTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mChatPresenter.Destroy();
        super.onDestroy();
    }

    // 监听事件
    public PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            mChatPresenter.MenuItemClick(item);
            return false;
        }
    };

    public View.OnLongClickListener mShowmore = new View.OnLongClickListener()
    {

        @Override
        public boolean onLongClick(View v) {
            mChatPresenter.showMore(v, (Conversation) v.getTag());
            return true;
        }
    };

    public AdapterView.OnItemLongClickListener mlongclicke = new AdapterView.OnItemLongClickListener()
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //mChatPresenter.showMore(view, (UserMessageModel) parent.getAdapter().getItem(position));
            return false;
        }
    };


    public View.OnClickListener pickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mChatPresenter.doPick();
        }
    };

    public View.OnClickListener takeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mChatPresenter.takePhoto();
        }
    };

    public View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mChatPresenter.doSendMessage();
        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                mChatPresenter.takePhotoResult();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }


    }

    public View.OnClickListener mOnClickListener = new  View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Conversation msg = (Conversation) v.getTag();
            mChatPresenter.onItemClick(msg);
        }
    };

    public View.OnClickListener mOnHeadListener = new  View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mChatPresenter.mChatActivity, ContactDetialActivity.class);
            intent.putExtra("contacts",mChatPresenter.mChatActivity.mContacts);
            mChatPresenter.mChatActivity.startActivity(intent);
        }
    };

    public View.OnClickListener mOnSelfHeadListener = new  View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mChatPresenter.mChatActivity, ContactDetialActivity.class);
            Contacts contacts = new Contacts(BigwinerApplication.mApp.mAccount);
            intent.putExtra("contacts",contacts);
            mChatPresenter.mChatActivity.startActivity(intent);
        }
    };

    public View.OnClickListener reSendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mChatPresenter.doResend((Conversation) v.getTag());
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {


            view.onHeaderRefreshComplete();
        }
    };

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if(mChatPresenter.mChatActivity.impuArer.faseshow == false)
        return mBasePresenter.onFling(motionEvent, motionEvent1, v, v1);
        else
            return  false;
    }


    @Override
    public void onScrollToStart() {
        mChatPresenter.onHead();
    }

    @Override
    public void onScrollToEnd() {

    }

    public static class ChatImageProgressListener implements ProgressInterceptor.ProgressListener
    {

        public View view;

        public ChatImageProgressListener(View view)
        {
            this.view = view;
        }

        @Override
        public void onProgress(int progress) {
            RoundProgressBar roundProgressBar = view.findViewById(R.id.roundProgressBar);
            roundProgressBar.setProgress(progress);
        }
    }

    public static class ChatRequestListener implements RequestListener {

        public String url;
        public View view;
        public Conversation conversation;
        public Context context;
        public ChatRequestListener(String url,View view,Conversation conversation,Context context) {
            this.url = url;
            this.view = view;
            this.conversation = conversation;
            this.context = context;
            ProgressInterceptor.addListener(url,new ChatImageProgressListener(view));
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            ProgressInterceptor.removeListener(url);
            RoundProgressBar roundProgressBar = view.findViewById(R.id.roundProgressBar);
            roundProgressBar.setVisibility(View.INVISIBLE);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
            ProgressInterceptor.removeListener(url);
            RoundProgressBar roundProgressBar = view.findViewById(R.id.roundProgressBar);
            roundProgressBar.setVisibility(View.INVISIBLE);
            if(conversation.sourceSize == 0) {
                File file = GlideConfiguration.mGlideConfiguration.getCachedFile( BigwinerApplication.mApp.measureImg(conversation.sourceId),context);
                String path = file.getPath();
                if(file.exists())
                {
                    long size = file.length();
                    FileUtils.copyFile(file.getPath(),conversation.sourcePath);
                    Intent intent = new Intent(ChatUtils.ACTION_IM_IMG_DOWNLOAD_FINISH);
                     intent.putExtra("item",conversation);
                    intent.putExtra("size",file.length());
                    intent.setPackage(BigwinerApplication.mApp.getPackageName());
                    BigwinerApplication.mApp.sendBroadcast(intent);
                }
            }
            return false;
        }
    }

    public Action<ArrayList<AlbumFile>> arrayListAction = new Action<ArrayList<AlbumFile>>() {
        @Override
        public void onAction(@NonNull ArrayList<AlbumFile> result) {
            for(int i = 0 ; i < result.size() ; i++)
            {
                AlbumFile albumFile = result.get(i);
            }
        }
    };

    public View.OnClickListener locationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mChatPresenter.selectLocation();
        }
    };

    public View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mChatPresenter.sendCard();
        }
    };
}
