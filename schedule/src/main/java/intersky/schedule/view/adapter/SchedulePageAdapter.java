package intersky.schedule.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.GridView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import intersky.schedule.R;
import intersky.schedule.ScheduleHelper;

public class SchedulePageAdapter extends PagerAdapter
{
	public ArrayList<View> mViews;
	private Context mContext;
	private Handler mCircleHandler;
    private ScheduleHelper mScheduleHelper;
	public GridView gridView;
	public String key;
	public int mRealpos;
	public SchedulePageAdapter(ArrayList<View> mViews, ScheduleHelper mScheduleHelper)
	{
		super();
		this.mViews = mViews;
		this.mScheduleHelper = mScheduleHelper;
	}

	@Override
	public int getCount()
	{
		return Integer.MAX_VALUE;
	}


	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object)
	{
		// TODO Auto-generated method stub
		// return super.getItemPosition(object);
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2)
	{
		// TODO Auto-generated method stub
//		((ViewPager) arg0).removeView(mViews.get(arg1));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		// TODO Auto-generated method stub



		try {
			View mview = mViews.get(position % mViews.size());
			GridView grid = (GridView) mview.findViewById(R.id.date_List);
			gridView = grid;
			key = String.format("%04d-%02d",mview.getTag(),position % mViews.size()+1);
			grid.setOnItemClickListener(mScheduleHelper.clickDatelistener);
			DateAdapter mDateAdapter = mScheduleHelper.mScheduleAdapters.get(key);
			grid.setAdapter(mDateAdapter);
			ViewParent vp =mview.getParent();
			if (vp!=null){
				ViewGroup parent = (ViewGroup)vp;
				parent.removeView(mview);
			}
			container.addView(mview);
		} catch (Exception e) {
		}
		return mViews.get(position % mViews.size());

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0)
	{
		// TODO Auto-generated method stub
		// ((ViewPager) arg0).removeAllViews();
	}

	@Override
	public void finishUpdate(View arg0)
	{
		// TODO Auto-generated method stub

	}

	public void updataGride() {
		gridView.setAdapter(mScheduleHelper.mScheduleAdapters.get(key));
	}
}