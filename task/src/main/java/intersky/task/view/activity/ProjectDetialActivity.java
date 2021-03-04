package intersky.task.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.mywidget.WebEdit;
import intersky.task.asks.ProjectReplyAsks;
import intersky.task.entity.Log;
import intersky.task.entity.Project;
import intersky.task.prase.ProjectPrase;
import intersky.task.presenter.ProjectDetialPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectDetialActivity extends BaseActivity {

    public static final String ACTION_SET_PROJECT_DES = "ACTION_SET_PROJECT_DES";
    public static final String ACTION_SET_PROJECT_LEADER = "ACTION_SET_LEADER";

    public static final int TYPE_OTEHR = 4;
    public static final int TYPE_SENDER = 3;
    public static final int TYPE_ADMIN = 2;
    public static final int TYPE_LEADER = 1;
    public ProjectDetialPresenter mProjectDetialPresenter = new ProjectDetialPresenter(this);
    public TextView projectName;
    public WebEdit projectDes;
    public LinearLayout mHorizontalListView;
    public LinearLayout addAttachment;
    public RelativeLayout mRightTeb;
    public RelativeLayout mMiddeleTeb;
    public RelativeLayout mLefttTeb;
    public RelativeLayout mLine1;
    public RelativeLayout mLine2;
    public RelativeLayout mLine3;
    public RelativeLayout memBer;
    public RelativeLayout shade;
    public TextView mRightImg;
    public TextView mMiddleImg;
    public TextView mLefttImg;
    public TextView headTitle;
    public TextView pcount;
    public EditText mEditTextContent;
    public TextView headImg;
    public Project project;
    public ArrayList<Contacts> mContactModels = new ArrayList<Contacts>();
    public LinearLayout mAnswerlayer;
    public LinearLayout mAttchmentView;
    public LinearLayout mLogsArea;
    public  ArrayList<Attachment> mAttachments = new ArrayList<Attachment>();
    public  ArrayList<Log> mLogModels = new ArrayList<Log>();
    public ArrayList<Reply> mReplys= new ArrayList<Reply>();
    public ArrayList<View> mViews = new ArrayList<View>();
    public int replyPage = 1;
    public boolean isreplyall = false;
    public int logPage = 1;
    public boolean islogall = false;
    public int attachmentPage = 1;
    public boolean isattachmentall = false;
    public String hashs = "";
    public String attjson = "";
    public int current = 0;
    public PopupWindow popupWindow;
    public boolean attachall = false;
    public ProjectPrase.MemberDetial memberDetial = new ProjectPrase.MemberDetial();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mProjectDetialPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectDetialPresenter.showLeft();
        }
    };

    public View.OnClickListener middleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mProjectDetialPresenter.showMiddle();
        }
    };

    public View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectDetialPresenter.showRight();
        }
    };

    public View.OnClickListener mSetMembers = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectDetialPresenter.setMembers();
        }
    };

    public View.OnClickListener mUpdtatName = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectDetialPresenter.updtatName();
        }
    };

    public TextView.OnEditorActionListener sendtext = new TextView.OnEditorActionListener() {


        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if(mEditTextContent.getText().toString().length() > 0)
                    ProjectReplyAsks.mSendReply(mProjectDetialPresenter.mProjectDetialActivity,mProjectDetialPresenter.mProjectDetialHandler,project,mEditTextContent.getText().toString());
            }

            return true;
        }


    };

    public View.OnClickListener mSetLeader = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectDetialPresenter.updataLeader();
        }
    };


    public View.OnClickListener mDeleteReplyListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mProjectDetialPresenter.showDeleteReport((Reply) v.getTag());
        }

    };

    public View.OnClickListener mdetial = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProjectDetialPresenter.dodetial();
        }
    };

    public View.OnClickListener mShowPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Bus.callData(mProjectDetialPresenter.mProjectDetialActivity,"filetools/startAttachment",v.getTag());
        }
    };
}
