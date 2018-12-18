package com.hlub.dev.testgpsclass;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvLocation;
    private Button button;
    private TextView tvLastKnow;
    private Button btnInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLocation = findViewById(R.id.tvLocation);
        button = findViewById(R.id.button);
        tvLastKnow = findViewById(R.id.tvLastKnow);
        btnInternet = findViewById(R.id.btnInternet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGPSEnable()) {
                    getLocation();
                } else {
                    Toast.makeText(MainActivity.this, "You do not open GPS", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isInternetConnected();
            }
        });
    }

    public void getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //listen event when positon change
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tvLocation.setText(location.getLongitude() + " - " + location.getLatitude());
                Log.e("name",location.getLongitude() + " - " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            //open/closed GPS
            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //xin quyền bằng code
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 999);
            return;
        }
        //truy cập cache của gps (vị trí cuối cùng)
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            tvLastKnow.setText(location.getLongitude() + " - " + location.getLatitude());
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, locationListener);
    }

    //kiểm tra có bật gps k?
    public boolean isGPSEnable() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isGPSEnable;
    }

    public boolean isInternetConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn=networkInfo.isConnected();

        networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn=networkInfo.isConnected();

        if(isWifiConn) Toast.makeText(this, "WIFI is connected", Toast.LENGTH_SHORT).show();
        if(isMobileConn) Toast.makeText(this, "MOBILE is connected", Toast.LENGTH_SHORT).show();

        return false;
    }

}
