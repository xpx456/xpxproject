package intersky.schedule.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.mywidget.MyViewPager;
import intersky.schedule.entity.Event;
import intersky.schedule.presenter.SchedulePresenter;
import intersky.schedule.view.adapter.EventAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ScheduleActivity extends BaseActivity {

    public static final String ACTION_SET_SCHEDULE_CONTACT = "ACTION_SET_SCHEDULE_CONTACT";
    public static final String ACTION_UPDATA_EVENT= "ACTION_UPDATA_EVENT";
    public PopupWindow mPopupWindow;
    public RecyclerView eventList;
    public TextView btnCreat;
    public Contacts mContacts;
    public EventAdapter mEventAdapter;
    public MyViewPager myViewPager;
    public SchedulePresenter mSchedulePresenter = new SchedulePresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSchedulePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSchedulePresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener showMoreListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mSchedulePresenter.showMore();
        }
    };

    public View.OnClickListener showMyListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mSchedulePresenter.showMy();
        }
    };

    public View.OnClickListener showOthreListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mSchedulePresenter.showOther();
        }
    };

    public View.OnClickListener setDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSchedulePresenter.setDate();
        }
    };

    public View.OnClickListener doCreatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSchedulePresenter.doCreat();
        }
    };

    public EventAdapter.OnItemClickListener onItemClickListener = new EventAdapter.OnItemClickListener()
    {

        @Override
        public void onItemClick(Event contacts, int position, View view) {
            mSchedulePresenter.doEdit(contacts);
        }
    };

    public AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener()
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mSchedulePresenter.doDelete((Event) parent.getAdapter().getItem(position));
            return true;
        }
    };

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
