package intersky.apputils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.animation.Animation;
import android.widget.ImageView;

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
