package intersky.chat;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.entity.Contacts;
import intersky.apputils.CharacterParser;

public class PhoneContactsThread extends Thread {

    public Handler mHandler;
    public Context mContext;
    public int eventCode;

    public PhoneContactsThread(Handler mHandler, Context mContext, int eventCode) {
        this.mHandler = mHandler;
        this.mContext = mContext;
        this.eventCode = eventCode;
    }

    @Override
    public void run() {
        Message msg = new Message();
        msg.what = eventCode;
        msg.obj = testGetContact2(mContext);
        if (mHandler != null)
            mHandler.sendMessage(msg);
    }


    public Contacts testGetContact2(Context mContext)
    {
//        ArrayList<Contacts> contacts = new ArrayList<Contacts>();
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        Contacts phoneContacts = new Contacts();
        phoneContacts.mId = "localroot";
        phoneContacts.mType = Contacts.TYPE_CLASS;
        phoneContacts.setName( mContext.getString(R.string.contacts_phone));

        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                String contactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Contacts contact = new Contacts();
                if(name != null)
                {
                    contact.mId = name;
                    contact.setName(name);
                    Cursor phones = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                    + contactId, null, null);
                    if(phones != null) {
                        while (phones.moveToNext())
                        {
                            String phone = phones.getString(phones.getColumnIndex("data1"));
                            int type = phones.getInt(phones.getColumnIndex("data2"));
                            switch (type)
                            {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    if(contact.mMobile.length() == 0 && phone != null)
                                        contact.mPhone = phone;
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                                    if(contact.mFax.length()== 0 && phone != null)
                                        contact.mFax = phone;
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    if(contact.mPhone.length()== 0 && phone != null)
                                        contact.mPhone = phone;
                                    break;
                            }
                            if(contact.mMobile.length()> 0 && contact.mFax.length()> 0 && contact.mPhone.length()> 0) {
                                break;
                            }
                        }
                    }
                    Cursor emails = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
                                    + contactId, null, null);
                    if(emails != null)
                    {
                        while (emails.moveToNext())
                        {
                            String email = emails.getString(emails.getColumnIndex("data1"));
                            if (contact.eMail.length() == 0 && email != null)
                            {
                                contact.eMail = email;
                            }
                            else
                            {
                                break;
                            }
                        }
                        emails.close();
                    }
                    Cursor orgs = contentResolver
                            .query(ContactsContract.Data.CONTENT_URI,
                                    new String[]
                                            {
                                                    ContactsContract.CommonDataKinds.Organization.COMPANY,
                                                    ContactsContract.CommonDataKinds.Organization.TITLE },
                                    ContactsContract.CommonDataKinds.Organization.CONTACT_ID
                                            + " = "
                                            + contactId
                                            + " AND "
                                            + ContactsContract.Data.MIMETYPE
                                            + "='"
                                            + ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                                            + "'", null, null);
                    if(orgs != null) {
                        while (orgs.moveToNext()) {
                            String org2 = orgs
                                    .getString(orgs
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

                            if (contact.mDepartMent.length() == 0 && org2 != null) {
                                contact.mDepartMent = org2;
                            } else {
                                break;
                            }
                        }
                        orgs.close();
                    }
                    contact.islocal  = true;
                    phoneContacts.mMyContacts.add(contact);
                    String s = contact.getmPingyin().substring(0, 1).toUpperCase();
                    int pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (phoneContacts.typebooleans[pos] == false) {
                            phoneContacts.mHeadContacts.add(new Contacts(s));
                            phoneContacts.typebooleans[pos] = true;
                        }
                    }
                    else
                    {
                        s = "#";
                        pos = CharacterParser.typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (phoneContacts.typebooleans[pos] == false) {
                                phoneContacts.mHeadContacts.add(new Contacts(s));
                                phoneContacts.typebooleans[pos] = true;
                            }
                        }
                    }
                }
            }
        }
        phoneContacts.mMyContacts.addAll(0, phoneContacts.mHeadContacts);
        Collections.sort(phoneContacts.mMyContacts, new SortContactComparator());
        Collections.sort(phoneContacts.mHeadContacts, new SortContactComparator());
        phoneContacts.mContacts.addAll(phoneContacts.mMyContacts);
        return phoneContacts;
    }
}
