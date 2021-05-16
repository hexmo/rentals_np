package np.com.ranjan.rental.View.Onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import np.com.ranjan.rental.View.MainApp.MainActivity;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.View.Notification.MyBroadCastReceiver;
import np.com.ranjan.rental.View.UserManagement.LoginActivity;

/**
 * Show splash screen.
 * Check if app was opened first time.
 * Shows onboarding screen if app was opened for first time.
 * If app was opened for second time checks if user was logged in
 * Shows MainActivity if user was logged in
 * else shows login screen
 */
public class SplashScreen extends AppCompatActivity {

    //Variables
    ImageView splashLogo;
    SharedPreferences onBoardingScreen;

    //For firebase
    private FirebaseAuth mAuth;

    //Animations
    Animation fadeAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Hooks
        splashLogo = findViewById(R.id.splash_screen_logo);

        //Animations
        fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.opacity_anim);

        //Setting animations on image
        splashLogo.setAnimation(fadeAnimation);

        //Time for handler function
        int SPLASH_TIMER = 2000;


        //startNotification
        startNotification();
        // Handler function which sets timer to this splash activity
        // As well as it checks if App was launched for first time
        // OR app has already been opened second time
        // AND if user is logged into the app
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onBoardingScreen = getSharedPreferences("onboardingScreen", MODE_PRIVATE);

                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

                //Checking if app has been launched first time with help of shared preferences
                if (isFirstTime) {
                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
                    startActivity(intent);
                    finish();

                    //Checking if user is logged in or not
                } else if (mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIMER);
    }

    private void startNotification() {
        Intent intent = new Intent(this.getApplicationContext(), MyBroadCastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this.getApplicationContext(), 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            long triggerTime = 2 * 1000; //set time here to run the notifications.

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, triggerTime, sender);
        }


    }
} 