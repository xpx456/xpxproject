package intersky.sign.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.sign.SignManager;
import intersky.sign.asks.SignAsks;
import intersky.sign.prase.SignPrase;
import intersky.xpxnet.net.NetObject;

//03
public class SignManagerHandler extends Handler
{

    public Context context;

    public SignManagerHandler(Context context)
    {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case SignAsks.EVENT_GET_LIST_SUCCESS:
                SignPrase.praseSignHit((NetObject) msg.obj,this.context);
                this.context.sendBroadcast(new Intent(SignManager.ACTION_UPDATE_SIGN_COUNT));
                break;

        }
        super.handleMessage(msg);
    }

}