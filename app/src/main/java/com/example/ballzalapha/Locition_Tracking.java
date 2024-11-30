package com.example.ballzalapha;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Collections;
import java.util.List;

public class Locition_Tracking extends AppCompatActivity {
    private GeofencingClient geofencingClient;
    private Geofence gf;
    private static final String REQUEST_ID = "IT";
    private static final double LATITUDE =31.275157,LONGITUDE= 34.815265;
    private static final float GEOFENCE_RADIUS_IN_METERS = 100;
    private static final int REQUEST_LOCATION_PERMISSION = 455;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locition_tracking);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.POST_NOTIFICATIONS},
                PackageManager.PERMISSION_GRANTED);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        geofencingClient = LocationServices.getGeofencingClient(this);
        gf = new Geofence.Builder().setCircularRegion(
                        LATITUDE, LONGITUDE,GEOFENCE_RADIUS_IN_METERS)
                .setRequestId(REQUEST_ID)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.AddUser){
            Intent si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        else if(id == R.id.PicCamera){
            Intent si = new Intent(this,PicFromCamera.class);
            startActivity(si);
        }
        else if(id == R.id.NFC){
            Intent si = new Intent(this,NFC_Reading.class);
            startActivity(si);
        }
        else if(id == R.id.PicGallery){
            Intent si = new Intent(this,PicFromGallery.class);
            startActivity(si);
        }

        return true;
    }
    /**
     * onRequestPermissionsResult method
     * <p> Method triggered by other result of permissions request
     * </p>
     *
     * @param requestCode the request code triggered the activity
     * @param permissions the array of permissions granted
     * @param grantResults the array of permissions granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        return PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
    }


    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        List<Geofence> singleGeofenceList = Collections.singletonList(gf);
        builder.addGeofences(singleGeofenceList);
        return builder.build();
    }
    public void start_tracking(View view) {
        GeofencingRequest geofencingRequest = getGeofencingRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Geofence added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding geofence", Toast.LENGTH_SHORT).show();
                });
    }
}