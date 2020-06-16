package intersky.chat;

import org.json.JSONException;

import java.util.Collections;

import intersky.appbase.entity.Contacts;
import intersky.apputils.CharacterParser;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;

public class ContactsPrase {

    public static int praseContacts(XpxJSONObject xpxJsonObject, Contacts mContactsClass) {
        int sum = 0;
        try {
            XpxJSONArray ja = xpxJsonObject.getJSONArray("orgs");
            XpxJSONArray ja2 = xpxJsonObject.getJSONArray("users");
            String name = xpxJsonObject.getString("name");
            if (name.equals("(Root)")) {
                name = "我的公司";
            }
            Contacts mContacts = new Contacts();
            mContacts.setName(name);
            mContacts.mId = xpxJsonObject.getString("orgid");
            mContacts.mRecordid = xpxJsonObject.getString("orgid");
            mContacts.mType = Contacts.TYPE_CLASS;
            mContactsClass.mContacts.add(mContacts);

            for (int i = 0; i < ja.length(); i++) {

                XpxJSONObject jo = (XpxJSONObject) ja.getJSONObject(i);
                int count = praseContacts(jo, mContacts);
                sum += count;
            }


            for (int i = 0; i < ja2.length(); i++) {
                XpxJSONObject jo22 = ja2.getJSONObject(i);
                Contacts mContacts2;
                String RecordID = "";
                RecordID = jo22.getString("userid");
                XpxJSONObject jo2 = getUserinfo(RecordID);
                if (jo2 == null) {
                    jo2 = jo22;
                }
                String ismale = "未知";
                if (jo2.has("sex")) {
                    if (jo2.getInt("sex",0) == 0) {
                        ismale = "男";
                    } else if (jo2.getInt("sex",0) == 1) {
                        ismale = "女";
                    }
                }
                String id = "";
                String text = "";
                String Phone = "";
                String Mobile = "";
                String Fax = "";
                String Email = "";
                String position = "";
                id = jo2.getString("username");
                if(id.length() == 0)
                {
                    id = "未知";
                }
                if (jo2.getString("realname") != null) {
                    if (jo2.getString("realname").length() > 0) {
                        text = jo2.getString("realname");
                    } else
                        text = id;
                } else
                    text = id;

                Phone = jo2.getString("phone");
                Mobile = jo2.getString("mobile");
                Fax = jo2.getString("fax");
                Email = jo2.getString("email");
                position = jo2.getString("position");

                mContacts2 = new Contacts();
                mContacts2.setName(id);
                mContacts2.mId = id;
                mContacts2.mRName = text;
                mContacts2.mPosition = position;
                mContacts2.mPhone = Phone;
                mContacts2.mMobile = Mobile;
                mContacts2.mFax = Fax;
                mContacts2.eMail = Email;
                mContacts2.mDepartMent = mContacts.getName();
                mContacts2.mRecordid = RecordID;
                mContacts2.mSex = ismale;
                ContactManager.mContactManager.mOrganization.allContacts.add(mContacts2);
                ContactManager.mContactManager.mOrganization.mAllContactsMap.put(mContacts2.mRecordid,mContacts2);
                mContacts.mMyContacts.add(mContacts2);

                String s = mContacts2.getmPingyin().substring(0, 1).toUpperCase();
                int pos = CharacterParser.typeLetter.indexOf(s);
                if (pos != -1) {
                    if (mContacts.typebooleans[pos] == false) {
                        mContacts.mHeadContacts.add(new Contacts(s));
                        mContacts.typebooleans[pos] = true;
                    }
                }
                else
                {
                    s = "#";
                    pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (mContacts.typebooleans[pos] == false) {
                            mContacts.mHeadContacts.add(new Contacts(s));
                            mContacts.typebooleans[pos] = true;
                        }
                    }
                }
                sum++;

            }
            mContacts.mMyContacts.addAll(0, mContacts.mHeadContacts);
            Collections.sort(mContacts.mMyContacts, new SortContactComparator());
            Collections.sort(mContacts.mHeadContacts, new SortContactComparator());
            mContacts.mContacts.addAll(mContacts.mMyContacts);
            mContacts.mPersonCount = sum;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sum;
    }

    public static XpxJSONObject getUserinfo(String id) {

        XpxJSONObject jo = null;
        try {
            for (int i = 0; i < ContactManager.mContactManager.mUsers.length(); i++) {
                jo = ContactManager.mContactManager.mUsers.getJSONObject(i);
                if (jo.getString("userid").equals(id)) {
                    return jo;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
