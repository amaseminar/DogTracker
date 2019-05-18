package ro.marianperca.dogtracker.ui;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.List;

import ro.marianperca.dogtracker.R;
import ro.marianperca.dogtracker.db.entity.DogLocationEntity;
import ro.marianperca.dogtracker.viewmodel.DogLocationsViewModel;

public class LocationsMapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gMap;
    private List<DogLocationEntity> mLocations;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        View root = inflater.inflate(R.layout.maps_fragment, container, false);

        mapView = root.findViewById(R.id.map);

        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final DogLocationsViewModel model = ViewModelProviders.of(this)
                .get(DogLocationsViewModel.class);

        model.getDogLocations().observe(this, new Observer<List<DogLocationEntity>>() {
            @Override
            public void onChanged(@Nullable List<DogLocationEntity> locations) {
                mLocations = locations;
                displayMapLocations();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (getActivity() == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }

        displayMapLocations();
    }

    private void displayMapLocations() {
        // daca harta sau locatiile nu sunt inca disponibile, nu fa nimic
        if (gMap == null || mLocations == null)
            return;

        gMap.clear();

        PolylineOptions polylineOptions = new PolylineOptions().width(8).color(Color.RED).geodesic(true);
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();

        for (DogLocationEntity point : mLocations) {
            Log.d("###", point.toString());
            LatLng pointLatLng = new LatLng(point.getLatitude(), point.getLongitude());
            polylineOptions.add(pointLatLng);
            latLngBoundsBuilder.include(pointLatLng);

            gMap.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude())));
        }

        polylineOptions.startCap(new RoundCap());
        polylineOptions.endCap(new RoundCap());
        polylineOptions.jointType(JointType.ROUND);

        // display route
        gMap.addPolyline(polylineOptions);

        // center map to display polyline
        LatLngBounds polylineBounds = latLngBoundsBuilder.build();
        int padding = getResources().getDimensionPixelSize(R.dimen.map_polyline_padding);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(polylineBounds, padding);

        gMap.moveCamera(cu);
        gMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

    }
}
