package intersky.mail.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.entity.MailContact;

/**
 * Created by xpx on 2016/10/12.
 */

public class MailContactsAdapter extends BaseAdapter {

    public ArrayList<MailContact> mContacts;
    private Context mContext;
    private LayoutInflater mInflater;

    public MailContactsAdapter( Context mContext,ArrayList<MailContact> mContacts)
    {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MailContactsAdapter(Context mContext,ArrayList<MailContact> mContacts, boolean islocal)
    {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public MailContact getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        MailContact mContact = getItem(position);
        if(mContact.mType == Contacts.TYPE_PERSON)
        {
            convertView = mInflater.inflate(R.layout.mail_contact_item, null);
            TextView mname;
            TextView mhead;
            TextView msub;
            mhead = (TextView) convertView.findViewById(R.id.contact_img);
            mname = (TextView) convertView.findViewById(R.id.conversation_title);
            MailManager.setContactCycleHead(mhead,mContact);
            mname.setText(mContact.getName());
//            if(!mContact.mDepartMent.equals("(Root)"))
//            msub.setText(mContact.mDepartMent);
            if(position != mContacts.size()-1)
            {
                if(mContacts.get(position+1).mType != Contacts.TYPE_PERSON)
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
        }
        else if(mContact.mType == Contacts.TYPE_CLASS)
        {
            convertView = mInflater.inflate(R.layout.contact_item_class, null);
            TextView mname;
            ImageView mhead;
            mhead = (ImageView) convertView.findViewById(R.id.contact_img);
            mname = (TextView) convertView.findViewById(R.id.conversation_title);
            if(mContact.mRecordid.equals("baseroot"))
            {
                mhead.setImageResource(R.drawable.contact_icon_dept_1);
                ImageView array = (ImageView) convertView.findViewById(R.id.contact_array);
                array.setVisibility(View.VISIBLE);
                mname.setText(mContact.getName());
            }
            else if(mContact.mRecordid.equals("localroot"))
            {
                mhead.setImageResource(R.drawable.contact_icon_local);
                ImageView array = (ImageView) convertView.findViewById(R.id.contact_array);
                array.setVisibility(View.VISIBLE);
                mname.setText(mContact.getName());
            }
            else
            {
                mhead.setImageResource(R.drawable.contact_icon_wfp);
//                mname.setText(mContact.getName()+"("+ String.valueOf(mContact.mPersonCount)+")");
            }
            if(position != mContacts.size()-1)
            {
                RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
                if(mContacts.get(position+1).mType != Contacts.TYPE_CLASS)
                {
                    line.setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
                line.setVisibility(View.INVISIBLE);

            }
        }
        else
        {
            convertView = mInflater.inflate(R.layout.contact_item_letter, null);
            TextView mName = (TextView) convertView.findViewById(R.id.item_letter_name);
            mName.setText(mContact.getName());
        }


        return convertView;
    }



}