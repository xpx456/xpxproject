package intersky.schedule.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.schedule.R;
import intersky.schedule.entity.Day;

@SuppressLint("InflateParams")
public class DateAdapter extends BaseAdapter {
    public ArrayList<Day> mDates;
    private Context mContext;
    private LayoutInflater mInflater;
    public String key = "";

    public DateAdapter(Context context, ArrayList<Day> mDates, String key) {
        this.mContext = context;
        this.mDates = mDates;
        this.key = key;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDates.size();
    }

    @Override
    public Day getItem(int position) {
        // TODO Auto-generated method stub
        return mDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Day mDate = mDates.get(position);
        ViewHolder holder;
        RelativeLayout date;
        TextView mdateTitle;
        if (mDate.type == Day.DATE_ITEM_TYPE_WEEK) {
            convertView = mInflater.inflate(R.layout.week_item, null);
            date = (RelativeLayout) convertView.findViewById(R.id.date);
            mdateTitle = (TextView) convertView.findViewById(R.id.date_text);
            mdateTitle.setTextColor(Color.rgb(176,176,176));
        } else {
            convertView = mInflater.inflate(R.layout.date_item, null);
            date = (RelativeLayout) convertView.findViewById(R.id.date);
            mdateTitle = (TextView) convertView.findViewById(R.id.date_text);
            mdateTitle.setTextColor(Color.rgb(176,176,176));

        }
        if (mDate.type == Day.DATE_ITEM_TYPE_NOMAL) {

            if(key.equals(mDate.key.substring(0,7)) )
            {
                mdateTitle.setTextColor(Color.rgb(0,0,0));
            }
            else
            {
                mdateTitle.setTextColor(Color.rgb(176,176,176));
            }

        }
        mdateTitle.setText(mDate.title);
        if (mDate.isSelect == true) {
            mdateTitle.setBackgroundResource(R.drawable.shape_bg_round_blue);
            mdateTitle.setTextColor(Color.rgb(255, 255, 255));
        }

        if (mDate.isToday == true && mDate.isSelect == false) {
            mdateTitle.setBackgroundResource(R.drawable.shape_bg_round_red);
            mdateTitle.setTextColor(Color.rgb(255, 255, 255));
        }
        else if(mDate.isToday == false && mDate.isSelect == false && mDate.mEvents.size() > 0)
        {
            mdateTitle.setBackgroundResource(R.drawable.shape_bg_cycle_blue);
        }

        return convertView;
    }

    private static class ViewHolder {
        private RelativeLayout date;
        private TextView mdateTitle;
    }
}
