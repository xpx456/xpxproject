package intersky.chat.presenter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.apputils.MenuItem;
import intersky.chat.ContactManager;
import intersky.chat.R;
import intersky.chat.handler.ContactsDetialHandler;
import intersky.chat.view.activity.ChatActivity;
import intersky.chat.view.activity.ContactsDetialActivity;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ContactsDetialPresenter implements Presenter {

    public ContactsDetialActivity mContactsDetialActivity;
    public ContactsDetialHandler mContactsDetialHandler;

    public ContactsDetialPresenter(ContactsDetialActivity mContactsDetialActivity) {
        this.mContactsDetialActivity = mContactsDetialActivity;
        this.mContactsDetialHandler = new ContactsDetialHandler(mContactsDetialActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mContactsDetialActivity.setContentView(R.layout.activity_contacts_detial);
        ToolBarHelper.setRightBtnText(mContactsDetialActivity.mActionBar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.creatDialogTowButtonEdit(mContactsDetialActivity,"",mContactsDetialActivity.getString(R.string.button_compliant),
                        mContactsDetialActivity.getString(R.string.button_word_cancle),mContactsDetialActivity.getString(R.string.button_word_ok),null,new EditDialogListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppUtils.showMessage(mContactsDetialActivity,mContactsDetialActivity.getString(R.string.complaint_mesage_receive));
                            }
                        },mContactsDetialActivity.getString(R.string.complaint_mesage_content));
            }
        }, mContactsDetialActivity.getString(R.string.button_compliant));
        mContactsDetialActivity.mUserName = (TextView) mContactsDetialActivity.findViewById(R.id.name);
        mContactsDetialActivity.mMobil = (TextView) mContactsDetialActivity.findViewById(R.id.phone);
        mContactsDetialActivity.mDepartMent = (TextView) mContactsDetialActivity.findViewById(R.id.departmentname);
        mContactsDetialActivity.mEmail = (TextView) mContactsDetialActivity.findViewById(R.id.mailname);
        mContactsDetialActivity.mTel = (ImageView) mContactsDetialActivity.findViewById(R.id.contact_tel);
        mContactsDetialActivity.mMsn = (ImageView) mContactsDetialActivity.findViewById(R.id.contact_mns);
        mContactsDetialActivity.mDuaty = (TextView) mContactsDetialActivity.findViewById(R.id.duatyname);
        mContactsDetialActivity.mFax = (TextView) mContactsDetialActivity.findViewById(R.id.faxname);
        mContactsDetialActivity.mMailSend = (TextView) mContactsDetialActivity.findViewById(R.id.mail);
        mContactsDetialActivity.mMessageSend = (TextView) mContactsDetialActivity.findViewById(R.id.message);
        mContactsDetialActivity.showContacts = mContactsDetialActivity.getIntent().getParcelableExtra("contacts");
        if (ContactManager.mContactManager.mAccount.isouter == true) {
            mContactsDetialActivity.mMailSend.setVisibility(View.INVISIBLE);
            mContactsDetialActivity.mMessageSend.setVisibility(View.INVISIBLE);
        }

        mContactsDetialActivity.mUserName.setText(mContactsDetialActivity.showContacts.getName());
        mContactsDetialActivity.mRelativeLayout = (RelativeLayout) mContactsDetialActivity.findViewById(R.id.shade);
        if (mContactsDetialActivity.showContacts.mMobile.length() > 0)
            mContactsDetialActivity.mMobil.setText(mContactsDetialActivity.showContacts.mMobile);
        else
            mContactsDetialActivity.mMobil.setText(mContactsDetialActivity.showContacts.mPhone);
        if (mContactsDetialActivity.mMobil.getText().length() == 0) {
            mContactsDetialActivity.mTel.setVisibility(View.INVISIBLE);

        }
        if (mContactsDetialActivity.showContacts.mMobile.length() == 0) {
            mContactsDetialActivity.mMsn.setVisibility(View.INVISIBLE);
        }


        mContactsDetialActivity.mDepartMent.setText(mContactsDetialActivity.showContacts.mDepartMent);
        mContactsDetialActivity.mEmail.setText(mContactsDetialActivity.showContacts.eMail);
        mContactsDetialActivity.mDuaty.setText(mContactsDetialActivity.showContacts.mPosition);
        mContactsDetialActivity.mFax.setText(mContactsDetialActivity.showContacts.mFax);
        mContactsDetialActivity.mTel.setOnClickListener(mContactsDetialActivity.mTelListener);
        mContactsDetialActivity.mMsn.setOnClickListener(mContactsDetialActivity.mMsnListener);
        if (mContactsDetialActivity.showContacts.mRecordid.equals(ContactManager.mContactManager.mAccount.mRecordId)) {
            mContactsDetialActivity.mMailSend.setVisibility(View.INVISIBLE);
            mContactsDetialActivity.mMessageSend.setVisibility(View.INVISIBLE);
        } else if (mContactsDetialActivity.showContacts.eMail.length() != 0 && mContactsDetialActivity.showContacts.islocal == false) {
            mContactsDetialActivity.mMailSend.setOnClickListener(mContactsDetialActivity.mMailListener);
            mContactsDetialActivity.mMessageSend.setOnClickListener(mContactsDetialActivity.mMessageListener);
        } else if (mContactsDetialActivity.showContacts.eMail.length() == 0 && mContactsDetialActivity.showContacts.islocal == false) {
            mContactsDetialActivity.mMailSend.setVisibility(View.INVISIBLE);
            mContactsDetialActivity.mMessageSend.setOnClickListener(mContactsDetialActivity.mMessageListener);
        } else if (mContactsDetialActivity.showContacts.eMail.length() != 0 && mContactsDetialActivity.showContacts.islocal == true) {
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mContactsDetialActivity.mMailSend.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.mailaddress);
            mContactsDetialActivity.mMailSend.setLayoutParams(mFajianLayerParams);
            mContactsDetialActivity.mMailSend.setOnClickListener(mContactsDetialActivity.mMailListener);
            mContactsDetialActivity.mMessageSend.setVisibility(View.INVISIBLE);
        } else {
            mContactsDetialActivity.mMailSend.setVisibility(View.INVISIBLE);
            mContactsDetialActivity.mMessageSend.setVisibility(View.INVISIBLE);
        }
        if (mContactsDetialActivity.showContacts.mPhone.length() > 0) {
            MenuItem menuItem = new MenuItem();
            menuItem.btnName = mContactsDetialActivity.showContacts.mPhone;
            menuItem.item = mContactsDetialActivity.showContacts.mPhone;
            menuItem.mListener = mContactsDetialActivity.doTelListener;
            mContactsDetialActivity.menuItems.add(menuItem);
        }
        if (mContactsDetialActivity.showContacts.mMobile.length() > 0) {
            MenuItem menuItem = new MenuItem();
            menuItem.btnName = mContactsDetialActivity.showContacts.mMobile;
            menuItem.item = mContactsDetialActivity.showContacts.mMobile;
            menuItem.mListener = mContactsDetialActivity.doTelListener;
            mContactsDetialActivity.menuItems.add(menuItem);
        }
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }


    public void startMail() {
//        Intent mIntent = new Intent();
//        mIntent.setClass(mContactsDetialActivity, MailEditActivity.class);
//        mIntent.putExtra("hasaddress", true);
//        mIntent.putExtra("mailaddress", MailManager.getInstance().mShowContactDetial.geteMail());
//        mIntent.putExtra("action", MailEditActivity.ACTION_NEW);
//        mContactsDetialActivity.startActivity(mIntent);
    }

    public void startChart() {


        if (mContactsDetialActivity.showContacts.mRecordid.equals(ContactManager.mContactManager.mAccount.mRecordId)) {
            AppUtils.showMessage(mContactsDetialActivity, mContactsDetialActivity.getString(R.string.contacts_sendtoself));
        } else {
            Intent intent = new Intent(mContactsDetialActivity, ChatActivity.class);
            intent.putExtra("contacts", mContactsDetialActivity.showContacts);
            mContactsDetialActivity.startActivity(intent);
        }


    }

    public void telMenu() {
        mContactsDetialActivity.popupWindow1 = AppUtils.creatButtomMenu(mContactsDetialActivity,  mContactsDetialActivity.mRelativeLayout,
                mContactsDetialActivity.menuItems, mContactsDetialActivity.findViewById(R.id.activity_contacts_detial));
    }

    public void doDimiss() {
        mContactsDetialActivity.popupWindow1.dismiss();
    }

    public void doTel(String number) {
        mContactsDetialActivity.telnumber = number;
        AppUtils.getPermission(Manifest.permission.CALL_PHONE, mContactsDetialActivity, ContactsDetialHandler.PERMISSION_CALL_PHONE_CONTACTS, mContactsDetialHandler);
    }

    public void tel() {
        if (mContactsDetialActivity.telnumber.length() > 0) {
            Uri uri = Uri.parse("del:" + mContactsDetialActivity.telnumber);
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            if (ActivityCompat.checkSelfPermission(mContactsDetialActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            mContactsDetialActivity.startActivity(intent);
        } else {
            AppUtils.showMessage(mContactsDetialActivity, "号码为空");
        }
    }

    public void doMsn() {

        AppUtils.getPermission(Manifest.permission.SEND_SMS, mContactsDetialActivity, ContactsDetialHandler.PERMISSION_SEND_SMS_CONTACTS, mContactsDetialHandler);
    }

    public void msn()
    {
        if (mContactsDetialActivity.mMobil.getText().toString().length() > 0) {
            Uri uri2 = Uri.parse("smsto:" + mContactsDetialActivity.mMobil.getText().toString());
            Intent intentMessage = new Intent(Intent.ACTION_VIEW, uri2);
            mContactsDetialActivity.startActivity(intentMessage);
        } else {
            AppUtils.showMessage(mContactsDetialActivity, "号码为空");
        }
    }
}
