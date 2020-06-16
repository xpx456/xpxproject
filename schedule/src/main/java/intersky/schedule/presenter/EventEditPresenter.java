package intersky.schedule.presenter;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.mywidget.conturypick.PinyinUtil;
import intersky.schedule.R;
import intersky.schedule.ScheduleManager;
import intersky.schedule.asks.ScheduleAsks;
import intersky.schedule.handler.EventEditHandler;
import intersky.schedule.receive.EventEditReceiver;
import intersky.schedule.view.activity.EventEditActivity;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class EventEditPresenter implements Presenter {


    public EventEditHandler mEventEditHandler;
    public EventEditActivity mEventEditActivity;
    public ArrayList<Select> mReminds = new ArrayList<Select>();
    public EventEditPresenter(EventEditActivity mEventEditActivity)
    {
        this.mEventEditActivity = mEventEditActivity;
        this.mEventEditHandler = new EventEditHandler(mEventEditActivity);
        mEventEditActivity.setBaseReceiver(new EventEditReceiver(mEventEditHandler));
    }

    @Override
    public void Create() {
        initView();

    }

    @Override
    public void initView() {
        mEventEditActivity.setContentView(R.layout.activity_event_edit);
        mEventEditActivity.mEvent = mEventEditActivity.getIntent().getParcelableExtra("event");
        mEventEditActivity.eTxtContent = (EditText) mEventEditActivity.findViewById(R.id.answer_text);
        mEventEditActivity.btnTime = (RelativeLayout) mEventEditActivity.findViewById(R.id.timelayer);
        mEventEditActivity.btnRemind = (RelativeLayout) mEventEditActivity.findViewById(R.id.remindlayer);
        mEventEditActivity.txtTime = (TextView) mEventEditActivity.findViewById(R.id.time_content);
        mEventEditActivity.txtRemind = (TextView) mEventEditActivity.findViewById(R.id.remind_content);
        mEventEditActivity.btnTime.setOnClickListener(mEventEditActivity.setTimeListener);
        mEventEditActivity.btnRemind.setOnClickListener(mEventEditActivity.selectRemindListener);
        mEventEditActivity.eTxtContent.setText(mEventEditActivity.mEvent.mContent);
        mEventEditActivity.txtTime.setText(mEventEditActivity.mEvent.mTime.substring(0,16));
        if(mEventEditActivity.mEvent.mScheduleid.length() > 0)
        {

            if(!mEventEditActivity.mEvent.mUserid.equals(ScheduleManager.getInstance().oaUtils.mAccount.mRecordId))
            {
                mEventEditActivity.btnTime.setEnabled(false);
                mEventEditActivity.btnRemind.setEnabled(false);
                mEventEditActivity.eTxtContent.setEnabled(false);
                ToolBarHelper.setTitle(mEventEditActivity.mActionBar,mEventEditActivity.getString(R.string.schdule_event_show));
            }
            else
            {
                ToolBarHelper.setRightBtnText(mEventEditActivity.mActionBar, mEventEditActivity.saveLitener, mEventEditActivity.getString(R.string.button_word_save));
                ToolBarHelper.setTitle(mEventEditActivity.mActionBar,mEventEditActivity.getString(R.string.schdule_event_edit));
            }


        }
        else
        {
            ToolBarHelper.setRightBtnText(mEventEditActivity.mActionBar, mEventEditActivity.saveLitener, mEventEditActivity.getString(R.string.button_word_save));
            ToolBarHelper.setTitle(mEventEditActivity.mActionBar,mEventEditActivity.getString(R.string.schdule_event_new));
        }
        Bus.callData(mEventEditActivity,"function/updateOahit");
        ScheduleAsks.getReminds(mEventEditActivity,mEventEditHandler);
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

    public void updataRemind() {
        String text = "";
        mEventEditActivity.mEvent.mReminds = "";
        for(int i = 0 ; i < mReminds.size() ; i++)
        {
            if(mReminds.get(i).iselect)
            {
                if (text.length() == 0) {
                    text += mReminds.get(i).mName;
                } else {
                    text += "," + mReminds.get(i).mName;
                }
                if(mEventEditActivity.mEvent.mReminds.length() == 0)
                    mEventEditActivity.mEvent.mReminds += mReminds.get(i).mId;
                else
                    mEventEditActivity.mEvent.mReminds += ","+mReminds.get(i).mId;
            }

        }
        mEventEditActivity.txtRemind.setText(text);
    }


    public void doSave() {
        if (mEventEditActivity.eTxtContent.getText().length() == 0) {
            AppUtils.showMessage(mEventEditActivity, mEventEditActivity.getString(R.string.remind_content_e));
            return;
        }
        mEventEditActivity.mEvent.mContent = mEventEditActivity.eTxtContent.getText().toString();
        mEventEditActivity.waitDialog.show();
        ScheduleAsks.saveEvent(mEventEditActivity,mEventEditHandler,mEventEditActivity.mEvent);
    }

    public void doSelectRemind() {
        SelectManager.getInstance().startSelectView(mEventEditActivity,mReminds,mEventEditActivity.getString(R.string.title_remind),EventEditActivity.ACTION_SET_SCHEDULE_REMIND,false,false);
    }

    public void doSetTime()
    {
        AppUtils.creatTimePicker(mEventEditActivity, mEventEditActivity.txtTime.getText().toString(),mEventEditActivity.getString(R.string.schdule_time_title),mOnTimeSetListener);
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnTimeSetListener = new DoubleDatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            mEventEditActivity.mEvent.mTime = String.format("%04d-%02d-%02d %02d:%02d",startYear,startMonthOfYear+1,startDayOfMonth,hour,miniute)+":00";
            mEventEditActivity.txtTime.setText(String.format("%04d-%02d-%02d %02d:%02d",startYear,startMonthOfYear+1,startDayOfMonth,hour,miniute));
        }
    };
}
