package intersky.talk;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.apputils.TimeUtils;

public class AudioLayout extends RelativeLayout {

    private View audio;
    public ImageView volueCancle;
    public TextView volueTitle;
    public RelativeLayout recordVolue;
    public RelativeLayout volueArea;
    public ImageView vleave1;
    public ImageView vleave2;
    public ImageView vleave3;
    public ImageView vleave4;
    public ImageView vleave5;
    public ImageView vleave6;
    public Context context;
    public AudioRecoderUtils mAudioRecoderUtils;
    public recorddata mrecorddata;
    public boolean canadudio = false;
    public long audioduriton = 0;
    public String atdiotime = "";

    public AudioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        audio = mInflater.inflate(R.layout.im_autiolayout, null);

        recordVolue = (RelativeLayout) audio.findViewById(R.id.record_area);
        vleave1 = (ImageView) audio.findViewById(R.id.leave1);
        vleave2 = (ImageView) audio.findViewById(R.id.leave2);
        vleave3 = (ImageView) audio.findViewById(R.id.leave3);
        vleave4 = (ImageView) audio.findViewById(R.id.leave4);
        vleave5 = (ImageView) audio.findViewById(R.id.leave5);
        vleave6 = (ImageView) audio.findViewById(R.id.leave6);
        volueCancle = (ImageView) audio.findViewById(R.id.canclevoice);
        volueTitle = (TextView) audio.findViewById(R.id.title_volue);
        volueArea = (RelativeLayout) audio.findViewById(R.id.volue_area);
        mAudioRecoderUtils = new AudioRecoderUtils(Environment.getExternalStorageDirectory().getPath()+"/audio");
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(mOnAudioStatusUpdateListener);
        this.addView(audio);
//        hidRecordView();
    }

    public void doRecord() {
        audioduriton = 0;
        atdiotime = TimeUtils.getDateAndTimeCode();
        mAudioRecoderUtils.startRecord(atdiotime);

    }

    public void showCancleVoice() {
        volueArea.setVisibility(View.INVISIBLE);
        volueCancle.setVisibility(View.VISIBLE);
        volueTitle.setText(context.getString(R.string.volue_move_up_cancle_end));
        volueTitle.setBackgroundResource(R.drawable.shape_volue_title_bg);
    }

    public void hidCancleVoice() {
        volueArea.setVisibility(View.VISIBLE);
        volueCancle.setVisibility(View.INVISIBLE);
        volueTitle.setText(context.getString(R.string.volue_move_up_cancle));
        volueTitle.setBackground(null);
    }

    public void showRecordView() {
        recordVolue.setVisibility(View.VISIBLE);
        hidCancleVoice();
    }

    public void hidRecordView() {
        recordVolue.setVisibility(View.INVISIBLE);
        hidCancleVoice();

    }

    public AudioRecoderUtils.OnAudioStatusUpdateListener mOnAudioStatusUpdateListener = new AudioRecoderUtils.OnAudioStatusUpdateListener()
    {

        @Override
        public void onUpdate(double db, long time) {
            audioduriton = time/1000;
            updataRecordData(db,time);
        }

        @Override
        public void onStop(String filePath, boolean send) {
            if(send)
            {
                if(mrecorddata != null)
                    mrecorddata.finish(filePath);
            }

        }
    };

    public void updataRecordData(double db,long time) {
        int leave = 0;
        if(db < 20)
        {
            leave = 0;
        }
        else if(db <= 45)
        {
            leave = 1;
        }
        else
        {
            leave = 1+(int) ((db - 50)/10);
        }

        switch (leave)
        {
            case 0:
                vleave1.setVisibility(View.INVISIBLE);
                vleave2.setVisibility(View.INVISIBLE);
                vleave3.setVisibility(View.INVISIBLE);
                vleave4.setVisibility(View.INVISIBLE);
                vleave5.setVisibility(View.INVISIBLE);
                vleave6.setVisibility(View.INVISIBLE);
                break;
            case 1:
                vleave1.setVisibility(View.VISIBLE);
                vleave2.setVisibility(View.INVISIBLE);
                vleave3.setVisibility(View.INVISIBLE);
                vleave4.setVisibility(View.INVISIBLE);
                vleave5.setVisibility(View.INVISIBLE);
                vleave6.setVisibility(View.INVISIBLE);
                break;
            case 2:
                vleave1.setVisibility(View.VISIBLE);
                vleave2.setVisibility(View.VISIBLE);
                vleave3.setVisibility(View.INVISIBLE);
                vleave4.setVisibility(View.INVISIBLE);
                vleave5.setVisibility(View.INVISIBLE);
                vleave6.setVisibility(View.INVISIBLE);
                break;
            case 3:
                vleave1.setVisibility(View.VISIBLE);
                vleave2.setVisibility(View.VISIBLE);
                vleave3.setVisibility(View.VISIBLE);
                vleave4.setVisibility(View.INVISIBLE);
                vleave5.setVisibility(View.INVISIBLE);
                vleave6.setVisibility(View.INVISIBLE);
                break;
            case 4:
                vleave1.setVisibility(View.VISIBLE);
                vleave2.setVisibility(View.VISIBLE);
                vleave3.setVisibility(View.VISIBLE);
                vleave4.setVisibility(View.VISIBLE);
                vleave5.setVisibility(View.INVISIBLE);
                vleave6.setVisibility(View.INVISIBLE);
                break;
            case 5:
                vleave1.setVisibility(View.VISIBLE);
                vleave2.setVisibility(View.VISIBLE);
                vleave3.setVisibility(View.VISIBLE);
                vleave4.setVisibility(View.VISIBLE);
                vleave5.setVisibility(View.VISIBLE);
                vleave6.setVisibility(View.INVISIBLE);
                break;
            default:
                vleave1.setVisibility(View.VISIBLE);
                vleave2.setVisibility(View.VISIBLE);
                vleave3.setVisibility(View.VISIBLE);
                vleave4.setVisibility(View.VISIBLE);
                vleave5.setVisibility(View.VISIBLE);
                vleave6.setVisibility(View.VISIBLE);
                break;
        }

        if(mrecorddata != null)
            mrecorddata.updataRecordData(time);

    }

    public void setrecorddata(recorddata mrecorddata) {
        this.mrecorddata = mrecorddata;
    }

    public interface recorddata {
        void updataRecordData(long time);
        void finish(String path);
    }
}
