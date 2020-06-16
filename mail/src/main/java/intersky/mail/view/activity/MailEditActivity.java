package intersky.mail.view.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.mail.entity.Mail;
import intersky.mail.entity.MailContact;
import intersky.mail.presenter.MailEditPresenter;
import intersky.mail.view.adapter.AttachmentAdapter;
import intersky.mail.view.adapter.MailSelectAdapter;
import intersky.appbase.Actions;
import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Attachment;
import intersky.mywidget.HorizontalListView;
import intersky.mywidget.MyLinearLayout;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailEditActivity extends BaseActivity {
    public static final String ACTION_MAIL_PHOTO_SELECT = "ACTION_MAIL_PHOTO_SELECT";
    public static final String ACTION_MAIL_VIDEO_SELECT = "ACTION_MAIL_VIDEO_SELECT";
    public static final String ACTION_ADD_MAIL_CONTACT = "ACTION_ADD_MAIL_CONTACT";
    public static final int CONTENT_TYPE_SHOUJIAN = 1;
    public static final int CONTENT_TYPE_CC = 2;
    public static final int CONTENT_TYPE_BCC = 3;
    public static final int CONTENT_TYPE_LCC = 4;
    public static final int CONTENT_TYPE_CONTENT= 5;
    public static final int CONTENT_TYPE_SUBJECT= 6;
    public static final int EVENT_SHOW_BUTTOM_LAYER = 201;
    public RelativeLayout mshada;
    public WebView mWebView;
    public RelativeLayout mShoujianLayer;
    public MyLinearLayout mShoujianContentLayer;
    public RelativeLayout mLccLayer;
    public MyLinearLayout mLccLayerContentLayer;
    public RelativeLayout mFajianLayer;
    public RelativeLayout mCcLayer;
    public MyLinearLayout mCcLayerContentLayer;
    public RelativeLayout mBccLayer;
    public MyLinearLayout mBccLayerContentLayer;
    public View mShoujianEditLayer;
    public View mLccEditLayer;
    public View mCcEditLayer;
    public View mBccEditLayer;
    public EditText mShoujian;
    public EditText mLcc;
    public TextView mFajian;
    public TextView mFajianTitle;
    public EditText mCc;
    public EditText mBcc;
    public EditText mZhuti;
    public EditText mContent;
    public ImageView mFujian;
    public ImageView mShoujianAdd;
    public ImageView mCcAdd;
    public ImageView mBccAdd;
    public ImageView mLccAdd;
    public RelativeLayout mfujianLayer;
    public RelativeLayout mButtomLayer;
    public RelativeLayout takePhoto;
    public RelativeLayout picture;
    public RelativeLayout video;
    public HorizontalListView mFujianList;
    public AttachmentAdapter mAttachmentAdapter;
    public MailSelectAdapter mMailSelectAdapter;
    public MailContact lastSelectTo;
    public MailContact lastSelectLcc;
    public MailContact lastselectCC;
    public MailContact lastselectBCC;
    public boolean isFujian = false;
    public boolean onShoujian = false;
    public boolean onCc = false;
    public boolean onBcc = false;
    public boolean onZhuti = false;
    public boolean onContent = false;
    public boolean onLcc = false;
    public boolean unshowall = false;
    public Dialog selectDialog;
    public Uri fileUri;
    public Mail mMail;
    public String mRecordID = "";
    public ArrayList<MailContact> mToPesrons = new ArrayList<MailContact>();
    public ArrayList<MailContact> mLccPesrons = new ArrayList<MailContact>();
    public ArrayList<MailContact> mCcPesrons = new ArrayList<MailContact>();
    public ArrayList<MailContact> mBccPesrons = new ArrayList<MailContact>();
    public int mAction;
    public PopupWindow popupWindow1;
    public int finishcount = 0;
    public int count = 0;
    public MailEditPresenter mMailEditPresenter = new MailEditPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMailEditPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMailEditPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mSendListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.doSend();
        }
    };

    public View.OnClickListener mFujianListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.Fujianclick();
        }
    };

    public AdapterView.OnItemClickListener mFujianItemClick = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mMailEditPresenter.showDeletedialog(mMail.attachments.get(position));
        }

    };

    public AdapterView.OnItemClickListener mUserMailItemClick = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.selectMail(position);
        }

    };

    public View.OnKeyListener mOnKeyListener1 = new View.OnKeyListener()
    {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN)
            {
                mMailEditPresenter.onKeylistener(CONTENT_TYPE_SHOUJIAN);

            }

            return false;
        }

    };

    public View.OnKeyListener mOnKeyListener12 = new View.OnKeyListener()
    {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN)
            {

                mMailEditPresenter.onKeylistener(CONTENT_TYPE_LCC);
            }

            return false;
        }

    };

    public View.OnKeyListener mOnKeyListener2 = new View.OnKeyListener()
    {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN)
            {
                mMailEditPresenter.onKeylistener(CONTENT_TYPE_CC);

            }

            return false;
        }

    };

    public View.OnKeyListener mOnKeyListener3 = new View.OnKeyListener()
    {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN)
            {
                mMailEditPresenter.onKeylistener(CONTENT_TYPE_BCC);


            }

            return false;
        }

    };

    public View.OnClickListener mFajianlistener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub

            mMailEditPresenter.onfajianlistener();
        }
    };

    public View.OnClickListener mAddClickListener1 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.startAddContacts(CONTENT_TYPE_SHOUJIAN);

        }
    };

    public View.OnClickListener mAddClickListener2 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.startAddContacts(CONTENT_TYPE_CC);

        }
    };

    public View.OnClickListener mAddClickListener3 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.startAddContacts(CONTENT_TYPE_BCC);

        }
    };

    public View.OnClickListener mAddClickListener4 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.startAddContacts(CONTENT_TYPE_LCC);
        }
    };



    public View.OnClickListener mPersonTextClickListener1 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onPersonTextListener(MailEditActivity.CONTENT_TYPE_SHOUJIAN,(MailContact) v.getTag());
        }
    };

    public View.OnClickListener mPersonTextClickListener2 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onPersonTextListener(MailEditActivity.CONTENT_TYPE_CC,(MailContact) v.getTag());

        }
    };

    public View.OnClickListener mPersonTextClickListener3 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onPersonTextListener(MailEditActivity.CONTENT_TYPE_BCC,(MailContact) v.getTag());

        }
    };

    public View.OnClickListener mPersonTextClickListener4 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onPersonTextListener(MailEditActivity.CONTENT_TYPE_LCC,(MailContact) v.getTag());
        }
    };

    public TextView.OnEditorActionListener mOnEditorActionListener1 = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_NEXT)
            {
                mMailEditPresenter.onNext(CONTENT_TYPE_SHOUJIAN);
            }

            return true;
        }
    };

    public TextView.OnEditorActionListener mOnEditorActionListener12 = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_NEXT)
            {
                mMailEditPresenter.onNext(CONTENT_TYPE_LCC);

            }

            return true;
        }
    };

    public TextView.OnEditorActionListener mOnEditorActionListener2 = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_NEXT)
            {
                mMailEditPresenter.onNext(CONTENT_TYPE_CC);

            }
            return true;
        }
    };

    public TextView.OnEditorActionListener mOnEditorActionListener3 = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_NEXT)
            {
                mMailEditPresenter.onNext(CONTENT_TYPE_BCC);

            }
            return true;
        }
    };

    public TextView.OnEditorActionListener mOnEditorActionListener4 = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_NEXT)
            {
                mMailEditPresenter.onNext(CONTENT_TYPE_CONTENT);
            }
            return true;
        }

    };

    public View.OnFocusChangeListener mOnFocusChangeListener1 = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.OnFocusChangeListener(CONTENT_TYPE_SHOUJIAN,hasFocus);
        }
    };

    public View.OnFocusChangeListener mOnFocusChangeListener12 = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.OnFocusChangeListener(CONTENT_TYPE_LCC,hasFocus);

        }
    };

    public View.OnFocusChangeListener mOnFocusChangeListener2 = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.OnFocusChangeListener(CONTENT_TYPE_CC,hasFocus);

        }
    };

    public View.OnFocusChangeListener mOnFocusChangeListener5 = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.OnFocusChangeListener(CONTENT_TYPE_BCC,hasFocus);

        }
    };

    public View.OnFocusChangeListener mOnFocusChangeListener3 = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.OnFocusChangeListener(CONTENT_TYPE_SUBJECT,hasFocus);

        }
    };

    public View.OnFocusChangeListener mOnFocusChangeListener4 = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.OnFocusChangeListener(CONTENT_TYPE_CONTENT,hasFocus);
        }
    };

    public View.OnClickListener onEditclicklisten1 = new View.OnClickListener() {

        @SuppressLint("NewApi")
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onEditclicklisten(CONTENT_TYPE_SHOUJIAN);
        }
    };

    public View.OnClickListener onEditclicklisten12 = new View.OnClickListener()
    {

        @SuppressLint("NewApi")
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onEditclicklisten(CONTENT_TYPE_LCC);

        }
    };

    public View.OnClickListener onEditclicklisten2 = new View.OnClickListener()
    {

        @SuppressLint("NewApi")
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onEditclicklisten(CONTENT_TYPE_CC);


        }
    };

    public View.OnClickListener onEditclicklisten4 = new View.OnClickListener()
    {

        @SuppressLint("NewApi")
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onEditclicklisten(CONTENT_TYPE_BCC);
        }
    };

    public View.OnClickListener onEditclicklisten3 = new View.OnClickListener()
    {

        @SuppressLint("NewApi")
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onEditclicklisten(CONTENT_TYPE_SUBJECT);
        }
    };

    public View.OnClickListener montouchlistener = new View.OnClickListener()
    {

        @SuppressLint("NewApi")
        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.onEditclicklisten(CONTENT_TYPE_CONTENT);
        }
    };

    public View.OnClickListener mVideoListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.getVideo();
        }
    };

    public View.OnClickListener mtekePhotoListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.takePhoto();
        }
    };

    public View.OnClickListener mPictureListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailEditPresenter.getPicture();
        }
    };

    public View.OnClickListener mattachshowListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailEditPresenter.showpic((Attachment) v.getTag());
        }

    };

    public View.OnClickListener mattachdeleteListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailEditPresenter.deletepic((Attachment) v.getTag());
        }

    };

    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mMailEditPresenter.doBack();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                mMailEditPresenter.takePhotoResult();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }


    }
}
