package intersky.function.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import intersky.appbase.Actions;
import intersky.appbase.BaseActivity;
import intersky.function.receiver.entity.Function;
import intersky.function.receiver.entity.TableDetial;
import intersky.function.presenter.GridDetialPresenter;
import intersky.mywidget.TableCloumArts;
import intersky.scan.ScanUtils;

/**
 * Created by xpx on 2017/8/18.
 */

public class GridDetialActivity extends BaseActivity {

    public static final String ACTION_SET_SELECT_LIST = "ACTION_SET_SELECT_LIST";
    public static final String ACTION_GRID_PHOTO_SELECT = "ACTION_GRID_PHOTO_SELECT";
    public static final String ACTION_WORKFLOW_HIT = "ACTION_WORKFLOW_HIT";
    public static final String ACTION_GRID_DETIAL_UPDAT = "ACTION_GRID_DETIAL_UPDAT";
    public RelativeLayout btnSave;
    public LinearLayout mActions;
    public HorizontalScrollView mHorizontalScrollView;
    public ScrollView imScrollView;
    public LinearLayout mClassManager;
    public PopupWindow mPopupWindow;
    public PopupWindow popupWindow2;
    public PopupWindow mPopupWindow3;
    public View mPopupWindowView2;
    public Button back;
    public Button summit;
    public Button accept;
    public Button veto;
    public Function mFunction;
    public TableDetial mTableDetial = new TableDetial();
    public TableCloumArts mTableCloumArts;
    public LinearLayout mTable;
    public ScrollView mTableArea;
    public boolean isNew = false;
    public String address = "";
    public Uri fileUri;
    public GridDetialPresenter mGridDetialPresenter = new GridDetialPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGridDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mGridDetialPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mMoreListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridDetialPresenter.showMore();
        }
    };

    public View.OnClickListener showAttachmentListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            mGridDetialPresenter.doAttachment();
        }
    };

    public View.OnClickListener newListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            mGridDetialPresenter.startCreat();
        }
    };

    public View.OnClickListener editListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            mGridDetialPresenter.startEdit();
        }
    };

    public View.OnClickListener deleteListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            mGridDetialPresenter.doDelete();
        }
    };

    public View.OnClickListener doCreatListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            mGridDetialPresenter.doCreat(isNew);
        }
    };

    public View.OnClickListener doSaveListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            mGridDetialPresenter.doEdit(isNew);
        }
    };

    public View.OnClickListener msimpleoperListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridDetialPresenter.simpleoper(v);
        }

    };

    public View.OnClickListener mOpenGaodeListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridDetialPresenter.onGaode();
        }
    };

    public View.OnClickListener mOpenGaodeDownListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridDetialPresenter.onGaodeDown();
        }
    };

    public View.OnClickListener mOpenBaiduListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridDetialPresenter.onBaidu();
        }
    };

    public View.OnClickListener mOpenBaiduDownListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridDetialPresenter.onBaiduDown();
        }
    };

    public View.OnClickListener mSelectListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridDetialPresenter.doSelectList((TextView) v);
        }
    };

    public View.OnClickListener mTextTimeListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridDetialPresenter.setTime( (TextView) v);
        }

    };

    public View.OnClickListener acceptListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGridDetialPresenter.doAccept();
        }
    };

    public View.OnClickListener vetoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGridDetialPresenter.doVote();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                mGridDetialPresenter.takePhotoResult();
                break;
            case ScanUtils.SCAN_FINISH:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    mGridDetialPresenter.setScan(bundle.getString("result"));
                }

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }


    }
}
