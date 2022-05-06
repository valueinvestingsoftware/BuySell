package x.stocks.valueinvesting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class MapFragment extends Fragment {

    Location currentLocation;
    FusedLocationProviderClient client;
    String clienteNombre, x, y;
    TextView prueba;
    Double latCustomer, lonCustomer;
    Location ubicacionCustomer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        //Initialize location client
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        prueba = view.findViewById(R.id.txtTest);

        Bundle bundle = getArguments();

        if (bundle != null){
            clienteNombre = bundle.getString("nameCliente");
            x = bundle.getString("x");
            y = bundle.getString("y");
            prueba.setText(" Latitud: " + y + ", Longitud: " + x);

            getActivity().setTitle(clienteNombre);

        //  Toast.makeText(getContext(), clienteNombre.toString() + " Lat: " + y + "Lon: " + x, Toast.LENGTH_LONG).show();

          latCustomer = Double.parseDouble(y);
         lonCustomer = Double.parseDouble(x);
         ubicacionCustomer = new Location("");
            ubicacionCustomer.setLatitude(latCustomer);
            ubicacionCustomer.setLongitude(lonCustomer);
        }else{
            prueba.setVisibility(View.INVISIBLE);
        }

        //Initialize map Fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(GoogleMap googleMap) {

                fetchLastLocation();

                //when map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //when clicked on map
                        //Initialize marker options

                        MarkerOptions markerOptions = new MarkerOptions();
                        //Set position of marker
                        markerOptions.position(latLng);
                        //Set title of marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        //Remove all markers
                        googleMap.clear();
                        //Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng, 5
                        ));
                        //Add marker on map
                        googleMap.addMarker(markerOptions);

                    }
                });
            }
        });
        //return view
        return view;

    }

    private void WhatLocationToShow(Location location){
        currentLocation = location;

        Toast.makeText(getActivity(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_LONG).show();
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //Initialize lat lng
                LatLng latLng = new LatLng(location.getLatitude()
                        , location.getLongitude());
                //create market options
                MarkerOptions options = new MarkerOptions().position(latLng)
                        .title("I am here");
                //Zoom to map
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                //Add marker on map
                googleMap.addMarker(options);
            }
        });
    }
    private void fetchLastLocation() {
        if (ContextCompat.checkSelfPermission(getActivity()
                , Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {
                if (ubicacionCustomer != null){
                    WhatLocationToShow(ubicacionCustomer);
                }else{
                    if (location != null) {
                        WhatLocationToShow(location);
                    }
                }

            }
        });
        }else{
            //When permission is not granted
            //Request permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if (requestCode == 100 && (grantResults.length > 0) &&
                (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            //When permissions are granted
            //Call Method
            fetchLastLocation();
        }else{
            //When permissions are denied
            //Display toast
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
