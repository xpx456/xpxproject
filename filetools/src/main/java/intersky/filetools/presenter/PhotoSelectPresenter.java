package intersky.filetools.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.filetools.entity.AlbumItem;
import intersky.filetools.entity.ImageItem;
import intersky.filetools.handler.PhotoSelectHandler;
import intersky.filetools.receiver.SetPositionReceive;
import intersky.filetools.thread.AlbumThread;
import intersky.filetools.view.activity.AlbumActivity;
import intersky.filetools.view.activity.BigImageViewActivity;
import intersky.filetools.view.activity.PhotoSelectActivity;
import intersky.filetools.view.adapter.PhotoGrideAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class PhotoSelectPresenter implements Presenter {

    public PhotoSelectHandler mPhotoSelectHandler;
    public PhotoSelectActivity mPhotoSelectActivity;
    public PhotoSelectPresenter(PhotoSelectActivity mPhotoSelectActivity)
    {
        this.mPhotoSelectActivity = mPhotoSelectActivity;
        this.mPhotoSelectHandler = new PhotoSelectHandler(mPhotoSelectActivity);
        mPhotoSelectActivity.setBaseReceiver(new SetPositionReceive(mPhotoSelectHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mPhotoSelectActivity.setContentView(R.layout.activity_select_photo);
        ToolBarHelper.setBackBtnText2(mPhotoSelectActivity.mActionBar, mPhotoSelectActivity.malbumListener, "选择相册",Color.argb(255,44,167,234));
        ToolBarHelper.setBgColor(mPhotoSelectActivity, mPhotoSelectActivity.mActionBar, Color.rgb(249, 249, 249));
        mPhotoSelectActivity.mSelectRelativeLayout = mPhotoSelectActivity.findViewById(R.id.btnsave2);
        mPhotoSelectActivity.mUploadRelativeLayout = mPhotoSelectActivity.findViewById(R.id.allfilebuttomimf);
        mPhotoSelectActivity.btnOk = mPhotoSelectActivity.findViewById(R.id.save2);
        mPhotoSelectActivity.btnPreview = mPhotoSelectActivity.findViewById(R.id.priview);
        mPhotoSelectActivity.btnPosition = mPhotoSelectActivity.findViewById(R.id.btnleft);
        mPhotoSelectActivity.btnUpload = mPhotoSelectActivity.findViewById(R.id.btnright);
        mPhotoSelectActivity.mPhotoGridView = mPhotoSelectActivity.findViewById(R.id.photo_grid);
        mPhotoSelectActivity.mPhotoGridView.setOnItemClickListener(mPhotoSelectActivity.mOnItemClickListener);
        mPhotoSelectActivity.btnOk.setOnClickListener(mPhotoSelectActivity.mOkListener);
        mPhotoSelectActivity.btnUpload.setOnClickListener(mPhotoSelectActivity.mOkListener);
        mPhotoSelectActivity.selectid =  mPhotoSelectActivity.getIntent().getIntExtra("id",-1);
        if(mPhotoSelectActivity.getIntent().getIntExtra("type",FileUtils.SELECT_TYPE_NOMAL) == FileUtils.SELECT_TYPE_DOCUMENTMANAGER)
        {
            mPhotoSelectActivity.mSelectRelativeLayout.setVisibility(View.INVISIBLE);
            mPhotoSelectActivity.mUploadRelativeLayout.setVisibility(View.VISIBLE);
            mPhotoSelectActivity.btnPosition.setText(mPhotoSelectActivity.getIntent().getStringExtra("position"));
            mPhotoSelectActivity.btnPosition.setOnClickListener(FileUtils.mFileUtils.setPositionListener);
        }
        else
        {
            mPhotoSelectActivity.mSelectRelativeLayout.setVisibility(View.VISIBLE);
            mPhotoSelectActivity.mUploadRelativeLayout.setVisibility(View.INVISIBLE);
        }
        if(mPhotoSelectActivity.getIntent().getBooleanExtra("takephoto",false)  == true)
        ToolBarHelper.setRightBtnText(mPhotoSelectActivity.mActionBar,mPhotoSelectActivity.mTakePhotoListener,mPhotoSelectActivity.getString(R.string.button_word_takephoto));

        AlbumItem.getInstance().max = mPhotoSelectActivity.getIntent().getIntExtra("max",9);


        if(AlbumItem.getInstance().hasBuildImagesBucketList == false)
        {
            mPhotoSelectActivity.waitDialog.show();
            AlbumThread mAlbumThread = new AlbumThread(mPhotoSelectHandler,mPhotoSelectActivity,PhotoSelectHandler.INIT_PHOTO_VIEW);
            mAlbumThread.start();
        }
        else
        {
            mPhotoSelectHandler.sendEmptyMessage(PhotoSelectHandler.INIT_PHOTO_VIEW);
        }
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
        doCleanSelect();
    }

    public void initPhoto() {
        if(mPhotoSelectActivity.selectid == -1)
        {
            for(int i = 0; i < AlbumItem.getInstance().contentList.size() ; i++)
            {
                if(AlbumItem.getInstance().contentList.get(i).bucketName != null)
                {
                    if(AlbumItem.getInstance().contentList.get(i).bucketName.equals("Camera"))
                    {
                        mPhotoSelectActivity.selectid = i;
                        break;
                    }
                }


            }
        }
        if(mPhotoSelectActivity.selectid == -1)
        {
            mPhotoSelectActivity.selectid = 0;
        }
        if(AlbumItem.getInstance().contentList.size() > 0)
        {
            mPhotoSelectActivity.BucketPath = getBucketPath(
                    AlbumItem.getInstance().contentList.get(mPhotoSelectActivity.selectid).imageList.get(0).imagePath);
        }
        mPhotoSelectActivity.dataList = new ArrayList<ImageItem>();
        if(AlbumItem.getInstance().contentList.size() > 0)
        mPhotoSelectActivity.dataList.addAll(AlbumItem.getInstance().contentList.get(mPhotoSelectActivity.selectid).imageList);

        if(AlbumItem.getInstance().contentList.size() > 0)
        ToolBarHelper.setTitle(mPhotoSelectActivity.mActionBar
                , AlbumItem.getInstance().contentList.get(mPhotoSelectActivity.selectid).bucketName, Color.rgb(0, 0, 0));
        mPhotoSelectActivity.mPhotoDetialGrideAdapter = new PhotoGrideAdapter(mPhotoSelectActivity,mPhotoSelectActivity.dataList,mPhotoSelectHandler);
        mPhotoSelectActivity.mPhotoGridView.setAdapter( mPhotoSelectActivity.mPhotoDetialGrideAdapter);
    }

    private String getBucketPath(String path) {
        String bpath = null;
        bpath = path.substring(0, path.lastIndexOf("/"));
        return bpath;
    }

    public void clickitem(int position) {
        ImageItem mImageItem = mPhotoSelectActivity.mPhotoDetialGrideAdapter.getItem(position);
        showPic(mImageItem);

    }

    public void showPic(ImageItem mImageItem)
    {
        Intent mintetn = new Intent(mPhotoSelectActivity,BigImageViewActivity.class);
        mintetn.putExtra("isedit",true);
        mintetn.putExtra("islocal",true);
        mintetn.putExtra("select",true);
        mintetn.putExtra("class",mPhotoSelectActivity.getIntent().getStringExtra("class"));
        mintetn.putParcelableArrayListExtra("attachments", AlbumItem.getInstance().contentList.get(mPhotoSelectActivity.selectid).imageList);
        mintetn.setAction(mPhotoSelectActivity.getIntent().getAction());
        mintetn.putExtra("index", AlbumItem.getInstance().contentList.get(mPhotoSelectActivity.selectid).imageList.indexOf(mImageItem));
        mPhotoSelectActivity.startActivity(mintetn);
    }

    public void doFinish() {
        if(mPhotoSelectActivity.mUploadRelativeLayout.getVisibility() == View.VISIBLE)
        {
            FileUtils.mFileUtils.doSelectFinish(mPhotoSelectActivity,mPhotoSelectActivity.getIntent().getStringExtra("class")
                    ,mPhotoSelectActivity.getIntent().getAction(), FileUtils.mFileUtils.makePhotoAttachmentList(),true);
        }
        else {
            FileUtils.mFileUtils.doSelectFinish(mPhotoSelectActivity,mPhotoSelectActivity.getIntent().getStringExtra("class")
                    ,mPhotoSelectActivity.getIntent().getAction(), FileUtils.mFileUtils.makePhotoAttachmentList(),false);
        }
        AlbumItem.getInstance().hasBuildImagesBucketList = false;
    }

    public void takePhoto() {
        mPhotoSelectActivity.permissionRepuest = FileUtils.mFileUtils.checkPermissionTakePhoto(mPhotoSelectActivity,mPhotoSelectActivity.BucketPath);
    }

    public void takePhotoResult() {
        ImageItem imageItem = new ImageItem();
        File file = new File(FileUtils.mFileUtils.takePhotoUri.getPath());
        if(file.exists())
        {
            imageItem.imagePath = file.getPath();
            AlbumItem.getInstance().contentList.get(mPhotoSelectActivity.selectid).imageList.add(imageItem);
        }
        mPhotoSelectActivity.dataList.add(imageItem);
        mPhotoSelectActivity.mPhotoDetialGrideAdapter.notifyDataSetChanged();
    }

    private void doCleanSelect() {

        for(int i = 0; i < AlbumItem.getInstance().adds.size() ; i++) {
            AlbumItem.getInstance().adds.get(i).isSelected = false;
        }
    }

    public void setPosition(Intent intent) {
        mPhotoSelectActivity.btnPosition.setText(intent.getStringExtra("position"));
    }

    public void showAlbum() {
        // AppUtils.removeActivity(this);
        Intent mainIntent = new Intent();
        doCleanSelect();
        AlbumItem.getInstance().adds.clear();
        mainIntent.setClass(mPhotoSelectActivity, AlbumActivity.class);
        mainIntent.setAction(mPhotoSelectActivity.getIntent().getAction());
        mainIntent.putExtra("takephoto",mPhotoSelectActivity.getIntent().getBooleanExtra("takephoto",false));
        mainIntent.putExtra("type",mPhotoSelectActivity.getIntent().getIntExtra("type",FileUtils.SELECT_TYPE_NOMAL));
        mainIntent.putExtra("class",mPhotoSelectActivity.getIntent().getStringExtra("class"));
        if(mPhotoSelectActivity.getIntent().hasExtra("position"))
        mainIntent.putExtra("position",mPhotoSelectActivity.getIntent().getStringExtra("position"));
        mainIntent.putExtra("max",mPhotoSelectActivity.getIntent().getIntExtra("max",9999));
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mPhotoSelectActivity.startActivity(mainIntent);
        mPhotoSelectActivity.finish();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlbumItem.getInstance().hasBuildImagesBucketList = false;
            mPhotoSelectActivity.finish();

            return false;
        }
        else {
            return false;
        }
    }
}
