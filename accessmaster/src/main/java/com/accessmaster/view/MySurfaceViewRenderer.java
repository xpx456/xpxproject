package com.accessmaster.view;

import android.content.Context;
import android.util.AttributeSet;

import org.webrtc.SurfaceEglRenderer;
import org.webrtc.SurfaceViewRenderer;

public class MySurfaceViewRenderer extends SurfaceViewRenderer {


    public MySurfaceViewRenderer(Context context) {
        super(context);

    }



    public MySurfaceViewRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
