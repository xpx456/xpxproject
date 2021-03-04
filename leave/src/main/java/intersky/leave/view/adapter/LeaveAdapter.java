package intersky.leave.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;


import intersky.apputils.AppUtils;
import intersky.apputils.StringUtils;
import intersky.apputils.TimeUtils;
import intersky.leave.LeaveManager;
import intersky.leave.R;
import intersky.leave.asks.LeaveAsks;
import intersky.leave.entity.Leave;

/**
 * Created by xpx on 2016/10/12.
 */

public class LeaveAdapter extends BaseAdapter {

    private ArrayList<Leave> mLeaves;
    private Context mContext;
    private LayoutInflater mInflater;
    public int nowpage = 1;
    public int totalCount = -1;
    public int endPage = 1;
    public int page = 0;
    public Handler handler;
    public LeaveAdapter(ArrayList<Leave> mLeaves, Context mContext, int page, Handler handler) {
        this.mContext = mContext;
        this.mLeaves = mLeaves;
        this.page = page;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.handler = handler;
    }


    @Override
    public int getCount() {
        return mLeaves.size();
    }

    @Override
    public Leave getItem(int position) {
        return mLeaves.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Leave mLeave = getItem(position);
        ViewHoder mview = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.leave_item, null);
            mview = new ViewHoder();
            mview.mtitle = (TextView) convertView.findViewById(R.id.conversation_title);
            mview.type = (TextView) convertView.findViewById(R.id.leavetype);
            mview.start = (TextView) convertView.findViewById(R.id.start);
            mview.end = (TextView) convertView.findViewById(R.id.end);
            mview.buttom = (RelativeLayout) convertView.findViewById(R.id.button_layer);
            mview.refouse = (TextView) convertView.findViewById(R.id.refouse);
            mview.accept = (TextView) convertView.findViewById(R.id.accept);
            mview.imageView = (TextView) convertView.findViewById(R.id.conversation_img);
            mview.msubject = (TextView) convertView.findViewById(R.id.conversation_subject);
            mview.content = (TextView) convertView.findViewById(R.id.content);
            mview.state = (TextView) convertView.findViewById(R.id.state_img);
            mview.hit = (TextView) convertView.findViewById(R.id.hit);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        if(mLeave.isread == false && page == 2)
        {
            mview.hit.setVisibility(View.VISIBLE);
        }
        else
        {
            mview.hit.setVisibility(View.INVISIBLE);
        }
        AppUtils.setContactCycleHead(mview.imageView,mLeave.name);
        mview.mtitle.setText(mLeave.name+"-"+mContext.getString(R.string.xml_leave));
        mview.msubject.setText(mLeave.create_time.substring(5, mLeave.create_time.length()-3)+" "+mContext.getString(R.string.keyword_xingqi)+ TimeUtils.getWeek(mLeave.create_time.substring(0,10),mContext));
        mview.content.setText(StringUtils.htmlToString(mLeave.content));
        mview.start.setText(mContext.getString(R.string.keyword_begin)+":"+mLeave.start.substring(0,16));
        mview.end.setText(mContext.getString(R.string.keyword_end)+":"+mLeave.end.substring(0,16));
        if(LeaveManager.getInstance().hashMap.containsKey(String.valueOf(mLeave.leave_type_id)))
        {
            mview.type.setText(mContext.getString(R.string.xml_leave_type_title)+":"+LeaveManager.getInstance().hashMap.get(String.valueOf(mLeave.leave_type_id)).mName);
        }

        switch (Integer.valueOf(mLeave.is_approval))
        {
            case 0:
                mview.state.setText(mContext.getString(R.string.approve_type_0));
                mview.state.setTextColor(Color.parseColor("#16C48D"));
                mview.state.setBackgroundResource(R.drawable.shape_approve0);
                mview.buttom.setVisibility(View.VISIBLE);
                mview.accept.setTag(mLeave);
                mview.refouse.setTag(mLeave);
                mview.accept.setOnClickListener(acceptListener);
                mview.refouse.setOnClickListener(refouseListener);
                break;
            case 1:
                mview.state.setText(mContext.getString(R.string.approve_type_1));
                mview.state.setTextColor(Color.parseColor("#ebb100"));
                mview.state.setBackgroundResource(R.drawable.shape_approve1);
                mview.buttom.setVisibility(View.GONE);
                break;
            case 2:
                mview.state.setText(mContext.getString(R.string.approve_type_2));
                mview.state.setTextColor(Color.parseColor("#15BD85"));
                mview.state.setBackgroundResource(R.drawable.shape_approve2);
                mview.buttom.setVisibility(View.GONE);
                break;
            case 3:
                mview.state.setText(mContext.getString(R.string.approve_type_3));
                mview.state.setTextColor(Color.parseColor("#E94142"));
                mview.state.setBackgroundResource(R.drawable.shape_approve3);
                mview.buttom.setVisibility(View.GONE);
                break;
        }



        return convertView;
    }

    public   class ViewHoder {
        TextView imageView;
        TextView mtitle;
        TextView type;
        TextView start;
        TextView end;
        TextView msubject;
        TextView content;
        TextView hit;
        TextView accept;
        TextView refouse;
        RelativeLayout buttom;
        TextView state;
    };

    public ArrayList<Leave> getmLeaves() {
        return mLeaves;
    }

    public View.OnClickListener acceptListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LeaveAsks.doAccept(handler,mContext, (Leave) v.getTag());
        }
    };

    public View.OnClickListener refouseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LeaveAsks.doRefouse(handler,mContext, (Leave) v.getTag());
        }
    };
}
