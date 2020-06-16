package intersky.filetools.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Attachment;
import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.filetools.entity.Video;
import intersky.filetools.handler.VideoHandler;
import intersky.filetools.provider.VideoProvider;
import intersky.filetools.view.activity.VideoActivity;
import intersky.filetools.view.adapter.VideoAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class VideoPresenter implements Presenter {

    private VideoActivity mVideoActivity;
    public VideoHandler mVideoHandler;

    public VideoPresenter(VideoActivity mVideoActivity) {
        mVideoHandler = new VideoHandler(mVideoActivity);
        this.mVideoActivity = mVideoActivity;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mVideoActivity.setContentView(R.layout.activity_video);
        ToolBarHelper.setTitle(mVideoActivity.mActionBar, mVideoActivity.getString(R.string.video_selectvideo));
        mVideoActivity.mButtomLayer = (RelativeLayout) mVideoActivity.findViewById(R.id.allfilebuttomimf);
        mVideoActivity.mListView = (ListView) mVideoActivity.findViewById(R.id.Video_grid);
        mVideoActivity.mListView.setOnItemClickListener(mVideoActivity.mOnItemClickListener);
        mVideoActivity.mbtnleft = (TextView) mVideoActivity.findViewById(R.id.btnleft);
        mVideoActivity.mbtnright = (TextView) mVideoActivity.findViewById(R.id.btnright);
        mVideoActivity.mbtnright2 = (TextView) mVideoActivity.findViewById(R.id.save2);
        mVideoActivity.layer2 = (RelativeLayout) mVideoActivity.findViewById(R.id.btnsave2);
        mVideoActivity.mbtnright.setOnClickListener(mVideoActivity.mFinishListener);
        mVideoActivity.mbtnright2.setOnClickListener(mVideoActivity.mFinishListener);
        if (mVideoActivity.getIntent().getIntExtra("type", FileUtils.SELECT_TYPE_NOMAL) == FileUtils.SELECT_TYPE_DOCUMENTMANAGER) {
            mVideoActivity.layer2.setVisibility(View.INVISIBLE);
            mVideoActivity.mButtomLayer.setVisibility(View.VISIBLE);
            mVideoActivity.mbtnleft.setOnClickListener(FileUtils.mFileUtils.setPositionListener);
        } else {
            mVideoActivity.layer2.setVisibility(View.VISIBLE);
            mVideoActivity.mButtomLayer.setVisibility(View.INVISIBLE);
        }
        if (mVideoActivity.getIntent().getBooleanExtra("takephoto", false) == true)
            ToolBarHelper.setRightBtnText(mVideoActivity.mActionBar, mVideoActivity.mTakePhotoListener, mVideoActivity.getString(R.string.button_word_takevideo));

    }

    private void initData() {
        mVideoActivity.provider = new VideoProvider(mVideoActivity);
        mVideoActivity.listVideos.clear();
        mVideoActivity.provider.getList(mVideoActivity.listVideos, null);
        mVideoActivity.mVideoAdapter = new VideoAdapter(mVideoActivity, mVideoActivity.listVideos);
        mVideoActivity.mListView.setAdapter(mVideoActivity.mVideoAdapter);


    }


    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        initData();
    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
    }

    public void addMailAttachmenFinish() {
        ArrayList<Attachment> attachments = new ArrayList<Attachment>();
        for (int i = 0; i < mVideoActivity.listVideos.size(); i++) {
            if (mVideoActivity.listVideos.get(i).isselect) {
                File mFile = new File(mVideoActivity.listVideos.get(i).path);
                attachments.add(new Attachment("", mFile.getName(), mVideoActivity.listVideos.get(i).path, "", mFile.length(), mFile.length(), ""));
            }
        }
        if(mVideoActivity.mButtomLayer.getVisibility() == View.VISIBLE)
        {
            FileUtils.mFileUtils.doSelectFinish(mVideoActivity, mVideoActivity.getIntent().getStringExtra("class"), mVideoActivity.getIntent().getAction(), attachments,true);
        }
        else
        {
            FileUtils.mFileUtils.doSelectFinish(mVideoActivity, mVideoActivity.getIntent().getStringExtra("class"), mVideoActivity.getIntent().getAction(), attachments,false);
        }

    }

    public void doItemClick(int position) {
        Video mVideoItem = mVideoActivity.listVideos.get(position);
        if (mVideoItem.isselect == false) {
        } else {
            mVideoItem.isselect = false;
        }
        if (mVideoHandler != null)
            mVideoHandler.sendEmptyMessage(VideoHandler.EVENT_UPDATA_VIDEO);
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FileUtils.TAKE_VIDEO:
                if (resultCode == Activity.RESULT_OK) {

                    File mFile = new File(FileUtils.mFileUtils.takeVideoUri.getPath());
                    if (mFile.exists()) {
                        Video mVideoItem = new Video();
                        mVideoItem.title = mFile.getName();
                        mVideoItem.path = mFile.getPath();
                        mVideoItem.isselect = true;
                        mVideoActivity.listVideos.add(0, mVideoItem);
                    }
                    if (mVideoHandler != null)
                        mVideoHandler.sendEmptyMessage(VideoHandler.EVENT_UPDATA_VIDEO);
                }
                break;
        }
    }

    public void takePhoto() {
        mVideoActivity.permissionRepuest = FileUtils.mFileUtils.checkPermissionTakePhoto(mVideoActivity, getOutputMediaFileUri());
    }

    private static String getOutputMediaFileUri() {
        // get the mobile Pictures directory
        File picDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // get the current time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        File videoFile = new File(picDir.getPath() + File.separator + "VIDEO_"
                + timeStamp + ".mp4");

        return videoFile.getPath();
    }


}
