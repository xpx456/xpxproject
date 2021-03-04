package intersky.oa;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;

import intersky.xpxnet.net.NameValuePair;

public class UploadThread extends Thread{


    public ArrayList<File> files;
    public Handler handler;
    public String mBase;

    public UploadThread(ArrayList<File> files,Handler handler,String mBase) {
        this.files = files;
        this.handler = handler;
        this.mBase = mBase;
    }

    @Override
    public void run() {
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        for(int i = 0 ; i < files.size() ; i++) {
            File file = OaUtils.getOaUtils().getUpFile(files.get(i));
            NameValuePair item = new NameValuePair("file_name[]",file.getPath());
            item.isFile = true;
            item.path = file.getPath();
            items.add(item);
        }
        if(items.size() > 0)
        {
            Message message = new Message();
            message.what = OaHandler.EVENT_UPLOAD_FILES;
            message.obj = new OaDataItem(handler,items,mBase);
            if(OaUtils.getOaUtils().mOaHandler != null)
            OaUtils.getOaUtils().mOaHandler.sendMessage(message);

        }
        else
        {
            Message message = new Message();
            message.what = OaUtils.EVENT_UPLOAD_FILE_RESULT;
            message.obj = "dsaf";
            handler.sendMessage(message);
        }
    }
}
