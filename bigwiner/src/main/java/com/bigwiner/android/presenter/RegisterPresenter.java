package com.bigwiner.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.bigwiner.R;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.handler.RegisterHandler;
import com.bigwiner.android.receiver.RegisterReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ConfirmCodeActivity;
import com.bigwiner.android.view.activity.PasswordActivity;
import com.bigwiner.android.view.activity.PickActivity;
import com.bigwiner.android.view.activity.RegisterActivity;
import com.bigwiner.android.view.activity.SafeActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;
import com.umeng.commonsdk.debug.I;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.conturypick.Country;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import xpx.com.toolbar.utils.ToolBarHelper;

public class RegisterPresenter implements Presenter {

    public RegisterActivity mRegisterActivity;
    public RegisterHandler mRegisterHandler;

    public RegisterPresenter(RegisterActivity mRegisterActivity) {
        this.mRegisterActivity = mRegisterActivity;
        mRegisterHandler = new RegisterHandler(mRegisterActivity);
        mRegisterActivity.setBaseReceiver(new RegisterReceiver(mRegisterHandler));
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
//        ToolBarHelper.setSutColor(mRegisterActivity, Color.argb(0, 255, 255, 255));
        mRegisterActivity.setContentView(R.layout.activity_register);
        mRegisterActivity.mToolBarHelper.hidToolbar2(mRegisterActivity);
        ToolBarHelper.setBgColor(mRegisterActivity, mRegisterActivity.mActionBar, Color.rgb(255, 255, 255));
        mRegisterActivity.back = mRegisterActivity.findViewById(R.id.back);
        mRegisterActivity.title = mRegisterActivity.findViewById(R.id.title);
        mRegisterActivity.areaTxt = mRegisterActivity.findViewById(R.id.area_text);
        mRegisterActivity.btnArea = mRegisterActivity.findViewById(R.id.phone_title);
        mRegisterActivity.arename = mRegisterActivity.findViewById(R.id.name_title);
        mRegisterActivity.code = mRegisterActivity.findViewById(R.id.code_text);
        mRegisterActivity.passNumber = (EditText) mRegisterActivity.findViewById(R.id.pass_text);
        mRegisterActivity.passWord = (EditText) mRegisterActivity.findViewById(R.id.password_text);
        mRegisterActivity.error = mRegisterActivity.findViewById(R.id.error);
        mRegisterActivity.error2 = mRegisterActivity.findViewById(R.id.error2);
        mRegisterActivity.error3 = mRegisterActivity.findViewById(R.id.error3);
        mRegisterActivity.mailbtn = mRegisterActivity.findViewById(R.id.mail_number);
        mRegisterActivity.mail = mRegisterActivity.findViewById(R.id.mail_text);
        mRegisterActivity.mail.addTextChangedListener(mailText);
        mRegisterActivity.btnsendcode = mRegisterActivity.findViewById(R.id.register_btn);
        mRegisterActivity.btnGetCode = (TextView) mRegisterActivity.findViewById(R.id.code_send);
        mRegisterActivity.btnRegister = (TextView) mRegisterActivity.findViewById(R.id.register_btn);
        mRegisterActivity.safe = mRegisterActivity.findViewById(R.id.a6);
        mRegisterActivity.citybtn = mRegisterActivity.findViewById(R.id.city);
        mRegisterActivity.city = mRegisterActivity.findViewById(R.id.city_text);
        mRegisterActivity.areabtn = mRegisterActivity.findViewById(R.id.uarea);
        mRegisterActivity.area = mRegisterActivity.findViewById(R.id.uarea_text);
        mRegisterActivity.typebtn = mRegisterActivity.findViewById(R.id.type);
        mRegisterActivity.type = mRegisterActivity.findViewById(R.id.type_text);
        mRegisterActivity.citybtn.setOnClickListener(mRegisterActivity.citySelectListener);
        mRegisterActivity.typebtn.setOnClickListener(mRegisterActivity.typeSelectListener);
        mRegisterActivity.areabtn.setOnClickListener(mRegisterActivity.areaSelectListener);
        SpannableString content = new SpannableString(mRegisterActivity.safe.getText().toString());
        content.setSpan(new UnderlineSpan(), 1, mRegisterActivity.safe.getText().toString().length() - 1, 0);
        mRegisterActivity.safe.setText(content);
        mRegisterActivity.safe.setOnClickListener(mRegisterActivity.safeListener);
        mRegisterActivity.compatCheckBox = mRegisterActivity.findViewById(R.id.check);
        mRegisterActivity.compatCheckBox.setOnCheckedChangeListener(mRegisterActivity.onCheckedChangeListener);
//        if(mRegisterActivity.compatCheckBox.isChecked() == false)
//        {
//            mRegisterActivity.btnRegister.setEnabled(false);
//            mRegisterActivity.btnRegister.setBackgroundResource(R.drawable.shape_login_bg_btn_gray);
//        }
//        else
        {
            mRegisterActivity.btnRegister.setEnabled(true);
            mRegisterActivity.btnRegister.setBackgroundResource(R.drawable.shape_login_bg_btn);
        }
        mRegisterActivity.title.setText(mRegisterActivity.getIntent().getStringExtra("title"));
        if (mRegisterActivity.getIntent().getStringExtra("title").equals(mRegisterActivity.getString(R.string.forget_password_set))) {
            mRegisterActivity.findViewById(R.id.buttom).setVisibility(View.GONE);
            mRegisterActivity.btnRegister.setEnabled(true);
            mRegisterActivity.btnRegister.setBackgroundResource(R.drawable.shape_login_bg_btn);
            mRegisterActivity.areabtn.setVisibility(View.GONE);
            mRegisterActivity.citybtn.setVisibility(View.GONE);
            mRegisterActivity.typebtn.setVisibility(View.GONE);
            mRegisterActivity.mailbtn.setVisibility(View.GONE);
        }
        mRegisterActivity.back.setOnClickListener(mRegisterActivity.backListener);
        mRegisterActivity.btnsendcode.setOnClickListener(mRegisterActivity.sendCodeListener);
        mRegisterActivity.phoneNumber = (EditText) mRegisterActivity.findViewById(R.id.phone_text);
        mRegisterActivity.passNumber.addTextChangedListener(mRegisterActivity.onTxtChangeListener);
        mRegisterActivity.passWord.addTextChangedListener(mRegisterActivity.onTxtChangeListener2);

        mRegisterActivity.btnRegister.setText(mRegisterActivity.getIntent().getStringExtra("btn"));
        mRegisterActivity.btnGetCode.setOnClickListener(mRegisterActivity.sendCodeListener);
        mRegisterActivity.btnRegister.setOnClickListener(mRegisterActivity.nextListener);
        mRegisterActivity.btnArea.setOnClickListener(mRegisterActivity.areaListener);
        mRegisterActivity.areaTxt.setOnClickListener(mRegisterActivity.areaListener);
        BigwinerApplication.mApp.ports.reset();
        BigwinerApplication.mApp.businesstypeSelect.reset();
        BigwinerApplication.mApp.businessareaSelect.reset();
        initTime();
    }

