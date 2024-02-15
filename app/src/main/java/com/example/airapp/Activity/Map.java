package com.example.airapp.Activity;

import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.airapp.ApiClient;
import com.example.airapp.LogIn.IntergfaceMap;
import com.example.airapp.R;
import com.example.airapp.RepondMap;
import com.example.airapp.URL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
//import com.mapbox.maps.CameraChanged;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Map extends AppCompatActivity {

    MapView mapView;
    Button back;
    IntergfaceMap intergfaceMap;
    FloatingActionButton floatingActionButton;
    Intent intent = new Intent();
    String access_token;
    private static final String BASE_URL = "https://uiot.ixxc.dev/api/master/";
    private static final String assetId = "4EqQeQ0L4YNWNNTzvTOqjy";
    private static String authorization = "";
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result){
                // Khúc này người truy cập cấp quyền truy cập vị trí
                Toast.makeText(Map.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Map.this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    });
    private final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
        }
    };
    private final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(15.0).build());
            getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
        }
    };

    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
            getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
            getGestures(mapView).removeOnMoveListener(onMoveListener);
            floatingActionButton.show();
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        access_token =  intent.getStringExtra("access_token");
        Log.d("calling", "access_token");
        Log.d("access token map", "access_token");
        mapView = findViewById(R.id.mapView);
        floatingActionButton = findViewById(R.id.focusLocation);
        floatingActionButton.hide();
        authorization = "Bearer "+access_token;
        back = findViewById(R.id.btnMapBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();Log.d("Call", "calling");
            }
        });




        if (ActivityCompat.checkSelfPermission(Map.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        mapView.getMapboxMap().loadStyleUri("mapbox://styles/minhtoan87/clpv8na1301ew01paeo5u0l7c", new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build());
                LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
                locationComponentPlugin.setEnabled(true);
                LocationPuck2D locationPuck2D = new LocationPuck2D();
                locationPuck2D.setBearingImage(AppCompatResources.getDrawable(Map.this, R.drawable.baseline_location_on_24));

                locationComponentPlugin.setLocationPuck(locationPuck2D);
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                getGestures(mapView).addOnMoveListener(onMoveListener);

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Xử lí lấy tọa độ
                        Retrofit retrofit = ApiClient.getClient(URL.mainURL);
                        intergfaceMap = retrofit.create(IntergfaceMap.class);
                        Call<RepondMap> call = intergfaceMap.getMap();
                        Log.d("Call", "calling");
                        call.enqueue(new Callback<RepondMap>() {
                            @Override
                            public void onResponse(Call<RepondMap> call, Response<RepondMap> response) {
                                if (response.isSuccessful()) {
                                    Log.d("Location Map", "successful");
                                    RepondMap repondMap = response.body();
                                    if(repondMap != null){
                                        List<Double> center = repondMap.getOptionSuperIdol().getDefaultSuperIdol().getCenter();
                                        // Lấy tọa độ từ danh sách center
                                        double latitude = center.get(1);
                                        double longitude = center.get(0);

                                        // Thiết lập CameraOptions để di chuyển đến tọa độ mới
                                        CameraOptions cameraOptions = new CameraOptions.Builder()
                                                .center(Point.fromLngLat(longitude, latitude))
                                                .zoom(15.0)
                                                .build();

                                        // Thiết lập camera cho bản đồ
                                        mapView.getMapboxMap().setCamera(cameraOptions);


                                    }
                                }
                                else{
                                    Log.d("Eror Map", "Khong lay duoc");
                                }

                            }

                            @Override
                            public void onFailure(Call<RepondMap> call, Throwable t) {
                                Log.d("suscess", "deny");
                            }
                        });

//                        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
//                        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                        getGestures(mapView).addOnMoveListener(onMoveListener);
                        floatingActionButton.hide();
                    }
                });
            }
        });
    }
}
