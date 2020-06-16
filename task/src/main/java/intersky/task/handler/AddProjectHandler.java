package intersky.task.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.apputils.AppUtils;
import intersky.task.R;
import intersky.task.asks.ProjectAsks;
import intersky.task.prase.ProjectPrase;
import intersky.task.view.activity.AddProjectActivity;
import intersky.xpxnet.net.NetObject;
//00
public class AddProjectHandler extends Handler {
    public AddProjectActivity theActivity;

    public AddProjectHandler(AddProjectActivity mAddProjectActivity) {
        theActivity = mAddProjectActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case ProjectAsks.MEMBER_APPLAY_SUCCESS:
                theActivity.waitDialog.hide();
                if(ProjectPrase.praseDelete((NetObject) msg.obj,theActivity))
                    AppUtils.showMessage(theActivity, theActivity.getString(R.string.project_add_success));
                break;
        }

    }
}
