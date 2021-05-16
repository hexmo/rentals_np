package np.com.ranjan.rental.View.Onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import np.com.ranjan.rental.ViewControllers.OnboardingSliderAdapter;
import np.com.ranjan.rental.R;
import np.com.ranjan.rental.View.UserManagement.LoginActivity;

/**
 * user Viewpager component to show images which show features of app.
 */
public class OnBoardingActivity extends AppCompatActivity {

    //Objects and variables declaration
    ViewPager viewPager;
    LinearLayout dotsLayout;
    OnboardingSliderAdapter sliderAdapter;
    TextView[] dots;
    Button letsGetStartedBtn;
    Animation animation;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        //Hooks
        viewPager = findViewById(R.id.onboarding_view_pager);
        dotsLayout = findViewById(R.id.onboarding_linear_layout_dots);
        letsGetStartedBtn = findViewById(R.id.onboarding_get_started_btn);

        //Call adapter
        sliderAdapter  = new OnboardingSliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        //Calling functions which adds color to dots on onboarding screen based on provided position.
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    //Skips function that launches Login Activity when pressed on skip button
    public void skip(View view){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //Function which view next item in view pager
    public void next(View view){
        viewPager.setCurrentItem(currentPosition+1);
    }

    //Function to programmatically adds dots on onboarding screen
    //It takes in position and assigns color dot representing that dot
    private void addDots(int position) {
        dots = new TextView[4];
        dotsLayout.removeAllViews();

        //Creating dots
        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0){

            //change color of dots
            dots[position].setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }


    //Onchange listener to change resources
    final ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPosition = position;
            //if else statement to show get started button when last slide of view pager is viewed
            if(position == 3){
                animation = AnimationUtils.loadAnimation(OnBoardingActivity.this, R.anim.bottom_anim);
                letsGetStartedBtn.setAnimation(animation);
                letsGetStartedBtn.setVisibility(View.VISIBLE);
            }else{
                letsGetStartedBtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}