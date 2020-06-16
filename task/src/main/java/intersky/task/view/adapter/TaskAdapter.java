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
            holder.dete = (TextView) convertView.findViewById(R.id.taskdete);
            holder.newtitle = (TextView) convertView.findViewById(R.id.isnew);
            holder.imagetask = (ImageView) convertView.findViewById(R.id.task_son);
            holder.imagelist = (ImageView) convertView.findViewById(R.id.task_list);
            holder.taskcount = (TextView) convertView.findViewById(R.id.task_son_count);
            holder.listcount = (TextView) convertView.findViewById(R.id.task_list_count);
            holder.detiallayer = (LinearLayout) convertView.findViewById(R.id.detiallayer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        if (mTask.isread == true) {
            holder.newtitle.setVisibility(View.GONE);
        } else {
            holder.newtitle.setVisibility(View.VISIBLE);
        }

        if (mTask.isComplete == 1) {
            holder.title.setText(mTask.taskName);
            holder.title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.title.setText(mTask.taskName);
            holder.title.getPaint().setFlags(Paint.DITHER_FLAG);
        }

        String s = "";
        Contacts taskPerson = (Contacts) Bus.callData(mContext,"chat/getContactItem",mTask.leaderId);
        AppUtils.setContactCycleHead( holder.head,taskPerson.getName(),taskPerson.colorhead);
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
            holder.dete.setVisibility(View.VISIBLE);
            if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), mTask.endTime) >= 0) {
                if (mTask.isComplete == 1) {
                    holder.dete.setTextColor(Color.rgb(76, 175, 80));
                    holder.dete.setText(mContext.getString(R.string.task_finish_nomal));
                } else {
                    if(mTask.beginTime.length() > 0)
                    {
                        if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), mTask.beginTime) > 0) {
                            holder.dete.setText(TimeUtils.praseTaskItemdata(mTask.beginTime) + "~" + TimeUtils.praseTaskItemdata(mTask.endTime));
                            holder.dete.setTextColor(Color.rgb(118, 118, 118));
                        }
                        else
                        {
                            holder.dete.setText(TimeUtils.praseTaskItemdata(mTask.beginTime) + "~" + TimeUtils.praseTaskItemdata(mTask.endTime));
                            holder.dete.setTextColor(Color.rgb(118, 118, 118));
                        }
                    }
                    else
                    {
                        holder.dete.setText(TimeUtils.praseTaskItemdata(mTask.endTime)+mContext.getString(R.string.task_date_end));
                        holder.dete.setTextColor(Color.rgb(118, 118, 118));
                    }

                }
            } else {
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
                holder.dete.setTextColor(Color.rgb(233, 79, 79));
                if (mTask.isComplete == 1) {
                    holder.dete.setText(mContext.getString(R.string.task_beyond)  + mContext.getString(R.string.button_word_finish));
                } else {
                    holder.dete.setText(mContext.getString(R.string.task_beyond_1) + text);
                }
            }
        } else {
            if (mTask.beginTime.length() > 0 && mTask.isComplete == 0) {
                holder.dete.setVisibility(View.VISIBLE);
                if (TimeUtils.minuteBetween(TimeUtils.getDateAndTime(), mTask.beginTime) > 0) {
                    holder.dete.setText(TimeUtils.praseTaskItemdata(mTask.beginTime)  +mContext.getString(R.string.task_date_start));
                    holder.dete.setTextColor(Color.rgb(118, 118, 118));
                }
                else
                {
                    holder.dete.setText(TimeUtils.praseTaskItemdata(mTask.beginTime)+mContext.getString(R.string.task_date_start));
                    holder.dete.setTextColor(Color.rgb(118, 118, 118));
                }
            } else if (mTask.isComplete == 1) {
                holder.dete.setVisibility(View.VISIBLE);
                holder.dete.setTextColor(Color.rgb(76, 175, 80));
                holder.dete.setText(mContext.getString(R.string.task_finish_nomal));
            } else {
                holder.dete.setVisibility(View.GONE);
            }
        }


        if (mTask.taskcount > 0) {
            holder.taskcount.setVisibility(View.VISIBLE);
            holder.imagetask.setVisibility(View.VISIBLE);
            holder.taskcount.setText(String.valueOf(mTask.taskfinish) + "/" + String.valueOf(mTask.taskcount));
        } else {
            holder.taskcount.setVisibility(View.GONE);
            holder.imagetask.setVisibility(View.GONE);
        }
        if (mTask.listcount > 0) {
            holder.listcount.setVisibility(View.VISIBLE);
            holder.imagelist.setVisibility(View.VISIBLE);
            holder.listcount.setText(String.valueOf(mTask.listfinish) + "/" + String.valueOf(mTask.listcount));
        } else {

            holder.listcount.setVisibility(View.GONE);
            holder.imagelist.setVisibility(View.GONE);
        }
        return convertView;
    }


    private static class ViewHolder {
        private TextView title;
        private TextView head;
        private ImageView imagetask;
        private ImageView imagelist;
        private TextView taskcount;
        private TextView listcount;
        private LinearLayout detiallayer;
        private TextView newtitle;
        private TextView dete;
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
