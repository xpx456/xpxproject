package com.accesscontrol;

import android.os.Handler;

import com.accesscontrol.handler.ChatHandler;

import org.webrtc.VideoFrame;
import org.webrtc.VideoSink;


import intersky.apputils.SystemUtil;

public class ProxyVideoSink implements VideoSink {

    private VideoSink mTarget;
    private long currentTime = System.currentTimeMillis();
    @Override
    public void onFrame(VideoFrame videoFrame) {
        currentTime = System.currentTimeMillis();
        if(mTarget == null)
            return;
        mTarget.onFrame(videoFrame);
    }


    public synchronized void setTarget(VideoSink target)
    {
        this.mTarget = target;
    }


}
