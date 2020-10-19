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
import intersky.function.receiver.entity.Function;
import intersky.function.view.activity.WebMessageActivity;
import intersky.mail.MailManager;
import intersky.mywidget.MyGridView;
import intersky.task.TaskManager;
import intersky.xpxnet.net.NetUtils;

public class WorkFragment extends BaseFragment {

    public MainActivity mMainActivity;
    public MyGridView mygrid;
    public MyGridView allgrid;
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
        mygrid = mView.findViewById(R.id.mygrid);
        allgrid = mView.findViewById(R.id.allgrid);
        mMainActivity.mMainPresenter.upDateWorkView();
        return mView;
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
