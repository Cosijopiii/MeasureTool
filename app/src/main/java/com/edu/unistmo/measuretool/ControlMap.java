package com.edu.unistmo.measuretool;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

/**
 * Created by cosijopii on 29/01/17.
 */

public class ControlMap implements OnMapReadyCallback, LocationListener, View.OnClickListener{

    private ArrayList<LatLng> points;

    private GoogleMap map;
    private LocationManager locationManager;
    private Button btncalculate;
    private TextView area;
    private boolean flag=true;

    public ControlMap(MapToolActivity mapToolActivity, Button btncalculate, TextView textStep) {
        this.btncalculate = btncalculate;
        this.area = textStep;
        locationManager = (LocationManager) mapToolActivity.getSystemService(mapToolActivity.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mapToolActivity.getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapToolActivity.getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {}
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 1, this);
        points=new ArrayList<>();


    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map=map;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

    }
    boolean s=true;
    @Override
    public void onLocationChanged(Location location) {
        if(flag) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(s)
            map.addMarker(new MarkerOptions().position(latLng).title("Inicio"));
        s=false;
            points.add(latLng);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
            map.addPolyline(new PolylineOptions().addAll(points));

        }
    }
    @Override
    public void onClick(View v) {

        flag=false;
        points.add(points.get(0));
        double a= SphericalUtil.computeArea(points);
        Double as=new Double(a);
        as.floatValue();
        area.setText("Área: "+as+" metros cuadrados");
        map.clear();
        map.addMarker(new MarkerOptions().position(points.get(0)).title("Inicio"));
        map.addMarker(new MarkerOptions().position(points.get(points.size()-1)).title("Final"));

        map.addPolyline(new PolylineOptions().addAll(points));


    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
