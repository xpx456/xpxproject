package intersky.notice.view.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.StringUtils;
import intersky.notice.R;
import intersky.notice.entity.Notice;

/**
 * Created by xpx on 2016/10/12.
 */

public class NoticeAdapter extends BaseAdapter {

    private ArrayList<Notice> mNotices;
    private Context mContext;
    private LayoutInflater mInflater;
    public int nowpage = 1;
    public int totalCount = -1;
    public int endPage = 1;
    public NoticeAdapter(ArrayList<Notice> mNotices, Context mContext) {
        this.mContext = mContext;
        this.mNotices = mNotices;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mNotices.size();
    }

    @Override
    public Notice getItem(int position) {
        return mNotices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Notice mConversation = getItem(position);

        ViewHoder mview;
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.notice_list_item, null);
            mview = new ViewHoder();
            mview.mtitle = (TextView) convertView.findViewById(R.id.conversation_title);
            mview.msubject = (TextView) convertView.findViewById(R.id.conversation_content);
            mview.mtime = (TextView) convertView.findViewById(R.id.conversation_dete);
            mview.mhead = (TextView) convertView.findViewById(R.id.contact_img);
            convertView.setTag(mview);
        }
        else
        {
            mview = (ViewHoder) convertView.getTag();
        }
        mview.mtitle.setText(mConversation.title);
        mview.msubject.setText(StringUtils.htmlToString(mConversation.content));
        mview.mtime.setText(mConversation.create_time+"  "+mContext.getString(R.string.keyword_public_name)+ mConversation.username);
        AppUtils.setContactCycleHead(mview.mhead,mConversation.username);
        return convertView;
    }

    public class ViewHoder {
        TextView mtitle;
        TextView msubject;
        TextView mtime;
        TextView mhead;
    }



    public ArrayList<Notice> getList() {
        return mNotices;
    }
}
