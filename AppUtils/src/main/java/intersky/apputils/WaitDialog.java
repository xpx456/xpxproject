package intersky.apputils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class WaitDialog {

    Dialog dialog;
    Context context;
    boolean cancelable = false;
    public WaitDialog(Context context)
    {
        this.context = context;
    };

    public boolean isshow()
    {
        if(dialog == null)
        {
            return false;
        }
        else
        {
            return dialog.isShowing();
        }
    }

    public void show()
    {
        if(dialog != null)
        {
            dialog.hide();
        }
        dialog = AppUtils.createLoadingDialogBlue(context,"");
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    public void show(View view,String msg)
    {
        if(dialog != null)
        {
            dialog.hide();
        }
        dialog = AppUtils.createLoadingDialogBlue(context,view,msg);
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    public void show(String msg)
    {
        if(dialog != null)
        {
            dialog.hide();
        }
        dialog = AppUtils.createLoadingDialogBlue(context,msg);
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    public String getText()
    {
        if(dialog != null)
        {
            TextView textView = dialog.findViewById(R.id.text);
            return textView.getText().toString();
        }
        return "";
    }

    public void updata(String msg)
    {
        if(dialog != null)
        {
            TextView textView = dialog.findViewById(R.id.text);
            textView.setText(msg);
        }
    }

    public void hide()
    {
        if(dialog != null)
        dialog.cancel();

    }

    public void dismiss()
    {
        if(dialog != null)
        dialog.cancel();
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setTitle(String title) {
        dialog.setTitle(title);
    }
}
