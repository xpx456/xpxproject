package intersky.leave.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.leave.LeaveManager;
import intersky.leave.asks.LeaveAsks;
import intersky.leave.prase.LeavePrase;
import intersky.leave.view.activity.LeaveListActivity;
import intersky.xpxnet.net.NetObject;

//03
public class LeaveListHandler extends Handler {

    public static final int EVENT_UPDATA_HIT = 3190300;
    public static final int EVENT_SET_USER = 3190301;
    public static final int EVENT_LEAVE_UPDATA = 3190302;
    public LeaveListActivity theActivity;

    public LeaveListHandler(LeaveListActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case LeaveAsks.EVENT_GET_LIST_SUCCESS:
                    NetObject netObject = (NetObject) msg.obj;
                    String keyword = "";
                    if((int)netObject.item == LeaveManager.TYPE_SEND)
                    {
                        if(theActivity.mViewPager.getCurrentItem() == 0)
                        {
                            keyword = theActivity.mSearchViewLayout.getText();
                        }
                        LeavePrase.praseList((NetObject) msg.obj,theActivity.myLeaveAdapter,theActivity.mysLeaveAdapter,keyword,theActivity);
                    }
                    else if((int)netObject.item == LeaveManager.TYPE_APPROVE)
                    {
                        if(theActivity.mViewPager.getCurrentItem() == 1)
                        {
                            keyword = theActivity.mSearchViewLayout.getText();
                        }
                        LeavePrase.praseList((NetObject) msg.obj,theActivity.approveLeaveAdapter,theActivity.approvesLeaveAdapter,keyword,theActivity);
                    }
                    else
                    {
                        if(theActivity.mViewPager.getCurrentItem() == 2)
                        {
                            keyword = theActivity.mSearchViewLayout.getText();
                        }
                        LeavePrase.praseList((NetObject) msg.obj,theActivity.copyLeaveAdapter,theActivity.copysLeaveAdapter,keyword,theActivity);
                    }
                    theActivity.waitDialog.hide();
                    break;
                case LeaveAsks.EVENT_GET_LIST_LIST_SUCCESS:
                    NetObject netObject1 = (NetObject) msg.obj;
                    String keyword1 = "";
                    if((int)netObject1.item == LeaveManager.TYPE_SEND)
                    {
                        if(theActivity.mViewPager.getCurrentItem() == 0)
                        {
                            keyword1 = theActivity.mSearchViewLayout.getText();
                        }
                        if(LeavePrase.praseList2((NetObject) msg.obj,theActivity.myLeaveAdapter,theActivity.mysLeaveAdapter,keyword1,theActivity))
                        {
                            if(theActivity.myLeaveAdapter.totalCount > theActivity.myLeaveAdapter.getmLeaves().size()
                                    && theActivity.myLeaveAdapter.endPage > theActivity.myLeaveAdapter.nowpage)
                            {
                                LeaveAsks.getLeaveList(theActivity.mLeaveListPresenter.mLeaveListHandler,theActivity,LeaveManager.getInstance().getSetUserId()
                                        ,LeaveManager.TYPE_SEND,theActivity.myLeaveAdapter.nowpage);
                            }
                        }
                        else
                        {

                            LeaveAsks.getLeaveList(theActivity.mLeaveListPresenter.mLeaveListHandler,theActivity,LeaveManager.getInstance().getSetUserId()
                                    ,LeaveManager.TYPE_SEND,theActivity.myLeaveAdapter.nowpage);
                        }

                    }
                    else if((int)netObject1.item == LeaveManager.TYPE_APPROVE)
                    {
                        if(theActivity.mViewPager.getCurrentItem() == 1)
                        {
                            keyword1 = theActivity.mSearchViewLayout.getText();
                        }
                        if(LeavePrase.praseList2((NetObject) msg.obj,theActivity.approveLeaveAdapter,theActivity.approvesLeaveAdapter,keyword1,theActivity))
                        {
                            if(theActivity.approveLeaveAdapter.totalCount > theActivity.approveLeaveAdapter.getmLeaves().size()
                                    && theActivity.approveLeaveAdapter.endPage > theActivity.approveLeaveAdapter.nowpage)
                            {
                                LeaveAsks.getLeaveList(theActivity.mLeaveListPresenter.mLeaveListHandler,theActivity,LeaveManager.getInstance().getSetUserId()
                                        ,LeaveManager.TYPE_APPROVE,theActivity.approveLeaveAdapter.nowpage);
                            }
                        }
                        else
                        {
                            LeaveAsks.getLeaveList(theActivity.mLeaveListPresenter.mLeaveListHandler,theActivity,LeaveManager.getInstance().getSetUserId()
                                    ,LeaveManager.TYPE_APPROVE,theActivity.approveLeaveAdapter.nowpage);
                        }
                    }
                    else
                    {
                        if(theActivity.mViewPager.getCurrentItem() == 2)
                        {
                            keyword1 = theActivity.mSearchViewLayout.getText();
                        }
                        if(LeavePrase.praseList2((NetObject) msg.obj,theActivity.copyLeaveAdapter,theActivity.copysLeaveAdapter,keyword1,theActivity))
                        {
                            if(theActivity.copyLeaveAdapter.totalCount > theActivity.copyLeaveAdapter.getmLeaves().size()
                                    && theActivity.copyLeaveAdapter.endPage > theActivity.copyLeaveAdapter.nowpage)
                            {
                                LeaveAsks.getLeaveList(theActivity.mLeaveListPresenter.mLeaveListHandler,theActivity,LeaveManager.getInstance().getSetUserId()
                                        ,LeaveManager.TYPE_COPY,theActivity.copyLeaveAdapter.nowpage);
                            }
                        }
                        else
                        {
                            LeaveAsks.getLeaveList(theActivity.mLeaveListPresenter.mLeaveListHandler,theActivity,LeaveManager.getInstance().getSetUserId()
                                    ,LeaveManager.TYPE_APPROVE,theActivity.approveLeaveAdapter.nowpage);
                        }
                    }
                    theActivity.waitDialog.hide();
                    break;
                case EVENT_SET_USER:
                    theActivity.mLeaveListPresenter.setuser((Intent) msg.obj);
                    break;
                case EVENT_UPDATA_HIT:
                    theActivity.mLeaveListPresenter.initHit();
                    break;
                case EVENT_LEAVE_UPDATA:
                    theActivity.waitDialog.hide();
                    theActivity.mLeaveListPresenter.leaveUpdateAll();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
