package intersky.mywidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import intersky.apputils.AppUtils;

public class TabHeadView extends LinearLayout {
    public NoScrollViewPager mViewPager;
    private View mTab;
    public LayoutInflater mInflater;
    public LinearLayout headlayer;
    public DisplayMetrics metric;
    public boolean iscoll = false;
    public OnTabLisener mOnTabLisener;
    public int colortxt1 = -1;
    public int colortxt2 = -1;
    public int colorline = -1;
    public int colorlineselect = -1;
    public float textsizenomal = 0;
    public float textsizebig = 0;
    public RelativeLayout selectline;
    public RelativeLayout selectlines;
    public int linewidth = 0;
    public TabHeadView(Context context) {
        super(context);
    }
    public ArrayList<TextView> textViews = new ArrayList<TextView>();
    public ArrayList<TextView> hitViews = new ArrayList<TextView>();
    public int eachwidth = 0;
    public int selectLinetype = 0;
    public int lastleft = 0;
    public float itemHeight = 40;
    public int oldid = 0;
    public TabHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        metric = new DisplayMetrics();
        ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabHeadView);
        iscoll=a.getBoolean(R.styleable.TabHeadView_isScoll,false);
        colortxt1=a.getColor(R.styleable.TabHeadView_colortxt1,Color.rgb(0,0,0));
        colortxt2=a.getColor(R.styleable.TabHeadView_colortxt2,Color.rgb(98,153,243));
        colorline=a.getColor(R.styleable.TabHeadView_colorline,Color.rgb(236,236,236));
        colorlineselect=a.getColor(R.styleable.TabHeadView_colorlineselect,Color.rgb(98,153,243));
        textsizenomal=a.getDimension(R.styleable.TabHeadView_textsizenomal,0);
        textsizebig=a.getDimension(R.styleable.TabHeadView_textsizebig,0);
        selectLinetype = a.getInt(R.styleable.TabHeadView_selectlingtype,0);
        itemHeight = a.getDimension(R.styleable.TabHeadView_itemheight,40*metric.density);
        initView();
    }

    public void initView() {
        LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTab = mInflater.inflate(R.layout.tabhead_view, null);
        if(selectLinetype > 0)
        {
            selectline = mTab.findViewById(R.id.selectline);
            selectlines = mTab.findViewById(R.id.selectlinedetial);
            selectlines.setBackgroundColor(colorlineselect);
            selectline.setVisibility(VISIBLE);
        }

        if(iscoll ==false)
        {
            mTab.findViewById(R.id.hv).setVisibility(View.GONE);
            mTab.findViewById(R.id.tebhead).setVisibility(View.VISIBLE);
            headlayer = mTab.findViewById(R.id.tebhead);
        }
        else
        {
            mTab.findViewById(R.id.hv).setVisibility(View.VISIBLE);
            mTab.findViewById(R.id.tebhead).setVisibility(View.GONE);
            headlayer = mTab.findViewById(R.id.tebhead1);
        }

        this.addView(mTab);
    }

    public void setViewPager(NoScrollViewPager mViewPager) {
        this.mViewPager = mViewPager;
        this.mViewPager.addOnPageChangeListener(onPageChangeListener);
        eachwidth = metric.widthPixels/this.mViewPager.getAdapter().getCount();
        textViews.clear();
        for (int i = 0; i < this.mViewPager.getAdapter().getCount(); i++) {
            View mView = mInflater.inflate(R.layout.tabitem_view,null);
            TextView mText = mView.findViewById(R.id.tabtitle);
            if(textsizebig > 0)
                mText.getPaint().setTextSize(textsizebig);
            if(this.mViewPager.getAdapter().getPageTitle(i) != null)
            mText.setText(this.mViewPager.getAdapter().getPageTitle(i));
            RelativeLayout line1 = mView.findViewById(R.id.line1);
            RelativeLayout line2 = mView.findViewById(R.id.line2);
            line1.setBackgroundColor(colorline);
            line2.setBackgroundColor(colorlineselect);

            mText.setTextColor(colortxt1);
            if(i != this.mViewPager.getCurrentItem())
            {
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                mText.measure(w, h);
                int width = mText.getMeasuredWidth();
                if(linewidth  < width)
                    linewidth = width;
            }
            if(textsizenomal > 0)
                mText.getPaint().setTextSize(textsizenomal);
            textViews.add(mText);
            TextView hitView = mView.findViewById(R.id.tabhit);
            hitViews.add(hitView);
            if (i == this.mViewPager.getCurrentItem()) {
                line1 = mView.findViewById(R.id.line1);
                line2 = mView.findViewById(R.id.line2);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                mText.setTextColor(colortxt2);
                if(textsizebig > 0)
                mText.getPaint().setTextSize(textsizebig);
            }
            if(selectLinetype > 0)
            {
                line1.setVisibility(INVISIBLE);
                line2.setVisibility(INVISIBLE);
            }
            mView.setOnClickListener(tabListener);
            if(iscoll==false)
            headlayer.addView(mView,eachwidth, (int) (itemHeight));
            else
                headlayer.addView(mView,metric.widthPixels/4, (int) (itemHeight));
        }
        if(selectLinetype != 0)
        {
            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) selectline.getLayoutParams();
            layoutParams1.width = eachwidth;
            selectline.setLayoutParams(layoutParams1);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) selectlines.getLayoutParams();
            layoutParams.width = linewidth;
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            selectlines.setLayoutParams(layoutParams);
            setSelectLineLocation();
        }

    }

    public void setHit(int count,int id)
    {
        AppUtils.setHit(count,hitViews.get(id));
    }

    public void setmOnTabLisener(OnTabLisener mOnTabLisener) {
        this.mOnTabLisener = mOnTabLisener;
    }

    public View.OnClickListener tabListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = headlayer.indexOfChild(v);
            if(i != mViewPager.getCurrentItem())
            {
                View item = headlayer.getChildAt(mViewPager.getCurrentItem());
                RelativeLayout line1 = item.findViewById(R.id.line1);
                RelativeLayout line2 = item.findViewById(R.id.line2);
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                if(selectLinetype > 0)
                {
                    line1.setVisibility(INVISIBLE);
                    line2.setVisibility(INVISIBLE);
                }
                TextView mText = item.findViewById(R.id.tabtitle);
                mText.setTextColor(Color.rgb(0,0,0));
                if(textsizenomal > 0)
                    mText.getPaint().setTextSize(textsizenomal);
                mText.setTextColor(colortxt1);
                View item2 = headlayer.getChildAt(i);
                line1 = item2.findViewById(R.id.line1);
                line2 = item2.findViewById(R.id.line2);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                mText = item2.findViewById(R.id.tabtitle);
                mText.setTextColor(colortxt2);
//                if(mOnTabLisener != null)
//                {
//                    mOnTabLisener.TabClick(i);
//                }
                if(textsizebig > 0)
                    mText.getPaint().setTextSize(textsizebig);
                if(selectLinetype > 0)
                {
                    line1.setVisibility(INVISIBLE);
                    line2.setVisibility(INVISIBLE);
                }
                mViewPager.setCurrentItem(i);
                if(selectLinetype > 0)
                going();
            }


        }
    };

    public void doScollChange(int oldid)
    {
        View item = headlayer.getChildAt(oldid);
        RelativeLayout line1 = item.findViewById(R.id.line1);
        RelativeLayout line2 = item.findViewById(R.id.line2);
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.INVISIBLE);
        if(selectLinetype > 0)
        {
            line1.setVisibility(INVISIBLE);
            line2.setVisibility(INVISIBLE);
        }
        TextView mText = item.findViewById(R.id.tabtitle);
        mText.setTextColor(Color.rgb(0,0,0));
        if(textsizenomal > 0)
            mText.getPaint().setTextSize(textsizenomal);
        mText.setTextColor(colortxt1);
        View item2 = headlayer.getChildAt(mViewPager.getCurrentItem());
        line1 = item2.findViewById(R.id.line1);
        line2 = item2.findViewById(R.id.line2);
        line1.setVisibility(View.INVISIBLE);
        line2.setVisibility(View.VISIBLE);
        mText = item2.findViewById(R.id.tabtitle);
        mText.setTextColor(colortxt2);
        if(mOnTabLisener != null)
        {
            mOnTabLisener.TabClick(mViewPager.getCurrentItem());
        }
        if(textsizebig > 0)
            mText.getPaint().setTextSize(textsizebig);
        if(selectLinetype > 0)
        {
            line1.setVisibility(INVISIBLE);
            line2.setVisibility(INVISIBLE);
        }
        if(selectLinetype > 0)
        going();
    }

    public interface OnTabLisener
    {
        void TabClick(int tab);

    }

    public void setSelectLineLocation() {
        int left = eachwidth*mViewPager.getCurrentItem();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) selectline.getLayoutParams();
        layoutParams.width = eachwidth;
        layoutParams.leftMargin = left;
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        selectline.setLayoutParams(layoutParams);
    }

    public void going() {
        int left = eachwidth*mViewPager.getCurrentItem();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) selectline.getLayoutParams();
        layoutParams.width = eachwidth;
        Animation translateAnimation = new TranslateAnimation(lastleft, left, layoutParams.topMargin, layoutParams.topMargin);//设置平移的起点和终点
        lastleft = left;
        translateAnimation.setDuration(200);//动画持续的时间为10s
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
//如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        translateAnimation.setAnimationListener(animationListener);
        selectline.setAnimation(translateAnimation);//给imageView添加的动画效果
        selectline.startAnimation(translateAnimation);
    }

    public ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {
            if(i == 1)
            {
                oldid = mViewPager.getCurrentItem();
            }
            if(i == 2)
            {
                doScollChange(oldid);
            }
        }
    };

    public Animation.AnimationListener animationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}