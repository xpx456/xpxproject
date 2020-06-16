package intersky.chat;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.entity.Contacts;

public class Organization {

    public Contacts organizationContacts = new Contacts();
    public Contacts phoneContacts;
    public ArrayList<Contacts> allClassContacts = new ArrayList<Contacts>();
    public ArrayList<Contacts> allContacts = new ArrayList<Contacts>();
    public ArrayList<Contacts> allContactsHead = new ArrayList<Contacts>();
    public HashMap<String,Contacts> mAllContactsMap = new HashMap<String,Contacts>();
    public ArrayList<Contacts> underLineClassContacts = new ArrayList<Contacts>();
    public ArrayList<Contacts> underLineContacts = new ArrayList<Contacts>();
    public ArrayList<Contacts> underLineContactsHead = new ArrayList<Contacts>();
    public ArrayList<Contacts> mlocked = new ArrayList<Contacts>();
    public ArrayList<Contacts> mselectitems = new ArrayList<Contacts>();
    public Contacts showContacts;
    public boolean[] typebooleans3 = new boolean[27];
    public boolean[] typebooleans4 = new boolean[27];

    public void clean()
    {
        organizationContacts = new Contacts();
        allClassContacts.clear();
        allContactsHead.clear();
        allContacts.clear();
        mAllContactsMap.clear();
        underLineClassContacts.clear();
        underLineContacts.clear();
        underLineContactsHead.clear();
        mlocked.clear();
        mselectitems.clear();
        typebooleans3 = new boolean[27];
        typebooleans4 = new boolean[27];
    }

    public Contacts getContact(String recordid)
    {
        if (mAllContactsMap == null)
        {
            Contacts mContacts = new Contacts();
            mContacts.setName("未知");
            mContacts.mRecordid = recordid;
            return mContacts;
        }

        if(mAllContactsMap.containsKey(recordid))
        {
            return mAllContactsMap.get(recordid);
        }
        else
        {
            Contacts mContacts = new Contacts();
            mContacts.setName("未知");
            mContacts.mRecordid = recordid;
            return mContacts;
        }

    }

    public String getContactName(String recordid)
    {
        if(mAllContactsMap.containsKey(recordid))
        {
            return mAllContactsMap.get(recordid).mName;
        }
        else
        {
            Contacts mContacts = new Contacts();
            mContacts.setName("未知");
            mContacts.mRecordid = recordid;
            return mContacts.mName;
        }
    }

    public String getContactId(String name)
    {
        for(int i = 0 ; i < allContacts.size() ; i++)
        {
            if(allContacts.get(i).mName.equals(name))
            {
                return allContacts.get(i).mName;
            }
        }
        return "";

    }
}
