package intersky.mail.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import intersky.mail.MailManager;
import intersky.mail.entity.Mail;
import intersky.mail.presenter.MailShowPresenter;
import intersky.appbase.entity.Attachment;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailShowActivity extends BaseActivity {

    public Mail mMail;
    public ScrollView mScrollView;
    public WebView mWebView;
    public TextView mTitle;
    public TextView mBtnDetial;
    public RelativeLayout mDtialLayer;
    public RelativeLayout mSimpleDtialLayer;
    public LinearLayout mFujianLayer;
    public ImageView mFujianimg;
    public ImageView mSFujianimg;
    public RelativeLayout mShade;
    public LinearLayout buttonArea;
    public RelativeLayout buttonArea2;
    public RelativeLayout buttonArea1;
    public PopupWindow popupWindow;
    public boolean showDetial = false;
    public MailShowPresenter mMailShowPresenter = new MailShowPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMailShowPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMailShowPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mShowfujianListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailShowPresenter.showFujian();
        }

    };

    public View.OnClickListener repeatListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailShowPresenter.doRepert();
        }
    };

    public View.OnClickListener repeatAllListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailShowPresenter.doRepertAll();
        }
    };

    public View.OnClickListener resendListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailShowPresenter.doResend();
        }
    };

    public View.OnClickListener fenfaListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailShowPresenter.doFenfa();
        }
    };


    public View.OnClickListener writeListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailShowPresenter.doEdit();
        }
    };

    public View.OnClickListener deleteListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailShowPresenter.doShowDelete();
        }
    };

    public View.OnClickListener acceptListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailShowPresenter.doAccept();
        }
    };

    public View.OnClickListener vetoListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailShowPresenter.doVote();
        }
    };

    public View.OnClickListener savePicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    public View.OnClickListener sharePicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    public View.OnClickListener repeatShowListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailShowPresenter.repeatShow();
        }
    };

    public View.OnClickListener mAttachmentListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailShowPresenter.startAttachment((Attachment) v.getTag());
        }
    };

    public View.OnClickListener mDetialListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailShowPresenter.doDetial();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MailManager.getInstance().xpxShare.onActivityResult(this,requestCode,resultCode,data);

    }
}
