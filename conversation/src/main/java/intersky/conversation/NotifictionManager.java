package intersky.conversation;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import intersky.appbase.Local.LocalData;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Register;
import intersky.apputils.AppUtils;
import intersky.budge.shortcutbadger.ShortcutBadger;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.conversation.entity.BrodcastData;
import intersky.conversation.entity.Channel;
import intersky.conversation.entity.NotificationData;
import intersky.xpxnet.net.NetUtils;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotifictionManager {

    public static final String ACTION_UPDATA_CONVERSATION_CLOD_IWABAPP = "ACTION_UPDATA_CONVERSATION_CLOD_IWABAPP";
    public static final String ACTION_UPDATA_CONVERSATION_CLOD_IWAB = "ACTION_UPDATA_CONVERSATION_CLOD_IWAB";
    public static final String ACTION_KICK_OUT = "ACTION_KICK_OUT";
    public static final String ACTION_UPDATA_CONVERSATION_CLOD = "ACTION_UPDATA_CONVERSATION_CLOD";
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static final String channel_Id_download = "download";
    private static final String channel_Id_tip = "tip";
    private static final String channel_Id_conversation = "conversation";

//    private static final String channel_Name_download = "文件下载";
//    private static final String channel_Name_tip = "提示";
//    private static final String channel_Name_conversation = "会话";

    private static final int importance_tip = NotificationManager.IMPORTANCE_HIGH;
    private static final int importance_download = NotificationManager.IMPORTANCE_DEFAULT;
    private static final int importance_conversation = NotificationManager.IMPORTANCE_HIGH;

    public static final int OA_CONVERSATIONDID_REPORT = 100;
    public static final int OA_CONVERSATIONDID_LEAVE = 101;
    public static final int OA_CONVERSATIONDID_NOTICE = 102;
    public static final int OA_CONVERSATIONDID_VOTE = 103;
    public static final int OA_CONVERSATIONDID_GROP = 104;
    public static final int OA_CONVERSATIONDID_MAIL = 105;
    public static final int OA_CONVERSATIONDID_APPROVE = 106;
    public static final int OA_CONVERSATIONDID_TASK = 107;
    public static final int OA_CONVERSATIONDID_MESSAGE_BASE = 200;
    public static final int CHAT_BASE_ID = 3000;
    public static NotifictionManager mNotifictionManager;
    private NotificationChannel channel;
    public NotificationManager manager;
    public Context context;
    public int smallicon;
    public int largeicon;
    public int chataddid = 0;
    public HashMap<String,Integer> chatMessageid = new HashMap<String,Integer>();
    public HashMap<String,String> contactShows = new HashMap<String,String>();
    public HashMap<String,String> membernames = new HashMap<String,String>();
    public DataMeasure dataMeasure;
    public String openClassname;
    public int ids = 0;
    public HashMap<String,Integer> oldMessageid = new HashMap<String,Integer>();
    public HashMap<String,Channel> channelHashMap = new HashMap<String,Channel>();

    public static NotifictionManager init(Context context,int samllicon,int largeicon) {
        mNotifictionManager = new NotifictionManager(context,samllicon,largeicon);
        return mNotifictionManager;
    }

    public static NotifictionManager init(Context context,int samllicon,int largeicon,ArrayList<Channel> channels) {
        mNotifictionManager = new NotifictionManager(context,samllicon,largeicon,channels);
        return mNotifictionManager;
    }

    public static NotifictionManager init(Context context,int samllicon,int largeicon,ArrayList<Channel> channels,String openClassname,DataMeasure dataMeasure) {
        mNotifictionManager = new NotifictionManager(context,samllicon,largeicon,channels);
        mNotifictionManager.dataMeasure = dataMeasure;
        mNotifictionManager.openClassname = openClassname;
        return mNotifictionManager;
    }

    public NotifictionManager(Context context,int samllicon,int largeicon) {
        this.context = context;
        this.smallicon = samllicon;
        this.largeicon = largeicon;
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (areNotificationsEnabled() == false) {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*调用createNotificationChannel()方法，传入id，名字，等级参数*/
            createNotificationChannel(channel_Id_download, context.getString(R.string.channel_Name_download), importance_download);
            createNotificationChannel(channel_Id_tip, context.getString(R.string.channel_Name_tip), importance_tip);
            createNotificationChannel(channel_Id_conversation, context.getString(R.string.channel_Name_conversation), importance_conversation);
        }

    }

    public NotifictionManager(Context context, int samllicon, int largeicon, ArrayList<Channel> channels) {
        this.context = context;
        this.smallicon = samllicon;
        this.largeicon = largeicon;
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (areNotificationsEnabled() == false) {

        }

        for(int i = 0 ; i < channels.size() ; i++)
        {
            String modules[] = channels.get(i).moduleid.split(",");
            for(int j = 0 ; j < modules.length ; j++)
            {
                channelHashMap.put(modules[j],channels.get(i));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel(channels.get(i).id, channels.get(i).name, channels.get(i).leave);
                }
                else
                {
                    channels.get(i).messageid = getSid(channels.get(i).id);
                }
            }

        }
    }

    public NotificationOper registerNotification(Channel channel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            createNotificationChannel(channel.id, channel.name, channel.leave);
            return new NotificationOper(channel,manager);
        }
        else
        {
            channel.messageid = getSid(channel.id);
            return new NotificationOper(manager,channel.getNid("0"),channel.id);
        }
    }

    public NotificationOper getOper(Channel channel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            return new NotificationOper(channel,manager);
        }
        else
        {
            return new NotificationOper(manager,channel.getNid("0"),channel.id);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channel_id, String channel_name, int importance) {
        channel = new NotificationChannel(channel_id, channel_name, importance);
        manager.deleteNotificationChannel(channel_id);
        manager.deleteNotificationChannelGroup(channel_id);
        if(importance == NotificationManager.IMPORTANCE_LOW)
        {
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setSound(null,null);
        }
        manager.createNotificationChannel(channel);
    }

    public void creatDownloadNotifiction(Context context, int id, String classname, String title, String content, int max, int now) {
        PendingIntent pi;
        try {
            Intent intent = null;
            intent = new Intent(context, Class.forName(classname));
            pi = PendingIntent.getActivity(context, 0, intent, 0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //构建通知对象需传入上下文和通知渠道ID
        Notification notification = new NotificationCompat.Builder(context, channel_Id_download)
                //标题
                .setContentTitle(title)
                //内容
                .setContentText(content)
                //自动取消
                .setAutoCancel(false)
                //小图标
                .setSmallIcon(smallicon)
                //系统时间
                .setWhen(System.currentTimeMillis())
                //大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                //要跳转的意图
//                .setContentIntent(pi)
                //处于锁屏状态时LED灯一闪一暗
                .setLights(Color.GREEN, 1000, 1000)
                .setProgress(max, now, false)
                //震动200Ms
                // VISIBILITY_PUBLIC没有锁屏时会显示通知
                // VISIBILITY_PRIVATE任何情况都会显示通知
                // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{0, 200})
                //设置横幅通知栏，需开启通知横幅提醒权限
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                /* 显示很长的文字
                 .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                .build();
        manager.notify(id, notification);
    }

    public void creatTipNotifiction(Context context, int id, String classname, String title, String content) {
        PendingIntent pi;
        try {
            Intent intent = null;
            intent = new Intent(context, Class.forName(classname));
            pi = PendingIntent.getActivity(context, 0, intent, 0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //构建通知对象需传入上下文和通知渠道ID
        Notification notification = new NotificationCompat.Builder(context, channel_Id_tip)
                //标题
                .setContentTitle(title)
                //内容
                .setContentText(content)
                //自动取消
                .setAutoCancel(true)
                //小图标
                .setSmallIcon(smallicon)
                //系统时间
                .setWhen(System.currentTimeMillis())
                //大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                //要跳转的意图
//                .setContentIntent(pi)
                //处于锁屏状态时LED灯一闪一暗
                .setLights(Color.GREEN, 1000, 1000)
                //震动200Ms
                // VISIBILITY_PUBLIC没有锁屏时会显示通知
                // VISIBILITY_PRIVATE任何情况都会显示通知
                // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{0, 200})
                //设置横幅通知栏，需开启通知横幅提醒权限
                .setPriority(NotificationCompat.PRIORITY_MAX)
                /* 显示很长的文字
                 .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                .build();
        manager.notify(id, notification);
    }

    public void creatConversationNotifiction(Context context, int id, String classname, String title, String content,JSONObject jsonObject) {
        PendingIntent pi = null;
        String type = "";
        int detialid = 0;
        try {
            JSONObject msg = jsonObject.getJSONObject("message");
            type = msg.getString("module");
            detialid = msg.getInt("module_id");
            Intent intent = null;
            intent = new Intent(context, Class.forName("com.intersky.android.receiver.NotifictionOpenReceiver"));
            intent.putExtra("json",jsonObject.toString());
            pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int sid = getConversationId(type,detialid);
        //构建通知对象需传入上下文和通知渠道ID
        Notification notification = new NotificationCompat.Builder(context, channel_Id_conversation)
                //标题
                .setContentTitle(title)
                //内容
                .setContentText(content)
                //自动取消
                .setAutoCancel(true)
                //小图标
                .setSmallIcon(smallicon)
                //系统时间
                .setWhen(System.currentTimeMillis())
                //大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                //要跳转的意图
                .setContentIntent(pi)
                //处于锁屏状态时LED灯一闪一暗
                .setLights(Color.GREEN, 1000, 1000)
                //震动200Ms
                // VISIBILITY_PUBLIC没有锁屏时会显示通知
                // VISIBILITY_PRIVATE任何情况都会显示通知
                // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{0, 200})
                //设置横幅通知栏，需开启通知横幅提醒权限
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi)
                /* 显示很长的文字
                 .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                .build();
        manager.notify(sid, notification);
    }

    public void creatBigwinerConversationNotifiction(Context context, int id, String classname, String title, String content,Intent intent1) {
        PendingIntent pi = null;
        String type = "";
        String detialid = "";
        try {
            type = intent1.getStringExtra("module");
            detialid = intent1.getStringExtra("detialid");
            Intent intent = null;
            intent = new Intent(context, Class.forName("com.bigwiner.android.receiver.NotifictionOpenReceiver"));
            intent.putExtra("json",intent1.getStringExtra("json"));
            pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int sid = getConversationId(type,intent1.getIntExtra("module_id",0));

        //构建通知对象需传入上下文和通知渠道ID
        Notification notification = new NotificationCompat.Builder(context, channel_Id_conversation)
                //标题
                .setContentTitle(title)
                //内容
                .setContentText(content)
                //自动取消
                .setAutoCancel(true)
                //小图标
                .setSmallIcon(smallicon)
                //系统时间
                .setWhen(System.currentTimeMillis())
                //大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                //要跳转的意图
                .setContentIntent(pi)
                //处于锁屏状态时LED灯一闪一暗
                .setLights(Color.GREEN, 1000, 1000)
                //震动200Ms
                // VISIBILITY_PUBLIC没有锁屏时会显示通知
                // VISIBILITY_PRIVATE任何情况都会显示通知
                // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{0, 200})
                //设置横幅通知栏，需开启通知横幅提醒权限
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi)
                /* 显示很长的文字
                 .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                .build();
        manager.notify(sid, notification);
    }

    public void updateNotifiction() {

    }

    public void removeNotifiction(int id) {
        manager.cancel(id);
    }

    public boolean areNotificationsEnabled() {
        if (Build.VERSION.SDK_INT >= 24) {
            return manager.areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager appOps =
                    (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE,
                        Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg)
                        == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException
                    | InvocationTargetException | IllegalAccessException | RuntimeException e) {
                return true;
            }
        } else {
            return true;
        }
    }

    public void measureMessage(Map<String, String> extraMap, String title) {
        try {


            SharedPreferences sharedPre = context.getSharedPreferences(LocalData.LOGIN_INFO, 0);
            boolean flag = sharedPre.getBoolean(LocalData.LOGIN_INFO_STATU, false);
            if (flag == false) {
                return;
            }
            String a = extraMap.get("data");
            JSONObject jsonObject = new JSONObject(a);
            JSONObject msg = jsonObject.getJSONObject("message");
            if (msg.has("source_type")) {

                if (msg.getString("source_type").toLowerCase().contains("iweb[api]")) {
                    Intent intent = new Intent();
                    intent.putExtra("json", jsonObject.toString());
                    intent.setAction(ACTION_UPDATA_CONVERSATION_CLOD_IWABAPP);
                    if (context != null) {
                        context.sendBroadcast(intent);
                    }
                } else if (msg.getString("source_type").toLowerCase().contains("iweb")) {
                    String token = "";
                    if (msg.getString("source_type").toLowerCase().contains("iweb[system]") && msg.getInt("module") == 100) {
                        token = msg.getString("module_id");
                        if (token.equals(NetUtils.getInstance().token) && NetUtils.getInstance().token.length() > 0) {

                            Intent intent = new Intent();
                            intent.setAction(ACTION_KICK_OUT);
                            if (context != null)
                            context.sendBroadcast(intent);
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("id", msg.getString("message_id"));
                        intent.putExtra("socetype", msg.getString("source_type"));
                        intent.setAction(ACTION_UPDATA_CONVERSATION_CLOD_IWAB);
                        if (context != null) {
                            context.sendBroadcast(intent);
                        }
                        if (msg.getString("source_type").toLowerCase().equals("iweb[im]") || msg.getString("source_type").toLowerCase().equals("iweb[mail]")
                                || msg.getString("source_type").toLowerCase().equals("iweb[workflow]") || msg.getString("source_type").toLowerCase().equals("iCloud[workflow]")) {
                            String classname = "";
                            creatConversationNotifiction(context, Integer.valueOf(extraMap.get("_ALIYUN_NOTIFICATION_ID_")),classname,title,msg.getString("title")
                            +":"+msg.getString("content"),jsonObject);
                            intent.putExtra("source_id", msg.getString("source_id"));
                        }

                    }

                } else if (msg.getString("source_type").equals("oa")) {

                    Intent intent = new Intent();
                    intent.putExtra("id", msg.getString("message_id"));
                    intent.putExtra("module",msg.getString("module"));
                    intent.setAction(ACTION_UPDATA_CONVERSATION_CLOD);
                    if (context != null) {
                        context.sendBroadcast(intent);
                    }
                    String classname = "";
                    creatConversationNotifiction(context, Integer.valueOf(extraMap.get("_ALIYUN_NOTIFICATION_ID_"))
                            ,classname,title,msg.getString("title")+":"+msg.getString("content"),jsonObject);

                }
            } else {
                Intent intent = new Intent();
                intent.putExtra("id", msg.getString("message_id"));
                intent.setAction(ACTION_UPDATA_CONVERSATION_CLOD);
                if (context != null) {
                    context.sendBroadcast(intent);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void measureBigwinerMessage(Map<String, String> extraMap, String title,boolean islogin) {
        try {

            if (islogin == false) {
                return;
            }
            String a = extraMap.get("data");
            JSONObject jsonObject = new JSONObject(a);
            JSONObject msg = jsonObject.getJSONObject("message");
            Intent intent = new Intent();
            intent.putExtra("id", msg.getString("message_id"));
            intent.putExtra("module",jsonObject.getString("module"));
            intent.putExtra("json",jsonObject.toString());
            if(jsonObject.getString("module").equals("MemberManager"))
            {
                intent.putExtra("detialid",msg.getString("source_id"));
                int moduleid = 0;
                if(chatMessageid.containsKey(msg.getString("source_id")))
                {
                    moduleid = chatMessageid.get(msg.getString("source_id"));
                }
                else
                {
                    moduleid = getChatid();
                    chatMessageid.put(msg.getString("source_id"),moduleid);
                }
                intent.putExtra("moduleid",moduleid);
                membernames.put(msg.getString("source_id"),msg.getString("message_id"));

//                int count = BigWinerDBHelper.getInstance(context).getAllUnReadCount(Conversation.CONVERSATION_TYPE_MESSAGE);
//                ShortcutBadger.applyCount(context, AppUtils.getHit(count));
            }
            else
            {
                intent.putExtra("detialid",jsonObject.getString("module_id"));
                intent.putExtra("moduleid",jsonObject.getString("module_id"));
            }
            intent.setAction(ACTION_UPDATA_CONVERSATION_CLOD);
            if (context != null) {
                context.sendBroadcast(intent);
            }
            String classname = "";
            if(jsonObject.getString("module").equals("MemberManager"))
            {
                if(!contactShows.containsKey(msg.getString("source_id")))
                creatBigwinerConversationNotifiction(context, Integer.valueOf(extraMap.get("_ALIYUN_NOTIFICATION_ID_"))
                        ,classname,title,context.getString(R.string.new_message),intent);
            }
            else
                creatBigwinerConversationNotifiction(context, Integer.valueOf(extraMap.get("_ALIYUN_NOTIFICATION_ID_"))
                        ,classname,title,msg.getString("title")+":"+msg.getString("content"),intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getConversationId(String type,int detialid) {
        int id = 0;
        if(type.toLowerCase().contains("report"))
        {
            id = NotifictionManager.OA_CONVERSATIONDID_REPORT;
        }
        else if(type.toLowerCase().contains("leave"))
        {
            id = NotifictionManager.OA_CONVERSATIONDID_LEAVE;
        }
        else if(type.toLowerCase().contains("notice"))
        {
            id = NotifictionManager.OA_CONVERSATIONDID_NOTICE;
        }
        else if(type.toLowerCase().contains("vote"))
        {
            id = NotifictionManager.OA_CONVERSATIONDID_VOTE;
        }
        else if(type.toLowerCase().contains("mail"))
        {
            id = NotifictionManager.OA_CONVERSATIONDID_MAIL;
        }
        else if(type.toLowerCase().contains("task"))
        {
            id = NotifictionManager.OA_CONVERSATIONDID_TASK;
        }
        else if(type.toLowerCase().contains("workflow"))
        {
            id = NotifictionManager.OA_CONVERSATIONDID_APPROVE;
        }
        else if(type.toLowerCase().contains("im"))
        {
            id = NotifictionManager.OA_CONVERSATIONDID_MESSAGE_BASE+detialid;
        }
        else if(type.toLowerCase().contains("Message"))
        {
            id = NotifictionManager.OA_CONVERSATIONDID_GROP;
        }
        return id;
    }



    public int getChatid() {
        int id = CHAT_BASE_ID+chataddid;
        chataddid++;
        return  id;
    }


    public void doCreatNotification(String data,String title) {
        NotificationData data1 = dataMeasure.praseNoticicationData(data,title);
        if(data1!= null)
        {
            if(dataMeasure.checkShowChat(data1) == false)
            {
                creatNotification(data1);
            }
            sendBrodcast(dataMeasure.parseBrodcastData(data1,data,title));
        }

    }

    public void sendBrodcast(BrodcastData brodcastData) {
        brodcastData.intent.setAction(brodcastData.action);
        context.sendBroadcast(brodcastData.intent);
    }

    public void creatNotification(NotificationData data) {
        PendingIntent pi = null;
        try {
            Intent intent = new Intent(context, Class.forName(openClassname));
            intent.putExtra("json",data.data);
            intent.putExtra("module",data.channel.registername);
            pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int sid = data.channel.getNid(data.detialid);
        //构建通知对象需传入上下文和通知渠道ID
        Notification notification = new NotificationCompat.Builder(context, data.channel.id)
                //标题
                .setContentTitle(data.title)
                //内容
                .setContentText(data.content)
                //自动取消
                .setAutoCancel(true)
                //小图标
                .setSmallIcon(smallicon)
                //系统时间
                .setWhen(System.currentTimeMillis())
                //大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                //要跳转的意图
                .setContentIntent(pi)
                //处于锁屏状态时LED灯一闪一暗
                .setLights(Color.GREEN, 1000, 1000)
                //震动200Ms
                // VISIBILITY_PUBLIC没有锁屏时会显示通知
                // VISIBILITY_PRIVATE任何情况都会显示通知
                // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{0, 200})
                //设置横幅通知栏，需开启通知横幅提醒权限
                .setPriority(data.channel.leave)
                .setContentIntent(pi)
                /* 显示很长的文字
                 .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                .build();
        manager.notify(sid, notification);
    }

    public interface DataMeasure {
         NotificationData praseNoticicationData(String data, String title);
         BrodcastData parseBrodcastData(NotificationData ndata,String data, String title);
         boolean checkShowChat(NotificationData ndata);
    }



    public class NotificationOper{

        public Channel channel;
        public NotificationManager manager;
        public int sid;
        public String name;
        public NotificationOper(Channel channel,NotificationManager manager) {
            this.channel = channel;
            this.manager = manager;
        }

        public NotificationOper(NotificationManager manager,int sid,String name) {
            this.manager = manager;
            this.name = name;
            this.sid = sid;
        }

        public void creatNotification(String title,String content,String json) {
            //构建通知对象需传入上下文和通知渠道ID

            PendingIntent pi = null;
            try {
                Intent intent = new Intent(context, Class.forName(openClassname));
                intent.putExtra("json",json);
                intent.putExtra("module",channel.registername);
                pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                int sid = channel.getNid("0");
                Notification notification = new NotificationCompat.Builder(context, channel.id)
                        //标题
                        .setContentTitle(title)
                        //内容
                        .setContentText(content)
                        //自动取消
                        .setAutoCancel(false)
                        //小图标
                        .setSmallIcon(smallicon)
                        //系统时间
                        .setWhen(System.currentTimeMillis())
                        //大图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                        //要跳转的意图
                .setContentIntent(pi)
                        //处于锁屏状态时LED灯一闪一暗
                        .setLights(Color.GREEN, 1000, 1000)
                        //震动200Ms
                        // VISIBILITY_PUBLIC没有锁屏时会显示通知
                        // VISIBILITY_PRIVATE任何情况都会显示通知
                        // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{0, 200})
                        //设置横幅通知栏，需开启通知横幅提醒权限
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                        /* 显示很长的文字
                         .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                        .build();
                manager.notify(sid, notification);
            }
            else
            {
                Notification notification = new NotificationCompat.Builder(context, name)
                        //标题
                        .setContentTitle(title)
                        //内容
                        .setContentText(content)
                        //自动取消
                        .setAutoCancel(false)
                        //小图标
                        .setSmallIcon(smallicon)
                        //系统时间
                        .setWhen(System.currentTimeMillis())
                        //大图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                        //要跳转的意图
                .setContentIntent(pi)
                        //处于锁屏状态时LED灯一闪一暗
                        .setLights(Color.GREEN, 1000, 1000)
                        //震动200Ms
                        // VISIBILITY_PUBLIC没有锁屏时会显示通知
                        // VISIBILITY_PRIVATE任何情况都会显示通知
                        // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{0, 200})
                        //设置横幅通知栏，需开启通知横幅提醒权限
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                        /* 显示很长的文字
                         .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                        .build();
                manager.notify(sid, notification);
            }



        }

        public void creatNotification(String title,String content,int max ,int now,String json) {

            String add = "";
            if(max>0)
            {
                add = String.valueOf(100*now/max)+"%";
            }
            PendingIntent pi = null;
            try {
                Intent intent = new Intent(context, Class.forName(openClassname));
                intent.putExtra("json",json);
                intent.putExtra("module",channel.registername);
                pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                //构建通知对象需传入上下文和通知渠道ID
                int sid = channel.getNid("0");

                Notification notification = new NotificationCompat.Builder(context, channel.id)
                        //标题
                        .setContentTitle(title)
                        //内容
                        .setContentText(content+"  "+add)
                        //自动取消
                        .setAutoCancel(false)
                        //小图标
                        .setSmallIcon(smallicon)
                        //系统时间
                        .setWhen(System.currentTimeMillis())
                        //大图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                        //要跳转的意图
                .setContentIntent(pi)
                        //处于锁屏状态时LED灯一闪一暗
                        .setLights(Color.GREEN, 1000, 1000)
                        .setProgress(max, now, false)
                        //震动200Ms
                        // VISIBILITY_PUBLIC没有锁屏时会显示通知
                        // VISIBILITY_PRIVATE任何情况都会显示通知
                        // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{0, 200})
                        //设置横幅通知栏，需开启通知横幅提醒权限
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                        /* 显示很长的文字
                         .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                        .build();
                manager.notify(sid, notification);
            }
            else
            {
                Notification notification = new NotificationCompat.Builder(context, name)
                        //标题
                        .setContentTitle(title)
                        //内容
                        .setContentText(content+"  "+add)
                        //自动取消
                        .setAutoCancel(false)
                        //小图标
                        .setSmallIcon(smallicon)
                        //系统时间
                        .setWhen(System.currentTimeMillis())
                        //大图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                        //要跳转的意图
                .setContentIntent(pi)
                        //处于锁屏状态时LED灯一闪一暗
                        .setLights(Color.GREEN, 1000, 1000)
                        .setProgress(max, now, false)
                        .setSound(null)
                        .setVibrate(null)
                        //震动200Ms
                        // VISIBILITY_PUBLIC没有锁屏时会显示通知
                        // VISIBILITY_PRIVATE任何情况都会显示通知
                        // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        //设置横幅通知栏，需开启通知横幅提醒权限
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                        /* 显示很长的文字
                         .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                        .build();
                manager.notify(sid, notification);
            }

        }

        public void creatNotificationNopen(String title,String content) {
            //构建通知对象需传入上下文和通知渠道ID

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                int sid = channel.getNid("0");
                Notification notification = new NotificationCompat.Builder(context, channel.id)
                        //标题
                        .setContentTitle(title)
                        //内容
                        .setContentText(content)
                        //自动取消
                        .setAutoCancel(false)
                        //小图标
                        .setSmallIcon(smallicon)
                        //系统时间
                        .setWhen(System.currentTimeMillis())
                        //大图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                        //要跳转的意图
                        //处于锁屏状态时LED灯一闪一暗
                        .setLights(Color.GREEN, 1000, 1000)
                        //震动200Ms
                        // VISIBILITY_PUBLIC没有锁屏时会显示通知
                        // VISIBILITY_PRIVATE任何情况都会显示通知
                        // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{0, 200})
                        //设置横幅通知栏，需开启通知横幅提醒权限
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                        /* 显示很长的文字
                         .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                        .build();
                manager.notify(sid, notification);
            }
            else
            {
                Notification notification = new NotificationCompat.Builder(context, name)
                        //标题
                        .setContentTitle(title)
                        //内容
                        .setContentText(content)
                        //自动取消
                        .setAutoCancel(false)
                        //小图标
                        .setSmallIcon(smallicon)
                        //系统时间
                        .setWhen(System.currentTimeMillis())
                        //大图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                        //要跳转的意图
                        //处于锁屏状态时LED灯一闪一暗
                        .setLights(Color.GREEN, 1000, 1000)
                        //震动200Ms
                        // VISIBILITY_PUBLIC没有锁屏时会显示通知
                        // VISIBILITY_PRIVATE任何情况都会显示通知
                        // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{0, 200})
                        //设置横幅通知栏，需开启通知横幅提醒权限
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                        /* 显示很长的文字
                         .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                        .build();
                manager.notify(sid, notification);
            }



        }

        public void creatNotificationNopen(String title,String content,int max ,int now) {

            String add = "";
            if(max>0)
            {
                add = String.valueOf(100*now/max)+"%";
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                //构建通知对象需传入上下文和通知渠道ID
                int sid = channel.getNid("0");

                Notification notification = new NotificationCompat.Builder(context, channel.id)
                        //标题
                        .setContentTitle(title)
                        //内容
                        .setContentText(content+"  "+add)
                        //自动取消
                        .setAutoCancel(false)
                        //小图标
                        .setSmallIcon(smallicon)
                        //系统时间
                        .setWhen(System.currentTimeMillis())
                        //大图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                        //要跳转的意图
                        //处于锁屏状态时LED灯一闪一暗
                        .setLights(Color.GREEN, 1000, 1000)
                        .setProgress(max, now, false)
                        //震动200Ms
                        // VISIBILITY_PUBLIC没有锁屏时会显示通知
                        // VISIBILITY_PRIVATE任何情况都会显示通知
                        // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{0, 200})
                        //设置横幅通知栏，需开启通知横幅提醒权限
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                        /* 显示很长的文字
                         .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                        .build();
                manager.notify(sid, notification);
            }
            else
            {
                Notification notification = new NotificationCompat.Builder(context, name)
                        //标题
                        .setContentTitle(title)
                        //内容
                        .setContentText(content+"  "+add)
                        //自动取消
                        .setAutoCancel(false)
                        //小图标
                        .setSmallIcon(smallicon)
                        //系统时间
                        .setWhen(System.currentTimeMillis())
                        //大图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeicon))
                        //要跳转的意图
                        //处于锁屏状态时LED灯一闪一暗
                        .setLights(Color.GREEN, 1000, 1000)
                        .setProgress(max, now, false)
                        .setSound(null)
                        .setVibrate(null)
                        //震动200Ms
                        // VISIBILITY_PUBLIC没有锁屏时会显示通知
                        // VISIBILITY_PRIVATE任何情况都会显示通知
                        // VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{0, 200})
                        //设置横幅通知栏，需开启通知横幅提醒权限
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                        /* 显示很长的文字
                         .setStyle(new NotificationCompat.BigTextStyle().bigText())*/
                        .build();
                manager.notify(sid, notification);
            }

        }

        public void cancleNotification() {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                int sid = channel.getNid("0");
                manager.cancel(sid);
            }
            else{
                manager.cancel(sid);
            }

        }
    }

    public int getSid(String name) {
        if(!oldMessageid.containsKey(name)) {
            oldMessageid.put(name,ids);
            ids++;
        }
        return oldMessageid.get(name);
    }
}
