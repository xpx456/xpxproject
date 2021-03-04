package intersky.mail.entity;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.mail.R;

public class Organization {

    public MailContact organizationMailContact = new MailContact();
    public MailContact phoneMailContact;
    public ArrayList<MailContact> allClassMailContact = new ArrayList<MailContact>();
    public ArrayList<MailContact> allMailContact = new ArrayList<MailContact>();
    public ArrayList<MailContact> allMailContactHead = new ArrayList<MailContact>();
    public HashMap<String,MailContact> mAllMailContactMap = new HashMap<String,MailContact>();
    public HashMap<String,MailContact> mAllMailContactDepartMap = new HashMap<String,MailContact>();
    public ArrayList<MailContact> underLineClassMailContact = new ArrayList<MailContact>();
    public ArrayList<MailContact> underLineMailContact = new ArrayList<MailContact>();
    public ArrayList<MailContact> underLineMailContactHead = new ArrayList<MailContact>();
    public ArrayList<MailContact> mlocked = new ArrayList<MailContact>();
    public ArrayList<MailContact> mselectitems = new ArrayList<MailContact>();
    public MailContact showMailContact;
    public boolean[] typebooleans3 = new boolean[27];
    public boolean[] typebooleans4 = new boolean[27];

    public void clean()
    {
        organizationMailContact = new MailContact();
        allClassMailContact.clear();
        allMailContactHead.clear();
        allMailContact.clear();
        mAllMailContactMap.clear();
        mAllMailContactDepartMap.clear();
        underLineClassMailContact.clear();
        underLineMailContact.clear();
        underLineMailContactHead.clear();
        mlocked.clear();
        mselectitems.clear();
        typebooleans3 = new boolean[27];
        typebooleans4 = new boolean[27];
    }

    public MailContact getContact(String recordid, Context context)
    {
        if (mAllMailContactMap == null)
        {
            MailContact mMailContact = new MailContact();
            mMailContact.setName(context.getString(R.string.unknow));
            mMailContact.mRecordid = recordid;
            return mMailContact;
        }

        if(mAllMailContactMap.containsKey(recordid))
        {
            return mAllMailContactMap.get(recordid);
        }
        else
        {
            MailContact mMailContact = new MailContact();
            mMailContact.setName("未知");
            mMailContact.mRecordid = recordid;
            return mMailContact;
        }

    }

    public String getContactName(String recordid,Context context)
    {
        if(mAllMailContactMap.containsKey(recordid))
        {
            return mAllMailContactMap.get(recordid).getmRName();
        }
        else
        {
            MailContact mMailContact = new MailContact();
            mMailContact.setName(context.getString(R.string.unknow));
            mMailContact.mRecordid = recordid;
            return mMailContact.mName;
        }
    }

    public String getContactId(String name)
    {
        for(int i = 0 ; i < allMailContact.size() ; i++)
        {
            if(allMailContact.get(i).mName.equals(name))
            {
                return allMailContact.get(i).mName;
            }
        }
        return "";

    }
}
