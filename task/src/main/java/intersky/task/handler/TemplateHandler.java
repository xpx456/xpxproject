package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TampaleteAsks;
import intersky.task.entity.Project;
import intersky.task.prase.ProjectPrase;
import intersky.task.prase.TampaletePrase;
import intersky.task.view.activity.TemplateActivity;
import intersky.xpxnet.net.NetObject;
//10
public class TemplateHandler extends Handler {
    public TemplateActivity theActivity;
    public TemplateHandler(TemplateActivity mTemplateActivity) {
        theActivity = mTemplateActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case TampaleteAsks.GET_TYPE_SUCCESS:
                theActivity.waitDialog.hide();
                TampaletePrase.praseTypes((NetObject) msg.obj,theActivity,theActivity.mTemplateTypes);
                theActivity.mTemplatePresenter.initTabs();
                break;
            case ProjectAsks.CREAT_PROJECT_SUCCESS:
                Project project = ProjectPrase.praseCreatProject((NetObject) msg.obj,theActivity);
                if(project != null)
                {
                    theActivity.mTemplatePresenter.startProjects(project);
                    theActivity.finish();
                }

                break;
        }

    }
}
