package intersky.mail.view.adapter;

import android.os.Parcelable;
import android.view.View;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class LoderPageAdapter extends PagerAdapter
{
	private ArrayList<View> mViews;

	public LoderPageAdapter(ArrayList<View> mViews)
	{
		super();
		this.mViews = mViews;
	}

	@Override
	public int getCount()
	{

		return mViews.size();
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
		((ViewPager) arg0).removeView(mViews.get(arg1));
	}

	@Override
	public Object instantiateItem(View arg0, int arg1)
	{
		// TODO Auto-generated method stub
		((ViewPager) arg0).addView(mViews.get(arg1));
		return mViews.get(arg1);
		// ((ViewPager)
		// arg0).removeView(splidviews.get(arg1%splidviews.size()));
		// ((ViewPager) arg0).addView(splidviews.get(arg1%splidviews.size()));
		// return splidviews.get(arg1%splidviews.size());
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

}