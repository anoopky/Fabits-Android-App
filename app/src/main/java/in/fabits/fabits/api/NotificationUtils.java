package in.fabits.fabits.api;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.fabits.fabits.HomeActivity;
import in.fabits.fabits.LoginActivity;
import in.fabits.fabits.R;

public class NotificationUtils extends AsyncTask<String, Void, Bitmap> {


    private final String title;
    Context ctx;
    String message;
    String ImageUrl;


    public NotificationUtils(Context context, String message, String title, String url) {
        this.ctx = context;
        this.ImageUrl = url;
        this.message = message;
        this.title = title;
    }


    private static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, LoginActivity.class);

        return PendingIntent.getActivities(context,
                10002,
                new Intent[]{startActivityIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.face);
    }

    public static void show(Context context, String Message, Bitmap face) {


    }

    @Override
    protected Bitmap doInBackground(String... params) {
        InputStream in;

        try {

            URL url = new URL(ImageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            return BitmapFactory.decodeStream(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap result) {

        super.onPostExecute(result);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx)
                .setColor(ContextCompat.getColor(ctx, R.color.loginBackground))
                .setSmallIcon(R.drawable.ic_fabits_f)
                .setLargeIcon(result)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        message
                ))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(ctx))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(100302, notificationBuilder.build());
    }
}
