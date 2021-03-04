package com.restaurant.presenter;


import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;


import com.restaurant.R;
import com.restaurant.asks.DeviceAsks;
import com.restaurant.entity.Device;
import com.restaurant.entity.UserDefine;
import com.restaurant.handler.RegisterHandler;
import com.restaurant.receiver.RegisterReceiver;
import com.restaurant.view.LocationView;
import com.restaurant.view.RestaurantApplication;
import com.restaurant.view.activity.MainActivity;
import com.restaurant.view.activity.RegisterActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;

public class RegisterPresenter implements Presenter {

    public RegisterActivity mRegisterActivity;
    public LocationView locationView;
    public RegisterHandler registerHandler;
    public PopupWindow popupWindow;
    public RegisterPresenter(RegisterActivity RegisterActivity) {
        mRegisterActivity = RegisterActivity;
        registerHandler = new RegisterHandler(mRegisterActivity);
        mRegisterActivity.setBaseReceiver(new RegisterReceiver(registerHandler));
    }

    @Override
    public void initView() {
        mRegisterActivity.flagFillBack = false;
        mRegisterActivity.setContentView(R.layout.activity_register);
        locationView = new LocationView(mRegisterActivity);
        mRegisterActivity.root = mRegisterActivity.findViewById(R.id.setting);
        mRegisterActivity.root.setFocusable(true);
        mRegisterActivity.root.setFocusableInTouchMode(true);
        mRegisterActivity.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterActivity.finish();
            }
        });
        mRegisterActivity.input = mRegisterActivity.findViewById(R.id.input);
        mRegisterActivity.input.setOnClickListener(hidInputListener);
        mRegisterActivity.name = mRegisterActivity.findViewById(R.id.register_name_value);
        mRegisterActivity.name.setOnClickListener(setNameListener);
        mRegisterActivity.address = mRegisterActivity.findViewById(R.id.register_adderss_value);
        mRegisterActivity.address.setOnClickListener(setAddressListener);

//        mRegisterActivity.access = mRegisterActivity.findViewById(R.id.register_access);
//        mRegisterActivity.btnaccess = mRegisterActivity.findViewById(R.id.accessbtn);
//        mRegisterActivity.btnaccess.setOnClickListener(accessLisetner);
//        mRegisterActivity.attdence = mRegisterActivity.findViewById(R.id.register_attdence);
//        mRegisterActivity.btnattdence = mRegisterActivity.findViewById(R.id.attdencebtn);
//        mRegisterActivity.btnattdence.setOnClickListener(attdenceLisetner);
//        initCheck();
        mRegisterActivity.btnRegister = mRegisterActivity.findViewById(R.id.registerbtn);
        mRegisterActivity.btnRegister.setOnClickListener(regiserListener);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
        locationView.hidView();
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
    }

    public View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    public void updataView(Device device)
    {
        if(device != null)
        {
            SharedPreferences sharedPre = mRegisterActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString(UserDefine.USER_SETTING_CONTACT_EQUIP_NAME, device.cname);
            e1.putString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESS, device.address);
            e1.putString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESSID, device.addressid);
            e1.putString(UserDefine.REGISTER_ACCESS, device.isaccess);
            e1.putString(UserDefine.REGISTER_ATTDENCE, device.isattence);
            e1.commit();
            RestaurantApplication.mApp.setregister();
            Intent intent = new Intent(MainActivity.ACTION_UPDTATA_BTN);
            mRegisterActivity.sendBroadcast(intent);
            mRegisterActivity.finish();
            AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_success));
            RestaurantApplication.mApp.audioManager.speak(mRegisterActivity,mRegisterActivity.getString(R.string.register_success));
        }
        else
        {
            AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_fail));
            RestaurantApplication.mApp.audioManager.speak(mRegisterActivity,mRegisterActivity.getString(R.string.register_fail));
        }
    }



    private View.OnClickListener hidInputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm2 = (InputMethodManager) mRegisterActivity.getSystemService(mRegisterActivity.INPUT_METHOD_SERVICE);
