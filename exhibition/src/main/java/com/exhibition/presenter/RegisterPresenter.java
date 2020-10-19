package com.exhibition.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.exhibition.R;
import com.exhibition.database.DBHelper;
import com.exhibition.entity.Guest;
import com.exhibition.handler.RegisterHandler;
import com.exhibition.receiver.RegisterReceiver;
import com.exhibition.utils.FingerUtils;
import com.exhibition.view.ExhibitionApplication;
import com.exhibition.view.activity.RegisterActivity;
import com.finger.entity.Finger;

import java.util.ArrayList;
import java.util.List;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.conturypick.DbHelper;

public class RegisterPresenter implements Presenter {

    public RegisterActivity registerActivity;
    public RegisterHandler registerHandler;
    public ArrayAdapter<String> sAdapter;
    public List<String> sexstring = new ArrayList<>();
    public TextView btnCancle;
    public TextView btnPrint;
    public TextView btnSubmit;
    public RelativeLayout hidinput;
    public EditText name;
    public Spinner sex;
    public EditText address;
    public EditText licence;
    public EditText mobil;
    public EditText type;
    public EditText count;

    public EditText items;
    public EditText car;
    public TextView time;
    public EditText card;
    public View fingerbtn;
    public View facebtn;
    public Guest guest = new Guest();
    public MyLinearLayout fingerlayout;
    public RelativeLayout close;

    public RegisterPresenter(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
        registerHandler = new RegisterHandler(registerActivity);
        registerActivity.setBaseReceiver(new RegisterReceiver(registerHandler));
    }

    @Override
    public void initView() {
        registerActivity.setContentView(R.layout.avtivity_register);
        btnCancle = registerActivity.findViewById(R.id.btn_cancle);
        btnPrint = registerActivity.findViewById(R.id.btn_print);
        btnSubmit = registerActivity.findViewById(R.id.btn_submit);
        hidinput = registerActivity.findViewById(R.id.hidinput);
        name = registerActivity.findViewById(R.id.namevalue);
        sex = registerActivity.findViewById(R.id.sexvalue);
        address = registerActivity.findViewById(R.id.addressvalue);
        licence = registerActivity.findViewById(R.id.licensevalue);
        mobil = registerActivity.findViewById(R.id.mobilvalue);
        type = registerActivity.findViewById(R.id.typevalue);
        count = registerActivity.findViewById(R.id.countvalue);
        items = registerActivity.findViewById(R.id.itemsvalue);
        car = registerActivity.findViewById(R.id.carvalue);
        time = registerActivity.findViewById(R.id.timevalue);
        card = registerActivity.findViewById(R.id.cardvalue);
        facebtn = registerActivity.findViewById(R.id.btnface);
        fingerlayout = registerActivity.findViewById(R.id.finger_image_content);
        if(registerActivity.getIntent().hasExtra("rid"))
        {
            guest.rid = registerActivity.getIntent().getStringExtra("rid");
            guest = DBHelper.getInstance(registerActivity).getGuestInfo(guest.rid);
        }


        if(guest.fingers.size() < ExhibitionApplication.MAX_FINGER_SIZE)
        {
            for(int i = 0 ; i < guest.fingers.size() ; i++)
            {
                addFingerView(guest.fingers.get(i));
            }
            initAddTextView();
        }
        sexstring.clear();
        sexstring.add(registerActivity.getString(R.string.sex_male));
        sexstring.add(registerActivity.getString(R.string.sex_female));
        sAdapter = new ArrayAdapter<String>(registerActivity, R.layout.sex_cell,
                sexstring);
        sex.setAdapter(sAdapter);
        setViewData();
        sex.setOnItemSelectedListener(sniperItemClick);
        time.setOnClickListener(timepickListener);
        btnSubmit.setOnClickListener(saveListener);
        hidinput.setOnClickListener(hidinputListener);
        fingerbtn.setOnClickListener(startRecordFingerListener);
        facebtn.setOnClickListener(startFaceListener);
        btnPrint.setOnClickListener(startFaceListener);
        btnCancle.setOnClickListener(closeListener);
        close =registerActivity.findViewById(R.id.view_register);
        close.setOnClickListener(closeListener);
        ExhibitionApplication.mApp.icCardManager.getCardIds.add(registerActivity.getCardId);
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

    }

    public void updataTimeout() {
        ExhibitionApplication.mApp.setTimeMax();
    }

    public void addFinger(Intent intent)
    {
        addFinger(ExhibitionApplication.mApp.fingerManger.lastgetFinger);
    }

