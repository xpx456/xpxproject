package com.dk.dkphone.presenter;


import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dk.dkphone.R;
import com.dk.dkphone.database.DBHelper;
import com.dk.dkphone.entity.OptationItem;
import com.dk.dkphone.handler.OptationHandler;
import com.dk.dkphone.receiver.MainReceiver;
import com.dk.dkphone.view.DkPadApplication;
import com.dk.dkphone.view.activity.OptationActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;

public class OptationPresenter implements Presenter {

    public OptationActivity mOptationActivity;
    public OptationHandler registerHandler;
    public ArrayList<OptationItem> optationItems = new ArrayList<OptationItem>();
    public OptationPresenter(OptationActivity OptationActivity) {
        mOptationActivity = OptationActivity;
        registerHandler = new OptationHandler(mOptationActivity);
    }

    @Override
    public void initView() {

        mOptationActivity.flagFillBack = false;
        mOptationActivity.setContentView(R.layout.activity_optation);
        mOptationActivity.optation = mOptationActivity.getIntent().getParcelableExtra("optation");
        mOptationActivity.title = mOptationActivity.findViewById(R.id.title);
        mOptationActivity.delete = mOptationActivity.findViewById(R.id.delete);
        if(mOptationActivity.optation.name.length() == 0 )
        {
            mOptationActivity.title.setText(mOptationActivity.getString(R.string.new_op));
            mOptationActivity.delete.setVisibility(View.INVISIBLE);
        }
        else {
            mOptationActivity.title.setText(mOptationActivity.optation.name);
        }
        mOptationActivity.time = mOptationActivity.findViewById(R.id.titletimevalue);
        mOptationActivity.close = mOptationActivity.findViewById(R.id.close);
        mOptationActivity.btnAdd = mOptationActivity.findViewById(R.id.add);
        mOptationActivity.btnDes = mOptationActivity.findViewById(R.id.des);
        mOptationActivity.btnSave = mOptationActivity.findViewById(R.id.save);
        mOptationActivity.operlayer = mOptationActivity.findViewById(R.id.listline);
        mOptationActivity.lineLayer = mOptationActivity.findViewById(R.id.xline);
        mOptationActivity.title.setText(mOptationActivity.optation.name);
        mOptationActivity.btnAdd.setOnClickListener(addOptationListener);
        mOptationActivity.btnDes.setOnClickListener(desOptationListener);
        mOptationActivity.btnSave.setOnClickListener(saveOptationListener);
        mOptationActivity.delete.setOnClickListener(deleteOptationListener);
        mOptationActivity.root = mOptationActivity.findViewById(R.id.root);
        mOptationActivity.root.setFocusable(true);
        mOptationActivity.root.setFocusableInTouchMode(true);
        mOptationActivity.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptationActivity.finish();
            }
        });
        mOptationActivity.all = mOptationActivity.findViewById(R.id.allcontent);
        mOptationActivity.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mOptationActivity.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptationActivity.finish();
            }
        });
        if(mOptationActivity.optation.name.length() == 0)
        {
            setnew();
        }
        else
        {
            initOptationItems();
        }

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

    public void initOptationItems(){
        try {
            XpxJSONArray ja = new XpxJSONArray(mOptationActivity.optation.data);
            for(int i = 0 ; i < ja.length() ; i++)
            {
                OptationItem optationItem = new OptationItem();
                optationItem.value = Integer.valueOf(ja.getString(i)) ;
                addView(optationItem,i);
                optationItems.add(optationItem);
            }
            uptataTotalTime();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setnew()
    {
        OptationItem optationItem = new OptationItem();
        addView(optationItem,0);
        optationItems.add(optationItem);
        uptataTotalTime();
    }

    public void uptataTotalTime() {
        int totalsecond = DkPadApplication.OPTATION_ITEM_PER_SECOND *optationItems.size();
        int second = totalsecond%60;
        int min = totalsecond/60%60;

        mOptationActivity.time.setText(String.format("%02d:%02d",min,second));
    }


    public String initOptationJson(){
        JSONArray ja = new JSONArray();
        for(int i = 0 ; i < optationItems.size() ; i++)
        {
            OptationItem optationItem = optationItems.get(i);
            ja.put(String.valueOf(optationItem.value));
        }
        return ja.toString();
    }

    public void addView(OptationItem optationItem,int postation)
    {
        LayoutInflater inflater = (LayoutInflater) mOptationActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View op = inflater.inflate(R.layout.oper_set_item,null);
        optationItem.verticalSeekBar = op.findViewById(R.id.seekBar3);
        optationItem.verticalSeekBar.setMax(100);
        optationItem.verticalSeekBar.setProgress(optationItem.value);
        optationItem.verticalSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        optationItem.verticalSeekBar.setTag(optationItem);
        optationItem.oper = op;
        TextView textView = optationItem.oper.findViewById(R.id.pv);
        textView.setText(String.valueOf(optationItem.value)+"%");
        mOptationActivity.operlayer.addView(op);
        View line = inflater.inflate(R.layout.oper_set_x_item,null);
        optationItem.xtime = line.findViewById(R.id.xtime);
        int second = DkPadApplication.OPTATION_ITEM_PER_SECOND *(postation+1)%60;
        int min = DkPadApplication.OPTATION_ITEM_PER_SECOND *(postation+1)/60%60;
        optationItem.line = line;
        optationItem.xtime.setText(String.format("%02d:%02d",min,second));
        mOptationActivity.lineLayer.addView(line);
    }



    public View.OnClickListener addOptationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(optationItems.size() > 0)
            {
                OptationItem last = optationItems.get(optationItems.size()-1);
                OptationItem optationItem = new OptationItem();
                optationItem.value = last.value;
                optationItems.add(optationItem);
                addView(optationItem,optationItems.size()-1);
                uptataTotalTime();
            }
            else
            {
                OptationItem optationItem = new OptationItem();
                optationItem.value = 1;
                optationItems.add(optationItem);
                addView(optationItem,optationItems.size()-1);
                uptataTotalTime();
            }

        }
    };


    public View.OnClickListener desOptationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OptationItem last = optationItems.get(optationItems.size()-1);
            optationItems.remove(optationItems.size()-1);
            mOptationActivity.operlayer.removeView(last.oper);
            mOptationActivity.lineLayer.removeView(last.line);
            uptataTotalTime();
        }
    };

    public View.OnClickListener saveOptationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.creatXpxDialogEdit2(mOptationActivity, null, mOptationActivity.getString(R.string.opname)
                    , mOptationActivity.optation.name, getName, v, InputType.TYPE_CLASS_TEXT);
        }
    };

    public AppUtils.GetEditText2 getName = new AppUtils.GetEditText2() {
        @Override
        public void onEditText(String s, PopupWindow popupWindow) {

            if(s.length() == 0)
            {
                AppUtils.showMessage(mOptationActivity,mOptationActivity.getString(R.string.opname_empty));
                return;
            }
            if(optationItems.size() == 0)
            {
                AppUtils.showMessage(mOptationActivity,mOptationActivity.getString(R.string.opcount_empty));
                popupWindow.dismiss();
                return;
            }
            mOptationActivity.optation.name = s;
            mOptationActivity.optation.data = initOptationJson();
            DBHelper.getInstance(mOptationActivity).addOptation(mOptationActivity.optation);
            Intent intent = new Intent(MainReceiver.ACTION_UPDTATA_OPTATION);
            intent.putExtra("optation",mOptationActivity.optation);
            intent.putExtra("delete",false);
            mOptationActivity.sendBroadcast(intent);
            popupWindow.dismiss();
            mOptationActivity.finish();
        }

    };


    public View.OnClickListener deleteOptationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(DkPadApplication.mApp.hashMap.size() == 1)
            {
                AppUtils.creatXpxDialog(mOptationActivity, null
                        , mOptationActivity.getString(R.string.op_delete_title),
                        "",mOptationActivity.getString(R.string.button_delete)
                        ,mOptationActivity.getString(R.string.button_word_cancle), doDeleteListener, v);
            }
            else
            {
                AppUtils.showMessage(mOptationActivity,mOptationActivity.getString(R.string.op_delete_empty));
            }

        }
    };

    public View.OnClickListener doDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DBHelper.getInstance(mOptationActivity).deleteOptation(mOptationActivity.optation);
            Intent intent = new Intent(MainReceiver.ACTION_UPDTATA_OPTATION);
            intent.putExtra("optation",mOptationActivity.optation);
            intent.putExtra("delete",true);
            mOptationActivity.sendBroadcast(intent);
            mOptationActivity.finish();

        }
    };

    public SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            OptationItem optationItem = (OptationItem) seekBar.getTag();
            optationItem.value = seekBar.getProgress();
            if(optationItem.value == 0)
            {
                optationItem.value = 1;
                seekBar.setProgress(optationItem.value);

            }
            TextView textView = optationItem.oper.findViewById(R.id.pv);
            textView.setText(String.valueOf(optationItem.value)+"%");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