//            imm2.hideSoftInputFromWindow(name.getWindowToken(), 0);

        }
    };

    private View.OnClickListener setNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow = AppUtils.creatXpxDialogEdit(mRegisterActivity,null,mRegisterActivity.getString(R.string.setting_contact_equipment_name_title2)
                    ,mRegisterActivity.name.getText().toString(),getNameListener,v, InputType.TYPE_CLASS_TEXT);
        }
    };


    public AppUtils.GetEditText getNameListener = new AppUtils.GetEditText() {
        @Override
        public void onEditText(String s) {
            setName(s);
        }
    };


    private void setName(String pass) {
        SharedPreferences sharedPre = mRegisterActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putString(UserDefine.USER_SETTING_CONTACT_EQUIP_NAME, pass);
        e1.commit();
        mRegisterActivity.name.setText(pass);
    }

    private View.OnClickListener setAddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            locationView.creatView(mRegisterActivity.findViewById(R.id.setting));
        }
    };


    public View.OnClickListener accessLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onAccessChange();
        }
    };

    public View.OnClickListener attdenceLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onAttenceChange();
        }
    };

    public View.OnClickListener regiserListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPre = mRegisterActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            Device device = new Device();
            device.cid = RestaurantApplication.mApp.clidenid;
            device.cname = sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_NAME,"");
            device.addressid = sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESSID,"");
            device.address = sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESS,"");
            device.isaccess = sharedPre.getString(UserDefine.REGISTER_ACCESS,"0");
            device.isattence = sharedPre.getString(UserDefine.REGISTER_ATTDENCE,"0");
            if(device.cname.length() == 0)
            {
                AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_name_empty));
                return;
            }
            if(device.address.length() == 0)
            {
                AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_address_empty));
                return;
            }
            if(device.addressid.length() == 0)
            {
                AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_address_empty));
                return;
            }

            if(AppUtils.getNetWorkState(mRegisterActivity) == 2)
            {
                Intent intent=new Intent("com.hyzn.sdk.CtlEthernet");
                intent.putExtra("mode", 0);
                mRegisterActivity.sendBroadcast(intent);
            }
            else
            {
                String ip = AppUtils.getLocalIpAddress2(mRegisterActivity);
                DeviceAsks.registerDevice(mRegisterActivity,registerHandler,device,ip);
            }


        }
    };

    public void setGetip(Intent intent) {
        SharedPreferences sharedPre = mRegisterActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        Device device = new Device();
        device.cid = RestaurantApplication.mApp.clidenid;
        device.cname = sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_NAME,"");
        device.addressid = sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESSID,"");
        device.address = sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESS,"");
        device.isaccess = sharedPre.getString(UserDefine.REGISTER_ACCESS,"0");
        device.isattence = sharedPre.getString(UserDefine.REGISTER_ATTDENCE,"0");
        if(device.cname.length() == 0)
        {
            AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_name_empty));
            return;
        }
        if(device.address.length() == 0)
        {
            AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_address_empty));
            return;
        }
        if(device.addressid.length() == 0)
        {
            AppUtils.showMessage(mRegisterActivity,mRegisterActivity.getString(R.string.register_address_empty));
            return;
        }
        String ip = intent.getStringExtra("EthIpAddress");
        DeviceAsks.registerDevice(mRegisterActivity,registerHandler,device,ip);
    }

    public void setLocation(Intent intent) {
        SharedPreferences sharedPre = mRegisterActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESSID, intent.getStringExtra("id"));
        e1.putString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESS, intent.getStringExtra("name"));
        mRegisterActivity.address.setText(intent.getStringExtra("name"));
        e1.commit();
    }

    public void onAccessChange()
    {
        SharedPreferences sharedPre = mRegisterActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        if(sharedPre.getString(UserDefine.REGISTER_ACCESS,"0").equals("1"))
        {
            e1.putString(UserDefine.REGISTER_ACCESS,"0");
            mRegisterActivity.access.setImageResource(R.drawable.bunselect);
        }
        else
        {
            e1.putString(UserDefine.REGISTER_ACCESS,"1");
            mRegisterActivity.access.setImageResource(R.drawable.bselect);
        }
        e1.commit();
    }


    public void onAttenceChange()
    {
        SharedPreferences sharedPre = mRegisterActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        if(sharedPre.getString(UserDefine.REGISTER_ATTDENCE,"0").equals("1"))
        {
            e1.putString(UserDefine.REGISTER_ATTDENCE,"0");
            mRegisterActivity.attdence.setImageResource(R.drawable.bunselect);
        }
        else
        {
            e1.putString(UserDefine.REGISTER_ATTDENCE,"1");
            mRegisterActivity.attdence.setImageResource(R.drawable.bselect);
        }
        e1.commit();
    }

    public void initCheck()
    {
        SharedPreferences sharedPre = mRegisterActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        if(sharedPre.getString(UserDefine.REGISTER_ATTDENCE,"0").equals("1"))
        {
            mRegisterActivity.attdence.setImageResource(R.drawable.bselect);
        }
        else
        {
            mRegisterActivity.attdence.setImageResource(R.drawable.bunselect);
        }
        if(sharedPre.getString(UserDefine.REGISTER_ACCESS,"0").equals("1"))
        {
            mRegisterActivity.access.setImageResource(R.drawable.bselect);
        }
        else
        {
            mRegisterActivity.access.setImageResource(R.drawable.bunselect);
        }
    }
}
