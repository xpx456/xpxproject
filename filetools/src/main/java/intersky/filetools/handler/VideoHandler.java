package intersky.filetools.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.filetools.view.activity.VideoActivity;
//04
public class VideoHandler extends Handler
{
    public static final int EVENT_UPDATA_VIDEO = 3030400;

    public VideoActivity theActivity;

    public VideoHandler(VideoActivity activity)
    {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg)
    {
        // AppUtils.dissMissDialog();
        if (theActivity != null)
        {
            switch (msg.what)
            {
                case EVENT_UPDATA_VIDEO:
                    theActivity.mVideoAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    }
};
