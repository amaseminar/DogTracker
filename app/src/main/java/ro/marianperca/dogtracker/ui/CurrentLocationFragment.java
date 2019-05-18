package ro.marianperca.dogtracker.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ro.marianperca.dogtracker.R;
import ro.marianperca.dogtracker.db.entity.DogLocationEntity;
import ro.marianperca.dogtracker.viewmodel.DogLocationsViewModel;

public class CurrentLocationFragment extends Fragment {

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long MIN_LOCATION_MOVEMENT = 5; // primim noua locatie doar daca este o diferenta de minim 5 metrii

    private static final int REQUEST_FINE_LOCATION = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;

    private DogLocationsViewModel mViewModel;

    private TextView mLatitudeView;
    private TextView mLongitudeView;
    private TextView mDateView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(MIN_LOCATION_MOVEMENT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                onLocationChanged(locationResult.getLastLocation());
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        View root = inflater.inflate(R.layout.current_location_fragment, container, false);

        mLatitudeView = root.findViewById(R.id.latitude);
        mLongitudeView = root.findViewById(R.id.longitude);
        mDateView = root.findViewById(R.id.date);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(DogLocationsViewModel.class);
    }

    private void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }

        // adauga in baza de date
        Date locationDate = Calendar.getInstance().getTime();
        mViewModel.insert(new DogLocationEntity(location.getLatitude(), location.getLongitude(), "Adresa", locationDate));

        // afiseza info in interfata
        mLatitudeView.setText(String.valueOf(location.getLatitude()));
        mLongitudeView.setText(String.valueOf(location.getLongitude()));

        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        mDateView.setText(dt.format(locationDate));
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (!checkPermissions())
            return;

        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(getContext(), "Nu am primit permisiunile necesare pentru preluarea locatiei", Toast.LENGTH_LONG).show();
            }
        }
    }
}
