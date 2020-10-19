package com.exhibition.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.exhibition.R;

import org.json.JSONException;

import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.mywidget.PopView;

public class BaseSettingView extends PopView {

    public TextView name;
    public TextView namebtn;
    public RadioGroup btnPrint;
    public RadioButton printOpen;
    public RadioButton printClose;
    public View hid;
    public BaseSettingView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_basesetting,null);
        close = mainView.findViewById(R.id.view_register);
        hid = mainView.findViewById(R.id.hidinput);
        name = mainView.findViewById(R.id.namevalue);
        namebtn = mainView.findViewById(R.id.namechange);
        btnPrint = mainView.findViewById(R.id.printgroup);
        printOpen = mainView.findViewById(R.id.printopen);
        printClose = mainView.findViewById(R.id.printcolse);
        printOpen.setEnabled(false);
        printClose.setEnabled(false);
        //btnPrint.setOnCheckedChangeListener(printChangeListener);
        btnPrint.setEnabled(false);
        namebtn.setOnClickListener(changeNameListener);
        hid.setOnClickListener(hidinputListener);
    }

    public void creatView(View location) {
        initData();
        super.creatView(location);
    }


    private void initData(){
        name.setText(ExhibitionApplication.mApp.getName());
    }


    private RadioGroup.OnCheckedChangeListener printChangeListener = new RadioGroup.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.licenceopen)
            {
                ExhibitionApplication.mApp.setPrint(true);
            }
            else
            {
                ExhibitionApplication.mApp.setPrint(false);
            }
        }
    };

    private View.OnClickListener changeNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeName();
        }
    };




    private void changeName() {

        if(ExhibitionApplication.mApp.isadmin == false)
        {
            AppUtils.showMessage(context,context.getString(R.string.error_use_admin));
            return;
        }

        String name = ExhibitionApplication.mApp.getName();
        AlertDialog dialog = AppUtils.creatDialogTowButtonEdit(context,"",context.getString(R.string.base_setting_title_name_title),
                context.getString(R.string.button_word_cancle),context.getString(R.string.button_word_ok),null,new EditDialogListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExhibitionApplication.mApp.setName(editText.getText().toString());
                    }
                },name);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }



    public View.OnClickListener hidinputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm2 = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    };
}
