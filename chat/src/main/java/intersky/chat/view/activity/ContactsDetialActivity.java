package intersky.chat.view.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.apputils.MenuItem;
import intersky.chat.handler.ContactsDetialHandler;
import intersky.chat.presenter.ContactsDetialPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ContactsDetialActivity extends BaseActivity {
    public TextView mUserName;
    public TextView mMobil;
    public ImageView mTel;
    public ImageView mMsn;
    public TextView mDepartMent;
    public TextView mEmail;
    public TextView mDuaty;
    public TextView mFax;
    public TextView mMailSend;
    public TextView mMessageSend;
    public PopupWindow popupWindow1;
    public RelativeLayout mRelativeLayout;
    public ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
    public Contacts showContacts;
    public ContactsDetialPresenter mContactsDetialPresenter = new ContactsDetialPresenter(this);
    public String telnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mContactsDetialPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mTelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactsDetialPresenter.telMenu();
        }
    };

    public View.OnClickListener mMsnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactsDetialPresenter.doMsn();
        }
    };

    public View.OnClickListener mMailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactsDetialPresenter.startMail();
        }
    };

    public View.OnClickListener mMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactsDetialPresenter.startChart();
        }
    };

    public View.OnClickListener doTelListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mContactsDetialPresenter.doTel((String) v.getTag());
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ContactsDetialHandler.PERMISSION_CALL_PHONE_CONTACTS:
                if(grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // MyPermission Granted
                        mContactsDetialPresenter.tel();
                    }
                }
                break;
            case ContactsDetialHandler.PERMISSION_SEND_SMS_CONTACTS:
                if(grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // MyPermission Granted
                        mContactsDetialPresenter.msn();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
