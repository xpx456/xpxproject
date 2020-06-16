package intersky.chat.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.chat.ContactManager;
import intersky.appbase.entity.Contacts;
import intersky.chat.R;

/**
 * Created by xpx on 2016/10/12.
 */

public class ContactSelectAdapter extends BaseAdapter {

    private ArrayList<Contacts> mContacts;
    private Context mContext;
    private LayoutInflater mInflater;
    private int personcount;

    public ContactSelectAdapter(ArrayList<Contacts> mContacts, Context mContext)
    {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initPersonCount();
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
            convertView = mInflater.inflate(R.layout.contact_select_item, null);
            TextView mname;
            TextView mhead;
            TextView msub;
            ImageView tel;
            ImageView select;
            mhead = (TextView) convertView.findViewById(R.id.contact_img);
            mname = (TextView) convertView.findViewById(R.id.conversation_title);
            msub = (TextView) convertView.findViewById(R.id.conversation_sub_title);
            select = (ImageView) convertView.findViewById(R.id.select);
            String s = "";
            ContactManager.setContactCycleHead(mhead,mContact);
            mname.setText(mContact.mName);
            msub.setText(mContact.mDepartMent);
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
            if(mContact.isselect == true)
            {
                select.setImageResource(R.drawable.mailpersonselect);
            }
            else
            {
                select.setImageResource(R.drawable.bunselect);
            }

            if(chemckLock(mContact))
            {
                select.setVisibility(View.INVISIBLE);
            }
            else
            {
                select.setVisibility(View.VISIBLE);
            }
        }
        else if(mContact.mType == 2)
        {
            convertView = mInflater.inflate(R.layout.contact_item_class, null);
            TextView mname;
            ImageView mhead;
            mhead = (ImageView) convertView.findViewById(R.id.contact_img);
            mname = (TextView) convertView.findViewById(R.id.conversation_title);
            if(mContact.mId.equals("baseroot"))
            {
                mhead.setImageResource(R.drawable.contact_icon_dept_1);
                mname.setText(mContact.mName);
            }
            else if(mContact.mId.equals("localroot"))
            {
                mhead.setImageResource(R.drawable.contact_icon_local);
                mname.setText(mContact.mName);
            }
            else
            {
                mhead.setImageResource(R.drawable.contact_icon_wfp);
                mname.setText(mContact.mName+"("+ String.valueOf(mContact.mPersonCount)+")");
            }
            if(position != mContacts.size()-1)
            {
                RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
                if(mContacts.get(position+1).mType != 2)
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
        else if(mContact.mType == 3)
        {
            convertView = mInflater.inflate(R.layout.contact_item_top, null);
            TextView mname;
            ImageView select;
            RelativeLayout mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.conversationlayer);
            select = (ImageView) convertView.findViewById(R.id.contact_img);
            mname = (TextView) convertView.findViewById(R.id.conversation_title);
            if(mContact.isselect == true)
            {
                select.setImageResource(R.drawable.mailpersonselect);
                mRelativeLayout.setBackgroundColor(Color.rgb(233,240,250));
            }
            else
            {
                select.setImageResource(R.drawable.bunselect);
                mRelativeLayout.setBackgroundColor(Color.rgb(255,255,255));
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


    public boolean chemckLock(Contacts mContact )
    {
        for(int i = 0 ; i < ContactManager.mContactManager.mOrganization.mlocked.size() ; i++)
        {
            if(mContact.mRecordid.equals( ContactManager.mContactManager.mOrganization.mlocked.get(i).mRecordid))
            {
                return true;
            }
        }
        return  false;
    }

    public ArrayList<Contacts> getmContacts() {
        return mContacts;
    }

    public void setmContacts(ArrayList<Contacts> mContacts) {
        this.mContacts = mContacts;
        initPersonCount();
    }

    public int getPersoncount() {
        return personcount;
    }

    private void initPersonCount()
    {
        personcount = 0;
        for(int i =0 ; i < mContacts.size() ; i++)
        {
            if(mContacts.get(i).mType  == 0 )
                personcount++;
        }
    }
}