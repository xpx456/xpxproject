package intersky.conversation.view.adapter;

import android.os.Parcelable;
import android.view.View;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ConversationPageAdapter extends PagerAdapter
{
	private ArrayList<View> mViews;
	private String[] names;

	public ConversationPageAdapter(ArrayList<View> mViews, String[] names)
	{
		super();
		this.mViews = mViews;
		this.names = names;
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

	@Override
	public CharSequence getPageTitle(int position)
	{
		return names[position];
	}

}