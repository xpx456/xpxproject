package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.apputils.AppUtils;
import intersky.select.SelectManager;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.prase.ProjectPrase;
import intersky.task.view.activity.ProjectSettingActivity;
import intersky.xpxnet.net.NetObject;

//05
public class ProjectSettingHandler extends Handler {

    public static final int PROJECT_POWER = 3250200;
    public static final int PROJECT_CONTRAL = 3250201;
    public static final int UPDATA_EXIST = 3250202;
    public static final int PROJECT_DELETE = 3250203;
    public static final int LEADER_CHANGE = 3250204;

    ProjectSettingActivity theActivity;

    public ProjectSettingHandler(ProjectSettingActivity mProjectSettingActivity) {
        theActivity = mProjectSettingActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case ProjectAsks.SET_PROJECT_TOP:
                theActivity.waitDialog.hide();
                ProjectPrase.praseTop((NetObject) msg.obj,theActivity,theActivity.project);
                theActivity.mProjectSettingePresenter.updtataTop();
                if(theActivity.project.projectId.length() > 0 && !theActivity.project.projectId.equals("0")) {
                    intent = new Intent(ProjectAsks.ACTION_SET_PROJECT_OTHRE);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case ProjectAsks.DELETE_PROJECT:
                theActivity.waitDialog.hide();
                ProjectPrase.praseDelete((NetObject) msg.obj,theActivity);
                if(theActivity.project.projectId.length() > 0 && !theActivity.project.projectId.equals("0")) {
                    intent = new Intent(ProjectAsks.ACTION_DELETE_PROJECT);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case ProjectAsks.REMOVE_MEMBER_SUCCESS:
                theActivity.waitDialog.hide();
                if(ProjectPrase.praseDelete((NetObject) msg.obj,theActivity)) {
                    if(theActivity.project.projectId.length() > 0 && !theActivity.project.projectId.equals("0")) {
                        intent = new Intent(ProjectAsks.ACTION_PROJECT_REMOVE_MEMBER);
                        intent.putExtra("projectid",theActivity.project.projectId);
                        intent.putExtra("cid",TaskManager.getInstance().oaUtils.mAccount.mRecordId);
                        theActivity.sendBroadcast(intent);
                    }
                    theActivity.finish();
                }
                break;
            case ProjectAsks.MEMBER_APPLAY_SUCCESS:
                theActivity.waitDialog.hide();
                if(ProjectPrase.praseDelete((NetObject) msg.obj,theActivity)) {
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.apply_send));
                }
                break;
            case ProjectAsks.SET_SAVE_SUCCESS:
                theActivity.waitDialog.hide();
                if(ProjectPrase.praseDelete((NetObject) msg.obj,theActivity)) {
                    AppUtils.showMessage(theActivity, theActivity.getString(R.string.save_temple));
                }
                break;
            case PROJECT_POWER:
                theActivity.waitDialog.show();
                ProjectAsks.setPower(theActivity,theActivity.mProjectSettingePresenter.mProjectSettingHandler,theActivity.project, SelectManager.getInstance().mSignal);
                break;
            case PROJECT_CONTRAL:
                theActivity.waitDialog.show();
                ProjectAsks.setContral(theActivity,theActivity.mProjectSettingePresenter.mProjectSettingHandler,theActivity.project, SelectManager.getInstance().mSignal);
                break;
            case ProjectAsks.SET_POWER_SUCCESS:
                theActivity.waitDialog.hide();
                if(ProjectPrase.praseDelete((NetObject) msg.obj,theActivity))
                theActivity.mProjectSettingePresenter.updataPower();
                if(theActivity.project.projectId.length() > 0 && !theActivity.project.projectId.equals("0")) {
                    intent = new Intent(ProjectAsks.ACTION_SET_PROJECT_OTHRE);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case ProjectAsks.SET_CONTRAL_SUCCESS:
                theActivity.waitDialog.hide();
                if(ProjectPrase.praseDelete((NetObject) msg.obj,theActivity))
                theActivity.mProjectSettingePresenter.updataContral();
                if(theActivity.project.projectId.length() > 0 && !theActivity.project.projectId.equals("0")) {
                    intent = new Intent(ProjectAsks.ACTION_SET_PROJECT_OTHRE);
                    intent.putExtra("projectid",theActivity.project.projectId);
                    theActivity.sendBroadcast(intent);
                }
                break;
            case UPDATA_EXIST:
                intent = (Intent) msg.obj;
                String id = intent.getStringExtra("projectid");
                String cid1 = intent.getStringExtra("cid");
                if(theActivity.project.projectId.equals(id) && TaskManager.getInstance().oaUtils.mAccount.mRecordId.equals(cid1))
                    theActivity.finish();
                break;
            case PROJECT_DELETE:
                intent = (Intent) msg.obj;
                String id1 = intent.getStringExtra("projectid");
                if(theActivity.project.projectId.equals(id1))
                    theActivity.finish();
                break;
            case LEADER_CHANGE:
                intent = (Intent) msg.obj;
                String id2 = intent.getStringExtra("projectid");
                String cid2 = intent.getStringExtra("cid");
                theActivity.project.leaderId = cid2;
                if(theActivity.project.projectId.equals(id2) )
                {
                    theActivity.mProjectSettingePresenter.initOper();
                }
                break;
        }

    }
}
