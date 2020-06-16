package intersky.filetools.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.filetools.entity.AlbumItem;
import intersky.filetools.entity.ImageItem;
import intersky.filetools.handler.BigImageViewHandler;
import intersky.filetools.view.activity.BigImageViewActivity;
import intersky.filetools.view.adapter.PhotoSelectPageAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class BigImageViewPresenter implements Presenter {

    public BigImageViewActivity mBigImageViewActivity;
    public BigImageViewHandler mBigImageViewHandler;

    public BigImageViewPresenter(BigImageViewActivity mBigImageViewActivity)
    {
        this.mBigImageViewActivity = mBigImageViewActivity;
        this.mBigImageViewHandler = new BigImageViewHandler(mBigImageViewActivity);
    }

    @Override
    public void  Create() {
        initView();
    }

    @Override
    public void initView() {
        mBigImageViewActivity.setContentView(R.layout.activity_big_image_view);
        mBigImageViewActivity.buttomLayer = mBigImageViewActivity.findViewById(R.id.buttom);
        mBigImageViewActivity.headLayer = mBigImageViewActivity.findViewById(R.id.head);
        mBigImageViewActivity.mViewPager = (ViewPager) mBigImageViewActivity.findViewById(R.id.load_pager);
        mBigImageViewActivity.txtPageId = mBigImageViewActivity.findViewById(R.id.page);
        mBigImageViewActivity.back = mBigImageViewActivity.findViewById(R.id.leftbtn);
        mBigImageViewActivity.select = mBigImageViewActivity.findViewById(R.id.select);
        mBigImageViewActivity.btnOrigen = mBigImageViewActivity.findViewById(R.id.priview);
        mBigImageViewActivity.btnOk = mBigImageViewActivity.findViewById(R.id.save);
        mBigImageViewActivity.showActionUp = AnimationUtils.loadAnimation(mBigImageViewActivity, R.anim.slide_in_top);
        mBigImageViewActivity.hideActionUp = AnimationUtils.loadAnimation(mBigImageViewActivity, R.anim.slide_out_top);
        mBigImageViewActivity.showActionDown = AnimationUtils.loadAnimation(mBigImageViewActivity, R.anim.slide_in_buttom);
        mBigImageViewActivity.hideActionDown = AnimationUtils.loadAnimation(mBigImageViewActivity, R.anim.slide_out_buttom);
        mBigImageViewActivity.btnOrigen.setOnClickListener(mBigImageViewActivity.showOrigenListener);
        mBigImageViewActivity.btnOk.setOnClickListener(mBigImageViewActivity.okListener);
        mBigImageViewActivity.back.setOnClickListener(mBigImageViewActivity.backListener);
        mBigImageViewActivity.select.setOnClickListener(mBigImageViewActivity.imageSelectListener);
        initViewPage();
//        mBigImageViewHandler.removeMessages(BigImageViewHandler.HID_EDIT);
//        mBigImageViewHandler.sendEmptyMessageDelayed(BigImageViewHandler.HID_EDIT,10*1000);
    }

    public void initViewPage()
    {
        mBigImageViewActivity.mViewPager.addOnPageChangeListener(mBigImageViewActivity.mPageChangeListener);
        mBigImageViewActivity.mViewPager.setCurrentItem(mBigImageViewActivity.getIntent().getIntExtra("position", 0));
        if(mBigImageViewActivity.getIntent().getBooleanExtra("select", false))
        {
            ArrayList<ImageItem> items = mBigImageViewActivity.getIntent().getParcelableArrayListExtra("attachments");
            mBigImageViewActivity.showItems.addAll(items);
            ArrayList<View> views = new ArrayList<View>();
            for (int i = 0; i < mBigImageViewActivity.showItems.size(); i++) {
                View mView = mBigImageViewActivity.getLayoutInflater().inflate(R.layout.picpager, null);
                mView.setTag(mBigImageViewActivity.showItems.get(i));
                mView.setOnClickListener(mBigImageViewActivity.showEditListener);
                views.add(mView);
            }
            PhotoSelectPageAdapter mPicPageAdapter = new PhotoSelectPageAdapter(views,0,mBigImageViewActivity);
            mBigImageViewActivity.mViewPager.setAdapter(mPicPageAdapter);
            mBigImageViewActivity.mViewPager.setCurrentItem(mBigImageViewActivity.getIntent().getIntExtra("index",0));
            mBigImageViewActivity.txtPageId.setText(String.valueOf(mBigImageViewActivity.mViewPager.getCurrentItem() + 1) + "/" + String.valueOf(mBigImageViewActivity.showItems.size()));
            ImageItem tiem = mBigImageViewActivity.showItems.get(mBigImageViewActivity.mViewPager.getCurrentItem());
            if(tiem.isSelected == true)
            {
                mBigImageViewActivity.select.setImageResource(R.drawable.checkbox_checked);
            }
            else
            {
                mBigImageViewActivity.select.setImageResource(R.drawable.checkbox_unchecked);
            }
        }
        else
        {
            ArrayList<Attachment> items = mBigImageViewActivity.getIntent().getParcelableArrayListExtra("attachments");
            mBigImageViewActivity.showItems1.addAll(items);
            ArrayList<View> views = new ArrayList<View>();
            for (int i = 0; i <  mBigImageViewActivity.showItems1.size(); i++) {
                View mView = mBigImageViewActivity.getLayoutInflater().inflate(R.layout.picpager, null);
                mView.setTag( mBigImageViewActivity.showItems1.get(i));
                mView.setOnClickListener(mBigImageViewActivity.showEditListener);
                views.add(mView);
            }
            PhotoSelectPageAdapter mPicPageAdapter;
            if(mBigImageViewActivity.getIntent().getBooleanExtra("islocal",false))
             mPicPageAdapter = new PhotoSelectPageAdapter(views,1,mBigImageViewActivity);
            else
                mPicPageAdapter = new PhotoSelectPageAdapter(views,2,mBigImageViewActivity);
            mBigImageViewActivity.mViewPager.setAdapter(mPicPageAdapter);
            mBigImageViewActivity.mViewPager.setCurrentItem(mBigImageViewActivity.getIntent().getIntExtra("index",0));
            mBigImageViewActivity.select.setVisibility(View.INVISIBLE);
        }
        if(mBigImageViewActivity.getIntent().getBooleanExtra("delete",false) == true) {
            mBigImageViewActivity.btnOk.setText(mBigImageViewActivity.getString(R.string.button_delete));
        }
        if(mBigImageViewActivity.showItems1.size() > 0)
        mBigImageViewActivity.txtPageId.setText(String.valueOf(mBigImageViewActivity.mViewPager.getCurrentItem() + 1) + "/" + String.valueOf( mBigImageViewActivity.showItems1.size()));
        else
            mBigImageViewActivity.txtPageId.setText(String.valueOf(mBigImageViewActivity.mViewPager.getCurrentItem() + 1) + "/" + String.valueOf( mBigImageViewActivity.showItems.size()));
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

    }

    public void changePage() {
        if(mBigImageViewActivity.showItems.size() > 0)
            mBigImageViewActivity.txtPageId.setText(String.valueOf(mBigImageViewActivity.mViewPager.getCurrentItem() + 1) + "/" + String.valueOf(mBigImageViewActivity.showItems.size()));
        else
            mBigImageViewActivity.txtPageId.setText(String.valueOf(mBigImageViewActivity.mViewPager.getCurrentItem() + 1) + "/" + String.valueOf(mBigImageViewActivity.showItems1.size()));

        if(mBigImageViewActivity.getIntent().getBooleanExtra("isedit",false) == true)
        {
            ImageItem tiem = mBigImageViewActivity.showItems.get(mBigImageViewActivity.mViewPager.getCurrentItem());
            if(tiem.isSelected == true)
            {
                mBigImageViewActivity.select.setImageResource(R.drawable.checkbox_checked);
            }
            else
            {
                mBigImageViewActivity.select.setImageResource(R.drawable.checkbox_unchecked);
            }
        }
    }

    public void doselect() {

        if(mBigImageViewActivity.getIntent().getBooleanExtra("isedit",false) == true )
        {
            ImageItem tiem = mBigImageViewActivity.showItems.get(mBigImageViewActivity.mViewPager.getCurrentItem());
            if(tiem.isSelected == true)
            {
                tiem.isSelected = false;
                AlbumItem.getInstance().adds.remove(tiem);
                mBigImageViewActivity.select.setImageResource(R.drawable.checkbox_unchecked);
                mBigImageViewActivity.btnOk.setText(mBigImageViewActivity.getString(R.string.button_word_ok)+"("+ String.valueOf(AlbumItem.getInstance().adds.size())+")");
            }
            else
            {
                if(AlbumItem.getInstance().adds.size() < AlbumItem.getInstance().max)
                {
                    tiem.isSelected = true;
                    AlbumItem.getInstance().adds.add(tiem);
                    mBigImageViewActivity.select.setImageResource(R.drawable.checkbox_checked);
                    mBigImageViewActivity.btnOk.setText(mBigImageViewActivity.getString(R.string.button_word_ok)+"("+ String.valueOf(AlbumItem.getInstance().adds.size())+")");
                }
                else
                {
                    AppUtils.showMessage(mBigImageViewActivity,mBigImageViewActivity.getString(R.string.album_photoaddmax1) + String.valueOf(AlbumItem.getInstance().max) + mBigImageViewActivity.getString(R.string.album_photoaddmax2));
                }
            }
        }
    }

    public void showOrigen()
    {
        String path = "";
        if(mBigImageViewActivity.showItems1.size() > 0)
        {
            path = mBigImageViewActivity.showItems1.get(mBigImageViewActivity.mViewPager.getCurrentItem()).mPath;
        }
        else
        {
            path = mBigImageViewActivity.showItems.get(mBigImageViewActivity.mViewPager.getCurrentItem()).imagePath;
        }
        mBigImageViewActivity.startActivity(FileUtils.getImageFileIntent(new File(path)));
    }

    public void doOk()
    {
        if(mBigImageViewActivity.getIntent().getBooleanExtra("delete",false) == true)
        {
            doDelete();
        }
        else
        {
            Intent mIntent = new Intent();
            mIntent.setAction(mBigImageViewActivity.getIntent().getAction());
            mIntent.putParcelableArrayListExtra("attachments",FileUtils.mFileUtils.makePhotoAttachmentList());
            mBigImageViewActivity.sendBroadcast(mIntent);
            if(mBigImageViewActivity.getIntent().hasExtra("class"))
            {
                Intent mIntent1 = null;
                try {
                    mIntent1 = new Intent(mBigImageViewActivity, Class.forName(mBigImageViewActivity.getIntent().getStringExtra("class")));
                    mIntent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mBigImageViewActivity.startActivity(mIntent1);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void doDelete() {
        AppUtils.creatDialogTowButton(mBigImageViewActivity,mBigImageViewActivity.getString(R.string.cicle_delete_pic),mBigImageViewActivity.getString(R.string.cicle_delete_pic_title),
                mBigImageViewActivity.getString(R.string.button_word_cancle),mBigImageViewActivity.getString(R.string.button_word_ok),null,deleteSelect);
    }

    public DialogInterface.OnClickListener deleteSelect = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            int index = mBigImageViewActivity.mViewPager.getCurrentItem();
            Intent intent = new Intent(mBigImageViewActivity.getIntent().getAction());
            intent.putExtra("index",index);
            mBigImageViewActivity.sendBroadcast(intent);
            mBigImageViewActivity.showItems1.remove(mBigImageViewActivity.mViewPager.getCurrentItem());
            if(mBigImageViewActivity.showItems1.size() == 0)
            {
                mBigImageViewActivity.finish();
            }
            ArrayList<View> views = new ArrayList<View>();
            for (int i = 0; i < mBigImageViewActivity.showItems1.size(); i++) {
                View mView = mBigImageViewActivity.getLayoutInflater().inflate(R.layout.picpager, null);
                mView.setTag(mBigImageViewActivity.showItems1.get(i));
                views.add(mView);
            }
            PhotoSelectPageAdapter mPicPageAdapter;
            if(mBigImageViewActivity.getIntent().getBooleanExtra("islocal",false) == true )
             mPicPageAdapter = new PhotoSelectPageAdapter(views,1,mBigImageViewActivity);
            else
                mPicPageAdapter = new PhotoSelectPageAdapter(views,2,mBigImageViewActivity);
            mBigImageViewActivity.mViewPager.setAdapter(mPicPageAdapter);
            if(index != 0)
                mBigImageViewActivity.mViewPager.setCurrentItem(index-1);
            mBigImageViewActivity.txtPageId.setText(String.valueOf(mBigImageViewActivity.mViewPager.getCurrentItem() + 1) + "/" + String.valueOf(mBigImageViewActivity.showItems1.size()));

        }
    };

    public void showButtom() {
        if( mBigImageViewActivity.buttomLayer.getVisibility() == View.INVISIBLE)
        {
            mBigImageViewActivity.buttomLayer.startAnimation(mBigImageViewActivity.showActionDown);
            mBigImageViewActivity.buttomLayer.setVisibility(View.VISIBLE);
        }
    }

    public void hidButtom() {
        if( mBigImageViewActivity.buttomLayer.getVisibility() == View.VISIBLE)
        {
            mBigImageViewActivity.buttomLayer.startAnimation(mBigImageViewActivity.hideActionDown);
            mBigImageViewActivity.buttomLayer.setVisibility(View.INVISIBLE);
        }
    }

    public void showHead() {
        if( mBigImageViewActivity.headLayer.getVisibility() == View.INVISIBLE)
        {
            mBigImageViewActivity.headLayer.startAnimation(mBigImageViewActivity.showActionUp);
            mBigImageViewActivity.headLayer.setVisibility(View.VISIBLE);
        }
    }

    public void hidhead() {
        if( mBigImageViewActivity.headLayer.getVisibility() == View.VISIBLE)
        {
            mBigImageViewActivity.headLayer.startAnimation(mBigImageViewActivity.hideActionUp);
            mBigImageViewActivity.headLayer.setVisibility(View.INVISIBLE);
        }
    }

    public void hitEdit() {
        hidhead();
        hidButtom();
    }

    public void showEdit() {
        showButtom();
        showHead();
        mBigImageViewHandler.removeMessages(BigImageViewHandler.HID_EDIT);
        mBigImageViewHandler.sendEmptyMessageDelayed(BigImageViewHandler.HID_EDIT,10*1000);
    }
}
