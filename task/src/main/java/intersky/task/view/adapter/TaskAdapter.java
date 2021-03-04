package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.task.R;
import intersky.task.entity.Task;

@SuppressLint("InflateParams")
public class TaskAdapter extends BaseAdapter {

    private ArrayList<Task> mTaskItems;
    private Context mContext;
    private LayoutInflater mInflater;

    public TaskAdapter(Context context, ArrayList<Task> mTaskItems) {
        this.mContext = context;
        this.mTaskItems = mTaskItems;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTaskItems.size();
    }

    @Override
    public Task getItem(int position) {
        // TODO Auto-generated method stub
        return mTaskItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Task mTask = mTaskItems.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.task_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.taskname);
            holder.head = (TextView) convertView.findViewById(R.id.contact_img);
            holder.dete = (TextView) convertView.findViewById(R.id.taskdetestart);
            holder.dete2 = (TextView) convertView.findViewById(R.id.taskdeteend);
            holder.newtitle = (TextView) convertView.findViewById(R.id.isnew);
            holder.imagetask = (View) convertView.findViewById(R.id.dian1);
            holder.imagelist = (View) convertView.findViewById(R.id.dian2);
            holder.taskcount1 = (TextView) convertView.findViewById(R.id.task_son_count_title);
            holder.listcount1 = (TextView) convertView.findViewById(R.id.task_list_count_title);
            holder.taskcount = (TextView) convertView.findViewById(R.id.task_son_count);
            holder.listcount = (TextView) convertView.findViewById(R.id.task_list_count);
            holder.statue = (TextView) convertView.findViewById(R.id.result);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        if (mTask.isread == true) {
            holder.newtitle.setVisibility(View.INVISIBLE);
        } else {
            holder.newtitle.setVisibility(View.VISIBLE);
        }

        holder.title.setText(mTask.taskName);
        Contacts taskPerson = (Contacts) Bus.callData(mContext,"chat/getContactItem",mTask.leaderId);
        AppUtils.setContactCycleHead( holder.head,taskPerson.getName());
        TextView mtext;
        mtext = (TextView) convertView.findViewById(R.id.basetag);
        mtext.setVisibility(View.GONE);
        mtext = (TextView) convertView.findViewById(R.id.tag_1);
        mtext.setVisibility(View.GONE);
        mtext = (TextView) convertView.findViewById(R.id.tag_2);
        mtext.setVisibility(View.GONE);
        mtext = (TextView) convertView.findViewById(R.id.tag_3);
        mtext.setVisibility(View.GONE);
        mtext = (TextView) convertView.findViewById(R.id.tag_4);
        mtext.setVisibility(View.GONE);
        mtext = (TextView) convertView.findViewById(R.id.tag_5);
        mtext.setVisibility(View.GONE);
        mtext = (TextView) convertView.findViewById(R.id.basetag2);
        mtext.setVisibility(View.GONE);
        if (mTask.tag.length() > 0) {
            String[] str = mTask.tag.split(",");
            for (int i = 0; i < str.length; i++) {
                if (i == 0)
                    showView(convertView, str[i], true);
                else
                    showView(convertView, str[i], false);
            }
        }
        if (mTask.isStar == 1) {
            ImageView image = (ImageView) convertView.findViewById(R.id.stare);
            image.setVisibility(View.VISIBLE);
        } else {
            ImageView image = (ImageView) convertView.findViewById(R.id.stare);
            image.setVisibility(View.GONE);
        }

