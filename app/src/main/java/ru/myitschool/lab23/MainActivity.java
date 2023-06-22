package ru.myitschool.lab23;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textView_latitude;
    TextView textView_longitude;
    TextView textView_accuracy;
    TextView textView_address;
    Button locationButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_longitude = findViewById(R.id.text_longitude);
        textView_latitude = findViewById(R.id.text_latitude);
        textView_accuracy = findViewById(R.id.text_accuracy);
        textView_address = findViewById(R.id.text_address);

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );

        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        LocationProvider locationProvider = new LocationProvider();
        locationButton = (Button) findViewById(R.id.get_location);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                textView_longitude.setText("gggggg");
//            }
                locationProvider.requestLocation(getApplicationContext(), new LocationProvider.ProviderRequestListener() {
                    @Override
                    public void onPositionUpdate(Location location) {
                        textView_longitude.setText(String.valueOf(location.getLongitude()));
                        textView_latitude.setText(String.valueOf(location.getLatitude()));
                        textView_accuracy.setText(String.valueOf(location.getAccuracy()));


                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
//                        String postalCode = addresses.get(0).getPostalCode();
//                        String knownName = addresses.get(0).getFeatureName();

                        textView_address.setText(String.valueOf(address + " " + city + " " + country));
                    }
                });

            }
        });
    }


}
