package intersky.mail.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import intersky.apputils.AppUtils;
import intersky.echartoption.ObjectData;
import intersky.mail.R;
import intersky.mail.entity.MailFile;

public class InputView {

    public View view;
    public TextView title1;
    public EditText value;
    public PopupWindow popupWindow;
    public DoOkListener doOkListener;
    public MailFile mailFile;
    public InputView(Context context, DoOkListener doOkListener, int inputType, String hit){
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.input_view, null);
        title1 = view.findViewById(R.id.title);
        value = view.findViewById(R.id.edit);
        value.setHint(hit);
        value.setInputType(inputType);
        TextView ok = view.findViewById(R.id.ok);
        ok.setOnClickListener(okListener);
        this.doOkListener = doOkListener;
        TextView cancle = view.findViewById(R.id.cancle);
        cancle.setOnClickListener(cancleListener);
    }

    public void creat(Activity context, RelativeLayout shade, View location,String hit){
        value.setText(hit);
        this.mailFile = null;
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
        popupWindow = AppUtils.creatButtomView(context,shade,location,view);
    }

    public void creat(Activity context, RelativeLayout shade, View location,String hit,MailFile mailFile){
        value.setText(hit);
        this.mailFile = null;
        this.mailFile = mailFile;
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
        popupWindow = AppUtils.creatButtomView(context,shade,location,view);
    }

    public View.OnClickListener cancleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
            {
                popupWindow.dismiss();
            }
        }
    };

    public View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
            {
                popupWindow.dismiss();
            }
            if(doOkListener != null)
            {
                if(mailFile == null)
                {
                    mailFile = new MailFile();
                }
                if(value.getText().length() > 0)
                mailFile.name = value.getText().toString();
                else
                    mailFile.name = value.getHint().toString();
                doOkListener.OkListener();
                doOkListener.OkListener(mailFile);
            }
        }
    };

    public String getString()
    {
        if(value.getText().toString().length() == 0)
        {
            return value.getHint().toString();
        }
        else
        {
            return value.getText().toString();
        }
    }

    public interface DoOkListener{
        void OkListener();
        void OkListener(Object item);
    }
}
