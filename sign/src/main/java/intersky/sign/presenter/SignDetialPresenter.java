package intersky.sign.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.amap.api.location.AMapLocation;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Actions;
import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.apputils.Onlocation;
import intersky.apputils.TimeUtils;
import intersky.mywidget.MyLinearLayout;
import intersky.oa.OaUtils;
import intersky.sign.R;
import intersky.sign.SignManager;
import intersky.sign.asks.SignAsks;
import intersky.sign.entity.Sign;
import intersky.sign.handler.SignDetialHandler;
import intersky.sign.receive.SignDetialReceiver;
import intersky.sign.view.activity.SignDetialActivity;
import intersky.xpxnet.net.NetObject;
import xpx.com.toolbar.utils.ToolBarHelper;

public class SignDetialPresenter implements Presenter {

    public SignDetialActivity mSignDetialActivity;
    public SignDetialHandler mSignDetialHandler;

    public SignDetialPresenter(SignDetialActivity mSignDetialActivity) {
        this.mSignDetialActivity = mSignDetialActivity;
        this.mSignDetialHandler = new SignDetialHandler(mSignDetialActivity);
        mSignDetialActivity.setBaseReceiver(new SignDetialReceiver(mSignDetialHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mSignDetialActivity.setContentView(R.layout.activity_attendance_detial);
        ToolBarHelper.setTitle(mSignDetialActivity.mActionBar, mSignDetialActivity.getString(R.string.keyword_attendance));
        ToolBarHelper.setRightBtnText(mSignDetialActivity.mActionBar, mSignDetialActivity.mSaveListener, mSignDetialActivity.getString(R.string.button_word_summit));
        mSignDetialActivity.mSign = mSignDetialActivity.getIntent().getParcelableExtra("sign");
        mSignDetialActivity.mRelativeLayout = (RelativeLayout) mSignDetialActivity.findViewById(R.id.shade);
        mSignDetialActivity.mAddress = (TextView) mSignDetialActivity.findViewById(R.id.address_text);
        mSignDetialActivity.mTime = (TextView) mSignDetialActivity.findViewById(R.id.time_text);
        mSignDetialActivity.mSave = (TextView) mSignDetialActivity.findViewById(R.id.save_text);
        mSignDetialActivity.editText = (EditText) mSignDetialActivity.findViewById(R.id.edit_text);
        mSignDetialActivity.mImageLayer = (MyLinearLayout) mSignDetialActivity.findViewById(R.id.image_content);
        mSignDetialActivity.mSave.setOnClickListener(mSignDetialActivity.mSaveListener);
        mSignDetialActivity.mAddress.setText( mSignDetialActivity.mSign.mAddress);
        mSignDetialActivity.mTime.setText(mSignDetialActivity.getString(R.string.xml_attendancetime) + ":" + TimeUtils.getTime());
        mSignDetialActivity.x = mSignDetialActivity.getIntent().getDoubleExtra("x", 0);
        mSignDetialActivity.y = mSignDetialActivity.getIntent().getDoubleExtra("y", 0);
        initAddTextView();
    }

    public void dodismiss() {
        mSignDetialActivity.popupWindow1.dismiss();
    }

    public void showAdd() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem menuItem = new MenuItem();
        menuItem.btnName = mSignDetialActivity.getString(R.string.button_word_takephoto);
        menuItem.mListener = mSignDetialActivity.mTakePhotoListenter;
        items.add(menuItem);
        menuItem = new MenuItem();
        menuItem.btnName = mSignDetialActivity.getString(R.string.button_word_album);
        menuItem.mListener = mSignDetialActivity.mAddPicListener;
        items.add(menuItem);
        mSignDetialActivity.popupWindow1 = AppUtils.creatButtomMenu(mSignDetialActivity,mSignDetialActivity.mRelativeLayout,items,mSignDetialActivity.findViewById(R.id.detial));
    }


    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
    }

