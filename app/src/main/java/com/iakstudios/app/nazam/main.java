package com.iakstudios.app.nazam;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class main extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mGoogleMap;
    public static final int REQUEST_LOCATION_CODE = 99;
    private static final String TAG = "MainActivity";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    private MapView mMapView;
    private Location mLocation;
    private LocationManager mLocationManager;
    EditText ed_name, ed_phon;
    TextView subData;
    private boolean showedSpecs = false;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 4 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;
    Location location;
    public String chang;
    Spinner ed_role;
    private RelativeLayout containerOfLogin;
    private ImageButton mImageButton;
    private LinearLayout specs;
    private ImageView hajj , bus , doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userIdRef = rootRef.child("users");
        userIdRef.addValueEventListener(new ValueEventListener() {
            List<userInfo> userInfo = new ArrayList<userInfo>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    userInfo.add(ds.getValue(userInfo.class));
                    hajj = (ImageView) findViewById(R.id.hajj);
                    bus = (ImageView) findViewById(R.id.bus);
                    doctor = (ImageView) findViewById(R.id.doctor);


                    hajj.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            hajj.setBackgroundResource(R.drawable.cercle2);
                            doctor.setBackgroundResource(R.drawable.back);
                            bus.setBackgroundResource(R.drawable.back);
                        }
                    });


                    bus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hajj.setBackgroundResource(R.drawable.back);
                            bus.setBackgroundResource(R.drawable.cercle2);
                            doctor.setBackgroundResource(R.drawable.back);
                        }
                    });

                    doctor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hajj.setBackgroundResource(R.drawable.back);
                            bus.setBackgroundResource(R.drawable.back);
                            doctor.setBackgroundResource(R.drawable.cercle2);
                        }
                    });


                    //and so on
                }
                for (int i=0;i<userInfo.size();i++){

                    ArrayList<locate> locations = userInfo.get(i).getLocations();
                    Log.d("pof", "++" + userInfo.get(i).getName().toString()+"\t"+locations.get(locations.size()-1).getLatitude() +"\t"+locations.get(locations.size()-1).getLongitude());


                    BitmapDescriptor image  ;
                    if(userInfo.get(i).getRole().equalsIgnoreCase("Hajj")){
                        image = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                    }else if(userInfo.get(i).getRole().equalsIgnoreCase("medic")){
                        image =BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                    } else {
                        image =BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    }


                    createMarker(locations.get(locations.size()-1).getLatitude(),locations.get(locations.size()-1).getLongitude(),userInfo.get(i).getName()+","+userInfo.get(i).getMobile(),image);

                }



            }

            private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
                Canvas canvas = new Canvas();
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                canvas.setBitmap(bitmap);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                drawable.draw(canvas);
                return BitmapDescriptorFactory.fromBitmap(bitmap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });




        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_phon = (EditText) findViewById(R.id.ed_phone);
        ed_role = (Spinner) findViewById(R.id.ed_type);
        containerOfLogin = (RelativeLayout) findViewById(R.id.login);
        mImageButton = (ImageButton) findViewById(R.id.menu);
        specs = (LinearLayout) findViewById(R.id.specs);
        mMapView = (MapView) findViewById(R.id.map);


        subData = (TextView) findViewById(R.id.sub_data);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone

        subData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                containerOfLogin.setVisibility(View.GONE);
                mImageButton.setVisibility(View.VISIBLE);


             /*  String d_phe;
                String e_nam;
                if (ed_name.getText() != null && ed_phon.getText() != null) {
                    e_nam = ed_name.getText().toString().trim();
                    d_phe = ed_phon.getText().toString().trim();

                    int ded_phon = Integer.parseInt(d_phe);

                    if (e_nam.isEmpty() && d_phe.isEmpty()) {
                        Toast.makeText(main.this, "no", Toast.LENGTH_LONG).show();
                    } else {
                        String d_rol = ed_role.getSelectedItem().toString();
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                        String userId = mDatabase.push().getKey();
                        chang = userId;

                        locate lo = new locate(location.getLatitude(), location.getLongitude());
                        ArrayList<locate> ad = new ArrayList<>();
                        ad.add(lo);
                        Log.d("test_2", ad.toString());
                        userInfo user = new userInfo(e_nam+"", d_rol+"", ded_phon+"", ad);

                        mDatabase.child(userId).setValue(user);
                    }
                }
*/

            }
        });


        mImageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (showedSpecs) {
                    specs.setVisibility(View.GONE);
                    mImageButton.setImageResource(R.drawable.menu_icon);
                    showedSpecs = false;
                } else {
                    specs.setVisibility(View.VISIBLE);
                    mImageButton.setImageResource(R.drawable.close);
                    showedSpecs = true;
                }

            }
        });

        if (Build.VERSION.SDK_INT >= 14) {
            checkLocationPermission();
        }

        mMapView = (MapView) findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }


    }

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(final Location location) {

        this.location = location;
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        mLongitudeTextView.setText(String.valueOf(location.getLongitude()));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


//       DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//
//
//
//        locate lo=new locate(location.getLatitude(),location.getLongitude());
//        ArrayList<locate> ad=new ArrayList<locate>();
//        ad.add(lo);
//        Log.d("test_2",ad.toString());
//        userInfo user = new userInfo(ad);
//
//      //  database.child("users/"+).child("locations").setValue(ad);
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        mGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);

            mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    LatLng centerOfMap = mGoogleMap.getCameraPosition().target;
                    Log.v("mmmmmm", centerOfMap.latitude + "====" + centerOfMap.longitude);
                }
            });


        }

        return;


    }

    protected synchronized void bulidGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

    }

    protected Marker createMarker(double latitude, double longitude, String title, BitmapDescriptor iconResID) {

        return mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(iconResID));
    }
}
