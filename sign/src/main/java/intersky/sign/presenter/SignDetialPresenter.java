package intersky.sign.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
        ImageView back = mSignDetialActivity.findViewById(R.id.back);
        back.setOnClickListener(mBackListener);

        mSignDetialActivity.mSign = mSignDetialActivity.getIntent().getParcelableExtra("sign");
        mSignDetialActivity.mRelativeLayout = (RelativeLayout) mSignDetialActivity.findViewById(R.id.shade);
        mSignDetialActivity.headImage = mSignDetialActivity.findViewById(R.id.headimg);
        mSignDetialActivity.mAddressName = mSignDetialActivity.findViewById(R.id.address_title);
        mSignDetialActivity.mAddress = (TextView) mSignDetialActivity.findViewById(R.id.address_text);
        mSignDetialActivity.mTime = (TextView) mSignDetialActivity.findViewById(R.id.time_text);
        mSignDetialActivity.mSave = (TextView) mSignDetialActivity.findViewById(R.id.save_text);
        mSignDetialActivity.reason = (EditText) mSignDetialActivity.findViewById(R.id.reason_edit_text);
        mSignDetialActivity.editText = (EditText) mSignDetialActivity.findViewById(R.id.edit_text);
        mSignDetialActivity.mImageLayer = (MyLinearLayout) mSignDetialActivity.findViewById(R.id.image_content);
        AppUtils.setContactCycleHead(mSignDetialActivity.headImage,mSignDetialActivity.mSign.mName);
        mSignDetialActivity.mSave.setOnClickListener(mSignDetialActivity.mSaveListener);
        if(mSignDetialActivity.mSign.mAddress.length() > 0)
        {
            String[] addresss = mSignDetialActivity.mSign.mAddress.split(",");
            if(addresss.length >=1)
                mSignDetialActivity.mAddress.setText(addresss[1]);
            else
                mSignDetialActivity.mAddress.setText(mSignDetialActivity.mSign.mAddress);
        }
        mSignDetialActivity.mAddressName.setText(mSignDetialActivity.mSign.mAddressName);
        mSignDetialActivity.mTime.setText(TimeUtils.getDateAndTimeC2(mSignDetialActivity));
        mSignDetialActivity.x = mSignDetialActivity.getIntent().getDoubleExtra("x", 0);
        mSignDetialActivity.y = mSignDetialActivity.getIntent().getDoubleExtra("y", 0);
        initAddTextView();
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
        mSignDetialActivity.mSign.mReason = mSignDetialActivity.reason.getText().toString();
        mSignDetialActivity.waitDialog.show();
        SignAsks.saveSign(mSignDetialHandler,mSignDetialActivity,mSignDetialActivity.mSign);
    }
    

    public void addPic() {
        Bus.callData(mSignDetialActivity,"filetools/getPhotos",false,SignManager.MAX_PIC_COUNT,"intersky.sign.view.activity.SignDetialActivity",SignDetialActivity.ACTION_SIGN_ADDPICTORE);
        if(mSignDetialActivity.popupWindow1 != null)
            mSignDetialActivity.popupWindow1.dismiss();
    }
    

    public void takePhoto() {
        if(mSignDetialActivity.mPics.size() < SignManager.MAX_PIC_COUNT)
        {
            mSignDetialActivity.permissionRepuest = (PermissionResult) Bus.callData(mSignDetialActivity,"filetools/checkPermissionTakePhoto"
                    ,mSignDetialActivity,Bus.callData(mSignDetialActivity,"filetools/getfilePath","sign/photo"));
        }
        else
        {
            AppUtils.showMessage(mSignDetialActivity,mSignDetialActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(SignManager.MAX_PIC_COUNT)+mSignDetialActivity.getString(R.string.keyword_photoaddmax2));
        }
        if(mSignDetialActivity.popupWindow1 != null)
        mSignDetialActivity.popupWindow1.dismiss();
    }



    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mSignDetialActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),mFile.getPath(),"",mFile.length(),mFile.length(),"");
                    mSignDetialActivity.mPics.add(attachment);
                    Bus.callData(mSignDetialActivity,"filetools/addPicView3",attachment,"",mSignDetialActivity.mImageLayer,mSignDetialActivity.removePicListener);
                    if (mSignDetialActivity.mPics.size() == SignManager.MAX_PIC_COUNT) {
                        View mview = mSignDetialActivity.mImageLayer.getChildAt(mSignDetialActivity.mImageLayer.getChildCount() - 1);
                        mSignDetialActivity.mImageLayer.removeView(mview);
                    }
                }
                break;
        }
    }

    

    public void removePic(View view) {
        View v = (View) view.getTag();
        Attachment attachment = (Attachment) v.getTag();
        mSignDetialActivity.mImageLayer.removeView(v);
        int size = mSignDetialActivity.mPics.size();
        mSignDetialActivity.mPics.remove(attachment);
        LayoutInflater mInflater = (LayoutInflater) mSignDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(mSignDetialActivity.mPics.size() < SignManager.MAX_PIC_COUNT && size == SignManager.MAX_PIC_COUNT)
        {
            View mView = mInflater.inflate(R.layout.fujian_item_add_up, null);
            ImageView mTextView = (ImageView) mView.findViewById(R.id.add_title);
            mTextView.setOnClickListener(mSignDetialActivity.mShowAddListener);
            mSignDetialActivity.mImageLayer.addView(mView);
        }
    }


    public void showPic(View v) {
        Attachment mAttachmentModel = (Attachment) v.getTag();
        Bus.callData(mSignDetialActivity,"filetools/showSeriasPic",mSignDetialActivity.mPics,mAttachmentModel,true,true,SignDetialActivity.ACTION_SIGN_DELETEPICTORE);
    }

    public void initPicView(ArrayList<Attachment> mPics) {
        for (int i = 0; i < mPics.size(); i++) {
            Bus.callData(mSignDetialActivity,"filetools/addPicView3",mPics.get(i),"",mSignDetialActivity.mImageLayer,mSignDetialActivity.removePicListener);
        }
        if (mSignDetialActivity.mPics.size() == SignManager.MAX_PIC_COUNT) {
            View mview = mSignDetialActivity.mImageLayer.getChildAt(mSignDetialActivity.mImageLayer.getChildCount() - 1);
            mSignDetialActivity.mImageLayer.removeView(mview);
        }
    }

    public void setpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        if(attachments.size()+mSignDetialActivity.mPics.size() > SignManager.MAX_PIC_COUNT)
        {
            AppUtils.showMessage(mSignDetialActivity,mSignDetialActivity.getString(R.string.keyword_photoaddmax1)
                    + String.valueOf(SignManager.MAX_PIC_COUNT)+mSignDetialActivity.getString(R.string.keyword_photoaddmax2));
        }
        else
        {
            mSignDetialActivity.mPics.addAll(attachments);
            initPicView(attachments);
        }

    }


    public void deletePic(Intent intent)
    {
        LayoutInflater mInflater = (LayoutInflater) mSignDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(mSignDetialActivity.mPics.size() < SignManager.MAX_PIC_COUNT)
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

    public void askFinish()
    {
        AppUtils.creatDialogTowButton(mSignDetialActivity, mSignDetialActivity.getString(R.string.save_ask),
                mSignDetialActivity.getString(R.string.save_ask_title),mSignDetialActivity.getString(R.string.button_word_cancle)
                ,mSignDetialActivity.getString(R.string.button_word_ok),null,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSignDetialActivity.finish();
                    }
                });
    }

    public void chekcBack()
    {
        if(mSignDetialActivity.reason.getText().length() > 0)
        {
            askFinish();
            return;
        }
        if(mSignDetialActivity.editText.getText().length() > 0)
        {
            askFinish();
            return;
        }
        if(mSignDetialActivity.mPics.size() > 0)
        {
            askFinish();
            return;
        }
        mSignDetialActivity.finish();
    }



    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        if (e1.getX() - e2.getX() > mSignDetialActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mSignDetialActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {
            return false;
        }
        else if (e2.getX() - e1.getX() > mSignDetialActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mSignDetialActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {

            if (e2.getX() - e1.getX() > e2.getY() - e1.getY() && e2.getX() - e1.getX() > e1.getY() - e2.getY() && mSignDetialActivity.flagFillBack == true)
            {
                mSignDetialActivity.isdestory = true;
                chekcBack();
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }

    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            chekcBack();
        }
    };
}
