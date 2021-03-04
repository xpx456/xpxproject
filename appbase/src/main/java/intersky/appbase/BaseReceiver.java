package intersky.appbase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;


public class BaseReceiver extends BroadcastReceiver {

    public IntentFilter intentFilter;
    public Receive onReceive;
    public Handler handler;
    public BaseReceiver()
    {
        this.intentFilter = new IntentFilter();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(onReceive != null) {
            onReceive.onReceive(handler,context,intent);
        }
    }

    public interface Receive{
        void onReceive(Handler handler,Context context, Intent intent);
    }
}
