package com.exhibition.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.exhibition.R;

import org.json.JSONException;

import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.mywidget.PopView;

public class SafeSettingView extends PopView {

    public EditText name;
    public TextView btnChangecode;
    public RadioGroup btnSafe;
    public RadioButton safeOpen;
    public RadioButton safeClose;
    public RadioGroup btnFace;
    public RadioGroup btnLic;
    public RadioButton licOpen;
    public RadioButton licClose;
    public View hid;
    public SafeSettingView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_safesetting,null);
        close = mainView.findViewById(R.id.view_register);
        hid = mainView.findViewById(R.id.hidinput);
        btnFace = mainView.findViewById(R.id.facegroup);
        btnFace.setEnabled(false);
        btnSafe = mainView.findViewById(R.id.safegroup);
        safeOpen = mainView.findViewById(R.id.safeopen);
        safeClose = mainView.findViewById(R.id.safecolse);
        btnLic = mainView.findViewById(R.id.licencegroup);
        licOpen = mainView.findViewById(R.id.licenceopen);
        licClose = mainView.findViewById(R.id.licencecolse);
        btnChangecode = mainView.findViewById(R.id.button_admin_code);
        btnChangecode.setOnClickListener(changePasswordListener);
        btnSafe.setOnCheckedChangeListener(safeChangeListener);
        btnLic.setOnCheckedChangeListener(licChangeListener);
        hid.setOnClickListener(hidinputListener);
    }

    public void creatView(View location) {
        initData();
        super.creatView(location);
    }


    private void initData(){
        if(ExhibitionApplication.mApp.getLic() == true)
        {
            licOpen.setChecked(true);
        }
        else
        {
            licClose.setChecked(true);
        }

        if(ExhibitionApplication.mApp.getSafe() == true)
        {
            safeOpen.setChecked(true);
        }
        else
        {
            safeClose.setChecked(true);
        }
    }

    private View.OnClickListener changePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changePasssword();
        }
    };

    private RadioGroup.OnCheckedChangeListener licChangeListener = new RadioGroup.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.licenceopen)
            {
                ExhibitionApplication.mApp.setLic(true);
            }
            else
            {
                ExhibitionApplication.mApp.setLic(false);
            }
        }
    };


    private RadioGroup.OnCheckedChangeListener safeChangeListener = new RadioGroup.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.safeopen)
            {
                ExhibitionApplication.mApp.setSafe(true);
            }
            else
            {
                ExhibitionApplication.mApp.setSafe(false);
            }
        }
    };


    private void changePasssword() {

        if(ExhibitionApplication.mApp.isadmin == false)
        {
            AppUtils.showMessage(context,context.getString(R.string.error_use_admin));
            return;
        }

        String password = ExhibitionApplication.mApp.getPassword();
        AlertDialog dialog = AppUtils.creatDialogTowButtonEditPass(context,"",context.getString(R.string.base_setting_title_admincode_title),
                context.getString(R.string.button_word_cancle),context.getString(R.string.button_word_ok),null,new EditDialogListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExhibitionApplication.mApp.setPassword(editText.getText().toString());
                    }
                },password);

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
