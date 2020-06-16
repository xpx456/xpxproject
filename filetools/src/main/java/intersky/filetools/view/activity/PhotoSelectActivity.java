package intersky.filetools.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.filetools.FileUtils;
import intersky.filetools.entity.ImageItem;
import intersky.filetools.presenter.PhotoSelectPresenter;
import intersky.filetools.view.adapter.PhotoGrideAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class PhotoSelectActivity extends BaseActivity {

    public PhotoSelectPresenter mPhotoSelectPresenter = new PhotoSelectPresenter(this);
    public RelativeLayout mUploadRelativeLayout;
    public RelativeLayout mSelectRelativeLayout;
    public TextView btnUpload;
    public TextView btnPosition;
    public TextView btnPreview;
    public TextView btnOk;
    public GridView mPhotoGridView;
    public int selectid = -1;
    public ArrayList<ImageItem> dataList;
    public PhotoGrideAdapter mPhotoDetialGrideAdapter;
    public String BucketPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotoSelectPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mPhotoSelectPresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
        {
            // TODO Auto-generated method stub
            mPhotoSelectPresenter.clickitem(position);
        }

    };

    public View.OnClickListener mOkListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mPhotoSelectPresenter.doFinish();
        }
    };

    public View.OnClickListener mTakePhotoListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mPhotoSelectPresenter.takePhoto();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case FileUtils.TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK)
                mPhotoSelectPresenter.takePhotoResult();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }


    }

    public View.OnClickListener malbumListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mPhotoSelectPresenter.showAlbum();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPhotoSelectPresenter.onKeyDown(keyCode, event)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
