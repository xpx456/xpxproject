package xpx.video;

import android.content.Context;
import android.util.AttributeSet;

import org.webrtc.SurfaceEglRenderer;
import org.webrtc.SurfaceViewRenderer;

public class xpxSurfaceViewRenderer extends SurfaceViewRenderer {
    public xpxSurfaceViewRenderer(Context context) {
        super(context);
    }


    public xpxSurfaceViewRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

