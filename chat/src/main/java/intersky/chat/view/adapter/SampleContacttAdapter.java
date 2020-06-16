package intersky.chat.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.chat.ContactManager;
import intersky.chat.R;

/**
 * Created by xpx on 2016/10/12.
 */

public class SampleContacttAdapter extends BaseAdapter {

    private ArrayList<Contacts> mContacts;
    private Context mContext;
    private LayoutInflater mInflater;

    public SampleContacttAdapter(ArrayList<Contacts> mContacts, Context mContext) {
        this.mContext = mContext;
        this.mContacts = mContacts;
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
        ViewHoder mview = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sample_contact_item, null);
            mview = new ViewHoder();
            mview.mhead = (TextView) convertView.findViewById(R.id.contact_img);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        TextView mhead;
        mhead = mview.mhead;
        ContactManager.setContactCycleHead(mhead,mContact);
        return convertView;
    }


    public ArrayList<Contacts> getmContacts() {
        return mContacts;
    }

    public class ViewHoder {
        TextView mhead;

    }


}