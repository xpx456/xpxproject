package intersky.select;

import android.os.Handler;
import android.os.Message;

import intersky.select.view.activity.CustomSelectActivity;

//00
public class SelectHandler extends Handler {


    public static final int UPDATA_LIST_VIEW = 3140000;

    public CustomSelectActivity theActivity;

    public SelectHandler(CustomSelectActivity mCustomSelectActivity) {
         theActivity  = mCustomSelectActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case UPDATA_LIST_VIEW:
                if(SelectManager.getInstance().selectAdapter != null)
                SelectManager.getInstance().selectAdapter.notifyDataSetChanged();
                theActivity.mSearchAdapter.notifyDataSetChanged();
                break;
        }

    }
}
