package com.example.airapp.Fragment;

import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.airapp.LogIn.InterfaceLogin;
import com.example.airapp.LogIn.InterfaceWeather;
import com.example.airapp.LogIn.RetrofitClient;
import com.example.airapp.RepondWeather;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.airapp.ApiClient;
import com.example.airapp.LogIn.IntergfaceMap;
import com.example.airapp.Place;
import com.example.airapp.R;
import com.example.airapp.RepondMap;
import com.example.airapp.URL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraBoundsOptions;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.CoordinateBounds;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer;
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.FeatureCollection;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static MapboxMap mapboxMap;
    IntergfaceMap apiServiceMapData;

    public MapFragment() {
        // Required empty public constructor
    }
    MapView mapView;
    AnnotationConfig annoConfig;
    String accessToken="";
    AnnotationPlugin annoPlugin;
    PointAnnotationManager pointAnnoManager;
    Button back;
    InterfaceWeather interfaceWeather;
    RepondMap mapData;
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
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    });

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String accessToken) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString("access_token", accessToken);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("Hello", "Fragment Map");
    }
    private void DrawMap() {
        mapView.setVisibility(View.INVISIBLE);

        new Thread(() -> {
            while (!RepondMap.isReady) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            requireActivity().runOnUiThread(() -> setMapView());
        }).start();
    }
    private void showDialogDefaultWeather(){
        authorization = "Bearer "+accessToken;
        Retrofit retrofit = ApiClient.getClient(URL.mainURL);
        interfaceWeather = retrofit.create(InterfaceWeather.class);
        Toast.makeText(requireContext(), "response thanh cong", Toast.LENGTH_SHORT).show();
        Call<RepondWeather> call = interfaceWeather.getWeather("5zI6XqkQVSfdgOrZ1MyWEf", authorization);
        call.enqueue(new Callback<RepondWeather>() {
            @Override
            public void onResponse(Call<RepondWeather> call, Response<RepondWeather> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "response thanh cong", Toast.LENGTH_SHORT).show();
                    RepondWeather repondWeather = response.body();
                    double temperature = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getMainWeather().getTemperature();
                    int humidity = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getMainWeather().getHumidity();
                    double wind = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getWindSuperIdol().getSpeed();
                    RepondWeather.AttributeWeather.DataWeather.ValueWeather.WeatherSuperIdol firstWeather = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getWeatherSuperIdol().get(0);
                    String description = firstWeather.getDescription();
                    RepondWeather.AttributeWeather.DataWeather.ValueWeather.WeatherSuperIdol todayWeather = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getWeatherSuperIdol().get(0);
                    String todayWeather2 = firstWeather.getMainWeatherSuperIdol();

                }
            }

            @Override
            public void onFailure(Call<RepondWeather> call, Throwable t) {

            }
        });
    }
    private void setMapView() {
        mapData = RepondMap.reponeMap.getRepondMapData();
        mapView.setVisibility(View.VISIBLE);
        mapboxMap = mapView.getMapboxMap();
        Log.d("Coordinate", String.valueOf(mapData.getOptionSuperIdol().getDefaultSuperIdol().getBounds()));

        Point point = Point.fromLngLat(106.80280655508835, 10.869778736885038);
        Point point1 = Point.fromLngLat(106.80345028525176, 10.869905172970164);
        if (mapboxMap != null) {
            mapboxMap.loadStyleUri("mapbox://styles/minhtoan87/clpv8na1301ew01paeo5u0l7c", style -> {
                style.removeStyleLayer("poi-level-1");
                style.removeStyleLayer("highway-name-major");

                annoPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
                annoConfig = new AnnotationConfig("map_annotation");
                pointAnnoManager = (PointAnnotationManager) annoPlugin.createAnnotationManager(AnnotationType.PointAnnotation, annoConfig);
                pointAnnoManager.addClickListener(pointAnnotation -> {
                    String id = Objects.requireNonNull(pointAnnotation.getData()).getAsJsonObject().get("id").getAsString();
                    if ("5zI6XqkQVSfdgOrZ1MyWEf".equals(id)) {
//                       showDialogDefaultWeather();
                        Toast.makeText(requireContext(), "Hello ae xã đoàn điểm 1", Toast.LENGTH_SHORT).show();
                    } else if ("6iWtSbgqMQsVq8RPkJJ9vo".equals(id)) {
                        Toast.makeText(requireContext(), "Hello ae xã đoàn điểm 2", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
                // Create point annotations
                createPointAnnotation(point, "5zI6XqkQVSfdgOrZ1MyWEf", R.drawable.baseline_location_on_24);
                createPointAnnotation(point1, "6iWtSbgqMQsVq8RPkJJ9vo", R.drawable.baseline_location_on_24);

                // Set camera values
                setCameraValues();

                CameraAnimationsPlugin cameraAnimationsPlugin = mapView.getPlugin(Plugin.MAPBOX_CAMERA_PLUGIN_ID);
                if (cameraAnimationsPlugin != null) {
                    mapView.setVisibility(View.VISIBLE);
                }
                mapView.setVisibility(View.VISIBLE);
            });
        }
    }

    private void createPointAnnotation(Point point, String id, int iconResource) {
        ArrayList<PointAnnotationOptions> markerList = new ArrayList<>();
        Drawable iconDrawable = getResources().getDrawable(iconResource);
        Bitmap iconBitmap = drawableToBitmap(iconDrawable);
        JsonObject idDeviceTemperature = new JsonObject();
        idDeviceTemperature.addProperty("id", id);
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(iconBitmap)
                .withData(idDeviceTemperature);
        markerList.add(pointAnnotationOptions);
        pointAnnoManager.create(markerList);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    private void setCameraValues() {
        if (mapboxMap != null) {
            mapboxMap.setCamera(
                    new CameraOptions.Builder()
                            .center(Point.fromLngLat(mapData.getOptionSuperIdol().getDefaultSuperIdol().getCenter().get(0),mapData.getOptionSuperIdol().getDefaultSuperIdol().getCenter().get(1) ))
                            .zoom(mapData.getOptionSuperIdol().getDefaultSuperIdol().getZoom())
                            .build()
            );

            mapboxMap.setBounds(
                    new CameraBoundsOptions.Builder()
                            .minZoom(0.0)
                            .maxZoom(19.0)
                            .bounds(mapData.getOptionSuperIdol().getDefaultSuperIdol().getBounds())
                            .build()
            );
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Lấy access_token từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            accessToken = bundle.getString("access_token");
            Log.d("MainDashBoardFragment", "Received access_token: " + accessToken);
            access_token=accessToken;
        }

    }
    private void GetDataMap(){
        Retrofit retrofit = ApiClient.getClient(URL.mainURL);
        apiServiceMapData = retrofit.create(IntergfaceMap.class);
        Call<RepondMap> call = apiServiceMapData.getMap();
        call.enqueue(new Callback<RepondMap>() {
            @Override
            public void onResponse(Call<RepondMap> call, Response<RepondMap> response) {
                if (response.isSuccessful()) {
                    RepondMap.setRepondMapData(response.body());
                } else {
                    // Xử lý khi có lỗi từ server
                }
            }

            @Override
            public void onFailure(Call<RepondMap> call, Throwable t) {

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        floatingActionButton = view.findViewById(R.id.focusLocation);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        Toast.makeText(requireContext(), "Accesstoken: "+accessToken, Toast.LENGTH_SHORT).show();
        GetDataMap();
        DrawMap();
        return view;
    }
}