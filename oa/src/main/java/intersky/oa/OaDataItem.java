package intersky.oa;

import android.os.Handler;

import java.util.ArrayList;

import intersky.xpxnet.net.NameValuePair;

public class OaDataItem {

    public Handler handler;
    public ArrayList<NameValuePair> nameValuePairs;
    public String base = "";

    public OaDataItem(Handler handler,ArrayList<NameValuePair> nameValuePairs,String base) {
        this.handler = handler;
        this.nameValuePairs = nameValuePairs;
        this.base = base;
    }
}
