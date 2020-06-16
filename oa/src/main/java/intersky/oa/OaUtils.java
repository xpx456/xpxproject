package intersky.oa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.chat.ContactManager;
import intersky.xpxnet.net.HTTP;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.Service;
import intersky.xpxnet.net.URLEncodedUtils;
import intersky.xpxnet.net.nettask.PostNetTask;
import xpx.map.MapManager;

public class OaUtils {

    public static final String APP_PATH = Environment.getExternalStorageDirectory().getPath() + "/intersky/temp";
    public static final int EVENT_UPLOAD_FILE_RESULT = 3060000;
    public static final int EVENT_UPLOAD_FILE_RESULT_FAIL = 3060001;
    public Context context;
    public Account mAccount;
    public Service service;
    public MapManager mapManager;
    public OaHandler mOaHandler = new OaHandler();
    public ContactManager mContactManager;
    public volatile static OaUtils mOaUtils;
    public static OaUtils init(Context context,MapManager mapManager,ContactManager contactManager) {

        if (mOaUtils == null) {
            synchronized (OaUtils.class) {
                if (mOaUtils == null) {
                    mOaUtils = new OaUtils(context,mapManager,contactManager);
                }
                else
                {
                    mOaUtils.context = context;
                    mOaUtils.mapManager = mapManager;
                    mOaUtils.mContactManager = contactManager;
                }
            }
        }
        return mOaUtils;
    }

    public OaUtils(Context context,MapManager mapManager,ContactManager contactManager) {
        this.context = context;
        this.mapManager = mapManager;
        this.mContactManager = contactManager;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setAccount(Account account) {
        mAccount = account;
    }

    public static OaUtils getOaUtils() {
        return mOaUtils;
    }

    public String praseClodAttchmentUrl(String hash) {
        String urlString = createURLStringoa(service,OaAsks.ATTACHMENT_PATH);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("method", OaAsks.ATTACHMENT_GET));
        nvps.add(new NameValuePair("hash", hash));
        urlString = urlString + "?" + URLEncodedUtils.format(nvps, HTTP.UTF_8);
        return urlString;
    }

