package intersky.function.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.BaseActivity;
import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.apputils.TimeUtils;
import intersky.function.FunctionUtils;
import intersky.function.R;
import intersky.function.asks.FunAsks;
import intersky.function.entity.BusinessCardModel;
import intersky.function.entity.Function;
import intersky.function.handler.BusinesHandler;
import intersky.function.view.activity.BusinesCardActivity;
import intersky.function.view.adapter.BusinesAdapter;
import intersky.mywidget.SearchViewLayout;
import xpx.com.toolbar.utils.ToolBarHelper;

@TargetApi(23)
@SuppressLint("DefaultLocale")
public class BusinesCardPresenter implements Presenter {

    private BusinesCardActivity mBusinesCardActivity;
    public BusinesHandler mBusinesHandler;

    public BusinesCardPresenter(BusinesCardActivity mBusinesCardActivity) {
        this.mBusinesCardActivity = mBusinesCardActivity;
        this.mBusinesHandler = new BusinesHandler(mBusinesCardActivity);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mBusinesCardActivity.setContentView(R.layout.activity_businescard);
//		Cache.getInstance(this.getApplicationContext().getFilesDir()
//				.getAbsolutePath(), this);

        ImageView back = mBusinesCardActivity.findViewById(R.id.back1);
        back.setOnClickListener(mBusinesCardActivity.mBackListener);
        TextView save = mBusinesCardActivity.findViewById(R.id.save);
        save.setOnClickListener(mBusinesCardActivity.mUpLoadBusines);

        mBusinesCardActivity.mListView = (ListView) mBusinesCardActivity.findViewById(R.id.busines_List);
        mBusinesCardActivity.searchViewLayout = (SearchViewLayout) mBusinesCardActivity.findViewById(R.id.search);

        initData();
        mBusinesCardActivity.mListView.setOnItemClickListener(mBusinesCardActivity.mOnItemClickListener);
        mBusinesCardActivity.searchViewLayout.setDotextChange(mBusinesCardActivity.doTextChange);
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        mBusinesHandler = null;
    }

    private void initData() {
        mBusinesCardActivity.mBusinesAdapter = new BusinesAdapter(mBusinesCardActivity, mBusinesCardActivity.mtBusinesItems);
        mBusinesCardActivity.waitDialog.show();
        mBusinesCardActivity.mBusinesItems.clear();
        getPermission();
    }

    public void getContacts() {
        new Thread() {

            @Override
            public void run() {
                testGetContact(mBusinesCardActivity, mBusinesCardActivity.mBusinesItems);
                if (mBusinesHandler != null)
                    mBusinesHandler.sendEmptyMessage(BusinesHandler.EVENT_SET_DATA);
            }
        }.start();
    }

    private void getPermission() {
        if (VERSION.RELEASE.equals("6.0")) {
            AppUtils.getPermission(Manifest.permission.READ_CONTACTS, mBusinesCardActivity, BusinesHandler.REQUEST_CODE_ASK_PERMISSIONS, mBusinesHandler);
        } else {
            getContacts();
        }

    }

    public void initlistdata() {
        mBusinesCardActivity.mtBusinesItems.clear();
        if (mBusinesCardActivity.searchViewLayout.getText().toString().length() != 0) {
            for (int i = 0; i < mBusinesCardActivity.mBusinesItems.size(); i++) {
                if (mBusinesCardActivity.mBusinesItems.get(i).name.contains(mBusinesCardActivity.searchViewLayout.getText().toString().toLowerCase()) ||
                        mBusinesCardActivity.mBusinesItems.get(i).getPingyin().contains(mBusinesCardActivity.searchViewLayout.getText().toString().toLowerCase())) {
                    mBusinesCardActivity.mtBusinesItems.add(mBusinesCardActivity.mBusinesItems.get(i));
                }
            }
        } else {
            mBusinesCardActivity.mtBusinesItems.addAll(mBusinesCardActivity.mBusinesItems);
        }
    }





    public void upLoadBusines() {
        ArrayList<BusinessCardModel> maddMkxCards = new ArrayList<BusinessCardModel>();
        for (int i = 0; i < mBusinesCardActivity.mBusinesItems.size(); i++) {
            if (mBusinesCardActivity.mBusinesItems.get(i).isselect == true) {
                maddMkxCards.add(mBusinesCardActivity.mBusinesItems.get(i));
            }
        }
        if (maddMkxCards.size() > 0) {
            FunAsks.sendtoServer(mBusinesCardActivity,mBusinesHandler,maddMkxCards);
        } else {
            AppUtils.showMessage(mBusinesCardActivity, mBusinesCardActivity.getString(R.string.select_businesscard));
        }
    }

    public void itemClick(int position) {
        if (mBusinesCardActivity.mBusinesItems.get(position).type == 0) {
            if (mBusinesCardActivity.mBusinesItems.get(position).isselect == true) {
                mBusinesCardActivity.mBusinesItems.get(position).isselect = false;
            } else {
                mBusinesCardActivity.mBusinesItems.get(position).isselect = true;
            }
        }
        if (mBusinesHandler != null)
            mBusinesHandler.sendEmptyMessage(BusinesHandler.EVENT_UPDATA_DATA);
    }
    


    public void testGetContact(Context mContext, ArrayList<BusinessCardModel> mBusinesItems)
    {
//        ArrayList<Contacts> contacts = new ArrayList<Contacts>();
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext())
        {
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            if(name != null)
            {
                BusinessCardModel contact = new BusinessCardModel();
                contact.name = name;
                contact.setneame(name);

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
                                if(contact.mobile1.length() == 0 && phone != null)
                                    contact.mobile1 = phone;
                                else if(contact.mobile2.length() == 0 && phone != null)
                                {
                                    contact.mobile2 = phone;
                                }
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                                if(contact.fax.length()== 0 && phone != null)
                                    contact.fax = phone;
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                if(contact.tel1.length()== 0 && phone != null)
                                    contact.tel1 = phone;
                                else if(contact.tel2.length() == 0 && phone != null)
                                {
                                    contact.tel2 = phone;
                                }
                                break;
                        }
                        if(contact.mobile1.length()> 0 && contact.fax.length()> 0 && contact.tel1.length()> 0
                                && contact.mobile2.length()> 0&& contact.tel2.length()> 0) {
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
                        if (contact.email.length() == 0 && email != null)
                        {
                            contact.email = email;
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

                        if (contact.department.length() == 0 && org2 != null) {
                            contact.department = org2;
                        } else {
                            break;
                        }
                    }
                    orgs.close();
                }

                mBusinesItems.add(contact);
            }
        }

    }
}
