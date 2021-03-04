package intersky.filetools.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;
import intersky.filetools.entity.ImageItem;
import intersky.filetools.presenter.BigImageViewPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class BigImageViewActivity extends Activity {

    public ViewPager mViewPager;
    public RelativeLayout buttomLayer;
    public RelativeLayout headLayer;
    public RelativeLayout back;
    public TextView txtPageId;
    public TextView btnOrigen;
    public TextView btnOk;
    public ImageView select;
    public Animation showActionUp;
    public Animation hideActionUp;
    public Animation showActionDown;
    public Animation hideActionDown;
    public ArrayList<Attachment> showItems1 = new ArrayList<Attachment>();
    public ArrayList<ImageItem> showItems = new ArrayList<ImageItem>();
    public BigImageViewPresenter mBigImageViewPresenter = new BigImageViewPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBigImageViewPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mBigImageViewPresenter.Destroy();
        super.onDestroy();
    }

    public ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener()
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mBigImageViewPresenter.changePage();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public View.OnClickListener imageSelectListener = new View.OnClickListener()
    {


        @Override
        public void onClick(View v) {
            mBigImageViewPresenter.doselect();
        }
    };

    public View.OnClickListener showOrigenListener = new View.OnClickListener()
    {


        @Override
        public void onClick(View v) {
            mBigImageViewPresenter.showOrigen();
        }
    };

    public View.OnClickListener okListener = new View.OnClickListener()
    {


        @Override
        public void onClick(View v) {
            mBigImageViewPresenter.doOk();
        }
    };


    public View.OnClickListener showEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mBigImageViewPresenter.showEdit();
        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
