package com.exhibition.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.exhibition.R;
import com.exhibition.database.DBHelper;
import com.exhibition.entity.Guest;
import com.exhibition.utils.FingerUtils;
import com.finger.entity.Finger;

import java.util.ArrayList;
import java.util.List;

import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.PopView;

public class RegisterView extends PopView {

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
    public Guest guest = new Guest();
    public MyLinearLayout fingerlayout;

    public RegisterView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        mainView = LayoutInflater.from(context).inflate(R.layout.view_register, null);
        btnCancle = mainView.findViewById(R.id.btn_cancle);
        btnPrint = mainView.findViewById(R.id.btn_print);
        btnSubmit = mainView.findViewById(R.id.btn_submit);
        hidinput = mainView.findViewById(R.id.hidinput);
        name = mainView.findViewById(R.id.namevalue);
        sex = mainView.findViewById(R.id.sexvalue);
        address = mainView.findViewById(R.id.addressvalue);
        licence = mainView.findViewById(R.id.licensevalue);
        mobil = mainView.findViewById(R.id.mobilvalue);
        type = mainView.findViewById(R.id.typevalue);
        count = mainView.findViewById(R.id.countvalue);
        items = mainView.findViewById(R.id.itemsvalue);
        car = mainView.findViewById(R.id.carvalue);
        time = mainView.findViewById(R.id.timevalue);
        card = mainView.findViewById(R.id.cardvalue);
        fingerlayout = mainView.findViewById(R.id.finger_image_content);
        if(guest.fingers.size() < ExhibitionApplication.MAX_FINGER_SIZE)
        {
            for(int i = 0 ; i < guest.fingers.size() ; i++)
            {
                addFingerView(guest.fingers.get(i));
            }
            initAddTextView();
        }
        sexstring.clear();
        sexstring.add(context.getString(R.string.sex_male));
        sexstring.add(context.getString(R.string.sex_female));
        sAdapter = new ArrayAdapter<String>(context, R.layout.sex_cell,
                sexstring);
        sex.setAdapter(sAdapter);
        setViewData();
        sex.setOnItemSelectedListener(sniperItemClick);
        time.setOnClickListener(timepickListener);
        btnSubmit.setOnClickListener(saveListener);
        hidinput.setOnClickListener(hidinputListener);
        fingerbtn.setOnClickListener(startRecordFingerListener);
        close =mainView.findViewById(R.id.view_register);
        super.initView();
    }

    @Override
    public void destoryView() {
        fingerlayout.removeAllViews();
        guest = null;
    }

    @Override
    public void cleanView() {

    }

    public void creatView(View location) {
        this.guest = new Guest();
        super.creatView(location);
    }



    private void initAddTextView() {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fingerbtn = mInflater.inflate(R.layout.finger_btn, null);
        fingerbtn.setOnClickListener(startRecordFingerListener);
        fingerlayout.addView(fingerbtn);

    }

    private void addFingerView(Finger finger) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.finger_image, null);
        ImageView pic = view.findViewById(R.id.fujian_img_big);
        if(finger.sampleimg.size() > 0)
        pic.setImageBitmap(finger.sampleimg.get(0));
        fingerlayout.addView(view);
    }

    private void addFingerView(Finger finger,int index) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.finger_image, null);
        ImageView pic = view.findViewById(R.id.fujian_img_big);
        if(finger.sampleimg.size() > 0)
            pic.setImageBitmap(finger.sampleimg.get(0));
        fingerlayout.addView(view,index);
    }


    public void setViewData()
    {
        if(this.guest != null)
        {
            name.setText(guest.name);
            if(guest.sex.equals(context.getString(R.string.sex_male)))
            {
                sex.setSelection(0);
            }
            if(guest.sex.equals(context.getString(R.string.sex_female)))
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


    public void onTimePick() {
        if(time.getText().length() > 0)
        AppUtils.creatTimePicker(context, time.getText().toString(), context.getString(R.string.register_s_time), mOnTimeSetListener);
        else
            AppUtils.creatTimePicker(context, time.getText().toString(), context.getString(R.string.register_s_time), mOnTimeSetListener);
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


    public View.OnClickListener hidinputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm2 = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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

                DBHelper.getInstance(context).addGuest(guest);
                for(int i = 0 ; i < guest.fingers.size() ; i++)
                {
                    ExhibitionApplication.mApp.fingerManger.saveFea(guest.fingers.get(i));
                }

                hidView();
            }

        }
    };

    public View.OnClickListener startRecordFingerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Finger finger = new Finger();
            finger.rid = FingerUtils.getFingerrid(guest);
            finger.gid = FingerUtils.getFingerGudid(guest.fingers.size());
            ExhibitionApplication.mApp.fingerManger.startGetFingerImage(context, location,2,finger);
        }
    };


    public void addFinger(Finger finger)
    {
        addFingerView(finger,guest.fingers.size());
        guest.fingers.add(finger);
    }
}
