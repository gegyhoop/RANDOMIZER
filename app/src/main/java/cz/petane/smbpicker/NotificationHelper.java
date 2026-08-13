package cz.petane.smbpicker;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.List;

public class NotificationHelper {
    private static final String CHANNEL_ID = "episode_updates";
    private static final int MANUAL_NOTIFICATION_ID = 1000001;

    public static void createChannel(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "Automatické aktualizace",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            channel.setDescription(
                    "Oznámení o automatických a ručních aktualizacích dílů"
            );
            NotificationManager manager =
                    context.getSystemService(
                            NotificationManager.class
                    );
            if(manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void showNotification(
            Context context,
            String profileName,
            List<String> files
    ) {
        createChannel(context);
        StringBuilder text = new StringBuilder();
        for(String file : files) {
            text.append("• ")
                    .append(file)
                    .append("\n");
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                android.R.drawable.stat_notify_sync
                        )
                        .setContentTitle(
                                "SMB Random Picker"
                        )
                        .setContentText(
                                "Profil " + profileName + " byl aktualizován"
                        )
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(text.toString())
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_DEFAULT
                        )
                        .setAutoCancel(true);

        NotificationManagerCompat.from(context)
                .notify(
                        profileName.hashCode(),
                        builder.build()
                );
    }

    public static void showManualUpdateNotification(
            Context context
    ) {
        createChannel(context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                android.R.drawable.stat_notify_sync
                        )
                        .setContentTitle(
                                "SMB Random Picker"
                        )
                        .setContentText(
                                "Všechny profily byly aktualizovány"
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_DEFAULT
                        )
                        .setAutoCancel(true);

        NotificationManagerCompat.from(context)
                .notify(
                        MANUAL_NOTIFICATION_ID,
                        builder.build()
                );
    }
}
