package ch.berufsbildungscenter.weathermasters.Location;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import ch.berufsbildungscenter.weathermasters.Alert.CustomDialog;
import ch.berufsbildungscenter.weathermasters.MainActivity;
import ch.berufsbildungscenter.weathermasters.R;

/**
 * Created by zmartl on 24.06.2015.
 * Version ${VERSION}
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    private boolean canGetLocation = false;
    private boolean isNetworkEnabled = false;
    private boolean isGPSEnabled = false;
    private double latitude;
    private double longitude;
    private Location location;
    private CustomDialog customDialog = new CustomDialog();

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000;

    protected LocationManager locationManager;


    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;

                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }
                if (locationManager != null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location != null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
            if(isGPSEnabled){
                if(location == null){
                    customDialog.displayAlertDialog(context, R.string.gpsErrorTitle, R.string.gpsErrorMessage, R.mipmap.warning);
                }

            }
        } catch (Exception e){
            customDialog.displayAlertDialog(context, R.string.gpsErrorTitle, R.string.gpsErrorMessage, R.mipmap.warning);
        }
        return location;
    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.gpsTitle);
        alertDialog.setMessage("GPS ist nicht eingeschalten. Einstellungen offnen?");
        alertDialog.setIcon(R.mipmap.gps);
        alertDialog.setPositiveButton("Einstellungen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Schliessen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}