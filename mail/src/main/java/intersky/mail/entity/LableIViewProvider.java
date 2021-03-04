package intersky.mail.entity;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import intersky.mail.R;
import intersky.mywidget.scollpick.IViewProvider;
import intersky.select.entity.Select;

public class LableIViewProvider implements IViewProvider {

    public Select nowSelect;

    @Override
    public int resLayout() {
        return R.layout.mail_lable_item2;
    }

    @Override
    public void onBindView(@NonNull View view, @Nullable Object itemData) {
        Select select = (Select) itemData;
        View tv = view.findViewById(R.id.item_mial_color);
        View tv2 = view.findViewById(R.id.item_mial_color2);
        view.setTag(itemData);
        if(select != null)
        {
            tv.setBackgroundColor(Color.parseColor(select.mColor));
            tv2.setBackgroundColor(Color.parseColor(select.mColor));
        }
    }

    @Override
    public void updateView(@NonNull View itemView, boolean isSelected) {
        View tv = itemView.findViewById(R.id.item_mial_color);
        View tv2 = itemView.findViewById(R.id.item_mial_color2);
        RelativeLayout main = itemView.findViewById(R.id.main);
        if(isSelected)
        {
            tv.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.VISIBLE);
            nowSelect = (Select) itemView.getTag();
            main.setBackgroundResource(R.drawable.shape_search_bg2);
        }
        else
        {
            tv.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.INVISIBLE);
            main.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }
}
