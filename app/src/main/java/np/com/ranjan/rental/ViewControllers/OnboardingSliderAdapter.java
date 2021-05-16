package np.com.ranjan.rental.ViewControllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import np.com.ranjan.rental.R;

/**
 * Responsible for creating onboarding screen
 */
public class OnboardingSliderAdapter extends PagerAdapter {
    final Context context;
    LayoutInflater layoutInflater;

    //Constructor
    public OnboardingSliderAdapter(Context context) {
        this.context = context;
    }

    //Creating and initializing images array with image(drawable) resources for onboarding screen
    final int[] images = {
            R.drawable.onboarding_01,
            R.drawable.onboarding_02,
            R.drawable.onboarding_03,
            R.drawable.onboarding_04,
    };

    //Creating and initializing heading array with string resources for onboarding screen
    final int[] headings = {
            R.string.first_slide_title,
            R.string.second_slide_title,
            R.string.third_slide_title,
            R.string.fourth_slide_title,
    };

    //Creating and initializing heading descriptions array with string resources for onboarding screen
    final int[] descriptions = {
            R.string.first_slide_desc,
            R.string.second_slide_desc,
            R.string.third_slide_desc,
            R.string.fourth_slide_desc,
    };

    /**
     * @return total no of pages for pager view
     */
    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    /**
     * Set appropriate resources for current view in view pages
     * Setting the resource layout file for view pager
     * @param container Container
     * @param position position
     * @return view
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_layout, container,false);

        //Hooks
        ImageView imageView = view.findViewById(R.id.onboarding_slider_image);
        TextView heading = view.findViewById(R.id.onboarding_slider_heading);
        TextView subHeading = view.findViewById(R.id.onboarding_slider_description);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        subHeading.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    /**
     * Destroys the item if slides are changes
     * @param container Container
     * @param position position of slide
     * @param object object
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
         container.removeView((ConstraintLayout)object);
    }
}
