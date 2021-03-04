package com.dk.dkhome.presenter;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dk.dkhome.R;
import com.dk.dkhome.entity.User;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.HealthActivity;
import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.RegisterActivity;
import com.github.abel533.echarts.style.ControlStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;

import static android.content.Context.ACTIVITY_SERVICE;


public class RegisterPresenter implements Presenter {

    public RegisterActivity mRegisterActivity;
    public RelativeLayout btnHead;
    public ImageView imgHead;
    public ImageView img2;
    public TextView btnNext;
    public EditText nameValue;
    public EditText ageValue;
    public TextView btnMale;
    public TextView btnFemale;
    public PopupWindow popupWindow;
    public RelativeLayout mShade;
    public boolean ismale = true;
    public User account = new User();
    public RegisterPresenter(RegisterActivity mRegisterActivity) {
        this.mRegisterActivity = mRegisterActivity;
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mRegisterActivity.setContentView(R.layout.activity_register);
        ImageView back = mRegisterActivity.findViewById(R.id.back);
        back.setOnClickListener(mRegisterActivity.mBackListener);
        imgHead = mRegisterActivity.findViewById(R.id.addimg1);
        img2 = mRegisterActivity.findViewById(R.id.addimg2);
        btnHead = mRegisterActivity.findViewById(R.id.btn_add);
        btnNext = mRegisterActivity.findViewById(R.id.btn_next);
        nameValue = mRegisterActivity.findViewById(R.id.name_value);
        ageValue = mRegisterActivity.findViewById(R.id.age_value);
        btnMale = mRegisterActivity.findViewById(R.id.btn_male);
        btnFemale = mRegisterActivity.findViewById(R.id.btn_female);
        mShade = mRegisterActivity.findViewById(R.id.shade);
        TextView title = mRegisterActivity.findViewById(R.id.title);
        if(mRegisterActivity.getIntent().getBooleanExtra(RegisterActivity.IS_REGISTER,false) == true)
        {
            title.setText(mRegisterActivity.getString(R.string.register_title));
        }
        else
        {
            title.setText(mRegisterActivity.getString(R.string.main_left_user));
            btnNext.setText(mRegisterActivity.getString(R.string.button_word_finish));
            account.name = DkhomeApplication.mApp.mAccount.name ;
            account.age = DkhomeApplication.mApp.mAccount.age;
            account.sex = DkhomeApplication.mApp.mAccount.sex;
            account.headpath = DkhomeApplication.mApp.mAccount.headpath;
            if(DkhomeApplication.mApp.mAccount.headpath.length() > 0)
            {
                File mFile = new File(DkhomeApplication.mApp.mAccount.headpath);
                if(mFile.exists())
                Glide.with(mRegisterActivity).load(mFile).into(imgHead);
            }
        }


        ageValue.addTextChangedListener(ageTextWatcher);
        btnHead.setOnClickListener(headListener);
        btnMale.setOnClickListener(maleListener);
        btnFemale.setOnClickListener(feMaleListener);
        btnNext.setOnClickListener(nextListener);
        initData();
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

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RegisterActivity.TAKE_PHOTO_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(DkhomeApplication.mApp.mFileUtils.takePhotoPath);
                    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        if (requestCode == RegisterActivity.TAKE_PHOTO_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            DkhomeApplication.mApp.mFileUtils.cropPhoto(mRegisterActivity, 1, 1, file.getPath(), 
                                    DkhomeApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(DkhomeApplication.mApp.mFileUtils.pathUtils.getfilePath(DkhomeApplication.HOME_PATH), name).getPath()
                                    , (int) (105 * mRegisterActivity.mBasePresenter.mScreenDefine.density), (int) (105 * mRegisterActivity.mBasePresenter.mScreenDefine.density),
                                    RegisterActivity.CROP_HEAD);
                        }
                    }
                }
                break;
            case RegisterActivity.CHOSE_PICTURE_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    String path = AppUtils.getFileAbsolutePath(mRegisterActivity, data.getData());
                    File file = new File(path);
                    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        // 设置可缩放
                        if (requestCode == RegisterActivity.CHOSE_PICTURE_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            DkhomeApplication.mApp.mFileUtils.cropPhoto(mRegisterActivity, 1, 1, path,
                                    DkhomeApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(DkhomeApplication.mApp.mFileUtils.pathUtils.getfilePath(DkhomeApplication.HOME_PATH), name).getPath()
                                    , (int) (105 * mRegisterActivity.mBasePresenter.mScreenDefine.density), (int) (105 * mRegisterActivity.mBasePresenter.mScreenDefine.density),
                                    RegisterActivity.CROP_HEAD);
                        } 
                    }
                }
                break;
            case RegisterActivity.CROP_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> paths = DkhomeApplication.mApp.mFileUtils.getImage(data);
                    File mFile = new File(paths.get(0));
                    Glide.with(mRegisterActivity).load(mFile).into(imgHead);
                    account.headpath = mFile.getPath();
                }
                break;

        }
    }

    private void initData() {
        nameValue.setText(DkhomeApplication.mApp.mAccount.name);
        ageValue.setText(DkhomeApplication.mApp.mAccount.age);
        if(DkhomeApplication.mApp.mAccount.sex == 0)
        {
            setMale();
        }
        else
        {
            setFemale();
        }
    }

    private View.OnClickListener maleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setMale();
        }
    };

    private View.OnClickListener feMaleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setFemale();
        }
    };

    private View.OnClickListener headListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<MenuItem> items = new ArrayList<MenuItem>();
            MenuItem menuItem = new MenuItem();
            menuItem.btnName = mRegisterActivity.getString(R.string.button_word_takephoto);
            menuItem.mListener = mTakePhotoListenter;
            items.add(menuItem);
            menuItem = new MenuItem();
            menuItem.btnName = mRegisterActivity.getString(R.string.button_word_album);
            menuItem.mListener = mAddPicListener;
            items.add(menuItem);
            popupWindow = AppUtils.creatButtomMenu(mRegisterActivity,mShade,items,mRegisterActivity.findViewById(R.id.activity_root));
        }
    };


    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(DkhomeApplication.mApp.mAccount.name.length() == 0 && nameValue.getText().toString().length() == 0)
            {
                AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_error_name));
                return;
            }
            if(DkhomeApplication.mApp.mAccount.age.length() == 0 && ageValue.getText().toString().length() == 0)
            {
                AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_error_age));
                return;
            }
            if(nameValue.getText().toString().length() > 0)
            {
                account.name = nameValue.getText().toString();
            }
            if(ageValue.getText().toString().length() > 0)
            {
                account.age = ageValue.getText().toString();
            }
            if(ismale)
            {
                account.sex = 0;
            }
            else
            {
                account.sex = 1;
            }
            if(mRegisterActivity.getIntent().getBooleanExtra(RegisterActivity.IS_REGISTER,false) == true)
            {
                Intent intent = new Intent(mRegisterActivity, HealthActivity.class);
                intent.putExtra("user",account);
                intent.putExtra(RegisterActivity.IS_REGISTER,true);
                mRegisterActivity.startActivity(intent);
            }
            else
            {
                DkhomeApplication.mApp.mAccount.name = account.name;
                DkhomeApplication.mApp.mAccount.age = account.age;
                DkhomeApplication.mApp.mAccount.sex = account.sex;
                DkhomeApplication.mApp.mAccount.headpath = account.headpath;
                DkhomeApplication.mApp.setUser();
                Intent intent = new Intent(MainActivity.ACTION_UPDATA_DAIRY);
                mRegisterActivity.sendBroadcast(intent);
                mRegisterActivity.finish();
            }

        }
    };


    private View.OnClickListener mAddPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            DkhomeApplication.mApp.mFileUtils.selectPhotos(mRegisterActivity,RegisterActivity.CHOSE_PICTURE_HEAD);
            popupWindow.dismiss();
        }
    };

    private View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            DkhomeApplication.mApp.mFileUtils.checkPermissionTakePhoto(mRegisterActivity, DkhomeApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),RegisterActivity.TAKE_PHOTO_HEAD);
            popupWindow.dismiss();
        }

    };

    private TextWatcher ageTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(ageValue.getText().toString().startsWith("0"))
            {
                ageValue.setText("");
                AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_error_age2));
            }
        }
    };

    private void setMale()
    {
        btnMale.setTextColor(Color.parseColor("#ff5e3a"));
        btnMale.setBackgroundResource(R.drawable.shape_bg_head_select_ed);
        btnFemale.setTextColor(Color.parseColor("#e5e5e5"));
        btnFemale.setBackgroundColor(Color.parseColor("#00000000"));
        ismale = true;
    }

    private void setFemale()
    {
        btnFemale.setTextColor(Color.parseColor("#ff5e3a"));
        btnFemale.setBackgroundResource(R.drawable.shape_bg_head_select_ed);
        btnMale.setTextColor(Color.parseColor("#e5e5e5"));
        btnMale.setBackgroundColor(Color.parseColor("#00000000"));
        ismale = false;
    }



}
