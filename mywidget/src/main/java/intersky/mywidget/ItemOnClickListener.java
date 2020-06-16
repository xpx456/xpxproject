package intersky.mywidget;

import android.view.View;

public class ItemOnClickListener implements View.OnClickListener {

    public Object item;
    public Click onClick;


    public ItemOnClickListener(Object item, Click onClick)
    {
        this.item = item;
        this.onClick = onClick;
    }

    @Override
    public void onClick(View v) {
        onClick.doClick();
    }

    public interface Click{

        void doClick();
    }
}
