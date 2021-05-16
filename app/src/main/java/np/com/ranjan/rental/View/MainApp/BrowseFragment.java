package np.com.ranjan.rental.View.MainApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import np.com.ranjan.rental.R;
import np.com.ranjan.rental.View.ProductManagement.ProductByCategoryActivity;

/**
 * This method will be implemented in future updates.
 */
public class BrowseFragment extends Fragment {

    private Button vehicle, real, music, video, tools, clothing, others,electronics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_browse, container, false);

        vehicle = view.findViewById(R.id.b_cat_vehicle);
        real = view.findViewById(R.id.b_cat_real);
        music = view.findViewById(R.id.b_cat_musical);
        video = view.findViewById(R.id.b_cat_video);
        tools = view.findViewById(R.id.b_cat_tools);
        clothing = view.findViewById(R.id.b_cat_clothing);
        electronics = view.findViewById(R.id.b_cat_electronics);
        others = view.findViewById(R.id.b_cat_other);

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategoryActivity("Vehicles and Transport");
            }
        });

        real.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategoryActivity("Real estate");
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategoryActivity("Musical Instruments");
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategoryActivity("Video, Photo and Audio");
            }
        });

        tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategoryActivity("Tools and Machinery");
            }
        });
        clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategoryActivity("Clothing");
            }
        });

        electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategoryActivity("Electronics");
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategoryActivity("Others");
            }
        });

        return view;
    }

    private void launchCategoryActivity(String category) {
        Intent intent = new Intent(getActivity(), ProductByCategoryActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }


}