package intersky.guide.presenter;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.viewpager.widget.ViewPager;

import intersky.appbase.Presenter;
import intersky.appbase.ScreenDefine;
import intersky.guide.GuideUtils;
import intersky.guide.R;
import intersky.guide.view.activity.GuideActivity;
import intersky.guide.view.adapter.GuidePageAdapter;
import intersky.mywidget.NoScrollViewPager;

public class GuidePresenter implements Presenter {

    private GuideActivity mGuideActivity;

    public GuidePresenter(GuideActivity mGuideActivity) {
        this.mGuideActivity = mGuideActivity;
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mGuideActivity.setContentView(R.layout.activity_guide);
        mGuideActivity.mScreenDefine = new ScreenDefine(mGuideActivity);
        mGuideActivity.mViewPager = (NoScrollViewPager) mGuideActivity.findViewById(R.id.gudie);
        mGuideActivity.mViewPager.setNoScroll(false);
        mGuideActivity.dians = mGuideActivity.findViewById(R.id.dians);
        mGuideActivity.leftbtn = mGuideActivity.findViewById(R.id.left);
        mGuideActivity.rightbtn = mGuideActivity.findViewById(R.id.right);
        for(int i = 0; i < GuideUtils.getInstance().pics.size(); i++)
        {
            View view = mGuideActivity.getLayoutInflater().inflate(R.layout.guide_page,null);
            ImageView imageView = view.findViewById(R.id.bg);
            TextView textView = view.findViewById(R.id.text);
            GuideUtils.getInstance().setImage(imageView,GuideUtils.getInstance().pics.get(i),mGuideActivity);
            textView.setText(GuideUtils.getInstance().pics.get(i).keyword);
            mGuideActivity.mViews.add(view);
            View dian = mGuideActivity.getLayoutInflater().inflate(R.layout.dian,null);
            if(i == 0)
            {
                dian.findViewById(R.id.dian).setBackgroundResource(R.drawable.shape_round_white);
            }
            mGuideActivity.dians.addView(dian,(int)mGuideActivity.mScreenDefine.density*16,(int)mGuideActivity.mScreenDefine.density*8);
            mGuideActivity.dianlist.add(dian);
        }
        mGuideActivity.rightbtn.setOnClickListener(mGuideActivity.startNextListener);
        mGuideActivity.leftbtn.setOnClickListener(mGuideActivity.startSkipListener);
        mGuideActivity.mLoderPageAdapter = new GuidePageAdapter(mGuideActivity.mViews);
        mGuideActivity.mViewPager.setAdapter(mGuideActivity.mLoderPageAdapter);
        mGuideActivity.mViewPager.addOnPageChangeListener(onPageChangeListener);
    }


    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
//		MobclickAgent.onResume(mGuideActivity);
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
//		MobclickAgent.onPause(mGuideActivity);
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    public ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener()
    {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {
            if(i == 2)
            {
                setDian();
            }
        }
    };

    public void setDian() {
        for(int i =0 ; i < mGuideActivity.dianlist.size() ; i++)
        {
            View view = mGuideActivity.dianlist.get(i).findViewById(R.id.dian);
            if(i == mGuideActivity.mViewPager.getCurrentItem())
            view.setBackgroundResource(R.drawable.shape_round_white);
            else
                view.setBackgroundResource(R.drawable.shape_round_gray);
        }
        if(mGuideActivity.mViewPager.getCurrentItem() == GuideUtils.getInstance().pics.size()-1)
        {
            mGuideActivity.rightbtn.setText(mGuideActivity.getString(R.string.guide_start));
        }
        else
        {
            mGuideActivity.rightbtn.setText(mGuideActivity.getString(R.string.button_right));
        }
    }

    public void startNext() {
        GuideUtils.getInstance().startNext(mGuideActivity);
        mGuideActivity.finish();
    }
}
