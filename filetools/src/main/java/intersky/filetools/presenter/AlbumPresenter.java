package intersky.filetools.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import java.lang.ref.WeakReference;

import intersky.appbase.Presenter;
import intersky.filetools.R;
import intersky.filetools.entity.AlbumItem;
import intersky.filetools.view.activity.AlbumActivity;
import intersky.filetools.view.activity.PhotoSelectActivity;
import intersky.filetools.view.adapter.AlbumItemAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class AlbumPresenter implements Presenter {

    public AlbumHandler mAlbumHandler;
    public AlbumActivity mAlbumActivity;
    public AlbumPresenter(AlbumActivity mAlbumActivity)
    {
        this.mAlbumActivity = mAlbumActivity;
        this.mAlbumHandler = new AlbumHandler(mAlbumActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mAlbumActivity.setContentView(R.layout.activity_album);
        ToolBarHelper.setRightBtnText(mAlbumActivity.mActionBar, mAlbumActivity.mBackListener,
                mAlbumActivity.getString(R.string.button_word_cancle), Color.rgb(0, 130, 255));
        ToolBarHelper.setTitle(mAlbumActivity.mActionBar, mAlbumActivity.getString(R.string.album_title), Color.rgb(0, 0, 0));
        ToolBarHelper.setBgColor(mAlbumActivity, mAlbumActivity.mActionBar, Color.rgb(249, 249, 249));
        ToolBarHelper.hidBack(mAlbumActivity.mActionBar);
        mAlbumActivity.mAlbumList = (ListView) mAlbumActivity.findViewById(R.id.album_List);
        mAlbumActivity.mAlbumItemAdapter = new AlbumItemAdapter(mAlbumActivity, AlbumItem.getInstance().contentList);
        mAlbumActivity.mAlbumList.setAdapter(mAlbumActivity.mAlbumItemAdapter);
		mAlbumActivity.mAlbumList.setOnItemClickListener(mAlbumActivity.mOnItemClickListener);
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
        AlbumItem.getInstance().hasBuildImagesBucketList = false;
    }

    public void clickItem(int position)
    {
        startPhotoDetial(position);
    }


    private void startPhotoDetial(int id)
    {
        Intent intent = new Intent(mAlbumActivity, PhotoSelectActivity.class);
        intent.setAction(mAlbumActivity.getIntent().getAction());
        if(mAlbumActivity.getIntent().getBooleanExtra("takephoto",false))
            intent.putExtra("takephoto",true);
        intent.putExtra("type", mAlbumActivity.getIntent().getIntExtra("type",0));
        intent.putExtra("class",mAlbumActivity.getIntent().getStringExtra("class"));
        intent.putExtra("position",mAlbumActivity.getIntent().getStringExtra("position"));
        intent.putExtra("max",mAlbumActivity.getIntent().getIntExtra("max",999));
        intent.setAction(mAlbumActivity.getIntent().getAction());
        intent.putExtra("id",id);
        mAlbumActivity.startActivity(intent);
    }

    public static class AlbumHandler extends Handler {
        public AlbumActivity theActivity;

        AlbumHandler(AlbumActivity mAlbumActivity) {
            theActivity = mAlbumActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            switch (msg.what) {
            }

        }
    }
}
