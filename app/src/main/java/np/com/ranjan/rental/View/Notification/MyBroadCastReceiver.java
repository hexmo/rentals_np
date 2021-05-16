package np.com.ranjan.rental.View.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import np.com.ranjan.rental.R;
import np.com.ranjan.rental.View.MainApp.MainActivity;
import np.com.ranjan.rental.View.Onboarding.SplashScreen;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyBroadCastReceiver extends BroadcastReceiver {


    //https://www.vogella.com/tutorials/AndroidNotifications/article.html#:~:text=To%20create%20notifications%20you%20use,The%20Notification.
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran", Toast.LENGTH_LONG).show();
        Log.d("Broadcast: ", "It was called. ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("ID001", "Rentals notification.", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent2 = new Intent(context, SplashScreen.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ID001").setSmallIcon(R.drawable.splash_screen_logo).
                setContentTitle("Hurry up! Add products for free.")
                .setContentText("Add your unused products and earn money.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1111, builder.build());
    }


}
