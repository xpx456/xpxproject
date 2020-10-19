package xpx.video;

import org.webrtc.AudioTrack;
import org.webrtc.EglBase;
import org.webrtc.PeerConnection;

public class MyRoomView {

//    public SurfaceViewRenderer remoteView;
    public PeerConnection peerConnection;
    public AudioTrack audioTrack;
    public boolean offersend = false;
    public boolean offerreceive = false;
    public boolean answerreceive = false;

    public boolean IceCandidateSend = false;


    public boolean IceCandidate = false;
    public org.webrtc.IceCandidate iceCandidate;
    public String mid = "";
    public boolean connected = false;
    public String answer = "";
    public MyRoomView(String roomid)
    {
        this.mid = roomid;
    }

    public void setView(EglBase.Context eglBaseContext) {
        //this.remoteView = remoteView;
    }

    public void openAudio()
    {
        if(audioTrack != null)
        {
            audioTrack.setEnabled(true);
        }
    }

    public void closeAudio()
    {
        if(audioTrack != null)
        {
            audioTrack.setEnabled(false);
        }
    }

    public void setValue(long value)
    {
        if(audioTrack != null)
        {
            audioTrack.setVolume(value);
        }
    }


}
