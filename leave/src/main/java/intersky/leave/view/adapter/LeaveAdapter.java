package intersky.leave.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.StringUtils;
import intersky.leave.R;
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
    public LeaveAdapter(ArrayList<Leave> mLeaves, Context mContext,int page) {
        this.mContext = mContext;
        this.mLeaves = mLeaves;
        this.page = page;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            mview.mtitle2 = (TextView) convertView.findViewById(R.id.conversation_title2);
            mview.imageView = (ImageView) convertView.findViewById(R.id.conversation_img);
            mview.msubject = (TextView) convertView.findViewById(R.id.conversation_subject);
            mview.content = (TextView) convertView.findViewById(R.id.content);
            mview.state = (ImageView) convertView.findViewById(R.id.state_img);
            mview.hit = (TextView) convertView.findViewById(R.id.hit);
//            mview.layerline = (RelativeLayout) convertView.findViewById(R.id.line);
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
//        if(position == mLeaves.size()-1)
//        {
//            mview.layerline.setVisibility(View.INVISIBLE);
//        }
//        else
//        {
//            mview.layerline.setVisibility(View.VISIBLE);
//        }
        mview.mtitle.setText(mLeave.name);
        mview.mtitle2.setText("["+mContext.getString(R.string.xml_leave_type)+"]");
        mview.msubject.setText(mLeave.create_time);
//        mview.msubject.setText(StringUtils.delHTMLTag(mLeave.content));
        mview.msubject.setText(StringUtils.htmlToString(mLeave.content));
        switch (Integer.valueOf(mLeave.is_approval))
        {
            case 0:
                mview.state.setImageResource(R.drawable.approve0);
                break;
            case 1:
                mview.state.setImageResource(R.drawable.approve1);
                break;
            case 2:
                mview.state.setImageResource(R.drawable.approve2);
                break;
            case 3:
                mview.state.setImageResource(R.drawable.approve3);
                break;
        }
        return convertView;
    }

    public   class ViewHoder {
        ImageView imageView;
        TextView mtitle;
        TextView mtitle2;
        TextView msubject;
        TextView content;
        TextView hit;
//        RelativeLayout layerline;
        ImageView state;
    };

    public ArrayList<Leave> getmLeaves() {
        return mLeaves;
    }
}
