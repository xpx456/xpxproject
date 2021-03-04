package com.intersky.android.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intersky.R;
import com.intersky.android.presenter.MainPresenter;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.MainActivity;

import java.util.ArrayList;

import intersky.appbase.BaseFragment;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.document.DocumentManager;
import intersky.function.FunctionUtils;
import intersky.function.entity.Function;
import intersky.function.view.activity.WebMessageActivity;
import intersky.mail.MailManager;
import intersky.mywidget.MyGridView;
import intersky.task.TaskManager;
import intersky.xpxnet.net.NetUtils;

public class WorkFragment extends BaseFragment {

    public MainActivity mMainActivity;
    public RelativeLayout mMail;
    public RelativeLayout mTask;
    public RelativeLayout mSmart;
    public RelativeLayout mDocument;
    public LinearLayout mFungrids;
    public TextView taskHit;
    public TextView mailHit;
    public TextView docHit;
    public ImageView image2;
    public ImageView image3;
    public TextView text2;
    public TextView text3;
    public ArrayList<MyGridView> mGridViews = new ArrayList<MyGridView>();
    public WorkFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_work, container, false);
        mMail = (RelativeLayout) mView.findViewById(R.id.mail);
        mTask = (RelativeLayout) mView.findViewById(R.id.task);
        image2 = mView.findViewById(R.id.mail_image);
        image3 = mView.findViewById(R.id.document_image);
        text2 = mView.findViewById(R.id.mail_text);
        text3 = mView.findViewById(R.id.document_text);
        mSmart = mView.findViewById(R.id.smart);
        taskHit = (TextView) mView.findViewById(R.id.taskhit);
        mailHit = (TextView) mView.findViewById(R.id.mailhit);
        docHit = (TextView) mView.findViewById(R.id.dochit);
        if(FunctionUtils.getInstance().account.isouter == true)
        {
            image2.setImageResource(R.drawable.wreport);
            image3.setImageResource(R.drawable.wsuc);
            text2.setText("工作汇报");
            text3.setText("日程");
            upDateOa();
        }
        updataTaskHit();
        if(FunctionUtils.getInstance().account.isouter == false)
        {
            updataMailHit();
        }
        mDocument = (RelativeLayout) mView.findViewById(R.id.document);
        mMail.setOnClickListener(mMailListener);
        mTask.setOnClickListener(mTaskListener);
        mDocument.setOnClickListener(mDocumentListener);
        mSmart.setOnClickListener(mSmartListner);
        mFungrids = (LinearLayout) mView.findViewById(R.id.functions);
        mMainActivity.mMainPresenter.upDateWorkView();
        return mView;
    }

    public void updataMailHit() {
        if(mailHit != null)
        {
            int a = (int) Bus.callData(mMainActivity.mMainPresenter.mMainActivity,"mail/getMailHitCount","");
            if(a > 0)
            {
                mailHit.setVisibility(View.VISIBLE);
                AppUtils.setHit(a,mailHit);
            }
            else
            {
                mailHit.setVisibility(View.INVISIBLE);
            }
        }

    }


    public void updataTaskHit() {
        if(taskHit != null)
        {
            int a = (int) Bus.callData(mMainActivity,"task/getTaskHitCount","");
            if(a > 0)
            {
                taskHit.setVisibility(View.VISIBLE);
                AppUtils.setHit(a,taskHit);
            }
            else
            {
                taskHit.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void upDateOa() {
        boolean has = false;
        if(mailHit != null)
        {
            int a = FunctionUtils.getInstance().mFunctionGrids.get("oa").get(0).hintCount;
            if(a > 0)
            {
                mailHit.setVisibility(View.VISIBLE);
                AppUtils.setHit(a,mailHit);
                has = true;
            }
            else
            {
                mailHit.setVisibility(View.INVISIBLE);
            }
            int b = FunctionUtils.getInstance().mFunctionGrids.get("oa").get(3).hintCount;
            if(b > 0)
            {
                docHit.setVisibility(View.VISIBLE);
                AppUtils.setHit(a,docHit);
                has = true;
            }
            else
            {
                docHit.setVisibility(View.INVISIBLE);
            }
        }
    }

    public AdapterView.OnItemClickListener clickFunctionListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mMainActivity.mMainPresenter.startFunction((Function) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener mMailListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(InterskyApplication.mApp.functionUtils.account.isouter == false)
            MailManager.startMailMain(mMainActivity.mMainPresenter.mMainActivity);
        }
    };

    public View.OnClickListener mTaskListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            TaskManager.getInstance().startTaskManager(mMainActivity.mMainPresenter.mMainActivity);
        }
    };

    public View.OnClickListener mDocumentListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            if(InterskyApplication.mApp.functionUtils.account.isouter == false)
                DocumentManager.startDocumentMain(mMainActivity.mMainPresenter.mMainActivity);
        }
    };

    public View.OnClickListener mSmartListner = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity.mMainPresenter.mMainActivity, WebMessageActivity.class);
            intent.putExtra("isurl", true);
            intent.putExtra("smart", true);
            intent.putExtra("url", NetUtils.getInstance().praseUrl(InterskyApplication.mApp.mService,"/app/smart_search","token="+NetUtils.getInstance().token
                    +"&userid="+ InterskyApplication.mApp.mAccount.mRecordId+"&cid="+ InterskyApplication.mApp.mAccount.mCompanyId));
            mMainActivity.mMainPresenter.mMainActivity.startActivity(intent);
        }
    };
}
