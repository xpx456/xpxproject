package intersky.notice.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.appbase.entity.Attachment;
import intersky.mywidget.WebEdit;
import intersky.notice.entity.Notice;
import intersky.notice.presenter.NoticeDetialPresenter;


@SuppressLint("ClickableViewAccessibility")
public class NoticeDetialActivity extends BaseActivity {

    public RelativeLayout mNoticePersonLayer;
    public LinearLayout mAnswerlayer;
    public LinearLayout mAttachmentlayer;
    public PopupWindow popupWindow1;
    public ArrayList<String> durls = new ArrayList<String>();
    public ArrayList<Contacts> mReaders = new ArrayList<Contacts>();
    public ArrayList<Contacts> mUnReaders = new ArrayList<Contacts>();
    public ArrayList<Reply> mReplys = new ArrayList<Reply>();
    public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
    public TextView mTitle;
    public WebEdit mContent;
    public TextView mSubject;
    public TextView mRead;
    public TextView mAnswer;
    public RelativeLayout mRelativeLayout;
    public EditText mEditTextContent;
    public String recordid = "";
    public String json = "";
    public NoticeDetialPresenter mNoticeDetialPresenter = new NoticeDetialPresenter(this);
    public Notice notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoticeDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNoticeDetialPresenter.Destroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNoticeDetialPresenter.Start();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mNoticeDetialPresenter.Pause();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mNoticeDetialPresenter.Resume();
    }


    public View.OnClickListener mShowReadListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mNoticeDetialPresenter.showRead();
        }

    };

    public View.OnClickListener mCancleListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mNoticeDetialPresenter.dodismiss();
        }

    };

    public View.OnClickListener mEditListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            mNoticeDetialPresenter.onEdit();
        }

    };

    public View.OnClickListener mDeleteListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mNoticeDetialPresenter.onDelete();
        }

    };


    public View.OnClickListener mMoreListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mNoticeDetialPresenter.showEdit();
        }

    };

    public TextView.OnEditorActionListener sendtext = new TextView.OnEditorActionListener() {


        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {

                mNoticeDetialPresenter.senderReply();
            }

            return true;
        }


    };

    public View.OnClickListener mDeleteReplyListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mNoticeDetialPresenter.showDeleteReply((Reply) v.getTag());
        }

    };

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
