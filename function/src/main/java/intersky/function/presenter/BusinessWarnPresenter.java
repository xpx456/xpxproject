package intersky.function.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ImageView;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.R;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.entity.BusinessWranData;
import intersky.function.entity.BussinessWarnItem;
import intersky.function.entity.Function;
import intersky.function.handler.BusinessWarnHandler;
import intersky.function.view.activity.BusinessWarnActivity;
import intersky.function.view.activity.GridDetialActivity;
import intersky.function.view.activity.WebMessageActivity;
import intersky.function.view.adapter.WarnListAdapter;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class BusinessWarnPresenter implements Presenter {

    public BusinessWarnHandler mBusinessWarnHandler;
    public BusinessWarnActivity mBusinessWarnActivity;
    public BusinessWarnPresenter(BusinessWarnActivity mBusinessWarnActivity)
    {
        this.mBusinessWarnActivity = mBusinessWarnActivity;
        this.mBusinessWarnHandler = new BusinessWarnHandler(mBusinessWarnActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mBusinessWarnActivity.setContentView(R.layout.activity_businesswarn);
        ImageView back = mBusinessWarnActivity.findViewById(R.id.back1);
        back.setOnClickListener(mBusinessWarnActivity.mBackListener);

        mBusinessWarnActivity.mListView = mBusinessWarnActivity.findViewById(R.id.warnList);
        mBusinessWarnActivity.mFunction = mBusinessWarnActivity.getIntent().getParcelableExtra("function");
        mBusinessWarnActivity.waitDialog.show();
        mBusinessWarnActivity.mListView.setOnItemClickListener(mBusinessWarnActivity.onItemClickListener);
        mBusinessWarnActivity.mListView.setOnItemLongClickListener(mBusinessWarnActivity.onItemLongClickListener);
        WorkFlowAsks.getRemindList(mBusinessWarnActivity,mBusinessWarnHandler);
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

    public void initData() {

        BusinessWranData mBusinessWranData = null;
        if(mBusinessWarnActivity.mFunData.funDatas.containsKey("0"))
        {
             mBusinessWranData = (BusinessWranData) mBusinessWarnActivity.mFunData.funDatas.get("0");
        }
        if(mBusinessWranData != null)
        {
            if (0 == mBusinessWranData.count) {
                AppUtils.showMessage(mBusinessWarnActivity, mBusinessWarnActivity.getResources().getString(R.string.message_noInfoFind));
            } else {
                ToolBarHelper.setRightBtnText(mBusinessWarnActivity.mActionBar, null, String.valueOf(mBusinessWranData.count) + mBusinessWarnActivity.getString(R.string.unit1));
                WarnListAdapter mWarnListAdapter = new WarnListAdapter(mBusinessWarnActivity,mBusinessWranData.bussinessWarnItems);
                mBusinessWarnActivity.mListView.setAdapter(mWarnListAdapter);
            }
        }
        else{
            AppUtils.showMessage(mBusinessWarnActivity, mBusinessWarnActivity.getResources().getString(R.string.message_noInfoFind));
        }
    }

    public void doClickListener(BussinessWarnItem mBussinessWarnItem) {

        if(mBusinessWarnActivity.getIntent().getBooleanExtra("iscloud",false) == false)
        {
            if(mBussinessWarnItem.module.toLowerCase().contains("mail"))
            {

            }
            else
            {
                Function info = new Function();
                info.mCaption = mBussinessWarnItem.subject;
                info.mName = mBussinessWarnItem.module;
                info.modulflag = mBussinessWarnItem.module;
                info.mRecordId = mBussinessWarnItem.parentID;
                Intent intent = new Intent(mBusinessWarnActivity, GridDetialActivity.class);
                intent.putExtra("function",info);
                intent.putExtra("edit",false);
                mBusinessWarnActivity.startActivity(intent);
            }
        }
        else
        {
            Intent intent = new Intent(mBusinessWarnActivity,
                    WebMessageActivity.class);
            intent.putExtra("isurl", true);
            String url = "";
            if(FunctionUtils.getInstance().service.https)
            {
                url = "https://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/reminder/detail"+mBussinessWarnItem.caption+"?token="+ NetUtils.getInstance().token
                        +"&rid="+mBussinessWarnItem.parentID+"&app_languge="+ AppUtils.getLangue(mBusinessWarnActivity);
            }
            else
            {
                url = "http://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/reminder/detail/"+mBussinessWarnItem.caption+"?token="+NetUtils.getInstance().token
                        +"&rid="+mBussinessWarnItem.parentID+"&app_languge="+ AppUtils.getLangue(mBusinessWarnActivity);
            }
            intent.putExtra("url", url);
            mBusinessWarnActivity.startActivity(intent);
        }



    }

    public void doLongClickListener(BussinessWarnItem mBussinessWarnItem) {
        AppUtils.creatDialogTowButton(mBusinessWarnActivity,mBusinessWarnActivity.getString(R.string.warn_deleteWarnMsg),mBusinessWarnActivity.getString(R.string.title_tip),
                mBusinessWarnActivity.getString(R.string.button_word_cancle),mBusinessWarnActivity.getString(R.string.button_word_ok),null,new DialogListener(mBussinessWarnItem));
    }

    public class DialogListener implements DialogInterface.OnClickListener {

        public BussinessWarnItem item;

        public DialogListener( BussinessWarnItem item)
        {
            this.item = item;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            doDelete(item);
        }
    }

    public void doDelete(BussinessWarnItem item) {
        mBusinessWarnActivity.waitDialog.show();
        WorkFlowAsks.getRemindDismiss(mBusinessWarnActivity,mBusinessWarnHandler,item);
    }

    public void removeItem(BussinessWarnItem item) {
        if(mBusinessWarnActivity.mFunData.funDatas.containsKey("0"))
        {
            BusinessWranData mBusinessWranData = (BusinessWranData) mBusinessWarnActivity.mFunData.funDatas.get("0");
            mBusinessWranData.bussinessWarnItems.remove(item);
            mBusinessWranData.count--;
            upDate();
        }
    }

    public void upDate() {
        if(mBusinessWarnActivity.mFunData.funDatas.containsKey("0"))
        {
            BusinessWranData mBusinessWranData = (BusinessWranData) mBusinessWarnActivity.mFunData.funDatas.get("0");
            WarnListAdapter mWarnListAdapter = (WarnListAdapter) mBusinessWarnActivity.mListView.getAdapter();
            mWarnListAdapter.notifyDataSetChanged();
            ToolBarHelper.setRightBtnText(mBusinessWarnActivity.mActionBar, null, String.valueOf(mBusinessWranData.count) + "条");
        }
    }
}
