package com.bigwiner.android.presenter;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.amap.api.location.AMapLocation;
import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.handler.ChatHandler;
import com.bigwiner.android.receiver.ChatReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ChatActivity;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.CheckedOutputStream;

import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import intersky.appbase.Downloadobject;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.PermissionCode;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.apputils.GlideApp;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import intersky.chat.RecordAudioPremissionResult;
import intersky.chat.entity.ChatPager;
import intersky.chat.entity.UPlayer;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.NotifictionManager;
import intersky.conversation.handler.SendMessageHandler;
import intersky.filetools.FileUtils;
import intersky.filetools.thread.DownloadThread;
import intersky.mywidget.CircleImageView;
import intersky.mywidget.RoundProgressBar;
import intersky.scan.QRHelper;
import intersky.talk.AudioLayout;
import intersky.talk.GifTextView;
import intersky.talk.ImFaceRelativeLayout;
import xpx.com.toolbar.utils.ToolBarHelper;
import xpx.map.MapManager;
import xpx.map.MapUtils;

/**
 * Created by xpx on 2017/8/18.
 */

public class ChatPresenter implements Presenter {

    public ChatHandler mChatHandler;
    public ChatActivity mChatActivity;
    public View activityRootView;
    public int send = 0;
    public boolean starttest = false;

