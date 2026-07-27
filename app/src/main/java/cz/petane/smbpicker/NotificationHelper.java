package cz.petane.smbpicker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

public class NotificationHelper {

    private static final String CHANNEL_ID = "episode_updates";


    public static void createChannel(Context context) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "Automatické aktualizace",
                            NotificationManager.IMPORTANCE_HIGH
                    );


            channel.setDescription(
                    "Oznámení o automatické změně dílů"
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


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if(context.checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }


        createChannel(context);


        StringBuilder text =
                new StringBuilder();


        for(String file : files) {

            text.append("• ")
                    .append(file)
                    .append("\n");
        }



        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID
                );


        builder.setSmallIcon(
                android.R.drawable.stat_notify_sync
        );


        builder.setContentTitle(
                "SMB Random Picker"
        );


        builder.setContentText(
                "Profil "
                        + profileName
                        + " byl aktualizován"
        );


        builder.setStyle(
                new NotificationCompat.BigTextStyle()
                        .bigText(
                                text.toString()
                        )
        );


        builder.setPriority(
                NotificationCompat.PRIORITY_HIGH
        );


        builder.setAutoCancel(
                true
        );



        NotificationManagerCompat.from(context)
                .notify(
                        profileName.hashCode(),
                        builder.build()
                );
    }
}
