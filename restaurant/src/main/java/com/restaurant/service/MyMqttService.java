package com.restaurant.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.restaurant.handler.MqttHandler;
import com.restaurant.prase.MqttPrase;
import com.restaurant.receiver.MqttReceiver;
import com.restaurant.view.RestaurantApplication;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;

/**
 * Author       wildma
 * Github       https://github.com/wildma
 * CreateDate   2018/11/08
 * Desc         ${MQTT服务}
 */

public class MyMqttService extends Service {

    public static  final String ACTION_INIT_MQTT = "ACTION_INIT_MQTT";
    public static  final String ACTION_SERVICE_STARTED = "ACTION_SERVICE_STARTED";
    public static  final String ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE";
    public final String TAG = MyMqttService.class.getSimpleName();
    private static MqttAndroidClient  mqttAndroidClient;
    private MqttConnectOptions mMqttConnectOptions;
    public String HOST = "";//服务器地址（协议+地址+端口号）
    public static String CLIENTID = "";
    public static String RESPONS = "mqtt-client-ent-test";
    public MqttReceiver mqttReceiver;
    public MqttHandler mqttHandler;
    public static HashMap<String,String> sendMessage =new HashMap<String,String>();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if(intent != null)
//        init(intent.getStringExtra("clientid"),intent.getStringExtra("host"));
        mqttHandler = new MqttHandler(this);
        mqttReceiver = new MqttReceiver(mqttHandler);
        this.registerReceiver(mqttReceiver,mqttReceiver.intentFilter);
        this.sendBroadcast(new Intent(ACTION_SERVICE_STARTED));
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 开启服务
     */
    public static void startService(Context mContext,String host,String userid) {
        Intent intent = new Intent(mContext, MyMqttService.class);
        intent.putExtra("clientid",userid);
        intent.putExtra("host",host);
        mContext.startService(intent);
    }


    public static void startService(Context mContext) {
        Intent intent = new Intent(mContext, MyMqttService.class);
        mContext.startService(intent);
    }
    /**
     * 发布 （模拟其他客户端发布消息）
     *
     * @param message 消息
     */
    public static void publish(String message,String topic,String messageid) {
        Integer qos = 2;
        Boolean retained = false;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            sendMessage.put(messageid,message);
            mqttAndroidClient.publish(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkHasMessage(String id)
    {
        if(sendMessage.containsKey(id))
        {
            return true;
        }
        else {
            return false;
        }
    }
    /**
     * 响应 （收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等）
     *
     * @param message 消息
     */
    public void response(String message,String topic) {
        Integer qos = 2;
        Boolean retained = false;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化75DB2FAD-753C-420F-B393-206CF29A1CDC
     */
    public void init(String host,String user) {
        boolean reconnect = false;
        if(!HOST.equals(host))
        {
            reconnect = true;
        }
        if(!CLIENTID.equals(user))
        {
            reconnect = true;
        }
        HOST = host;
        CLIENTID = user;
        String serverURI = HOST; //服务器地址（协议+地址+端口号）
        if(mqttAndroidClient != null)
        {
            if(mqttAndroidClient.isConnected())
            {
                if(reconnect == true)
                {
                    try {
                        mqttAndroidClient.disconnect();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    reconnect = true;
                }

            }
            else
            {
                reconnect = true;
            }

        }
        else
        {
            reconnect = true;
        }
        if(reconnect == true)
        {
            mqttAndroidClient = new MqttAndroidClient(this, serverURI, CLIENTID);
            mqttAndroidClient.setCallback(mqttCallback); //设置监听订阅消息的回调
            mMqttConnectOptions = new MqttConnectOptions();
            mMqttConnectOptions.setCleanSession(true); //设置是否清除缓存
            mMqttConnectOptions.setConnectionTimeout(10); //设置超时时间，单位：秒
            mMqttConnectOptions.setKeepAliveInterval(20); //设置心跳包发送间隔，单位：秒
            doClientConnection();
        }

    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if (!mqttAndroidClient.isConnected() && isConnectIsNomarl()) {
            try {
                mqttAndroidClient.connect(mMqttConnectOptions, RestaurantApplication.mApp, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "没有可用网络");
            /*没有可用网络的时候，延迟3秒再尝试重连*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doClientConnection();
                }
            }, 3000);
            return false;
        }
    }

    //MQTT是否连接成功的监听
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                mqttAndroidClient.subscribe(CLIENTID, 2);//订阅主题，参数：主题、服务质量
                mqttAndroidClient.subscribe("syncConfigTime01", 2);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            Log.i(TAG, "连接失败 ");
            doClientConnection();//连接失败，重连（可关闭服务器进行模拟）
        }
    };

    //订阅主题的回调
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.i(TAG, "收到消息： " + new String(message.getPayload()));
            //收到消息，这里弹出Toast表示。如果需要更新UI，可以使用广播或者EventBus进行发送
            String json = new String(message.getPayload());
            //Toast.makeText(getApplicationContext(), "messageArrived: " + new String(message.getPayload()), Toast.LENGTH_LONG).show();
            //收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等

            response(MqttPrase.praseMessage(json,sendMessage),RESPONS);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            Log.i(TAG, "连接断开 ");
            doClientConnection();//连接断开，重连
        }
    };

    @Override
    public void onDestroy() {
        try {
            mqttAndroidClient.disconnect(); //断开连接
            this.unregisterReceiver(mqttReceiver);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}