    public String praseClodAttchmentUrl(String hash, int size) {
        String urlString = createURLStringoa(service,OaAsks.ATTACHMENT_PATH);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new NameValuePair("method", OaAsks.ATTACHMENT_GET));
        nvps.add(new NameValuePair("hash", hash));
        nvps.add(new NameValuePair("width", String.valueOf(size)));
        nvps.add(new NameValuePair("height", String.valueOf(size)));
        urlString = urlString + "?" + URLEncodedUtils.format(nvps, HTTP.UTF_8);
        return urlString;
    }

    public void uploadAttchments(ArrayList<File> files, Handler handler,String base) {
        UploadThread mUploadThread = new UploadThread(files,handler,base);
        mUploadThread.start();
    }

    public void doSendFiles(OaDataItem mOaDataItem) {
        String urlString = createURLStringoa(service,OaAsks.ATTACHMENT_PATH);
        NameValuePair nameValuePair = new NameValuePair("method",OaAsks.ATTACHMENT_LIST);
        mOaDataItem.nameValuePairs.add(nameValuePair);
        PostNetTask postNetTask = new PostNetTask(urlString,mOaHandler,OaHandler.EVENT_UPLOAD_FINISH,context,mOaDataItem.nameValuePairs,mOaDataItem);
        NetTaskManager.getInstance().addNetTask(postNetTask);
    }

    public void praseHash(NetObject object) {
        OaDataItem oaDataItem = (OaDataItem) object.item;
        try {
            JSONObject mobj = new JSONObject(object.result);
            if (mobj.has("status")) {
                if (mobj.getString("status").equals("500")) {
                    Message message = new Message();
                    message.what = EVENT_UPLOAD_FILE_RESULT_FAIL;
                    message.obj = "";
                    oaDataItem.handler.sendMessage(message);
                    return;
                }
            }
            if (mobj.has("token")) {
                NetUtils.getInstance().token = mobj.getString("token");
            }
            if (mobj.has("code")) {
                if (mobj.getInt("code") != 0)
                {
                    JSONArray data = null;
                    data = mobj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jo = data.getJSONObject(i);
                        if (jo.getInt("status") == 200) {
                            if (oaDataItem.base.length() != 0)
                                oaDataItem.base += ","+jo.getString("hash");
                            else
                                oaDataItem.base += jo.getString("hash");
                        }
                    }
                    Message message = new Message();
                    message.what = EVENT_UPLOAD_FILE_RESULT;
                    message.obj = oaDataItem.base;
                    oaDataItem.handler.sendMessage(message);
                }
                else
                {
                    Message message = new Message();
                    message.what = EVENT_UPLOAD_FILE_RESULT_FAIL;
                    message.obj = "";
                    oaDataItem.handler.sendMessage(message);
                    return;
                }

            }
            else
            {
                JSONArray data = null;
                data = mobj.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jo = data.getJSONObject(i);
                    if (jo.getInt("status") == 200) {
                        if (oaDataItem.base.length() != 0)
                            oaDataItem.base += ","+jo.getString("hash");
                        else
                            oaDataItem.base += jo.getString("hash");
                    }
                }
                Message message = new Message();
                message.what = EVENT_UPLOAD_FILE_RESULT;
                message.obj = oaDataItem.base;
                oaDataItem.handler.sendMessage(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Message message = new Message();
            message.what = EVENT_UPLOAD_FILE_RESULT_FAIL;
            message.obj = "";
            oaDataItem.handler.sendMessage(message);
        }

    }

    public boolean getFileType(String aname) {
        String name = "";
        if (aname != null) {
            name = aname.toLowerCase();
        }

        if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif")
                || name.endsWith(".jpeg") || name.endsWith(".bmp") || name.endsWith(".pcx")
                || name.endsWith(".dxf") || name.endsWith(".wmf") || name.endsWith(".tga")) {
            return true;

        }
        else
        {
            return false;
        }
    }

    public File getUpFile(File file) {
        int size = (int) (file.length()/1024/1024)+1;

        if(getFileType(file.getName()))
        {
            File mfile = file;
            return mfile;
        }
        if(size > 1)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = size;
            options.inJustDecodeBounds = false;
            Bitmap mimage = BitmapFactory.decodeFile(file.getPath(), options);
            File mfile = saveTempBitmap(mimage,file.getName());
            mimage.recycle();
            System.gc();
            return mfile;
        }
        else
        {
            return file;
        }
    }

    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static File saveTempBitmap( Bitmap bMap,String name)
    {
        /* sd卡是否可用 */
        if( !isSdCardExist() )
        {
            /* sd卡不可用提示 */
            return null;
        }



        File file = new File( APP_PATH + "/" + name+"uptemp" + name.substring(name.lastIndexOf("."),name.length()));
        file.setWritable(true);
        file.setReadable(true);
        file.setExecutable(true);

        /*
         * 文件已经存在，则删除
         */

        if(bMap == null)
        {
            return null;
        }

        if( file.exists() )
        {
            file.delete();
        }

        /*
         * 保存文件
         */
        try
        {

            file.createNewFile();
            FileOutputStream outS = new FileOutputStream( file );
            bMap.compress( Bitmap.CompressFormat.PNG, 100, outS );
            outS.flush();
            outS.close();
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return file;
    }

    public void getAttachmentSize(Attachment attachment) {
        OaAsks.getAttachments(attachment.mRecordid,mOaHandler,context,attachment);
    }

    public void getAttachmentSize(Attachment attachment,Handler handler) {
        OaAsks.getAttachments(attachment.mRecordid,handler,context,attachment);
    }

    public static Attachment praseAddtchment(NetObject net) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return null;
            }
            Attachment attachment = (Attachment) net.item;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo.getString("hash").equals(attachment.mRecordid)) {
                    attachment.mSize = jo.getLong("size");
                }
            }
            return  attachment;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static String createURLStringoa(Service service,String path) {
        if(mOaUtils.mAccount.cloudServer.contains("http")||mOaUtils.mAccount.cloudServer.contains("https"))
        {
            return ( mOaUtils.mAccount.cloudServer // HOST
                    // PORT
                    + "/" + path); // PAGE
        }
        else
        {
            if(service == null)
            {
                return ("http://" + mOaUtils.mAccount.cloudServer // HOST
                        // PORT
                        + "/" + path); // PAGE
            }
            else
            {
                if(service.https)
                {
                    return ("https://" + mOaUtils.mAccount.cloudServer // HOST
                            // PORT
                            + "/" + path); // PAGE
                }
                else
                {
                    return ("http://" + mOaUtils.mAccount.cloudServer // HOST
                            // PORT
                            + "/" + path); // PAGE
                }
            }

        }
    }
}
