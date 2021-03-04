package intersky.workreport.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.workreport.R;
import intersky.workreport.WorkReportManager;
import intersky.workreport.entity.Report;

/**
 * Created by xpx on 2016/10/12.
 */

public class ReportAdapter extends BaseAdapter {

    private ArrayList<Report> mReports;
    private Context mContext;
    private LayoutInflater mInflater;
    public int nowpage = 1;
    public int totalCount = -1;
    public int tempPage = 0;
    public int endPage = 1;
    public boolean edit = false;
    public ReportAdapter(ArrayList<Report> mReports, Context mContext) {
        this.mContext = mContext;
        this.mReports = mReports;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mReports.size();
    }

    @Override
    public Report getItem(int position) {
        return mReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Report mReport = getItem(position);
        ViewHoder mview = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.report_item, null);
            mview = new ViewHoder();
            mview.mtitle = (TextView) convertView.findViewById(R.id.conversation_title);
            mview.imageView = (TextView) convertView.findViewById(R.id.conversation_img);
            mview.msubject = (TextView) convertView.findViewById(R.id.conversation_subject);
            mview.hit = (TextView) convertView.findViewById(R.id.hit);
            mview.mSelectIcon = (ImageView) convertView.findViewById(R.id.item_selecticon);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        if(mReport.isread == false)
        {
            mview.hit.setVisibility(View.VISIBLE);
        }
        else
        {
            mview.hit.setVisibility(View.INVISIBLE);
        }
        AppUtils.setContactCycleHead(mview.imageView,mReport.userName);
        String typename = "";
        switch (mReport.mType)
        {
            case WorkReportManager.TYPE_DAY:
                typename = mContext.getString(R.string.xml_workreport_dday);
                mview.mtitle.setText(mReport.userName+" "+typename+" ("+ mReport.mBegainTime.substring(0,10)+")");
                break;
            case WorkReportManager.TYPE_WEEK:
                typename = mContext.getString(R.string.xml_workreport_dweek);
                String a = mReport.userName+" "+typename+"("+ mReport.mBegainTime.substring(0,10)+"/"+ mReport.mEndTime.substring(0,10)+")";
                mview.mtitle.setText(mReport.userName+" "+typename+"("+ mReport.mBegainTime.substring(0,10)+"/"+ mReport.mEndTime.substring(0,10)+")");
                break;
            case WorkReportManager.TYPE_MONTH:
                typename = mContext.getString(R.string.xml_workreport_dmonthy);
                String time = mReport.mBegainTime.substring(0,4)+mContext.getString(R.string.year)+ mReport.mBegainTime.substring(4,6)
                        +mContext.getString(R.string.month)+typename;
                mview.mtitle.setText(mReport.userName+" "+typename+"("+ mReport.mBegainTime.substring(0,7)+")");
                break;
        }
        mview.msubject.setText(mReport.mCreatTime.substring(5, mReport.mCreatTime.length()-3)+" "+ TimeUtils.getWeek(mReport.mCreatTime.substring(0,10),mContext));

        if(edit)
        {
            mview.mSelectIcon.setVisibility(View.VISIBLE);
            if (mReport.select) {
                mview.mSelectIcon.setImageResource(R.drawable.bselect);
            } else {
                mview.mSelectIcon.setImageResource(R.drawable.bunselect);
            }
        }
        else
        {
            mview.mSelectIcon.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHoder {
        TextView imageView;
        TextView mtitle;
        TextView msubject;
        TextView hit;
        ImageView mSelectIcon;
    }

    ;

    public ArrayList<Report> getmReports() {
        return mReports;
    }
}
