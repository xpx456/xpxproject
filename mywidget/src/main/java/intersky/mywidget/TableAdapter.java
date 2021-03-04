package intersky.mywidget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.GrideViewHolder> {

    public ArrayList<TableItem> mDatas;
    public static final int TYPE_HEAD = 0;
    public static final int TYPE_LEFT = 1;
    public static final int TYPE_GRID = 2;
    public int type;
    public int cloums;
    public OnItemClickListener mOnItemClickListener;

    public TableAdapter(ArrayList<TableItem> mDatas, int type, int cloums)
    {
        this.mDatas = mDatas;
        this.type = type;
        this.cloums = cloums;
    }

    @NonNull
    @Override
    public GrideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_text_item, parent, false);
        return new GrideViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GrideViewHolder holder, final int position) {
        final TableItem tableItem = mDatas.get(position);
        holder.mTextView.setWidth(tableItem.mWidth);
        if(type == TYPE_LEFT) {
            if ((position) % 2 == 0) {
                holder.content.setBackgroundResource(R.drawable.grid_cell_shape2);
            } else {
                holder.content.setBackgroundResource(R.drawable.grid_cell_shape);
            }
        }
        else if(type == TYPE_HEAD) {
            holder.content.setBackgroundResource(R.drawable.grid_cell_shape);
        }
        else {
            if((position/cloums)%2 == 0) {
                holder.content.setBackgroundResource(R.drawable.grid_cell_shape2);
            }
            else {
                holder.content.setBackgroundResource(R.drawable.grid_cell_shape);
            }
        }
        holder.mTextView.setText(tableItem.mTitle);
        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position,holder.mTextView);
                }
            });
            holder.itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position,holder.mTextView);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class GrideViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextView;
        public LinearLayout content;
        public GrideViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.gride_text);
            content = itemView.findViewById(R.id.content);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position, TextView mTextView);
        void onLongClick(int position, TextView mTextView);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }
}
