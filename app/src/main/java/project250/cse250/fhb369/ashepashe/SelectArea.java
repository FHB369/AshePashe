package project250.cse250.fhb369.ashepashe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.light.Position;

import java.util.List;

public class SelectArea extends AppCompatActivity {
    private MapView mapView;
    private MapboxMap mapboxMap;

    private ImageView dropPinView = null;
    private Marker resultsPin = null;
    private Button selectLocationButton = null;
    private PermissionsManager permissionsManager;
    private LocationManager mLocationManager;

    private double longitude = 0.0, latitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_select_area);

        AddService.center = null;
        AddService.radius = 0.0;

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap map) {
                mapboxMap = map;

                LocationComponent locationComponent = mapboxMap.getLocationComponent();

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationComponent.activateLocationComponent(getApplicationContext());
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setRenderMode(RenderMode.NORMAL);
                locationComponent.setCameraMode(CameraMode.TRACKING);
            }
        });

        dropPinView = new ImageView(this);
        dropPinView.setImageResource(R.drawable.ic_area);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        dropPinView.setLayoutParams(params);
        mapView.addView(dropPinView);

        selectLocationButton = (Button) findViewById(R.id.select_area);
        selectLocationButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("BAPPY", "Location Selected!");
                        if (mapboxMap != null) {
                            Log.i("BAPPY", "dropPinView: height = " + dropPinView.getHeight() + "; width = " + dropPinView.getWidth() + "; left = " + dropPinView.getLeft() + "; bottom = " + dropPinView.getBottom());
                            float[] coords = getDropPinTipCoordinates();
                            LatLng latLng = mapboxMap.getProjection().fromScreenLocation(new PointF(coords[0], coords[1]));
                            LatLng latLng1 = mapboxMap.getProjection().fromScreenLocation(new PointF(coords[0], coords[2]));
                            Log.i("BAPPY", "location:  x = " + coords[0] + "; y = " + coords[1] + "; latLng = " + latLng);
                            Toast.makeText(getApplicationContext(), String.valueOf(calculateDistance(latLng, latLng1))+"KM", Toast.LENGTH_LONG).show();

                            AddService.center = latLng;
                            AddService.radius = calculateDistance(latLng, latLng1);

                            onBackPressed();
                            overridePendingTransition(R.anim.left_to_right_exit, R.anim.left_to_right);
                            finish();
                        }
                    }
                }
        );
    }

    private float[] getDropPinTipCoordinates() {
        float x = dropPinView.getLeft() + (dropPinView.getWidth() / 2);
        float y = dropPinView.getBottom() - (dropPinView.getHeight() / 2);
        float y1 = dropPinView.getBottom();
        return new float[] {x, y, y1};
    }

    private double calculateDistance(LatLng a, LatLng b){
        int R = 6371;
        double lat1 = a.getLatitude();
        double lat2 = b.getLatitude();
        double lon1 = a.getLongitude();
        double lon2 = b.getLongitude();

        double dLat = deg2rad(lat2-lat1);
        double dLon = deg2rad(lon2-lon1);

        double ans = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(ans), Math.sqrt(1-ans));
        double d = R * c;
        return d;
    }

    @Override
    public void onBackPressed() {
        if(AddService.center==null || AddService.radius==0.0){
            Toast.makeText(getApplicationContext(), "Please select your coverage area", Toast.LENGTH_SHORT).show();
        }else{
            super.onBackPressed();
        }
    }

    private double deg2rad(double deg){
        return deg*(Math.PI/180);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
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
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}

