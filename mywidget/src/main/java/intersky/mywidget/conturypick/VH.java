package intersky.mywidget.conturypick;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import intersky.mywidget.R;

public class VH extends RecyclerView.ViewHolder {

    public TextView tvName;
    public TextView tvCode;
    public View line;

    public VH(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvCode = (TextView) itemView.findViewById(R.id.tv_code);
        line = itemView.findViewById(R.id.line);
    }
}
