package np.com.ranjan.rental.View.MainApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import np.com.ranjan.rental.R;

/**
 * This is the main activity of app.
 * This contains bottom navigation bar.
 * This activity also show all menu fragments.
 */
public class MainActivity extends AppCompatActivity {

    //Objects and variables
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bottom navigation
        bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //Set
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_frame_layout, new HomeFragment()).commit();

    }

    //Switch fragment according to selected navigation menu
    final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {

                        case R.id.homeFragment:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.browseFragment:
                            selectedFragment = new BrowseFragment();
                            break;

                        case R.id.addProductFragment:
                            selectedFragment = new AddProductFragment();
                            break;

                        case R.id.notificationFragment:
                            selectedFragment = new NotificationFragment();
                            break;

                        case R.id.profileFragment:
                            selectedFragment = new ProfileFragment();
                            break;

                    }
                    //display fragment code

                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_frame_layout, selectedFragment).commit();
                    return true;
                }
            };
}