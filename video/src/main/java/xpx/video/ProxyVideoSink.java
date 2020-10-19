package xpx.video;

import org.webrtc.VideoFrame;
import org.webrtc.VideoSink;

public class ProxyVideoSink implements VideoSink {
    private VideoSink mTarget;

    @Override
    public void onFrame(VideoFrame videoFrame) {
        if(mTarget == null)
            return;

        VideoFrame adaptedFrame = new VideoFrame(videoFrame.getBuffer(),90,videoFrame.getTimestampNs());
        if(adaptedFrame != null)
        {
            mTarget.onFrame(adaptedFrame);
        }


    }

    public synchronized void setTarget(VideoSink target)
    {
        this.mTarget = target;
    }
}
