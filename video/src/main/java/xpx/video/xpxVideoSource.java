package xpx.video;

import androidx.annotation.Nullable;

import org.webrtc.CapturerObserver;
import org.webrtc.MediaSource;
import org.webrtc.VideoFrame;
import org.webrtc.VideoProcessor;
import org.webrtc.VideoSink;
import org.webrtc.VideoSource;

import java.util.Objects;

public class xpxVideoSource extends VideoSource {


    public xpxVideoSource(long nativeSource) {
        super(nativeSource);
    }
}
