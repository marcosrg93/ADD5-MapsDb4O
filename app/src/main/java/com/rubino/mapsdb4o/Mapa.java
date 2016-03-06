package com.rubino.mapsdb4o;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rubino.mapsdb4o.bd.Db4O;
import com.rubino.mapsdb4o.pojo.Posicion;
import com.rubino.mapsdb4o.servicio.Servicio;

import java.util.ArrayList;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private static final int CTEPLAY = 1;
    private LocationRequest peticionLocalizaciones;
    private GoogleApiClient cliente;
    private Db4O bd;
    private Location ultimaLocalizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        ponerMap();
        Intent intent = new Intent(this, Servicio.class);


        startService(intent);
    }


    /**
     * Menu contextual que pintara la ruta del dia que seleccionemos
     * usando los datos de la bd que el servicio ira llenando de contenido.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_mon) {
            setRuta(2);
            return true;
        }
        if (id == R.id.action_tues) {
            setRuta(3);
            return true;
        }

        if (id == R.id.action_wed) {
            setRuta(4);
            return true;
        }
        if (id == R.id.action_thurs) {
            setRuta(5);
            return true;
        }
        if (id == R.id.action_fri) {
            setRuta(6);
            return true;
        }
        if (id == R.id.action_satur) {
            setRuta(7);
            return true;
        }
        if (id == R.id.action_sund) {
            setRuta(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setRuta(int dia) {
        bd = new Db4O(this);
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions();
        ArrayList<Posicion> pos = bd.getRuta(dia);
        Log.v("DIAAAAA", pos.toString());
        for (Posicion p : pos) {
            rectOptions.add(new LatLng(p.getLatitud(), p.getLongitud()));
        }

        // Get back the mutable Polyline
        mMap.clear();
        mMap.addPolyline(rectOptions);
        bd.close();
    }


    @Override
    public void onConnected(Bundle bundle) {
        peticionLocalizaciones = new LocationRequest();
        peticionLocalizaciones.setSmallestDisplacement(1);
        peticionLocalizaciones.setFastestInterval(5000);
        peticionLocalizaciones.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(cliente, peticionLocalizaciones, this);
        ultimaLocalizacion = LocationServices.
                FusedLocationApi.getLastLocation(cliente);
        if (ultimaLocalizacion != null) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double la = location.getLatitude();
        double lo = location.getLongitude();
        //tv.append("Latitud: " + la + " " + "Longitude: " + lo + "\n");
        LatLng sydney = new LatLng(la, lo);
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }


    public void ponerMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            cliente = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            cliente.connect();
            //tv.setText("Conecta" + "\n");
        } else {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, this, CTEPLAY).show();
            } else {
                Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
            }
        }


        peticionLocalizaciones = new LocationRequest();
        peticionLocalizaciones.setInterval(10000);
        peticionLocalizaciones.setFastestInterval(5000);
        peticionLocalizaciones.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}