    private void initAddTextView() {
        LayoutInflater mInflater = (LayoutInflater) mSignDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.fujian_item_add_up, null);
        ImageView mTextView = (ImageView) mView.findViewById(R.id.add_title);
        mTextView.setOnClickListener(mSignDetialActivity.mShowAddListener);
        mSignDetialActivity.mImageLayer.addView(mView);

    }

    public void doSave() {
        if (mSignDetialActivity.issub == false) {
            mSignDetialActivity.issub = true;
            NetObject netObject = (NetObject) Bus.callData(mSignDetialActivity,"filetools/getUploadFiles",mSignDetialActivity.mPics);

            if(((ArrayList<File>) netObject.item).size() > 0)
            {
                mSignDetialActivity.waitDialog.show();
                SignManager.getInstance().oaUtils.uploadAttchments((ArrayList<File>) netObject.item,mSignDetialHandler,netObject.result);
            }
            else
            {
                doSave(netObject.result);
            }

        }
        else
        {
            mSignDetialActivity.waitDialog.show();
        }


    }

    public void doSave(String attach) {
        mSignDetialActivity.mSign.mImage = attach;
        mSignDetialActivity.mSign.mRemark = mSignDetialActivity.editText.getText().toString();
        mSignDetialActivity.waitDialog.show();
        SignAsks.saveSign(mSignDetialHandler,mSignDetialActivity,mSignDetialActivity.mSign);
    }


    public void addPic() {
        if(mSignDetialActivity.popupWindow1 != null)
        mSignDetialActivity.popupWindow1.dismiss();
        Bus.callData(mSignDetialActivity,"filetools/getPhotos",false, SignManager.MAX_PIC_COUNT,"intersky.sign.view.activity.SignDetialActivity", SignDetialActivity.ACTION_SIGN_ADDPICTORE);
    }

    public void takePhoto() {
        mSignDetialActivity.popupWindow1.dismiss();
        if(mSignDetialActivity.mPics.size() < SignManager.MAX_PIC_COUNT)
        {
            mSignDetialActivity.permissionRepuest = (PermissionResult) Bus.callData(mSignDetialActivity,"filetools/checkPermissionTakePhoto",mSignDetialActivity,Bus.callData(mSignDetialActivity,"filetools/getfilePath","sign/photo"));
        }
        else
        {
            AppUtils.showMessage(mSignDetialActivity,mSignDetialActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(SignManager.MAX_PIC_COUNT)+mSignDetialActivity.getString(R.string.keyword_photoaddmax2));
        }
    }


    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mSignDetialActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),mFile.getPath(),"",mFile.length(),mFile.length(),"");
                    mSignDetialActivity.mPics.add(attachment);
                    Bus.callData(mSignDetialActivity,"filetools/addPicView2",attachment,"",mSignDetialActivity.mImageLayer,mSignDetialActivity.removePicListener);
                    if (mSignDetialActivity.mPics.size() == SignManager.MAX_PIC_COUNT) {
                        View mview = mSignDetialActivity.mImageLayer.getChildAt(mSignDetialActivity.mImageLayer.getChildCount() - 1);
                        mSignDetialActivity.mImageLayer.removeView(mview);
                    }
                }
                break;
        }
    }

    public void removePic(View view) {
        Attachment attachment = (Attachment) view.getTag();
        mSignDetialActivity.mImageLayer.removeView(view);
        mSignDetialActivity.mPics.remove(attachment);
    }


    public void showPic(View v) {
        Attachment mAttachmentModel = (Attachment) v.getTag();
        Bus.callData(mSignDetialActivity,"filetools/showSeriasPic",mSignDetialActivity.mPics,mAttachmentModel,true,true,SignDetialActivity.ACTION_SIGN_DELETEPICTORE);
    }

    public void initPicView(ArrayList<Attachment> mPics) {
        for (int i = 0; i < mPics.size(); i++) {
            Bus.callData(mSignDetialActivity,"filetools/addPicView2",mPics.get(i),"",mSignDetialActivity.mImageLayer,mSignDetialActivity.removePicListener);
        }
        if (mSignDetialActivity.mPics.size() == SignManager.MAX_PIC_COUNT) {
            View mview = mSignDetialActivity.mImageLayer.getChildAt(mSignDetialActivity.mImageLayer.getChildCount() - 1);
            mSignDetialActivity.mImageLayer.removeView(mview);
        }
    }

    public void setpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        mSignDetialActivity.mPics.addAll(attachments);
        initPicView(attachments);
    }

    public void deletePic(Intent intent)
    {
        LayoutInflater mInflater = (LayoutInflater) mSignDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(mSignDetialActivity.mPics.size() == SignManager.MAX_PIC_COUNT)
        {
            View mView = mInflater.inflate(R.layout.fujian_item_add_up, null);
            ImageView mTextView = (ImageView) mView.findViewById(R.id.add_title);
            mTextView.setOnClickListener(mSignDetialActivity.mShowAddListener);
            mSignDetialActivity.mImageLayer.addView(mView);
        }
        View mview = mSignDetialActivity.mImageLayer.getChildAt(intent.getIntExtra("index",0));
        Attachment mAttachmentModel = (Attachment) mview.getTag();
        mSignDetialActivity.mImageLayer.removeView(mview);
        mSignDetialActivity.mPics.remove(mAttachmentModel);
    }


}