    public void setViewData()
    {
        if(this.guest != null)
        {
            name.setText(guest.name);
            if(guest.sex.equals(registerActivity.getString(R.string.sex_male)))
            {
                sex.setSelection(0);
            }
            if(guest.sex.equals(registerActivity.getString(R.string.sex_female)))
            {
                sex.setSelection(1);
            }
            address.setText(guest.address);
            licence.setText(guest.licence);
            type.setText(guest.type);

            mobil.setText(guest.mobil);
            count.setText(guest.count);
            items.setText(guest.items);
            car.setText(guest.car);
            time.setText(guest.utime);
            card.setText(guest.card);
        }
    }

    public void mesureViewData()
    {
        guest.name = name.getText().toString();
        guest.sex = sex.getSelectedItem().toString();
        guest.address = address.getText().toString();
        guest.licence = licence.getText().toString();
        guest.type = type.getText().toString();
        guest.mobil = mobil.getText().toString();
        guest.count = count.getText().toString();
        guest.items = items.getText().toString();
        guest.car = car.getText().toString();
        guest.time = TimeUtils.getDate();
        guest.card = card.getText().toString();
        guest.utime = time.getText().toString();
    }



    public View.OnClickListener closeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setHidinput();
            registerActivity.finish();
        }
    };


    public View.OnClickListener hidinputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setHidinput();
        }
    };

    public void setHidinput()
    {
        InputMethodManager imm2 = (InputMethodManager) registerActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(name.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(address.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(licence.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(type.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mobil.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(count.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(items.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(car.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(time.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(card.getWindowToken(), 0);
    }

    public void onTimePick() {
        if(time.getText().length() > 0)
            AppUtils.creatTimePicker(registerActivity, time.getText().toString(), registerActivity.getString(R.string.register_s_time), mOnTimeSetListener);
        else
            AppUtils.creatTimePicker(registerActivity, time.getText().toString(), registerActivity.getString(R.string.register_s_time), mOnTimeSetListener);
    }

    public View.OnClickListener timepickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onTimePick();
        }
    };

    public DoubleDatePickerDialog.OnDateSetListener mOnTimeSetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%02d:%02d", hour, miniute);
            time.setText(textString);
        }
    };


    public AdapterView.OnItemSelectedListener sniperItemClick = new AdapterView.OnItemSelectedListener()
    {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };




    public View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mesureViewData();
            if(guest.rid.length() > 0)
            {
                if(guest.name.length() == 0)
                {
                    return;
                }

                DBHelper.getInstance(registerActivity).addGuest(guest);
                setHidinput();
                registerActivity.finish();
            }

        }
    };

    private void addFingerView(Finger finger) {
        LayoutInflater mInflater = (LayoutInflater) registerActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.finger_image, null);
        view.setTag(finger);
        ImageView pic = view.findViewById(R.id.fujian_img_big);
        ImageView close = view.findViewById(R.id.close);
        close.setTag(view);
        close.setOnClickListener(deleteFingerListener);
        if(finger.sampleimg.size() > 0)
            pic.setImageBitmap(finger.sampleimg.get(0));
        fingerlayout.addView(view);
    }

    private void addFingerView(Finger finger,int index) {
        LayoutInflater mInflater = (LayoutInflater) registerActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.finger_image, null);
        view.setTag(finger);
        ImageView pic = view.findViewById(R.id.fujian_img_big);
        ImageView close = view.findViewById(R.id.close);
        close.setTag(view);
        if(finger.sampleimg.size() > 0)
            pic.setImageBitmap(finger.sampleimg.get(0));
        close.setOnClickListener(deleteFingerListener);
        fingerlayout.addView(view,index);
    }

    public void deleteView(View view)
    {
        fingerlayout.removeView(view);
    }

    private void initAddTextView() {
        LayoutInflater mInflater = (LayoutInflater) registerActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fingerbtn = mInflater.inflate(R.layout.finger_btn, null);
        fingerbtn.setOnClickListener(startRecordFingerListener);
        fingerlayout.addView(fingerbtn);

    }

    public View.OnClickListener deleteFingerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View mv = (View) v.getTag();
            deleteView(mv);
            Finger finger = (Finger) mv.getTag();
            guest.fingers.remove(finger);
        }
    };

    public View.OnClickListener startRecordFingerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Finger finger = new Finger();
            finger.rid = guest.rid;
            finger.gid = AppUtils.getguid();
            ExhibitionApplication.mApp.fingerManger.startGetFingerImage(registerActivity, registerActivity.findViewById(R.id.view_register),3,finger);
        }
    };

    public View.OnClickListener startFaceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           AppUtils.showMessage(registerActivity,registerActivity.getString(R.string.login_no_function));
        }
    };

    public void addFinger(Finger finger)
    {
        addFingerView(finger,guest.fingers.size());
        guest.fingers.add(finger);
    }

    public void praseIcCard(String id) {
        String mid = ExhibitionApplication.mApp.icCardManager.praseIcCardIs(id);
        card.setText(mid);
    }
}
