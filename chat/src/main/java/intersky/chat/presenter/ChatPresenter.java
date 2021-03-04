package intersky.chat.presenter;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Downloadobject;
import intersky.appbase.PermissionCode;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.apputils.EditDialogListener;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.MyGlideUrl;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import intersky.chat.ContactManager;
import intersky.chat.R;
import intersky.chat.RecordAudioPremissionResult;
import intersky.chat.entity.ChatPager;
import intersky.chat.entity.UPlayer;
import intersky.chat.handler.ChatHandler;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.chat.handler.SendMessageHandler;
import intersky.chat.receiver.ChatReceiver;
import intersky.chat.view.activity.ChatActivity;
import intersky.filetools.FileUtils;
import intersky.filetools.thread.DownloadThread;
import intersky.mywidget.CircleImageView;
import intersky.mywidget.RoundProgressBar;
import intersky.talk.AudioLayout;
import intersky.talk.GifTextView;
import intersky.talk.ImFaceRelativeLayout;
import xpx.com.toolbar.utils.ToolBarHelper;
import xpx.map.MapUtils;

/**
 * Created by xpx on 2017/8/18.
 */

public class ChatPresenter implements Presenter {

    public ChatHandler mChatHandler;
    public ChatActivity mChatActivity;
    public View activityRootView;

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
        ImageView back = mChatActivity.findViewById(R.id.back);
        back.setOnClickListener(mChatActivity.mBackListener);
        activityRootView = mChatActivity.findViewById(R.id.activity_chat);
        mChatActivity.mContacts = mChatActivity.getIntent().getParcelableExtra("contacts");
        mChatActivity.scrollView = mChatActivity.findViewById(R.id.scollview);
        mChatActivity.scrollView.setOnScrollChangeListener(mChatActivity);
        ChatUtils.getChatUtils().showContacts =  mChatActivity.mContacts;
        mChatActivity.chatPager = ChatUtils.getChatUtils().hashChatPager.get(mChatActivity.mContacts.mRecordid);
        if (mChatActivity.chatPager == null) {
            mChatActivity.chatPager = new ChatPager(ChatUtils.getChatUtils().mChatFunctions.getMessages(mChatActivity.mContacts.mRecordid).size());
            ChatUtils.getChatUtils().hashChatPager.put(mChatActivity.mContacts.mRecordid, mChatActivity.chatPager);
        } else {
            mChatActivity.chatPager.totalcount = ChatUtils.getChatUtils().mChatFunctions.getMessages(mChatActivity.mContacts.mRecordid).size();
            if (mChatActivity.chatPager.showcount < mChatActivity.chatPager.addsize) {
                if (mChatActivity.chatPager.showcount != mChatActivity.chatPager.totalcount) {
                    mChatActivity.chatPager.showcount = mChatActivity.chatPager.totalcount;
                }
            }
        }
        mChatActivity.chatPager.showindex = 0;
        mChatActivity.shade = mChatActivity.findViewById(R.id.shade);

