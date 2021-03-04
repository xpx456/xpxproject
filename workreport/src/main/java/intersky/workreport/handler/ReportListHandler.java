package intersky.workreport.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.workreport.WorkReportManager;
import intersky.workreport.asks.WorkReportAsks;
import intersky.workreport.prase.WorkReportPrase;
import intersky.workreport.view.activity.ReportListActivity;
import intersky.workreport.view.activity.WorkReportActivity;
import intersky.xpxnet.net.NetObject;
//03
public class ReportListHandler extends Handler {

    public static final int EVENT_GET_HIT_SUCCESS = 3270300;
    public static final int EVENT_REPORT_UPDATA = 3270301;
    public static final int EVENT_REPORT_CONVERSATION_UPDATA = 3270302;
    public ReportListActivity theActivity;

    public ReportListHandler(ReportListActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        NetObject netObject;
        int type;
        if (theActivity != null) {
            switch (msg.what) {
                case WorkReportAsks.EVENT_GET_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    netObject = (NetObject) msg.obj;
                    type = (int) netObject.item;
                    if(type == WorkReportManager.TYPE_DAY)
                    {
                        WorkReportPrase.praseList(netObject,theActivity.mReporttype,type,theActivity.dayReportAdapter,theActivity);
                        theActivity.dayReportAdapter.notifyDataSetChanged();
                    }
                    else if(type == WorkReportManager.TYPE_WEEK)
                    {
                        WorkReportPrase.praseList(netObject,theActivity.mReporttype,type,theActivity.weekReportAdapter,theActivity);
                        theActivity.weekReportAdapter.notifyDataSetChanged();
                    }
                    else if(type == WorkReportManager.TYPE_MONTH)
                    {
                        WorkReportPrase.praseList(netObject,theActivity.mReporttype,type,theActivity.monthReportAdapter,theActivity);
                        theActivity.monthReportAdapter.notifyDataSetChanged();
                    }
                    break;
                case WorkReportAsks.EVENT_GET_LIST_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    netObject = (NetObject) msg.obj;
                    type = (int) netObject.item;
                    if(type == WorkReportManager.TYPE_DAY)
                    {
                        if(WorkReportPrase.praseList2(netObject,theActivity.mReporttype,type,theActivity.dayReportAdapter,theActivity))
                        {
                            if(theActivity.dayReportAdapter.totalCount > theActivity.dayReportAdapter.getmReports().size()
                                    && theActivity.dayReportAdapter.endPage > theActivity.dayReportAdapter.nowpage)
                            {
                                WorkReportAsks.getReportList(theActivity,theActivity.mReportListPresenter.mWorkReportHandler, WorkReportManager.TYPE_DAY
                                        ,theActivity.mReporttype,theActivity.dayReportAdapter.nowpage);
                            }
                        }
                        else
                        {
                            WorkReportAsks.getReportList(theActivity,theActivity.mReportListPresenter.mWorkReportHandler, WorkReportManager.TYPE_DAY
                                    ,theActivity.mReporttype,theActivity.dayReportAdapter.nowpage);
                        }
                        theActivity.dayReportAdapter.notifyDataSetChanged();
                    }
                    else if(type == WorkReportManager.TYPE_WEEK)
                    {
                        if(WorkReportPrase.praseList2(netObject,theActivity.mReporttype,type,theActivity.weekReportAdapter,theActivity))
                        {
                            if(theActivity.weekReportAdapter.totalCount > theActivity.weekReportAdapter.getmReports().size()
                                    && theActivity.weekReportAdapter.endPage > theActivity.weekReportAdapter.nowpage)
                            {
                                WorkReportAsks.getReportList(theActivity,theActivity.mReportListPresenter.mWorkReportHandler, WorkReportManager.TYPE_WEEK
                                        ,theActivity.mReporttype,theActivity.weekReportAdapter.nowpage);
                            }
                        }
                        else
                        {
                            WorkReportAsks.getReportList(theActivity,theActivity.mReportListPresenter.mWorkReportHandler, WorkReportManager.TYPE_WEEK
                                    ,theActivity.mReporttype,theActivity.weekReportAdapter.nowpage);
                        }
                        theActivity.weekReportAdapter.notifyDataSetChanged();
                    }
                    else if(type == WorkReportManager.TYPE_MONTH)
                    {
                        if(WorkReportPrase.praseList2(netObject,theActivity.mReporttype,type,theActivity.monthReportAdapter,theActivity))
                        {
                            if(theActivity.monthReportAdapter.totalCount > theActivity.monthReportAdapter.getmReports().size()
                                    && theActivity.monthReportAdapter.endPage > theActivity.monthReportAdapter.nowpage)
                            {
                                WorkReportAsks.getReportList(theActivity,theActivity.mReportListPresenter.mWorkReportHandler, WorkReportManager.TYPE_MONTH
                                        ,theActivity.mReporttype,theActivity.monthReportAdapter.nowpage);
                            }
                        }
                        else
                        {
                            WorkReportAsks.getReportList(theActivity,theActivity.mReportListPresenter.mWorkReportHandler, WorkReportManager.TYPE_MONTH
                                    ,theActivity.mReporttype,theActivity.monthReportAdapter.nowpage);
                        }
                        theActivity.weekReportAdapter.notifyDataSetChanged();
                    }
                    break;
                case EVENT_REPORT_UPDATA:
                    theActivity.mReportListPresenter.updataAll();
                    break;
                case EVENT_GET_HIT_SUCCESS:
                    theActivity.mReportListPresenter.updataHit();
                    break;
                case EVENT_REPORT_CONVERSATION_UPDATA:
                    theActivity.waitDialog.hide();
                    theActivity.mReportListPresenter.reportConversationUpdate();
                    break;
                case WorkReportAsks.EVENT_LIST_READ:
                    theActivity.waitDialog.hide();
                    WorkReportManager.getInstance().upDataHit(theActivity);
                    theActivity.mReportListPresenter.updataAll();
                    break;
                case WorkReportAsks.EVENT_LIST_DELETE:
                    theActivity.waitDialog.hide();
                    theActivity.mReportListPresenter.updataAll();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