    //AppUtils.getPermission(Manifest.permission.RECORD_AUDIO,mChatActivity, ChatHandler.PERMISSION_REQUEST_RECORD_AUDIO,mChatHandler);
    public ChatPresenter(ChatActivity mChatActivity) {
        this.mChatActivity = mChatActivity;
        this.mChatHandler = new ChatHandler(mChatActivity);
        mChatActivity.setBaseReceiver(new ChatReceiver(mChatHandler));

    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mChatActivity.setContentView(R.layout.activity_chat);
        activityRootView = mChatActivity.findViewById(R.id.activity_chat);
        mChatActivity.mContacts = mChatActivity.getIntent().getParcelableExtra("contacts");
        mChatActivity.shade = mChatActivity.findViewById(R.id.shade);
        mChatActivity.mToolBarHelper.hidToolbar2(mChatActivity);
        ToolBarHelper.setBgColor(mChatActivity, mChatActivity.mActionBar, Color.rgb(255, 255, 255));
//        mChatActivity.measureStatubar(mChatActivity, (RelativeLayout) mChatActivity.findViewById(R.id.stutebar));
        TextView title = mChatActivity.findViewById(R.id.title);
        mChatActivity.backBtn = mChatActivity.findViewById(R.id.back);
        mChatActivity.scrollView = mChatActivity.findViewById(R.id.scollview);
        mChatActivity.scrollView.setOnScrollChangeListener(mChatActivity);
        mChatActivity.chatPager = BigwinerApplication.mApp.chatPagerHashMap.get(mChatActivity.mContacts.mRecordid);
        if (mChatActivity.chatPager == null) {
            if (BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid) == null) {
                BigwinerApplication.mApp.conversationManager.messageConversation.put(mChatActivity.mContacts.mRecordid, new ArrayList<Conversation>());
            }
            mChatActivity.chatPager = new ChatPager(BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid).size());
            BigwinerApplication.mApp.chatPagerHashMap.put(mChatActivity.mContacts.mRecordid, mChatActivity.chatPager);
        }
        else
        {
            mChatActivity.chatPager.totalcount = BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid).size();
            if(mChatActivity.chatPager.showcount < mChatActivity.chatPager.addsize)
            {
                if(mChatActivity.chatPager.showcount != mChatActivity.chatPager.totalcount)
                {
                    mChatActivity.chatPager.showcount = mChatActivity.chatPager.totalcount;
                }
            }
        }
        mChatActivity.chatPager.showindex = 0;
        mChatActivity.backBtn.setOnClickListener(mChatActivity.backListener);
        title.setText(mChatActivity.getString(R.string.chat_title_1) + mChatActivity.mContacts.getmRName() + mChatActivity.getString(R.string.chat_title_2));
        mChatActivity.chatView = (LinearLayout) mChatActivity.findViewById(R.id.msgArea);
        mChatActivity.impuArer = (ImFaceRelativeLayout) mChatActivity.findViewById(R.id.inputarea);
        mChatActivity.impuArer.setmHandler(mChatHandler);
        mChatActivity.audioLayout = mChatActivity.findViewById(R.id.audiolayout);
        mChatActivity.audioLayout.mAudioRecoderUtils.setFolderPath((String) Bus.callData(mChatActivity,"filetools/getfilePath","im/voice/"+mChatActivity.mContacts.mRecordid));
        mChatActivity.impuArer.setAudioLayout(mChatActivity.audioLayout);
        //initTest();

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { //高度变小100像素则认为键盘弹出
                    //这里执行需要的处理
                    int a = activityRootView.getRootView().getHeight();
                    int b = activityRootView.getHeight();
                    if (activityRootView.getHeight() < activityRootView.getRootView().getHeight() / 2) {
                        mChatActivity.impuArer.hidFace();
                    }
                }
            }
        });
        mChatActivity.impuArer.btnSend.setOnClickListener(mChatActivity.sendListener);
        if (BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid) == null) {
            BigwinerApplication.mApp.conversationManager.messageConversation.put(mChatActivity.mContacts.mRecordid, new ArrayList<Conversation>());
        }
        initChatView();
        mChatActivity.impuArer.setPicListener(mChatActivity.pickListener);
        mChatActivity.impuArer.setTakephotoListener(mChatActivity.takeListener);
        mChatActivity.impuArer.setLocationListener(mChatActivity.locationListener);
        mChatActivity.impuArer.setCardListener(mChatActivity.cardListener);
        mChatActivity.audioLayout.setrecorddata(recorddata);
        mChatActivity.permissionRepuest = new RecordAudioPremissionResult(mChatActivity.impuArer, mChatActivity);
        AppUtils.getPermission(Manifest.permission.RECORD_AUDIO, mChatActivity, PermissionCode.PERMISSION_REQUEST_AUDIORECORD, mChatHandler);
        NotifictionManager.mNotifictionManager.contactShows.put(mChatActivity.mContacts.mRecordid, mChatActivity.mContacts.getmRName());

        NotifictionManager.mNotifictionManager.contactShows.remove(mChatActivity.mContacts.mRecordid);
        Intent intent1 = new Intent();
        intent1.putExtra("id", mChatActivity.mContacts.mRecordid);
        intent1.setAction(BigWinerConversationManager.ACTION_CONVERSATION_SET_READ);
        intent1.setPackage(BigwinerApplication.mApp.getPackageName());
        mChatActivity.sendBroadcast(intent1);


    }

    public void initTest() {
        mChatActivity.textView = mChatActivity.findViewById(R.id.sendtest);
        mChatActivity.textView.setVisibility(View.VISIBLE);
        mChatActivity.editTextView = mChatActivity.findViewById(R.id.sendtime);
        mChatActivity.editTextView.setVisibility(View.VISIBLE);
        mChatActivity.textView.setOnClickListener(testSendlistener);
    }

    public View.OnClickListener testSendlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(starttest == false)
            {
                starttest = true;
                sendtest();
                mChatActivity.textView.setText("结束测试");
            }
            else
            {
                starttest = false;
                mChatActivity.textView.setText("开始测试");
                mChatHandler.removeMessages(ChatHandler.ADD_SEND_ITEM);
            }
        }
    };

    public void sendtest() {
        if(starttest == true)
        {
            Conversation msg = new Conversation();
            msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
            msg.mRecordId = AppUtils.getguid();
            msg.mTime = TimeUtils.getDate() + " " + TimeUtils.getTimeSecond();
            msg.mTitle = mChatActivity.mContacts.getmRName();
            msg.mSubject = String.valueOf(send);
            msg.mHit = 0;
            msg.detialId = mChatActivity.mContacts.mRecordid;
            msg.sourceType = Conversation.MESSAGE_TYPE_NOMAL;
            msg.isRead = true;
            msg.isSendto = true;
            BigwinerApplication.mApp.conversationManager.sendMessage(mChatActivity,msg);
            ConversationAsks.sendMsg(mChatActivity, BigWinerConversationManager.getInstance().mSendMessageHandler, msg, SendMessageHandler.SEND_MESSAGE_SUCCESS,
                    Calendar.getInstance().getTimeInMillis(),0);
            send++;
            int time = 100;
            if(mChatActivity.editTextView.getText().length() != 0)
            {
                time = Integer.valueOf(mChatActivity.editTextView.getText().toString());
            }
            mChatHandler.sendEmptyMessageDelayed(ChatHandler.ADD_SEND_ITEM,time);
        }
    }

    public void onHead() {
        if (mChatActivity.chatPager.checkShowmore()) {
            int heidght = mChatActivity.chatView.getHeight();
            View showviwe = null;
            for (int i = mChatActivity.chatPager.showindex; i < mChatActivity.chatPager.showcount; i++) {
                Conversation conversation = BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid).get(i);
                View view = getView(conversation, mChatActivity.chatPager.showindex, BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid));
                if (showviwe == null) {
                    showviwe = view;
                }
                mChatActivity.chatView.addView(view, 0);
                mChatActivity.chatPager.showindex++;
            }
            Message msg = new Message();
            msg.what = ChatHandler.SCOLL_END_FINISH;
            msg.arg1 = heidght;
            msg.obj = showviwe;
            mChatHandler.sendMessageDelayed(msg, 100);
        }
    }

    public void updataViews() {
        if (mChatActivity.chatPager.needprase) {
            for (int i = mChatActivity.chatPager.praseStart; i < mChatActivity.chatPager.praseStart + mChatActivity.chatPager.praseCount; i++) {
                Conversation conversation = BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid).get(i);
                mChatActivity.chatView.addView(getView(conversation, i, BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid)));

            }
            mChatActivity.chatPager.needprase = false;
        }
        mChatHandler.sendEmptyMessage(ChatHandler.SCOLL_END);
    }

    public void updataViews(ArrayList<Conversation> conversations) {
        for(int i = conversations.size()-1 ; i >= 0 ; i--)
        {
            Conversation conversation = BigwinerApplication.mApp.conversationManager.messageConversation2.
                    get(mChatActivity.mContacts.mRecordid).get(conversations.get(i).mRecordId);
            mChatActivity.chatView.addView(getView(conversation, i, BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid)));
        }
        mChatHandler.sendEmptyMessage(ChatHandler.SCOLL_END);
    }

    public void updataView(Conversation conversation) {

        Conversation conversation1 = BigWinerConversationManager.getInstance().messageConversation2.get(conversation.detialId).get(conversation.mRecordId);
        if(conversation1.image != null)
        {
            TextView fail = conversation1.image.findViewById(R.id.fail);
            ImageView loding = conversation1.image.findViewById(R.id.loding);
            fail.setTag(conversation1);
            fail.setOnClickListener(mChatActivity.reSendListener);
            if (conversation1.issend == Conversation.MESSAGE_STATAUE_SENDING) {
                fail.setVisibility(View.INVISIBLE);
                loding.setVisibility(View.VISIBLE);
                Animation mAnimation = AnimationUtils.loadAnimation(mChatActivity, R.anim.rotate_animation);
                loding.startAnimation(mAnimation);
            } else if (conversation1.issend == Conversation.MESSAGE_STATAUE_FAIL) {
                fail.setVisibility(View.VISIBLE);
                loding.setVisibility(View.INVISIBLE);
                loding.clearAnimation();
            } else {
                fail.setVisibility(View.INVISIBLE);
                loding.setVisibility(View.INVISIBLE);
                loding.clearAnimation();
            }

            if(conversation1.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
            {
                if(conversation1.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
                {
                    CircleImageView msgT = (CircleImageView) conversation1.image.findViewById(intersky.chat.R.id.tv_chatcontent);
                    if (conversation1.isSendto) {
                        File mFile = new File(conversation1.sourcePath);
                        BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (148*mChatActivity.mBasePresenter.mScreenDefine.density)
                                ,(int) (148*mChatActivity.mBasePresenter.mScreenDefine.density),mFile);
                        RequestOptions options = new RequestOptions().override(bitmapSize.width,bitmapSize.height)
                                .placeholder(intersky.chat.R.drawable.plugin_camera_no_pictures);
                        Glide.with(mChatActivity).load(mFile).apply(options).into(msgT);

                    } else {
                        File mFile = new File(conversation1.sourcePath);
                        if(mFile.exists())
                        {
                            BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (148*mChatActivity.mBasePresenter.mScreenDefine.density)
                                    ,(int) (148*mChatActivity.mBasePresenter.mScreenDefine.density),mFile);
                            RequestOptions options = new RequestOptions().override(bitmapSize.width,bitmapSize.height)
                                    .placeholder(intersky.chat.R.drawable.plugin_camera_no_pictures);
                            Glide.with(mChatActivity).load(mFile).apply(options).into(msgT);
                        }
                        else
                        {
                            msgT.setImageResource(intersky.chat.R.drawable.plugin_camera_no_pictures);
                        }
                    }
                }
            }
        }
    }

    public AudioLayout.recorddata recorddata = new AudioLayout.recorddata() {

        @Override
        public void updataRecordData(long time) {
        }

        @Override
        public void finish(String path) {
            sendFile(path, Conversation.MESSAGE_TYPE_VOICE);
        }
    };

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public void initChatView() {
        for (int i = mChatActivity.chatPager.showindex; i < mChatActivity.chatPager.showcount; i++) {
            Conversation conversation = BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid).get(i);
            mChatActivity.chatView.addView(getView(conversation, i,
                    BigwinerApplication.mApp.conversationManager.messageConversation.get(mChatActivity.mContacts.mRecordid)), 0);
            mChatActivity.chatPager.showindex++;
        }
        mChatHandler.sendEmptyMessage(ChatHandler.SCOLL_END);
    }

    public View getView(Conversation msg, int position, ArrayList<Conversation> conversations) {
        View convertView = null;
        LayoutInflater mInflater = mChatActivity.getLayoutInflater();
        if (msg.isSendto) {
            if (msg.sourceType == Conversation.MESSAGE_TYPE_NOMAL || msg.sourceType == Conversation.MESSAGE_TYPE_VOICE)
                convertView = mInflater.inflate(R.layout.ow_chatting_item_msg_text_right, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
            {
                convertView = mInflater.inflate(R.layout.ow_chatting_item_img_text_right, null);
            }
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_FILE)
                convertView = mInflater.inflate(R.layout.ow_chatting_item_file_text_right, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_MAP)
                convertView = mInflater.inflate(R.layout.ow_chatting_item_map_text_right, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_CARD)
                convertView = mInflater.inflate(R.layout.ow_chatting_item_card_text_right, null);

        } else {
            if (msg.sourceType == Conversation.MESSAGE_TYPE_NOMAL || msg.sourceType == Conversation.MESSAGE_TYPE_VOICE)
                convertView = mInflater.inflate(R.layout.ow_chatting_item_msg_text_left, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
            {
                convertView = mInflater.inflate(R.layout.ow_chatting_item_img_text_left, null);
            }
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_FILE)
                convertView = mInflater.inflate(R.layout.ow_chatting_item_file_text_left, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_MAP)
                convertView = mInflater.inflate(R.layout.ow_chatting_item_map_text_left, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_CARD)
                convertView = mInflater.inflate(R.layout.ow_chatting_item_card_text_left, null);
        }
        msg.image = convertView;
        convertView.setTag(msg);
        CircleImageView head = convertView.findViewById(R.id.iv_userhead);

        if (msg.isSendto) {
            head.setOnClickListener(mChatActivity.mOnSelfHeadListener);
            RequestOptions options = new RequestOptions()
                    .placeholder(com.bigwiner.R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override((int) (40 * mChatActivity.mBasePresenter.mScreenDefine.density));
            Glide.with(mChatActivity).load(ContactsAsks.getContactIconUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(head));
        } else {
            head.setOnClickListener(mChatActivity.mOnHeadListener);
            RequestOptions options = new RequestOptions()
                    .placeholder(com.bigwiner.R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override((int) (40 * mChatActivity.mBasePresenter.mScreenDefine.density));
            Glide.with(mChatActivity).load(ContactsAsks.getContactIconUrl(msg.detialId,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(head));
        }

        if (msg.sourceType == Conversation.MESSAGE_TYPE_VOICE) {

            if(msg.isSendto == false)
            {
                Message message = new Message();
                message.obj = new Conversation(msg);
                message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                    ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
            }
            TextView msgT = (TextView) convertView.findViewById(intersky.chat.R.id.tv_chatcontent);
            Button mButton = (Button) convertView.findViewById(intersky.chat.R.id.voice_btn);
            TextView time = (TextView) convertView.findViewById(intersky.chat.R.id.voice_tiem);
            ImageView imageView = (ImageView) convertView.findViewById(intersky.chat.R.id.voice_img);
            imageView.setVisibility(View.VISIBLE);
            if (msg.isSendto) {
                imageView.setImageResource(R.drawable.voice_b_right);
            } else {
                imageView.setImageResource(R.drawable.voice_b_left);
            }
            time.setVisibility(View.VISIBLE);
            mButton.setVisibility(View.VISIBLE);
            int second = 0;
            second = msg.mHit;
            int width = (int) (60 * mChatActivity.mBasePresenter.mScreenDefine.density + second * 2 * mChatActivity.mBasePresenter.mScreenDefine.density);
            mButton.setWidth(width);
            msgT.setWidth(width);
            time.setText(String.valueOf(second) + "\"");
            mButton.setTag(msg);
            mButton.setOnClickListener(mChatActivity.mOnClickListener);
            mButton.setOnLongClickListener(mChatActivity.mShowmore);
        } else if (msg.sourceType == Conversation.MESSAGE_TYPE_NOMAL) {
            GifTextView msgT = (GifTextView) convertView.findViewById(R.id.tv_chatcontent);
            msgT.setSpanText(mChatHandler, Jsoup.parse(msg.mSubject).text());
            msgT.setTag(msg);
            msgT.setOnLongClickListener(mChatActivity.mShowmore);
        }else if (msg.sourceType == Conversation.MESSAGE_TYPE_MAP) {
            if(msg.isSendto == false)
            {
                Message message = new Message();
                message.obj = new Conversation(msg);
                message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                    ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
            }
            RelativeLayout map = convertView.findViewById(R.id.map);
            ImageView msgT = (ImageView) convertView.findViewById(R.id.tv_chatcontent);
            try {
                JSONObject jsonObject = new JSONObject(msg.sourceName);
                TextView title = convertView.findViewById(R.id.maptitle);
                TextView address = convertView.findViewById(R.id.mapdetial);
                title.setText(jsonObject.getString("locationName"));
                address.setText(jsonObject.getString("locationAddress"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (msg.isSendto) {
                File mFile = new File(msg.sourcePath);
                RequestOptions options = new RequestOptions()
                        .placeholder(intersky.chat.R.drawable.plugin_camera_no_pictures);
                Glide.with(mChatActivity).load(mFile).apply(options).into(msgT);
                RoundProgressBar roundProgressBar = convertView.findViewById(R.id.roundProgressBar);
                roundProgressBar.setVisibility(View.INVISIBLE);
            } else {
                RoundProgressBar roundProgressBar = convertView.findViewById(R.id.roundProgressBar);
                roundProgressBar.setVisibility(View.VISIBLE);
                if(msg.sourceSize == 0)
                {
                    ConversationAsks.getImFileInfo(mChatActivity, BigwinerApplication.mApp.conversationManager.mSendMessageHandler,msg, SendMessageHandler.SEND_UPDATA_SIZE_SUCCESS);
                }
                RequestOptions options = new RequestOptions()
                        .placeholder(intersky.chat.R.drawable.plugin_camera_no_pictures).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mChatActivity).load(BigwinerApplication.mApp.measureImg(msg.sourceId)).apply(options)
                        .listener(new ChatActivity.ChatRequestListener(BigwinerApplication.mApp.measureImg(msg.sourceId),convertView,msg,mChatActivity)).into(msgT);
            }
            map.setOnClickListener(new ImageClickListener(msg));
            map.setOnLongClickListener(new ImageClickLongListener(msg));
        }
        else if (msg.sourceType == Conversation.MESSAGE_TYPE_CARD) {
            RelativeLayout map = convertView.findViewById(R.id.map);
            ImageView msgT = (ImageView) convertView.findViewById(R.id.tv_chatcontent);
            String userid = "";
            try {
                JSONObject jsonObject = new JSONObject(msg.sourceName);
                TextView title = convertView.findViewById(R.id.maptitle);
                TextView address = convertView.findViewById(R.id.mapdetial);
                title.setText(jsonObject.getString("cardusername"));
                address.setText(jsonObject.getString("carduserphone"));
                userid = jsonObject.getString("carduserid");
                RequestOptions options = new RequestOptions()
                        .placeholder(intersky.chat.R.drawable.plugin_camera_no_pictures).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mChatActivity).load(ContactsAsks.getContactIconUrl(userid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(msgT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.setOnClickListener(new ImageClickListener(msg));
            map.setOnLongClickListener(new ImageClickLongListener(msg));
        }
        else if (msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE) {
            if(msg.isSendto == false)
            {
                Message message = new Message();
                message.obj = new Conversation(msg);
                message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                    ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
            }
            CircleImageView msgT = (CircleImageView) convertView.findViewById(R.id.tv_chatcontent);
            if (msg.isSendto) {
                File mFile = new File(msg.sourcePath);
                BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (148*mChatActivity.mBasePresenter.mScreenDefine.density)
                        ,(int) (148*mChatActivity.mBasePresenter.mScreenDefine.density),mFile);
                RequestOptions options = new RequestOptions().override(bitmapSize.width,bitmapSize.height)
                        .placeholder(intersky.chat.R.drawable.plugin_camera_no_pictures);
                Glide.with(mChatActivity).load(mFile).apply(options).into(msgT);
                RoundProgressBar roundProgressBar = convertView.findViewById(R.id.roundProgressBar);
                roundProgressBar.setVisibility(View.INVISIBLE);

            } else {
                File mFile = new File(msg.sourcePath);
                if(mFile.exists())
                {
                    BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (148*mChatActivity.mBasePresenter.mScreenDefine.density)
                            ,(int) (148*mChatActivity.mBasePresenter.mScreenDefine.density),mFile);
                    RequestOptions options = new RequestOptions().override(bitmapSize.width,bitmapSize.height)
                            .placeholder(intersky.chat.R.drawable.plugin_camera_no_pictures);
                    Glide.with(mChatActivity).load(mFile).apply(options).into(msgT);
                    RoundProgressBar roundProgressBar = convertView.findViewById(R.id.roundProgressBar);
                    roundProgressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    msgT.setImageResource(intersky.chat.R.drawable.plugin_camera_no_pictures);
                    RoundProgressBar roundProgressBar = convertView.findViewById(R.id.roundProgressBar);
                    roundProgressBar.setVisibility(View.INVISIBLE);
//                    RequestOptions options = new RequestOptions()
//                            .placeholder(intersky.chat.R.drawable.plugin_camera_no_pictures).diskCacheStrategy(DiskCacheStrategy.ALL);
//                    Glide.with(mChatActivity).load(BigwinerApplication.mApp.measureImg(msg.sourceId)).apply(options)
//                            .listener(new ChatActivity.ChatRequestListener(BigwinerApplication.mApp.measureImg(msg.sourceId),convertView,msg,mChatActivity)).into(msgT);
                }
            }
            msgT.setOnClickListener(new ImageClickListener(msg));
            msgT.setOnLongClickListener(new ImageClickLongListener(msg));
        } else if (msg.sourceType == Conversation.MESSAGE_TYPE_FILE) {
            if(msg.isSendto == false)
            {
                Message message = new Message();
                message.obj = new Conversation(msg);
                message.what = LeaveMessageHandler.ADD_CHAT_SOURCE;
                if(ChatUtils.mChatUtils.mLeaveMessageHandler != null)
                    ChatUtils.mChatUtils.mLeaveMessageHandler.sendMessage(message);
            }
            ImageView msgT = (ImageView) convertView.findViewById(R.id.tv_chatcontent);
            Bus.callData(mChatActivity, "filetools/setfileimg", msgT, msg.sourceName);
            TextView name = (TextView) convertView.findViewById(intersky.chat.R.id.filename);
            File mFile = new File((String) Bus.callData(mChatActivity, "filetools/getfilePath", "im/" + msg.sourceName + "/" + msg.detialId + "/" + msg.sourceId));
            name.setText(mFile.getName());
            TextView seze = (TextView) convertView.findViewById(intersky.chat.R.id.filesize);
            Button btn = (Button) convertView.findViewById(intersky.chat.R.id.voice_btn);
            btn.setTag(msg);
            btn.setOnClickListener(mChatActivity.mOnClickListener);
            btn.setOnLongClickListener(mChatActivity.mShowmore);
        }
        TextView fail = convertView.findViewById(R.id.fail);
        ImageView loding = convertView.findViewById(R.id.loding);
        fail.setTag(msg);
        fail.setOnClickListener(mChatActivity.reSendListener);
        if (msg.issend == Conversation.MESSAGE_STATAUE_SENDING) {
            fail.setVisibility(View.INVISIBLE);
            loding.setVisibility(View.VISIBLE);
            Animation mAnimation = AnimationUtils.loadAnimation(mChatActivity, R.anim.rotate_animation);
            loding.startAnimation(mAnimation);
        } else if (msg.issend == Conversation.MESSAGE_STATAUE_FAIL) {
            fail.setVisibility(View.VISIBLE);
            loding.setVisibility(View.INVISIBLE);
            loding.clearAnimation();
        } else {
            fail.setVisibility(View.INVISIBLE);
            loding.setVisibility(View.INVISIBLE);
            loding.clearAnimation();
        }
        LinearLayout mLinearLayout = (LinearLayout) convertView.findViewById(intersky.chat.R.id.timelayer);
        TextView time = (TextView) convertView.findViewById(intersky.chat.R.id.tv_sendtime);
        if (position != conversations.size()-1) {
            Conversation msg1 = conversations.get(position + 1);
            if (AppUtils.chatShowTime(msg1.mTime + ":00", msg.mTime + ":00")) {
                int day = AppUtils.daysBetween(msg.mTime.substring(0, 10), TimeUtils.getDate());
                if (day == 0) {
                    time.setText("今天" + msg.mTime.substring(10, 16));
                } else if (day == 1) {
                    time.setText("昨天" + msg.mTime.substring(10, 16));
                } else {
                    time.setText(msg.mTime.substring(0, 16));
                }
            } else {
                time.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.GONE);
            }

        } else {
            int day = AppUtils.daysBetween(msg.mTime.substring(0, 10), TimeUtils.getDate());
            if (day == 0) {
                time.setText("今天" + msg.mTime.substring(10, 16));
            } else if (day == 1) {
                time.setText("昨天" + msg.mTime.substring(10, 16));
            } else {
                time.setText(msg.mTime.substring(0, 10));
            }
        }
        return convertView;
    }

    public void showMore(View view,Conversation msg) {
        if(mChatActivity.popup != null)
        {
            mChatActivity.popup.dismiss();
        }
        if ("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            Context wrapper = mChatActivity;
            wrapper = new ContextThemeWrapper(wrapper, R.style.NoPopupAnimation);
            mChatActivity.popup = new PopupMenu(wrapper, view);
        }
        else
        {
            mChatActivity.popup = new PopupMenu(mChatActivity, view);
        }

        mChatActivity.popup.getMenuInflater().inflate(R.menu.chat_more_all, mChatActivity.popup.getMenu());
        if (msg.sourceType == Conversation.MESSAGE_TYPE_NOMAL) {
            mChatActivity.popup.getMenu().findItem(R.id.code).setVisible(false);
            mChatActivity.popup.show();
        } else if (msg.sourceType == Conversation.MESSAGE_TYPE_FILE) {
            if(ChatUtils.getChatUtils().checkFinish(msg))
            {
                File file = new File(msg.sourcePath);
                if(file.exists())
                {
                    mChatActivity.popup.getMenu().findItem(R.id.code).setVisible(false);
                    mChatActivity.popup.getMenu().findItem(R.id.copy).setVisible(false);
                    mChatActivity.popup.show();
                }
                else
                {
                    AppUtils.showMessage(mChatActivity,mChatActivity.getString(R.string.file_delete));
                }
            }
            else
            {
                AppUtils.showMessage(mChatActivity,mChatActivity.getString(R.string.file_loading));
            }
        } else if (msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE) {
            if(doImage(msg,false)!= -1)
            {
                measureImage(msg);
                mChatActivity.popup.getMenu().findItem(R.id.code).setVisible(false);
                mChatActivity.popup.getMenu().findItem(R.id.copy).setVisible(false);
                mChatActivity.popup.show();
            }
            else
            {
                AppUtils.showMessage(mChatActivity,mChatActivity.getString(R.string.attachment_loding));
            }

        }
        else
        {
            mChatActivity.popup.getMenu().findItem(R.id.code).setVisible(false);
            mChatActivity.popup.getMenu().findItem(R.id.copy).setVisible(false);
            mChatActivity.popup.getMenu().findItem(R.id.send).setVisible(false);
        }
        mChatActivity.selectmsg = msg;
        mChatActivity.popup.setOnMenuItemClickListener(mChatActivity.mOnMenuItemClickListener);
        mChatActivity.popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
            }
        });
    }

    public void showAgain(String result) {
        if(mChatActivity.popup != null)
        {
            mChatActivity.popup.getMenu().findItem(R.id.code).setVisible(true);
        }
    }

    public void MenuItemClick(MenuItem item) {
        if (item.getTitle().equals(mChatActivity.getString(R.string.menu_save))) {
            doSave();
        } else if (item.getTitle().equals(mChatActivity.getString(R.string.menu_send))) {
            doSend();

        } else if (item.getTitle().equals(mChatActivity.getString(R.string.menu_copy))) {
            doCopy();

        } else if (item.getTitle().equals(mChatActivity.getString(R.string.menu_delete))) {
            doDelete();

        }
        else if (item.getTitle().equals(mChatActivity.getString(R.string.menu_code))) {
            doCode();

        }
    }

    public void doCode( ) {
        Intent intent = new Intent(mChatActivity, WebMessageActivity.class);
        intent.putExtra("url", mChatActivity.coderesult);
        intent.putExtra("showshare", false);
        mChatActivity.startActivity(intent);
    }


    public void doCopy() {
        ClipboardManager cm = (ClipboardManager) mChatActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        String text = mChatActivity.selectmsg.mSubject;
        ClipData myClip = ClipData.newPlainText("text", text);
        cm.setPrimaryClip(myClip);
    }

    public void doSend() {
        File mFile = new File(mChatActivity.selectmsg.sourcePath);
        if(mFile.exists() == false)
        {
            mFile = GlideConfiguration.getCachedFile( BigwinerApplication.mApp.measureImg(mChatActivity.selectmsg.sourceId),mChatActivity);
        }
        if (mFile.exists()) {
            BigwinerApplication.mApp.sendFileContacts(mChatActivity, mFile);
        } else {
            BigwinerApplication.mApp.sendContacts(mChatActivity, mChatActivity.selectmsg.mSubject);
        }

    }

    public void doSave() {
        File mFile = new File(mChatActivity.selectmsg.sourcePath);
        if(mFile.exists() == false)
        {
            mFile = GlideConfiguration.getCachedFile( BigwinerApplication.mApp.measureImg(mChatActivity.selectmsg.sourceId),mChatActivity);
        }
        if(mFile.exists())
        {
            File mFile2 = new File((String) Bus.callData(mChatActivity, "filetools/getfilePath", "collect" )+"/"+ mFile.getName());
            Bus.callData(mChatActivity, "filetools/collectFile", mFile, mFile2);
        }
    }

    public void doDelete() {
        mChatActivity.waitDialog.show();
        BigWinerConversationManager.getInstance().doDelete(mChatActivity,mChatActivity.selectmsg,mChatActivity.chatView.indexOfChild(mChatActivity.selectmsg.image));
    }

    public void deleteView(Intent intent) {
        int index = intent.getIntExtra("index",-1);
        if(index != -1)
        {
            mChatActivity.chatView.removeViewAt(index);
        }
    }

    public void showAiam(Conversation mUserMessageModel) {
        mUserMessageModel.isplay = true;
        updateItemView(mUserMessageModel);
    }

    public void hidAiam(Conversation mUserMessageModel) {

        mUserMessageModel.isplay = false;
        updateItemView(mUserMessageModel);
    }

    public UPlayer.onFinish onFinish = new UPlayer.onFinish() {
        @Override
        public void onFinish(Conversation mUserMessageModel) {
            if (mUserMessageModel != null) {
                mUserMessageModel.isplay = false;
                updateItemView(mUserMessageModel);
            }
        }
    };

    private void updateItemView(Conversation conversation) {
        if (conversation.image != null) {
            View view = conversation.image;
            ImageView imageView = (ImageView) view.findViewById(intersky.chat.R.id.voice_img);
            imageView.setVisibility(View.VISIBLE);
            if (conversation.isplay) {
                if (conversation.isSendto) {
                    imageView.setImageResource(intersky.chat.R.drawable.volue_b_right);
                } else {
                    imageView.setImageResource(intersky.chat.R.drawable.volue_b_left);
                }
                if(imageView.getDrawable() != null && conversation.sourceType == Conversation.MESSAGE_TYPE_VOICE)
                {
                    AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                    animationDrawable.start();
                }

            } else {
                if(imageView.getDrawable() != null && conversation.sourceType == Conversation.MESSAGE_TYPE_VOICE)
                {
                    AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                    animationDrawable.stop();
                }

                if (conversation.isSendto) {
                    imageView.setImageResource(intersky.chat.R.drawable.voice_b_right);
                } else {
                    imageView.setImageResource(intersky.chat.R.drawable.voice_b_left);
                }
            }
        }
    }

    public void startPlay(Conversation mUserMessageModel, File mFile) {
        mChatActivity.mUserMessageModel = mUserMessageModel;
        mChatActivity.mUPlayer = new UPlayer(mFile.getPath(), mUserMessageModel, mChatHandler, onFinish);
        mChatActivity.mUPlayer.start();
    }

    public void stopPlay() {
        if (mChatActivity.mUPlayer != null) {
            mChatActivity.mUPlayer.stop();
        }
    }

    public void doSendMessage() {
        String msgS = mChatActivity.impuArer.et_sendmessage.getText().toString();
        if (null != msgS && (0 != msgS.length())) {
            Conversation msg = new Conversation();
            msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
            msg.mRecordId = AppUtils.getguid();
            msg.mTime = TimeUtils.getDate() + " " + TimeUtils.getTimeSecond();
            msg.mTitle = mChatActivity.mContacts.getmRName();
            msg.mSubject = msgS;
            msg.mHit = 0;
            msg.detialId = mChatActivity.mContacts.mRecordid;
            msg.sourceType = Conversation.MESSAGE_TYPE_NOMAL;
            msg.isRead = true;
            msg.isSendto = true;
            BigwinerApplication.mApp.conversationManager.sendMessage(mChatActivity,msg);
            ConversationAsks.sendMsg(mChatActivity, BigWinerConversationManager.getInstance().mSendMessageHandler, msg,
                    SendMessageHandler.SEND_MESSAGE_SUCCESS,Calendar.getInstance().getTimeInMillis(),0);
            mChatActivity.impuArer.et_sendmessage.setText("");
        } else {
            AppUtils.showMessage(mChatActivity, "消息不能为空");
        }
    }

    public void doPick() {
        BigwinerApplication.mApp.mFileUtils.getPhotos(mChatActivity,false,9, "com.bigwiner.android.view.activity.ChatActivity",ChatUtils.ACTION_CHAT_PHOTO_SELECT);
        //Bus.callData(mChatActivity, "filetools/getPhotos", false, 1, "com.bigwiner.android.view.activity.ChatActivity", ChatUtils.ACTION_CHAT_PHOTO_SELECT);
//        BigwinerApplication.mApp.mFileUtils.selectPic(mChatActivity,true,9,mChatActivity.arrayListAction);
    }

    public void takePhoto() {
        mChatActivity.permissionRepuest = BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mChatActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("/im" + "/" + mChatActivity.mContacts.mRecordid));
    }

    public void takePhotoResult() {
        File mFile = new File(BigwinerApplication.mApp.mFileUtils.takePhotoPath);
        if (mFile.exists()) {
            sendFile(mFile.getPath(), Conversation.MESSAGE_TYPE_IMAGE);
        }
    }

    public void setPicResult(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        for(int i = 0 ; i < attachments.size() ; i++)
        {
            File mFile = new File(attachments.get(i).mPath);
            if (mFile.exists()) {
                sendFile(mFile.getPath(), Conversation.MESSAGE_TYPE_IMAGE);
            }
        }

    }

    public void sendFile(String path, int type) {
        if (mChatActivity.audioLayout.audioduriton > 0 || type != Conversation.MESSAGE_TYPE_VOICE) {
            File mfile = new File(path);
            Conversation msg = new Conversation();
            msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
            msg.mRecordId = AppUtils.getguid();
            msg.mTime = TimeUtils.getDate() + " " + TimeUtils.getTimeSecond();
            msg.mTitle = mChatActivity.mContacts.getmRName();
            msg.detialId = mChatActivity.mContacts.mRecordid;
            msg.sourceName = mfile.getName();
            msg.sourcePath = path;
            msg.isRead = true;
            msg.isSendto = true;
            if (type == Conversation.MESSAGE_TYPE_VOICE) {
                msg.mSubject = "[语音]";
                msg.mHit = (int) mChatActivity.audioLayout.audioduriton;
                msg.sourceType = Conversation.MESSAGE_TYPE_VOICE;
            } else if (type == Conversation.MESSAGE_TYPE_IMAGE) {
                msg.mSubject = "[图片]";
                msg.sourceType = Conversation.MESSAGE_TYPE_IMAGE;
            }

            if (mfile.exists()) {
                msg.sourceSize = mfile.length();
                ConversationAsks.uploadFile(mChatActivity, BigWinerConversationManager.getInstance().mSendMessageHandler, msg, SendMessageHandler.SEND_UPLOADFILE_SUCCESS);
                BigwinerApplication.mApp.conversationManager.sendMessage(mChatActivity,msg);
            } else {
                AppUtils.showMessage(mChatActivity, "找不到文件");
            }
        } else {
            File mfile = new File(path);
            if (mfile.exists()) {
                mfile.delete();
            }
        }
    }

    public void sendLocation(String path, JSONObject jsonObject) {

        File mfile = new File(path);
        String name = "";
        try {
            name = jsonObject.getString("locationName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Conversation msg = new Conversation();
        msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
        msg.mRecordId = AppUtils.getguid();
        msg.mTime = TimeUtils.getDate() + " " + TimeUtils.getTimeSecond();
        msg.mTitle = mChatActivity.mContacts.getmRName();
        msg.detialId = mChatActivity.mContacts.mRecordid;
        msg.sourcePath = path;
        msg.isRead = true;
        msg.isSendto = true;
        msg.mSubject = "位置:"+name;
        msg.sourceName = jsonObject.toString();
        msg.sourceType = Conversation.MESSAGE_TYPE_MAP;
        if (mfile.exists()) {
            msg.sourceSize = mfile.length();
            ConversationAsks.uploadFile(mChatActivity, BigWinerConversationManager.getInstance().mSendMessageHandler, msg, SendMessageHandler.SEND_UPLOADFILE_SUCCESS);
            BigwinerApplication.mApp.conversationManager.sendMessage(mChatActivity,msg);
        } else {
            AppUtils.showMessage(mChatActivity, "找不到文件");
        }
    }

    public void onItemClick(Conversation mUserMessageModel) {


         if (mUserMessageModel.sourceType == Conversation.MESSAGE_TYPE_MAP)
        {
            openMap(mUserMessageModel);
        }
        else if (mUserMessageModel.sourceType == Conversation.MESSAGE_TYPE_CARD)
        {
            openContact(mUserMessageModel);
        }
        else
         {
             File mFile = new File(mUserMessageModel.sourcePath);
             if(mUserMessageModel.isSendto == false) {
                 if(ChatUtils.getChatUtils().checkFinish(mUserMessageModel))
                 {
                     File file = new File(mUserMessageModel.sourcePath);
                     if(file.exists())
                     {
                         openItem(mUserMessageModel);
                     }
                     else
                     {
                         AppUtils.showMessage(mChatActivity,mChatActivity.getString(intersky.chat.R.string.file_delete));
                     }
                 }
                 else
                 {
                     AppUtils.showMessage(mChatActivity,mChatActivity.getString(intersky.chat.R.string.file_loading));
                 }
             }
             else {
                 File file = new File(mUserMessageModel.sourcePath);
                 if(file.exists())
                 {
                     openItem(mUserMessageModel);
                 }
                 else
                 {
                     AppUtils.showMessage(mChatActivity,mChatActivity.getString(intersky.chat.R.string.file_delete));
                 }
             }
         }


    }

    public void openItem(Conversation mUserMessageModel) {
        if (mUserMessageModel.sourceType == Conversation.MESSAGE_TYPE_VOICE)
        {
            doAudio(mUserMessageModel);
        }
        else if (mUserMessageModel.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
        {
            doImage(mUserMessageModel, true);
        }
        else if (mUserMessageModel.sourceType == Conversation.MESSAGE_TYPE_FILE)
        {
            ChatUtils.getChatUtils().mChatFunctions.openFile(mChatActivity,mUserMessageModel);
        }

    }

    public void doAudio(Conversation mUserMessageModel) {
        File mFile = new File(mUserMessageModel.sourcePath);
        if (mChatActivity.mUserMessageModel != null) {
            if (mChatActivity.mUserMessageModel == mUserMessageModel) {
                if (mChatActivity.mUPlayer != null) {
                    if (mChatActivity.mUPlayer.mPlayer == null) {
                        showAiam(mUserMessageModel);
                        startPlay(mUserMessageModel, mFile);
                    } else {
                        hidAiam(mUserMessageModel);
                        stopPlay();
                    }
                } else {
                    showAiam(mUserMessageModel);
                    startPlay(mUserMessageModel, mFile);
                }
            } else {
                if (mChatActivity.mUPlayer != null) {
                    if (mChatActivity.mUPlayer.mPlayer != null) {
                        hidAiam(mChatActivity.mUserMessageModel);
                        stopPlay();
                        showAiam(mUserMessageModel);
                        startPlay(mUserMessageModel, mFile);
                    } else {
                        showAiam(mUserMessageModel);
                        startPlay(mUserMessageModel, mFile);
                    }
                } else {
                    showAiam(mUserMessageModel);
                    startPlay(mUserMessageModel, mFile);
                }
            }
        } else {
            showAiam(mUserMessageModel);
            startPlay(mUserMessageModel, mFile);
        }
    }

    public int doImage(Conversation mUserMessageModel,boolean open) {
        File mFile = new File(mUserMessageModel.sourcePath);
        File mFile2 = GlideConfiguration.getCachedFile(BigwinerApplication.mApp.measureImg(mUserMessageModel.sourceId),mChatActivity);
        if (mFile.exists()) {
            if (mFile.length() == mUserMessageModel.sourceSize && mFile.length() > 0) {
                if(open)
                openFile(mFile);
                return 1;
            }
        }
        if (mFile2.exists()) {
            if (mFile2.length() == mUserMessageModel.sourceSize && mFile2.length() > 0) {
                if(open)
                openFile(mFile2);
                return 2;
            }
        }
        AppUtils.showMessage(mChatActivity, "");
        return -1;
    }

    public void openItem(Intent intent) {
        Conversation msg = intent.getParcelableExtra("msg");
        for (int i = 0; i < BigwinerApplication.mApp.conversationManager.messageConversation.get(msg.detialId).size(); i++) {
            if (msg.mRecordId.equals(BigwinerApplication.mApp.conversationManager.messageConversation.get(msg.detialId).get(i).mRecordId)) {
                onItemClick(BigwinerApplication.mApp.conversationManager.messageConversation.get(msg.detialId).get(i));
            }
        }
    }

    public void openFile(File mFile) {
        Bus.callData(mChatActivity, "", mFile);
        Intent intent = FileUtils.mFileUtils.openfile(mFile);
        if (intent != null) {
            mChatActivity.startActivity(intent);
        } else {
            AppUtils.showMessage(mChatActivity, "文件类型无法识别");
        }
    }

    public void openMap(Conversation msg) {

        try {
            JSONObject jsonObject = new JSONObject(msg.sourceName);
            jsonObject.put("path",msg.sourcePath);
            ChatUtils.getChatUtils().mapManager.startBigMap(mChatActivity,jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void openContact(Conversation msg) {

        try {
            JSONObject jsonObject = new JSONObject(msg.sourceName);
            Contacts contacts = new Contacts();
            contacts.mRecordid = jsonObject.getString("carduserid");
            contacts.setName(jsonObject.getString("cardusername"));
            contacts.mMobile = jsonObject.getString("carduserphone");
            Intent intent = new Intent(mChatActivity, ContactDetialActivity.class);
            intent.putExtra("contacts", contacts);
            mChatActivity.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    public class ImageClickListener implements View.OnClickListener {
        public Conversation conversation;

        public ImageClickListener(Conversation conversation) {
            this.conversation = conversation;
        }

        @Override
        public void onClick(View v) {
            onItemClick(conversation);
        }
    }

    public class ImageClickLongListener implements View.OnLongClickListener {
        public Conversation conversation;

        public ImageClickLongListener(Conversation conversation) {
            this.conversation = conversation;
        }

        @Override
        public boolean onLongClick(View v) {
            showMore(v,conversation);
            return true;
        }
    }

    public void doResend(Conversation conversation) {
        conversation.issend = 0;
        File mfile = new File(conversation.sourcePath);
        if(conversation.sourceType == Conversation.MESSAGE_TYPE_NOMAL)
        {
            ConversationAsks.sendMsg(mChatActivity, BigWinerConversationManager.getInstance().mSendMessageHandler, conversation,
                    SendMessageHandler.SEND_MESSAGE_SUCCESS,Calendar.getInstance().getTimeInMillis(),1);
//            BigwinerApplication.mApp.conversationManager.sendMessage(mChatActivity,conversation);
            mChatActivity.impuArer.et_sendmessage.setText("");
        }
        else
        {
            if (mfile.exists()) {
                conversation.sourceSize = mfile.length();
                ConversationAsks.uploadFile(mChatActivity, BigWinerConversationManager.getInstance().mSendMessageHandler, conversation, SendMessageHandler.SEND_UPLOADFILE_SUCCESS);
//                BigwinerApplication.mApp.conversationManager.sendMessage(mChatActivity,conversation);
            } else {
                conversation.issend = -1;
                AppUtils.showMessage(mChatActivity, "找不到文件");
            }
        }
        TextView fail = conversation.image.findViewById(R.id.fail);
        ImageView loding = conversation.image.findViewById(R.id.loding);
        fail.setTag(conversation);
        fail.setOnClickListener(mChatActivity.reSendListener);
        if (conversation.issend == Conversation.MESSAGE_STATAUE_SENDING) {
            fail.setVisibility(View.INVISIBLE);
            loding.setVisibility(View.VISIBLE);
            Animation mAnimation = AnimationUtils.loadAnimation(mChatActivity, R.anim.rotate_animation);
            loding.startAnimation(mAnimation);
        } else if (conversation.issend == Conversation.MESSAGE_STATAUE_FAIL) {
            fail.setVisibility(View.VISIBLE);
            loding.setVisibility(View.INVISIBLE);
            loding.clearAnimation();
        } else {
            fail.setVisibility(View.INVISIBLE);
            loding.setVisibility(View.INVISIBLE);
            loding.clearAnimation();
        }

    }

    public void measureImage(Conversation conversation) {
        new Thread(){
            @Override
            public void run() {
                String result = null;
                if(doImage(conversation,false) == 1)
                {
                    File mFile = new File(conversation.sourcePath);
                    result = QRCodeDecoder.syncDecodeQRCode(BitmapCache.bitmapMeasureSize(mFile,350,350));

                }
                else if(doImage(conversation,false) == 2)
                {
                    File mFile = GlideConfiguration.getCachedFile( BigwinerApplication.mApp.measureImg(conversation.sourceId),mChatActivity);
                    result = QRCodeDecoder.syncDecodeQRCode(BitmapCache.bitmapMeasureSize(mFile,350,350));
                }
                else
                {

                }
                if(result != null)
                {
                    Message message = new Message();
                    message.obj = result;
                    message.what = ChatHandler.CODE_RESULT;
                    mChatHandler.removeMessages(ChatHandler.CODE_RESULT);
                    mChatHandler.sendMessage(message);
                }
                else
                {

                }
            }
        }.start();


    }

    public void selectLocation() {
        ChatUtils.getChatUtils().mapManager.startSelectMap(mChatActivity, ChatUtils.ACTION_CHAT_LOCATION_SELECT,Bus.callData(mChatActivity, "filetools/getfilePath",  "/im/map/" + mChatActivity.mContacts.mRecordid )+ "/" + AppUtils.getguid() + ".png");
    }


    public void sendLocation(Intent intent) {
        File mFile = new File(intent.getStringExtra("path"));

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("longitude",intent.getDoubleExtra("longitude",0));
            jsonObject.put("latitude",intent.getDoubleExtra("latitude",0));
            jsonObject.put("locationName",intent.getStringExtra("locationname"));
            jsonObject.put("locationAddress",intent.getStringExtra("locationaddress"));
            jsonObject.put("filename",mFile.getName());
            sendLocation(mFile.getPath(),jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendCard() {
        BigwinerApplication.mApp.sendCard(mChatActivity,mChatActivity.mContacts);
    }

    public boolean checkAdd(ArrayList<Conversation> conversations)
    {
        if(conversations == null)
        {
            return false;
        }
        if(conversations.size() > 0)
        {
            Conversation conversation = conversations.get(0);
            if(!conversation.detialId.equals(mChatActivity.mContacts.mRecordid))
            {
                return false;
            }

        }

        if(mChatActivity.chatView.getChildCount() > 0)
        {
            View view = mChatActivity.chatView.getChildAt(mChatActivity.chatView.getChildCount()-1);
            Conversation conversation1 = (Conversation) view.getTag();
            if(conversation1.mRecordId.equals(conversations.get(conversations.size()-1).mRecordId))
            {
                return false;
            }
        }
        return true;
    }
}
