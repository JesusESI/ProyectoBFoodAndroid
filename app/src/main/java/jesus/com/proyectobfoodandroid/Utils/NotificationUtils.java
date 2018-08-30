package jesus.com.proyectobfoodandroid.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import jesus.com.proyectobfoodandroid.Objects.Notificacion;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String BFOOD_CHANNEL_ID = "com.jesuselvira.bfood.ANDROID";
    public static final String BFOOD_CHANNEL_NAME = "BFOOD CHANNEL";


    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationUtils(Context base) {
        super(base);
        createChannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannel() {

        // create bfood channel

        NotificationChannel bfoodChannel = new NotificationChannel(BFOOD_CHANNEL_ID, BFOOD_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        bfoodChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        bfoodChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        bfoodChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        bfoodChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(bfoodChannel);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getBfoodChannelNotification(int icono, PendingIntent pendingIntent, Notificacion notifyAux) {
        return new Notification.Builder(getApplicationContext(), BFOOD_CHANNEL_ID)
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(icono)
                                .setContentTitle(notifyAux.getTitulo())
                                .setContentText(notifyAux.getContenido())
                                .setVibrate(new long[]{100, 250, 100, 500})
                                .setAutoCancel(true);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
}
