//has absolute jackshit to do with anything
// balances out contr. graph
//will delete dont worry about it.



/*

package com.joblesscoders.arbook;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.joblesscoders.arbook", appContext.getPackageName());
    }
}


*/

/*

package com.softclique.trackwheels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback, LocationEngineCallback<LocationEngineResult> {
    private MapView mapView;
    public MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationEngineRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(getApplicationContext(), getString(R.string.MAPBOX_API_KEY));
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent() {

        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Toast.makeText(this, "ggg", Toast.LENGTH_SHORT).show();

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            // Toast.makeText(this, locationComponent.get, Toast.LENGTH_SHORT).show();

            // Activate with options
            locationComponent.activateLocationComponent(this, mapboxMap.getStyle());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            initRealTimeUpdates();

        } else {

            permissionsManager = new PermissionsManager(this);

            permissionsManager.requestLocationPermissions(this);

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRealTimeUpdates() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(MainActivity.this);
        request = new LocationEngineRequest.Builder(2000)
                .setPriority(LocationEngineRequest.PRIORITY_NO_POWER)
                .setMaxWaitTime(10000).build();


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationEngine.requestLocationUpdates(request, MainActivity.this, getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, permissionsToExplain.get(0), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @TargetApi(Build.VERSION_CODES.M)
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onStyleLoaded(@NonNull Style style) {

                    enableLocationComponent();


                }
            });
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }



    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap=mapboxMap;
       mapboxMap.getUiSettings().setLogoEnabled(false);

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                //  this.mapboxMap=mapboxMap;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    enableLocationComponent();
                }

                // Map is set up and the style has loaded. Now you can add data or make other map adjustments



            }
        });

    }

    @Override
    public void onSuccess(LocationEngineResult result) {
        Location lastLocation = result.getLastLocation();
        Log.e("location ",lastLocation.getLatitude()+" , "+lastLocation.getLongitude());


    }

    @Override
    public void onFailure(@NonNull Exception exception) {

    }
    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {

        if (locationEngine != null) {
         //   locationEngine.requestLocationUpdates(request, this, getMainLooper());
        }
        mapView.onStart();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {

        if (locationEngine != null) {
          //  locationEngine.removeLocationUpdates(this);
        }
       // mapView.onStop();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

package com.softclique.trackwheels;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.softclique.trackwheels.pojo.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText name,email;
    private MaterialButton button;
    private SharedPreferences sharedPreferences;
    private String uid;
    private RestApiHandler restApiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid",null);
        if(uid != null)
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiHandler.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restApiHandler = retrofit.create(RestApiHandler.class);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button:
                if(TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email.getText().toString())) {

                    Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                signinUser(name.getText().toString(),email.getText().toString());

                break;
        }

    }

    private void signinUser(String name,String email) {
        restApiHandler.createUser(new User(name,email)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200)
                {
                    sharedPreferences.edit().putString("uid",response.body().getUid()).commit();
                    Toast.makeText(LoginActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {


            }
        });
    }
}

/*