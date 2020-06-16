package intersky.function.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import intersky.function.FunctionUtils;
import intersky.function.R;
import intersky.function.receiver.entity.Function;


@SuppressLint("InflateParams")
public class FunctionAdapter extends BaseAdapter {
    public ArrayList<Function> mFunctions;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isMore;

    public FunctionAdapter(Context context, ArrayList<Function> mFunctions, boolean isMore) {
        this.mContext = context;
        this.mFunctions = mFunctions;
        this.isMore = isMore;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mFunctions.size();
    }

    @Override
    public Function getItem(int position) {
        // TODO Auto-generated method stub
        return mFunctions.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Function mFunction = getItem(position);
        if(isMore == true)
        {
            convertView = mInflater.inflate(R.layout.funcs_grid_item_more, null);
        }
        else
        {
            convertView = mInflater.inflate(R.layout.funcs_grid_item, null);
        }
        ImageView mimage = (ImageView) convertView.findViewById(R.id.fun_img);
        TextView title = (TextView) convertView.findViewById(R.id.fun_text);
        TextView hit = (TextView) convertView.findViewById(R.id.hit);
        if(!mFunction.typeName.equals(Function.BLACK))
        {
            mimage.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            Bitmap bmap =getBitmap(mFunction);
            if(bmap == null)
            {
                if(mFunction.mCatalogueName.equals("web"))
                {
                    String url = createIconURLStringWeb(mFunction.iconName, new String());
                    Glide.with(mContext).load(url).into(mimage);
                }
                else
                {
                    String url = createIconURLString(mFunction.iconName, new String());
                    Glide.with(mContext).load(url).into(mimage);
                }

            }
            else
            {
                mimage.setImageBitmap(bmap);
            }

            title.setText(mFunction.mCaption);
            if(isMore == false)
            {
                if (mFunction.hintCount > 0) {
                    hit.setVisibility(View.VISIBLE);
                    if(mFunction.hintCount <= 99)
                    hit.setText(String.valueOf(mFunction.hintCount));
                    else
                        hit.setText("99+");
                } else {
                    hit.setVisibility(View.INVISIBLE);
                }
            }
        }
        else
        {
            mimage.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            hit.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }


    public final String createIconURLStringWeb(String path, String params) {
        String urlString = null;
        if(FunctionUtils.getInstance().service.https)
        {
            if(path.startsWith("/"))
            {
                if(params.length() != 0)
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path;
            }
            else
            {
                if(params.length() != 0)
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "/" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort// HOST
                            + "" + path;
            }
        }
        else
        {
            if(path.startsWith("/"))
            {
                if(params.length() != 0)
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path;
            }
            else
            {
                if(params.length() != 0)
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "/" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort// HOST
                            + "" + path;
            }
        }



        return urlString;
    }

    public final String createIconURLString(String path, String params) {
        String urlString = null;
        if(FunctionUtils.getInstance().service.https)
        {
            if(path.startsWith("/"))
            {
                if(params.length() != 0)
                    urlString = "https://" + FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // PORT
                            + "" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path;
            }
            else
            {
                if(params.length() != 0)
                    urlString = "https://" + FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // PORT
                            + "/" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "https://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "/" + path;
            }
        }
        else
        {
            if(path.startsWith("/"))
            {
                if(params.length() != 0)
                    urlString = "http://" + FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // PORT
                            + "" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "" + path;
            }
            else
            {
                if(params.length() != 0)
                    urlString = "http://" + FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // PORT
                            + "/" + path // PAGE
                            + "?" + params; // params
                else
                    urlString = "http://" +FunctionUtils.getInstance().service.sAddress // HOST
                            + ":" + FunctionUtils.getInstance().service.sPort // HOST
                            + "/" + path;
            }
        }


        return urlString;
    }

    public Bitmap getBitmap(Function func) {
        Bitmap bmap = null;

        if (func.mCaption.equals(mContext.getResources().getString(R.string.function_businessWarn))) {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.business_warning);
        } else if (func.mCaption.equals(mContext.getResources().getString(R.string.function_taskExamine))) {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.main_approval);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_businesscard)))
        {

            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cardmanager);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_system)))
        {
            if(func.typeName.equals(Function.NOTICE))
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.system);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.system_m);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_newtask)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.task);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_newproject)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.projectm);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_newmail)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mailcreat);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_workreport)))
        {
            if(func.typeName.equals(Function.WORKREPORT))
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.workreport);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.workreport_m);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_sign)))
        {
            if(func.typeName.equals(Function.SIGN))
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sign);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sign_m);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_leave)))
        {
            if(func.typeName.equals(Function.LEAVE))
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.leave);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.leave_m);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_leave_n)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.leave_m);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_schedule)))
        {
            if(func.typeName.equals(Function.DATE))
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.schedule);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.schedule_m);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_date_n)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.schedule_m);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_attdence)))
        {
            if(func.typeName.equals(Function.WORKATTDENCE))
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.attdence);
            else
                bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.attdence_m);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_systemmesage_n)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.system_m);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_vote)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.vote);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_task)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.taskoa);
        }
        else if(func.mCaption.equals(mContext.getResources().getString(R.string.function_vote_n)))
        {
            bmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.vote_m);
        }
        return bmap;
    }

    private static class ViewHolder {
        ImageView mimage;

    }

}
