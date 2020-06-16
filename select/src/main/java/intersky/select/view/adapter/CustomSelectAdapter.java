package intersky.select.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import intersky.select.entity.CustomSelect;
import intersky.select.entity.Select;

public class CustomSelectAdapter extends BaseAdapter
{
    public CustomSelect select;
    public ArrayList<CustomSelect> mSelectMores;
    public Context mContext;
    public LayoutInflater mInflater;
    public CustomSelectAdapter(Context context, ArrayList<CustomSelect> mSelectMores)
    {
        this.mContext = context;
        this.mSelectMores = mSelectMores;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mSelectMores.size();
    }

    @Override
    public CustomSelect getItem(int position)
    {
        // TODO Auto-generated method stub
        return mSelectMores.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        return convertView;
    }

    public void setSelect(String id) {
        if(select != null)
        {
            select.iselect = false;
            select = null;
        }
        for(int i = 0 ; i < mSelectMores.size() ; i++)
        {
            if(mSelectMores.get(i).mId.equals(id))
            {
                mSelectMores.get(i).iselect = true;
                select = mSelectMores.get(i);
                break;
            }
        }
    }

    public boolean setSelect(CustomSelect select) {
        if(select.iselect == false)
        {
            select.iselect = true;
            if(this.select != null)
            {
                this.select.iselect = false;
            }
            this.select = select;
            return true;
        }
        else
        {
            select.iselect = false;
            if(this.select != null)
            {
                this.select.iselect = false;
                this.select = null;
            }
            return false;
        }
    }
}