        TextView title = mChatActivity.findViewById(R.id.title);
        title.setText(mChatActivity.getString(R.string.chat_title_1) + mChatActivity.mContacts.getmRName() + mChatActivity.getString(R.string.chat_title_2));
        mChatActivity.chatView = (LinearLayout) mChatActivity.findViewById(R.id.msgArea);
        mChatActivity.impuArer = (ImFaceRelativeLayout) mChatActivity.findViewById(R.id.inputarea);
        mChatActivity.impuArer.setmHandler(mChatHandler);
        mChatActivity.audioLayout = mChatActivity.findViewById(R.id.audiolayout);
        mChatActivity.impuArer.setAudioLayout(mChatActivity.audioLayout);
        mChatActivity.audioLayout.mAudioRecoderUtils.setFolderPath((String) Bus.callData(mChatActivity,"filetools/getfilePath","/im/voice/"+mChatActivity.mContacts.mRecordid));
        TextView warn = mChatActivity.findViewById(R.id.warn);
        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.creatDialogTowButtonEdit(mChatActivity,"",mChatActivity.getString(R.string.button_compliant),
                        mChatActivity.getString(R.string.button_word_cancle),mChatActivity.getString(R.string.button_word_ok),null,new EditDialogListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppUtils.showMessage(mChatActivity,mChatActivity.getString(R.string.complaint_mesage_receive));
                            }
                        },mChatActivity.getString(R.string.complaint_mesage_content));
            }
        });
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
        initChatView();
        mChatActivity.impuArer.setPicListener(mChatActivity.pickListener);
        mChatActivity.impuArer.setTakephotoListener(mChatActivity.takeListener);
        mChatActivity.impuArer.setLocationListener(mChatActivity.locationListener);
        mChatActivity.impuArer.hidLocation();
        mChatActivity.impuArer.hidCard();
        mChatActivity.audioLayout.setrecorddata(recorddata);
        ChatUtils.getChatUtils().mChatFunctions.readMessages(mChatActivity.mContacts.mRecordid);
        mChatActivity.permissionRepuest = new RecordAudioPremissionResult(mChatActivity.impuArer, mChatActivity);
        AppUtils.getPermission(Manifest.permission.RECORD_AUDIO, mChatActivity, PermissionCode.PERMISSION_REQUEST_AUDIORECORD, mChatHandler);
    }

    public void onHead() {
        if (mChatActivity.chatPager.checkShowmore()) {
            int heidght = mChatActivity.chatView.getHeight();
            View showviwe = null;
            for (int i = mChatActivity.chatPager.showindex; i < mChatActivity.chatPager.showcount; i++) {
                Conversation conversation = ChatUtils.getChatUtils().mChatFunctions.getMessages(mChatActivity.mContacts.mRecordid).get(i);
                View view = getView(conversation, mChatActivity.chatPager.showindex, ChatUtils.getChatUtils().mChatFunctions.getMessages(mChatActivity.mContacts.mRecordid));
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

    public void updataViews(ArrayList<Conversation> conversations) {
        for (int i = 0; i < conversations.size(); i++) {
            Conversation conversation = ChatUtils.getChatUtils().mChatFunctions.getMessages(mChatActivity.mContacts.mRecordid, conversations.get(i).mRecordId);
            mChatActivity.chatView.addView(getView(conversation, i, ChatUtils.getChatUtils().mChatFunctions.getMessages(mChatActivity.mContacts.mRecordid)));
        }
        mChatHandler.sendEmptyMessage(ChatHandler.SCOLL_END);
    }

    public void updataView(Conversation conversation) {

        Conversation conversation1 = ChatUtils.getChatUtils().mChatFunctions.getMessages(conversation.detialId, conversation.mRecordId);
        if(conversation1 != null)
        {
            if (conversation1.image != null) {
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

                if(conversation1.sourceType == Conversation.MESSAGE_TYPE_FILE)
                {
                    TextView seze = conversation1.image.findViewById(R.id.filesize);
                    seze.setText(AppUtils.getSizeText(conversation1.sourceSize));
                }
                if(conversation1.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
                {
                    CircleImageView msgT = (CircleImageView) conversation1.image.findViewById(R.id.tv_chatcontent);
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
        ChatUtils.getChatUtils().showContacts = mChatActivity.getIntent().getParcelableExtra("contacts");
    }

    @Override
    public void Pause() {

    }


    public void onStop() {
        ChatUtils.getChatUtils().showContacts = null;
    }

    @Override
    public void Destroy() {
        ChatUtils.getChatUtils().showContacts = null;
        ChatUtils.getChatUtils().mChatFunctions.readMessages(mChatActivity.mContacts.mRecordid);
    }

    public void initChatView() {
        for (int i = mChatActivity.chatPager.showindex; i < mChatActivity.chatPager.showcount; i++) {
            Conversation conversation = ChatUtils.getChatUtils().mChatFunctions.getMessages(mChatActivity.mContacts.mRecordid).get(i);
            mChatActivity.chatView.addView(getView(conversation, i, ChatUtils.getChatUtils().mChatFunctions.getMessages(mChatActivity.mContacts.mRecordid)), 0);
            mChatActivity.chatPager.showindex++;
        }
        mChatHandler.sendEmptyMessage(ChatHandler.SCOLL_END);
    }

    public View getView(Conversation msg, int position, ArrayList<Conversation> conversations) {
        View convertView = null;
        LayoutInflater mInflater = mChatActivity.getLayoutInflater();
        if (msg.isSendto) {
            if (msg.sourceType == Conversation.MESSAGE_TYPE_NOMAL || msg.sourceType == Conversation.MESSAGE_TYPE_VOICE)
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
                convertView = mInflater.inflate(R.layout.chatting_item_img_text_right, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_FILE)
                convertView = mInflater.inflate(R.layout.chatting_item_file_text_right, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_MAP)
                convertView = mInflater.inflate(R.layout.chatting_item_map_text_right, null);

        } else {
            if (msg.sourceType == Conversation.MESSAGE_TYPE_NOMAL || msg.sourceType == Conversation.MESSAGE_TYPE_VOICE)
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE)
                convertView = mInflater.inflate(R.layout.chatting_item_img_text_left, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_FILE)
                convertView = mInflater.inflate(R.layout.chatting_item_file_text_left, null);
            else if (msg.sourceType == Conversation.MESSAGE_TYPE_MAP)
                convertView = mInflater.inflate(R.layout.chatting_item_map_text_left, null);
        }
        convertView.setTag(msg);
        msg.image = convertView;
        TextView head = convertView.findViewById(R.id.iv_userhead);

        if (msg.isSendto) {
            head.setOnClickListener(mChatActivity.mOnSelfHeadListener);
            ContactManager.setContactCycleHead(head, ChatUtils.getChatUtils().my);
        } else {
            head.setOnClickListener(mChatActivity.mOnHeadListener);
            ContactManager.setContactCycleHead(head, mChatActivity.mContacts);
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
        }
        else if (msg.sourceType == Conversation.MESSAGE_TYPE_NOMAL) {
            GifTextView msgT = (GifTextView) convertView.findViewById(R.id.tv_chatcontent);
            msgT.setSpanText(mChatHandler, Jsoup.parse(msg.mSubject).text());
            msgT.setTag(msg);
            msgT.setOnLongClickListener(mChatActivity.mShowmore);
        }
        else if (msg.sourceType == Conversation.MESSAGE_TYPE_MAP) {
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
            } else {
                if(msg.sourceSize == 0)
                {
                    ChatUtils.getChatUtils().mChatFunctions.getFilesize(mChatActivity,msg);
                }
                RequestOptions options = new RequestOptions()
                        .placeholder(intersky.chat.R.drawable.plugin_camera_no_pictures).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mChatActivity).load(new MyGlideUrl(ChatUtils.getChatUtils().mChatFunctions.getFileUrl(msg.sourceId))).apply(options)
                        .listener(new ChatActivity.ChatRequestListener(ChatUtils.getChatUtils().mChatFunctions.getFileUrl(msg.sourceId), convertView, msg,mChatActivity)).into(msgT);
            }
            map.setOnClickListener(new ImageClickListener(msg));
            map.setOnLongClickListener(new ImageClickLongListener(msg));
        } else if (msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE) {
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
            File mFile = new File((String) Bus.callData(mChatActivity, "filetools/getfilePath", "/im/file/"  + msg.detialId + "/" + msg.sourceName));
            name.setText(mFile.getName());
            TextView seze = (TextView) convertView.findViewById(intersky.chat.R.id.filesize);
            seze.setText(AppUtils.getSizeText(msg.sourceSize));
            if(msg.sourceSize == 0)
            {
                ChatUtils.getChatUtils().mChatFunctions.getFilesize(mChatActivity,msg);
            }
            RelativeLayout btn = (RelativeLayout) convertView.findViewById(intersky.chat.R.id.textcontent);
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
        if (position != 0) {
            Conversation msg1 = conversations.get(position - 1);
            if (AppUtils.chatShowTime(msg1.mTime + ":00", msg.mTime + ":00")) {
                int day = AppUtils.daysBetween(msg.mTime.substring(0, 10), TimeUtils.getDate());
                if (day == 0) {
                    time.setText(mChatActivity.getString(R.string.today) + msg.mTime.substring(10, 16));
                } else if (day == 1) {
                    time.setText(mChatActivity.getString(R.string.yestday) + msg.mTime.substring(10, 16));
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
                time.setText(mChatActivity.getString(R.string.today) + msg.mTime.substring(10, 16));
            } else if (day == 1) {
                time.setText(mChatActivity.getString(R.string.yestday)+ msg.mTime.substring(10, 16));
            } else {
                time.setText(msg.mTime.substring(0, 10));
            }
        }
        return convertView;
    }

    public void showMore(View view, Conversation msg) {
        if (mChatActivity.popup != null) {
            mChatActivity.popup.dismiss();
        }
        mChatActivity.popup = new PopupMenu(mChatActivity, view);
        MenuInflater inflater = mChatActivity.popup.getMenuInflater();
        if (msg.sourceType == Conversation.MESSAGE_TYPE_NOMAL) {
            inflater.inflate(R.menu.chat_more_text, mChatActivity.popup.getMenu());
        } else if (msg.sourceType == Conversation.MESSAGE_TYPE_FILE) {
            if (downloadfile(msg, mChatActivity.getString(R.string.file_loading)))
                inflater.inflate(R.menu.chat_more_file, mChatActivity.popup.getMenu());
        } else if (msg.sourceType == Conversation.MESSAGE_TYPE_IMAGE) {
            if (doImage(msg, false) != -1) {
                measureImage(msg);
                inflater.inflate(R.menu.chat_more_image, mChatActivity.popup.getMenu());
            } else {
                AppUtils.showMessage(mChatActivity, mChatActivity.getString(R.string.file_loading));
            }

        }
        mChatActivity.selectmsg = msg;
        mChatActivity.popup.show();
        mChatActivity.popup.setOnMenuItemClickListener(mChatActivity.mOnMenuItemClickListener);

    }

    public void showAgain(String result) {
        if (mChatActivity.popup != null) {
            Menu menu = mChatActivity.popup.getMenu();
            menu.add(mChatActivity.getString(R.string.menu_code));
            mChatActivity.popup.show();
            mChatActivity.coderesult = result;
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

        } else if (item.getTitle().equals(mChatActivity.getString(R.string.menu_code))) {
            doCode();

        }
    }

    public void doCode() {
        ChatUtils.getChatUtils().mChatFunctions.scanCode(mChatActivity, mChatActivity.coderesult);
    }


    public void doCopy() {
        ClipboardManager cm = (ClipboardManager) mChatActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        String text = mChatActivity.selectmsg.mSubject;
        ClipData myClip = ClipData.newPlainText("text", text);
        cm.setPrimaryClip(myClip);
    }

    public void doSend() {
        File mFile = new File(mChatActivity.selectmsg.sourcePath);
        if (mFile.exists() == false) {
            mFile = GlideConfiguration.getCachedFile(ChatUtils.getChatUtils().mChatFunctions.getFileUrl(mChatActivity.selectmsg.sourceId), mChatActivity);
        }
        if (mFile.exists()) {
            ChatUtils.getChatUtils().mChatFunctions.sendFile(mChatActivity, mFile);
        } else {
            ChatUtils.getChatUtils().mChatFunctions.sendText(mChatActivity, mChatActivity.selectmsg.mSubject);
        }

    }

    public void doSave() {
        File mFile = new File(mChatActivity.selectmsg.sourcePath);
        if (mFile.exists() == false) {
            mFile = GlideConfiguration.getCachedFile(ChatUtils.getChatUtils().mChatFunctions.getFileUrl(mChatActivity.selectmsg.sourceId), mChatActivity);
        }
        if (mFile.exists()) {
            File mFile2 = new File((String) Bus.callData(mChatActivity, "filetools/getfilePath", "/collect/" + mFile.getName()));
            Bus.callData(mChatActivity, "filetools/collectFile", mFile, mFile2);
        }
    }

    public void doDelete() {
        mChatActivity.waitDialog.show();
        ChatUtils.getChatUtils().mChatFunctions.delete(mChatActivity, mChatActivity.selectmsg, mChatActivity.chatView.indexOfChild(mChatActivity.selectmsg.image));
    }

    public void deleteView(Intent intent) {
        int index = intent.getIntExtra("index", -1);
        if (index != -1) {
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
            ;

            if (conversation.isplay) {
                if (conversation.isSendto) {
                    imageView.setImageResource(intersky.chat.R.drawable.volue_b_right);
                } else {
                    imageView.setImageResource(intersky.chat.R.drawable.volue_b_left);
                }
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();
            } else {
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.stop();
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
            ChatUtils.getChatUtils().mChatFunctions.sendMessage(mChatActivity, msg);
            mChatActivity.impuArer.et_sendmessage.setText("");
        } else {
            AppUtils.showMessage(mChatActivity, "消息不能为空");
        }
    }

    public void doPick() {
        ChatUtils.getChatUtils().mChatFunctions.slectPhoto(mChatActivity);
    }

    public void takePhoto() {
        ChatUtils.getChatUtils().mChatFunctions.takePhoto(mChatActivity, mChatActivity.mContacts);
    }

    public void selectLocation() {
        ChatUtils.getChatUtils().mapManager.startSelectMap(mChatActivity, ChatUtils.ACTION_CHAT_LOCATION_SELECT,
                (String) Bus.callData(mChatActivity, "filetools/getfilePath",  "/im/map/" + mChatActivity.mContacts.mRecordid )+ "/" + AppUtils.getguid() + ".png");
    }

    public void takePhotoResult(String path) {
        File mFile = new File(path);
        if (mFile.exists()) {
            sendFile(mFile.getPath(), Conversation.MESSAGE_TYPE_IMAGE);
        }
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

    public void setPicResult(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        for (int i = 0; i < attachments.size(); i++) {
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
                msg.mSubject = mChatActivity.getString(R.string.audio);
                msg.mHit = (int) mChatActivity.audioLayout.audioduriton;
                msg.sourceType = Conversation.MESSAGE_TYPE_VOICE;
            } else if (type == Conversation.MESSAGE_TYPE_IMAGE) {
                msg.mSubject = mChatActivity.getString(R.string.pic);
                msg.sourceType = Conversation.MESSAGE_TYPE_IMAGE;
            }

            if (mfile.exists()) {
                msg.sourceSize = mfile.length();
                ChatUtils.getChatUtils().mChatFunctions.sendMessage(mChatActivity, msg, mfile);
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
        msg.mSubject = mChatActivity.getString(R.string.location)+":"+name;
        msg.sourceName = jsonObject.toString();
        msg.sourceType = Conversation.MESSAGE_TYPE_MAP;
        if (mfile.exists()) {
            msg.sourceSize = mfile.length();
            ChatUtils.getChatUtils().mChatFunctions.sendMessage(mChatActivity, msg, mfile);
        } else {
            AppUtils.showMessage(mChatActivity, mChatActivity.getString(R.string.nofile));
        }
    }

    public void onItemClick(Conversation mUserMessageModel) {
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
                    AppUtils.showMessage(mChatActivity,mChatActivity.getString(R.string.file_delete));
                }
            }
            else
            {
                AppUtils.showMessage(mChatActivity,mChatActivity.getString(R.string.file_loading));
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
                AppUtils.showMessage(mChatActivity,mChatActivity.getString(R.string.file_delete));
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
        else if (mUserMessageModel.sourceType == Conversation.MESSAGE_TYPE_MAP)
        {
            openMap(mUserMessageModel);
        }
        else if (mUserMessageModel.sourceType == Conversation.MESSAGE_TYPE_CARD)
        {

        }
    }

    public boolean downloadfile(Conversation msg, String txt) {
        File mFile = new File(msg.sourcePath);
        if (mFile.exists()) {
            if (msg.sourceSize != mFile.length() && msg.sourceSize > 0) {
                Attachment attachment = new Attachment();
                attachment.mName = msg.sourceName;
                attachment.mPath = mFile.getPath();
                attachment.mUrl = ChatUtils.getChatUtils().mChatFunctions.getFileUrl(msg.sourceId);
                Downloadobject mDownloadThread = ChatUtils.getChatUtils().mDownloadThreads.get(msg.mRecordId);
                if (mDownloadThread != null) {
                    DownloadThread downloadThread = (DownloadThread) mDownloadThread.thread;
                    if (downloadThread.state != DownloadThread.STATE_START) {
                        ChatUtils.getChatUtils().mDownloadThreads.remove(msg.mRecordId);
                        mDownloadThread = new Downloadobject(new DownloadThread(attachment, ChatUtils.ACTION_IM_FILE_DOWNLOAD_FAIL, ChatUtils.ACTION_IM_FILE_DOWNLOAD_UPDATA, ChatUtils.ACTION_IM_FILE_DOWNLOAD_FINISH, msg), msg);
                        ChatUtils.getChatUtils().mDownloadThreads.put(msg.mRecordId, mDownloadThread);
                        mDownloadThread.start();
                    } else {
                        AppUtils.showMessage(mChatActivity, txt);
                    }
                } else {
                    mDownloadThread = new Downloadobject(new DownloadThread(attachment, ChatUtils.ACTION_IM_FILE_DOWNLOAD_FAIL, ChatUtils.ACTION_IM_FILE_DOWNLOAD_UPDATA, ChatUtils.ACTION_IM_FILE_DOWNLOAD_FINISH, msg), msg);
                    ChatUtils.getChatUtils().mDownloadThreads.put(msg.mRecordId, mDownloadThread);
                    mDownloadThread.start();
                }
                return true;
            }
        } else {
            Attachment attachment = new Attachment();
            attachment.mName = msg.sourceName;
            attachment.mPath = mFile.getPath();
            attachment.mUrl = ChatUtils.getChatUtils().mChatFunctions.getFileUrl(msg.sourceId);
            Downloadobject mDownloadThread = new Downloadobject(new DownloadThread(attachment, ChatUtils.ACTION_IM_FILE_DOWNLOAD_FAIL, ChatUtils.ACTION_IM_FILE_DOWNLOAD_UPDATA, ChatUtils.ACTION_IM_FILE_DOWNLOAD_FINISH, msg), msg);
            ChatUtils.getChatUtils().mDownloadThreads.put(msg.mRecordId, mDownloadThread);
            mDownloadThread.start();
            return true;
        }
        return false;
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

    public int doImage(Conversation mUserMessageModel, boolean open) {
        if(mUserMessageModel.sourcePath.length() == 0)
        {
            mUserMessageModel.sourcePath = (String) Bus.callData(mChatActivity,"filetools/getfilePath","/im/img/"
                    +mUserMessageModel.detialId)+"/"+mUserMessageModel.sourceName;
        }
        File mFile = new File(mUserMessageModel.sourcePath);
        File mFile2 = GlideConfiguration.getCachedFile(ChatUtils.getChatUtils().mChatFunctions.getGlideUrl(mUserMessageModel.sourceId), mChatActivity);
        if (mFile.exists()) {
            if (mFile.length() == mUserMessageModel.sourceSize && mFile.length() > 0) {
                if (open)
                    openFile(mFile);
                return 1;
            }
        }
        if (mFile2.exists()) {
            if (mFile2.length() == mUserMessageModel.sourceSize && mFile2.length() > 0) {
                FileUtils.copyFile(mFile2.getPath(),mFile.getPath());
                if (open)
                    openFile(mFile);
                return 2;
            }
        }
        AppUtils.showMessage(mChatActivity, "找不到该文件");
        return -1;
    }

    public void openItem(Intent intent) {
        Conversation msg = intent.getParcelableExtra("msg");
        for (int i = 0; i < ChatUtils.getChatUtils().mChatFunctions.getMessages(msg.detialId).size(); i++) {
            if (msg.mRecordId.equals(ChatUtils.getChatUtils().mChatFunctions.getMessages(msg.detialId).get(i).mRecordId)) {
                onItemClick(ChatUtils.getChatUtils().mChatFunctions.getMessages(msg.detialId).get(i));
            }
        }
    }

    public void openFile(File mFile) {
        Bus.callData(mChatActivity, "", mFile);
        Intent intent = ChatUtils.getChatUtils().mChatFunctions.openFile(mFile);
        if (intent != null) {
            mChatActivity.startActivity(intent);
        } else {
            AppUtils.showMessage(mChatActivity,  mChatActivity.getString(R.string.typeerror));
        }
    }

    public void openMap(Conversation msg) {
        ChatUtils.getChatUtils().mChatFunctions.openLocation(mChatActivity,msg);
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
            showMore(v, conversation);
            return true;
        }
    }

    public void doResend(Conversation conversation) {
        conversation.issend = 0;
        File mfile = new File(conversation.sourcePath);
        if (conversation.sourceType == Conversation.MESSAGE_TYPE_NOMAL) {
            ChatUtils.getChatUtils().mChatFunctions.sendMessage(mChatActivity, conversation);
            mChatActivity.impuArer.et_sendmessage.setText("");
        } else {
            if (mfile.exists()) {
                conversation.sourceSize = mfile.length();
                ChatUtils.getChatUtils().mChatFunctions.sendMessage(mChatActivity, conversation, mfile);
            } else {
                conversation.issend = -1;
                AppUtils.showMessage(mChatActivity,  mChatActivity.getString(R.string.nofile));
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

    public void measureImage(final Conversation conversation) {
        new Thread() {
            @Override
            public void run() {
                String result = null;
                if (doImage(conversation, false) == 1) {
                    File mFile = new File(conversation.sourcePath);
                    result = ChatUtils.getChatUtils().mChatFunctions.measureCode(BitmapCache.bitmapMeasureSize(mFile, 350, 350));

                } else if (doImage(conversation, false) == 2) {
                    File mFile = GlideConfiguration.getCachedFile(ChatUtils.getChatUtils().mChatFunctions.getFileUrl(conversation.sourceId), mChatActivity);
                    result = ChatUtils.getChatUtils().mChatFunctions.measureCode(BitmapCache.bitmapMeasureSize(mFile, 350, 350));
                } else {

                }
                if (result != null) {
                    Message message = new Message();
                    message.obj = result;
                    message.what = ChatHandler.CODE_RESULT;
                    mChatHandler.removeMessages(ChatHandler.CODE_RESULT);
                    mChatHandler.sendMessage(message);
                } else {

                }
            }
        }.start();


    }
}
