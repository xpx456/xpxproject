package intersky.budge.shortcutbadger.impl;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import intersky.budge.R;
import intersky.budge.shortcutbadger.Badger;
import intersky.budge.shortcutbadger.ShortcutBadgeException;
import intersky.budge.shortcutbadger.util.BroadcastHelper;


/**
 * @author leolin
 */
public class DefaultBadger implements Badger {
    private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
    private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
    private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws ShortcutBadgeException {
            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount);
            intent.putExtra(INTENT_EXTRA_PACKAGENAME, componentName.getPackageName());
            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, componentName.getClassName());
        if (BroadcastHelper.canResolveBroadcast(context, intent)) {
            context.sendBroadcast(intent);
        } else {
            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi"))
            {
                setXiaoMiBadge(context,badgeCount);
            }
            else
            throw new ShortcutBadgeException("unable to resolve intent: " + intent.toString());
        }
    }

    private void setXiaoMiBadge(Context context, int number){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        Notification.Builder builder = new Notification.Builder(context).setContentTitle("").setContentText("").setSmallIcon(R.drawable.icon);
        Notification notification = builder.build();
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mNotificationManager.notify(0,notification);
    }

    @Override
    public List<String> getSupportLaunchers() {
        return new ArrayList<String>(0);
    }

    boolean isSupported(Context context) {
        Intent intent = new Intent(INTENT_ACTION);
        return BroadcastHelper.canResolveBroadcast(context, intent);
    }
}