    ;

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
//		MobclickAgent.onResume(mSplashActivity);
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
//		MobclickAgent.onPause(mSplashActivity);
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

    //操作方法

    public TextWatcher mailText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(AppUtils.checkEmail(mRegisterActivity.mail.getText().toString()))
            {
                mRegisterActivity.error3.setVisibility(View.INVISIBLE);
            }
            else
            {
                mRegisterActivity.error3.setVisibility(View.VISIBLE);
            }
        }
    };

    public void startRegister() {
//        if(mRegisterActivity.areaTxt.getText().equals("+86"))
//        {
//            if(AppUtils.isMobileNum(mRegisterActivity.phoneNumber.getText().toString()))
//            {
//                Intent intent = new Intent(mRegisterActivity, PasswordActivity.class);
//                intent.putExtra("title",mRegisterActivity.getString(R.string.btn_register));
//                intent.putExtra("phone",mRegisterActivity.phoneNumber.getText().toString());
//                intent.putExtra("code",mRegisterActivity.code.getText().toString());
//                mRegisterActivity.startActivity(intent);
//            }
//            else
//            {
//                AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.mobil_error));
//            }
//        }
//        else
//        {
//            AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.mobil_error));
//        }
        if (AppUtils.isMobileNum(mRegisterActivity.phoneNumber.getText().toString())) {
            if (mRegisterActivity.passNumber.getText().toString().length() == 0) {
                AppUtils.showMessage(mRegisterActivity, mRegisterActivity.getString(R.string.password_empty));
                return;
            }
            if(mRegisterActivity.error3.getVisibility() == View.VISIBLE)
            {
                return;
            }

            if (mRegisterActivity.getIntent().getStringExtra("title").equals(mRegisterActivity.getString(R.string.btn_regiest))) {
                if (mRegisterActivity.error.getVisibility() == View.INVISIBLE && mRegisterActivity.error2.getVisibility() == View.INVISIBLE) {
                    if (!mRegisterActivity.lastPhone.equals(mRegisterActivity.phoneNumber.getText().toString())) {
                        AppUtils.showMessage(mRegisterActivity, mRegisterActivity.getString(R.string.phone_code));
                        return;
                    }
                    if (mRegisterActivity.city.getText().toString().length() == 0) {
                        AppUtils.showMessage(mRegisterActivity, mRegisterActivity.getString(R.string.userinfo_city_hit));
                        return;
                    }
                    if (mRegisterActivity.type.getText().toString().length() == 0) {
                        AppUtils.showMessage(mRegisterActivity, mRegisterActivity.getString(R.string.userinfo_type_hit));
                        return;
                    }
                    if (mRegisterActivity.area.getText().toString().length() == 0) {
                        AppUtils.showMessage(mRegisterActivity, mRegisterActivity.getString(R.string.userinfo_area_hit));
                        return;
                    }
                    mRegisterActivity.waitDialog.show();
                    LoginAsks.doRegisterq(mRegisterActivity, mRegisterHandler, mRegisterActivity.phoneNumber.getText().toString(), mRegisterActivity.passWord.getText().toString()
                            , mRegisterActivity.code.getText().toString(), mRegisterActivity.city.getText().toString(), mRegisterActivity.type.getText().toString(),
                            mRegisterActivity.area.getText().toString(),mRegisterActivity.areaTxt.getText().toString().replace("+",""),mRegisterActivity.mail.getText().toString());
                }
            } else {
                if (mRegisterActivity.error.getVisibility() == View.INVISIBLE && mRegisterActivity.error2.getVisibility() == View.INVISIBLE) {
                    mRegisterActivity.waitDialog.show();
                    if (!mRegisterActivity.lastPhone.equals(mRegisterActivity.phoneNumber.getText().toString())) {
                        AppUtils.showMessage(mRegisterActivity, mRegisterActivity.getString(R.string.phone_code));
                        return;
                    }
                    LoginAsks.doPassword(mRegisterActivity, mRegisterHandler, mRegisterActivity.phoneNumber.getText().toString(),
                            mRegisterActivity.passWord.getText().toString(), mRegisterActivity.code.getText().toString(),mRegisterActivity.areaTxt.getText().toString().replace("+",""));
                }

            }
        } else {
            AppUtils.showMessage(mRegisterActivity, mRegisterActivity.getString(R.string.mobil_error));
        }
    }


    //网络方法
    public void doRegister() {


    }

    public void getCode() {
        if (AppUtils.isMobileNum(mRegisterActivity.phoneNumber.getText().toString())) {
            mRegisterActivity.waitDialog.show();
            mRegisterActivity.lastPhone = mRegisterActivity.phoneNumber.getText().toString();
            if (mRegisterActivity.getIntent().getStringExtra("title").equals(mRegisterActivity.getString(R.string.mobial_quick_regiest)))
                LoginAsks.getCode(mRegisterActivity, mRegisterHandler, mRegisterActivity.phoneNumber.getText().toString(), "register"
                        ,mRegisterActivity.areaTxt.getText().toString().replace("+",""));
            else
                LoginAsks.getCode(mRegisterActivity, mRegisterHandler, mRegisterActivity.phoneNumber.getText().toString(), "forgetpassword"
                        ,mRegisterActivity.areaTxt.getText().toString().replace("+",""));
        } else {
            AppUtils.showMessage(mRegisterActivity, mRegisterActivity.getString(R.string.mobil_error));
        }

    }

    public void checkUser() {


    }

    public void verCode() {


    }



    public void startTime() {
        BigwinerApplication.mApp.second = RegisterActivity.CODE_SECOND;
        mRegisterActivity.btnGetCode.setTextColor(Color.GRAY);
        mRegisterActivity.btnGetCode.setText(String.valueOf(BigwinerApplication.mApp.second) + "秒后再获取");
        mRegisterActivity.btnGetCode.setEnabled(false);
        mRegisterHandler.sendEmptyMessageDelayed(RegisterActivity.EVENT_UPDATA_CODE_SECOND, 1000);
    }

    public void initTime() {
        if (BigwinerApplication.mApp.second > 0) {
            mRegisterActivity.btnGetCode.setText(String.valueOf(BigwinerApplication.mApp.second) + "秒后再获取");
            mRegisterActivity.btnGetCode.setTextColor(Color.GRAY);
            mRegisterHandler.sendEmptyMessageDelayed(RegisterActivity.EVENT_UPDATA_CODE_SECOND, 1000);
        } else {
            mRegisterActivity.btnGetCode.setEnabled(true);
            mRegisterActivity.btnGetCode.setTextColor(Color.rgb(255, 0, 0));
            mRegisterActivity.btnGetCode.setText(mRegisterActivity.getString(R.string.btn_getcode));
        }

    }

    public void startArea() {
        Intent intent = new Intent(mRegisterActivity, PickActivity.class);
        intent.setAction(RegisterActivity.ACTION_AREA);
        mRegisterActivity.startActivity(intent);
    }

    public void updataTime() {
        BigwinerApplication.mApp.second--;
        if (BigwinerApplication.mApp.second > 0) {
            mRegisterActivity.btnGetCode.setText(String.valueOf(BigwinerApplication.mApp.second) + "秒后再获取");
            mRegisterHandler.sendEmptyMessageDelayed(RegisterActivity.EVENT_UPDATA_CODE_SECOND, 1000);
        } else {
            mRegisterActivity.btnGetCode.setEnabled(true);
            mRegisterActivity.btnGetCode.setTextColor(Color.rgb(255, 0, 0));
            mRegisterActivity.btnGetCode.setText(mRegisterActivity.getString(R.string.btn_getcode));
        }
    }

    public boolean checkPassword() {
//        Pattern pattern = Pattern.compile(mRegisterActivity.PW_PATTERN);
//        Matcher matcher = pattern.matcher(mRegisterActivity.passNumber.getText());
//        return matcher.matches();
        return true;
    }

    public void checkConfirm() {
        if (mRegisterActivity.passWord.getText().toString().equals(mRegisterActivity.passNumber.getText().toString())) {
            mRegisterActivity.error2.setVisibility(View.INVISIBLE);
        } else if (mRegisterActivity.passWord.length() > 0) {
            mRegisterActivity.error2.setVisibility(View.VISIBLE);
        } else {
            mRegisterActivity.error2.setVisibility(View.INVISIBLE);
        }
    }

    public void showSafe() {
        Intent intent = new Intent(mRegisterActivity, SafeActivity.class);
        mRegisterActivity.startActivity(intent);
    }

    public void startSelectCity() {
//        BigwinerApplication.mApp.startSelectViewCity(mRegisterActivity, mRegisterActivity.citySelect, mRegisterActivity.getString(R.string.company_edit_city_hit), UserInfoActivity.ACTION_CITY_SELECT, true, true);
        BigwinerApplication.mApp.startSelectView(mRegisterActivity, BigwinerApplication.mApp.ports, mRegisterActivity.getString(R.string.company_edit_city_hit), RegisterActivity.ACTION_L_CITY_SELECT, true, true, 1);
    }


    public void startSelectType() {
        BigwinerApplication.mApp.startSelectView(mRegisterActivity, BigwinerApplication.mApp.businesstypeSelect, mRegisterActivity.getString(R.string.userinfo_type_hit), RegisterActivity.ACTION_L_TYPE_SELECT, false, true, 3);
    }

    public void startSelectArea() {
        BigwinerApplication.mApp.startSelectView(mRegisterActivity, BigwinerApplication.mApp.businessareaSelect, mRegisterActivity.getString(R.string.userinfo_area_hit), RegisterActivity.ACTION_L_AREA_SELECT, false, true, 3);
    }

    public void setArea(Intent intent) {
        ArrayList<Select> selects = intent.getParcelableArrayListExtra("items");
        mRegisterActivity.area.setText(SelectManager.praseSelectString(selects));
        BigwinerApplication.mApp.businessareaSelect.updataSelect(mRegisterActivity.area.getText().toString());
        if (mRegisterActivity.area.getText().toString().length() == 0) {
            mRegisterActivity.area.setText(mRegisterActivity.getString(R.string.userinfo_area_hit));
        }
    }

    public void setCity(Intent intent) {
        Select select = intent.getParcelableExtra("item");
        mRegisterActivity.city.setText(select.mName);
        BigwinerApplication.mApp.ports.updataSelect(mRegisterActivity.city.getText().toString());
        if (mRegisterActivity.city.getText().toString().length() == 0) {
            mRegisterActivity.city.setText(mRegisterActivity.getString(R.string.userinfo_city_hit));
        }
    }

    public void setType(Intent intent) {
        ArrayList<Select> selects = intent.getParcelableArrayListExtra("items");
        mRegisterActivity.type.setText(SelectManager.praseSelectString(selects));

        BigwinerApplication.mApp.businesstypeSelect.updataSelect(mRegisterActivity.type.getText().toString());
        if (mRegisterActivity.type.getText().toString().length() == 0) {
            mRegisterActivity.type.setText(mRegisterActivity.getString(R.string.userinfo_type_hit));
        }
    }


    public void setArecode(Intent data) {
        mRegisterActivity.areaTxt.setText(data.getStringExtra("code"));
        mRegisterActivity.arename.setText(data.getStringExtra("name"));
    }
}