        if (mTask.endTime.length() > 0) {
            holder.dete2.setVisibility(View.VISIBLE);
            holder.dete2.setText("结束时间:"+mTask.endTime);
            if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), mTask.endTime) >= 0) {
                if (mTask.isComplete == 1) {
                    holder.statue.setTextColor(Color.parseColor("#16C48D"));
                    holder.statue.setText(mContext.getString(R.string.task_finish_nomal));
                } else {
                    if(mTask.beginTime.length() > 0)
                    {
                        holder.dete.setVisibility(View.VISIBLE);
                        holder.dete.setText("开始时间:"+mTask.beginTime);
                        holder.statue.setTextColor(Color.parseColor("#1EA1F3"));
                        holder.statue.setText("进行中");
                    }
                    else
                    {
                        holder.dete.setVisibility(View.GONE);
                        holder.statue.setTextColor(Color.parseColor("#FF8540"));
                        holder.statue.setText("未设置开始时间");
                    }

                }
            } else {

                if(mTask.beginTime.length() > 0)
                {
                    holder.dete.setVisibility(View.VISIBLE);
                    holder.dete.setText("开始时间:"+mTask.beginTime);
                }
                else
                {
                    holder.dete.setVisibility(View.GONE);
                }

                int day = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTask.endTime)) / 24;
                int hour = (-TimeUtils.hoursBetween(TimeUtils.getDateAndTime(), mTask.endTime)) % 24;
                String text;
                if (day == 0) {
                    if(hour == 0)
                    {
                        hour = 1;
                    }
                    text = String.valueOf(hour) + "小时";
                } else if (hour == 0) {
                    text = String.valueOf(day) + "天";
                } else {
                    text = String.valueOf(day) + "天" + String.valueOf(hour) + "小时";
                }
                if (mTask.isComplete == 1) {
                    holder.statue.setTextColor(Color.parseColor("#16C48D"));
                    holder.statue.setText(mContext.getString(R.string.task_beyond)  + mContext.getString(R.string.button_word_finish));
                } else {
                    holder.statue.setTextColor(Color.parseColor("#FF8540"));
                    holder.statue.setText(mContext.getString(R.string.task_beyond_1) + text);
                }
            }
        } else {
            holder.dete2.setVisibility(View.GONE);
            if (mTask.beginTime.length() > 0 ) {
                holder.dete.setVisibility(View.VISIBLE);
                holder.dete.setText("开始时间:"+mTask.beginTime);
            }
            else
            {
                holder.dete.setVisibility(View.GONE);
            }
            if (mTask.isComplete == 1) {
                holder.statue.setTextColor(Color.parseColor("#16C48D"));
                holder.statue.setText(mContext.getString(R.string.task_finish_nomal));
            }
        }

        if (mTask.taskcount > 0) {
            holder.taskcount.setVisibility(View.VISIBLE);
            holder.imagetask.setVisibility(View.VISIBLE);
            holder.taskcount1.setVisibility(View.VISIBLE);
            holder.taskcount.setText(String.valueOf(mTask.taskfinish) + "/" + String.valueOf(mTask.taskcount));
        } else {
            holder.taskcount.setVisibility(View.GONE);
            holder.imagetask.setVisibility(View.GONE);
            holder.taskcount1.setVisibility(View.GONE);
        }
        if (mTask.listcount > 0) {
            holder.listcount.setVisibility(View.VISIBLE);
            holder.imagelist.setVisibility(View.VISIBLE);
            holder.listcount1.setVisibility(View.VISIBLE);
            holder.listcount.setText(String.valueOf(mTask.listfinish) + "/" + String.valueOf(mTask.listcount));
        } else {

            holder.listcount.setVisibility(View.GONE);
            holder.imagelist.setVisibility(View.GONE);
            holder.listcount1.setVisibility(View.GONE);
        }
        return convertView;
    }


    private static class ViewHolder {
        private TextView title;
        private TextView head;
        private View imagetask;
        private View imagelist;
        private TextView taskcount1;
        private TextView listcount1;
        private TextView taskcount;
        private TextView listcount;
        private TextView statue;
        private TextView newtitle;
        private TextView dete;
        private TextView dete2;
    }

    public static void showView(View contentview, String id, boolean isfirst) {
        TextView mtext = null;
        TextView base = (TextView) contentview.findViewById(R.id.basetag);
        TextView base2 = (TextView) contentview.findViewById(R.id.basetag2);
        if (id.equals("1")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_1);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        } else if (id.equals("2")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_2);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        } else if (id.equals("3")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_3);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        } else if (id.equals("4")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_4);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        } else if (id.equals("5")) {
            mtext = (TextView) contentview.findViewById(R.id.tag_5);
            mtext.setVisibility(View.VISIBLE);
            if (base.getVisibility() == View.GONE) {
                base.setVisibility(View.VISIBLE);
            }
            if (base2.getVisibility() == View.GONE) {
                base2.setVisibility(View.VISIBLE);
            }
        }
        if (mtext != null) {

        }

    }
}
