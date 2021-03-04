package intersky.riche;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.mywidget.ColorAdapter;
import intersky.mywidget.ColorModel;
import jp.wasabeef.richeditor.RichEditor;


/**
 * Created by xpx on 2017/8/18.
 */

public class RichEditActivity extends BaseActivity {

    public RichEditPresenter mRichEditPresenter = new RichEditPresenter(this);
    public RichEditor mEditor;
    public TextView mPreview;
    public boolean isblod = false;
    public boolean isitalic = false;
    public boolean isstrikethrough = false;
    public boolean isunderline = false;
    public PopupWindow popupWindow1;
    public RelativeLayout mRelativeLayout;
    public ColorAdapter mColorAdapter;
    public ColorAdapter mColorAdapter2;
    public AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRichEditPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mRichEditPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
//            AppUtils.takePhoto(mRichEditPresenter.mRichEditActivity,popupWindow1,mRichEditPresenter.getOutputMediaFileUri());
        }

    };

    public View.OnClickListener mAddPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
//            AppUtils.addPic(mRichEditPresenter.mRichEditActivity,popupWindow1);
        }
    };

    public View.OnClickListener mCancleListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
        }

    };
    public View.OnClickListener mFinishListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mRichEditPresenter.odfinifsh();
        }

    };

    public AdapterView.OnItemClickListener clorClick = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mRichEditPresenter.doSetColor((ColorModel) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemClickListener clorClick2 = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mRichEditPresenter.doSetColor2((ColorModel) parent.getAdapter().getItem(position));
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
//            case AppUtils.REQUEST_CODE_ASK_TTAKPHOTO_PERMISSIONS:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission Granted
//                    AppUtils.doTakePhoto(mRichEditPresenter.mRichEditActivity,popupWindow1,mRichEditPresenter.getOutputMediaFileUri());
//                } else {
//                    // Permission Denied
//                    ViewUtils.showMessage(this, "无法打开摄像头，请给予相关权限");
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//        AppUtils.takePhotoResult(requestCode, resultCode, data,mRichEditPresenter.mRichEditHandler
//                ,AppUtils.EVENT_UPLOAD_PIC,AppUtils.EVENT_NONE_PIC,mRichEditPresenter.mRichEditActivity);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
