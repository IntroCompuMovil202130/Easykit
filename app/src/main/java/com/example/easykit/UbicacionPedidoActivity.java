package com.example.easykit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.easykit.databinding.ActivityMapsBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.easykit.databinding.ActivityUbicacionPedidoBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class UbicacionPedidoActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,RoutingListener {


    private GoogleMap mMap;
    private @NonNull ActivityUbicacionPedidoBinding binding;


    //Atributos de localizacion
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    //Atributo de GeoCoder
    Geocoder mGeocoder;

    //Atributo de el marcador de la localización del pedido
    private Marker currentLocation;

    //Ubicaciónes
    private LatLng actual;
    private LatLng entrega = new LatLng(4.629,-74.064);

    //Atributo de locacion Del ususario
    private Marker orderDir;


    private List<Polyline> polylines=null;

    int locationid = 3;

    static final int REQUEST_CHECK_SETTINGS = 7;
    boolean isGPSEnabled = false;

    String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    //Sensor de luz
    SensorManager sensorManager;
    Sensor lightSensor;
    SensorEventListener lightSensorListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUbicacionPedidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestPermission(this,locationPermission, "No lo podemos localizar sin su permiso",
                locationid);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = createLightListener();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = createLocationRequest();
        locationCallback = createLocationCallback();

        mGeocoder = new Geocoder(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSettingsLocation();
        sensorManager.registerListener(this.lightSensorListener,lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        sensorManager.unregisterListener(this.lightSensorListener);
    }

    private LocationRequest createLocationRequest(){

        LocationRequest locationRequest = LocationRequest.create().setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }

    private LocationCallback createLocationCallback(){
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null){

                    if(currentLocation != null) currentLocation.remove();

                    actual = new LatLng(location.getLatitude(), location.getLongitude());
                    currentLocation = mMap.addMarker(new MarkerOptions().position(actual)
                            .title("Ubicación del pedido").icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    orderDir = mMap.addMarker(new MarkerOptions().position(entrega)
                            .title("Ubicación del pedido").icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                    Findroutes(currentLocation.getPosition(),orderDir.getPosition());


                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(actual, 16);
                    mMap.animateCamera(cameraUpdate);
                }

            }
        };
        return locationCallback;
    }

    private void  startLocationUpdates(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//            Findroutes(actual,entrega);
        }
    }

    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void checkSettingsLocation(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                isGPSEnabled=true;
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                isGPSEnabled=false;
                switch (statusCode){
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(UbicacionPedidoActivity.this,REQUEST_CHECK_SETTINGS);
                        }catch (IntentSender.SendIntentException sendEx){

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

//        orderDir = mMap.addMarker(new MarkerOptions().position(entrega)
//                .title("Ubicación del pedido").icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
//
//        Findroutes(currentLocation.getPosition(),orderDir.getPosition());
    }


    private void requestPermission(UbicacionPedidoActivity context, String  permission, String justification, int id){

        if(ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(context,permission)){
                Toast.makeText(context,justification,Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(context,new String[]{permission},id);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == this.locationid){
            //initView();
            createLocationCallback();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS){
            if (resultCode == RESULT_OK){
                isGPSEnabled = true;
                startLocationUpdates();
            }else {
                Toast.makeText(this, "GPS no activado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private SensorEventListener createLightListener(){
        SensorEventListener sen = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (mMap!= null) {
                    if (event.values[0] < 120) {
                        Log.i("MAPS", "DARK MAP " + event.values[0]);
                        //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(UbicacionPedidoActivity
                          //              .this, R.raw.darkmap));
                    } else {
                        Log.i("MAPS", "LIGHT MAP " + event.values[0]);
                        //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(UbicacionPedidoActivity
                          //              .this, R.raw.lightmap));
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
        return sen;
    }
/*
    private LatLng searchByname(String direccion){
        LatLng ubicacion = null;
        if(!direccion.isEmpty()){
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(direccion,2);
                if(addresses != null && !addresses.isEmpty()){
                    Address addressResult = addresses.get(0);
                    ubicacion = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                }else {
                    Toast.makeText(UbicacionPedidoActivity.this, "direccion no encontrada", Toast.LENGTH_LONG);
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return ubicacion;
    }
    private String searchByLocation(double lat, double lon){
        String nombre=null;
        try {
            List<Address> addresses = mGeocoder.getFromLocation(lat,lon, 2);
            if(addresses != null && !addresses.isEmpty()){
                Address addressResult = addresses.get(0);
                nombre = addressResult.getFeatureName();
            }else {
                Toast.makeText(UbicacionPedidoActivity.this, "direccion no encontrada", Toast.LENGTH_LONG);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return nombre;
    }*/

    public void Findroutes(LatLng start, LatLng end) {
        if (start == null || end == null) {
            Toast.makeText(UbicacionPedidoActivity.this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(start, end)
                    .key("AIzaSyB0IpWk8QNHLAbkvlwvS_cUODg4rGbt7Us")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onRoutingStart() {
//        Toast.makeText(UbicacionPedidoActivity.this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(actual);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.purple_200));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
//        MarkerOptions startMarker = new MarkerOptions();
//        startMarker.position(polylineStartLatLng);
//        startMarker.title("My Location");
//        mMap.addMarker(startMarker);

        //Add Marker on route ending position
//        MarkerOptions endMarker = new MarkerOptions();
//        endMarker.position(polylineEndLatLng);
//        endMarker.title("Destination");
//        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(actual,entrega);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(actual,entrega);
    }
}