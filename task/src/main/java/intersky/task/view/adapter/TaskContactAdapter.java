package intersky.task.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.task.R;

/**
 * Created by xpx on 2016/10/12.
 */

public class TaskContactAdapter extends BaseAdapter {

    public static final int EVENT_START_MORE = 2634100;

    public ArrayList<Contacts> mContacts;
    public Context mContext;
    public LayoutInflater mInflater;
    public Handler mHandler;
    public int leavetype;

    public TaskContactAdapter(ArrayList<Contacts> mContacts, Context mContext, Handler mHandler
            , int leavetype)
    {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.mHandler = mHandler;
        this.leavetype = leavetype;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Contacts getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Contacts mContact = getItem(position);
        if(mContact.mType == 0)
        {
            convertView = mInflater.inflate(R.layout.contact_task_item, null);
            TextView mname;
            TextView mhead;
            ImageView image;
            mhead = (TextView) convertView.findViewById(R.id.contact_img);
            mname = (TextView) convertView.findViewById(R.id.conversation_title);
            image = (ImageView) convertView.findViewById(R.id.more);
            AppUtils.setContactCycleHead(mhead,mContact.getName(),mContact.colorhead);
            mname.setText(mContact.getName());
            if(position != mContacts.size()-1)
            {
                if(mContacts.get(position+1).mType != 0)
                {
                    RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
                    line.setVisibility(View.INVISIBLE);

                }

            }
            else
            {
                RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
                line.setVisibility(View.INVISIBLE);

            }
            image.setTag(mContact);
            image.setOnClickListener(mclickMore);
            if(this.leavetype <3)
            {
                image.setVisibility(View.VISIBLE);
            }
            else
            {
                image.setVisibility(View.INVISIBLE);
            }

            if(mContact.isapply == true)
            {
                if(this.leavetype == 1)
                {
                    image.setVisibility(View.VISIBLE);
                }
                else
                {
                    image.setVisibility(View.INVISIBLE);
                }
            }
        }

        else
        {
            convertView = mInflater.inflate(R.layout.mail_contact_letter_item, null);
            TextView mName = (TextView) convertView.findViewById(R.id.item_letter_name);
            mName.setText(mContact.getName());
        }


        return convertView;
    }

    public View.OnClickListener mclickMore = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Message msg = new Message();
            msg.what = EVENT_START_MORE;
            msg.obj = v.getTag();
            mHandler.sendMessage(msg);

        }
    };
}