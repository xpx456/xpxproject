package com.exhibition.view;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.exhibition.R;
import com.exhibition.database.DBHelper;
import com.exhibition.entity.Guest;

import java.util.ArrayList;
import java.util.List;

import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.xpxnet.net.Service;

public class RegisterView {

    public Context context;
    public View mainView;
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

    public Guest guest;
    public PopupWindow popupWindow;

    public RegisterView(Context context) {
        this.context = context;
    }

    public RegisterView(Context context,Guest guest) {
        this.context = context;
        this.guest = guest;
    }

    public AppUtils.InitView initView = new AppUtils.InitView() {
        @Override
        public void initView(View view) {
            mainView = view;
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
        }
    };


    public void creatView(View location) {
        popupWindow = AppUtils.creatPopView(context,R.layout.view_register,R.id.view_register,location,initView);
    }

    public void hidView() {
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
    }

    public void setData(Guest guest) {
        this.guest = guest;
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
        if(this.guest == null)
        {
            this.guest = new Guest();
        }
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
                DBHelper.getInstance(context).addGuest(guest);
                hidView();
            }

        }
    };
}
