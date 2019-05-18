package ro.marianperca.dogtracker.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ro.marianperca.dogtracker.R;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Fragment currentLocationFragment = getSupportFragmentManager().findFragmentByTag("CurrentLocationFragment");

                    if (currentLocationFragment == null) {
                        currentLocationFragment = new CurrentLocationFragment();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, currentLocationFragment, "CurrentLocationFragment").commit();

                    return true;

                case R.id.navigation_maps:
                    Fragment mapFragment = getSupportFragmentManager().findFragmentByTag("LocationsMapFragment");

                    if (mapFragment == null) {
                        mapFragment = new LocationsMapFragment();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mapFragment, "LocationsMapFragment").commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_home);
    }
}
