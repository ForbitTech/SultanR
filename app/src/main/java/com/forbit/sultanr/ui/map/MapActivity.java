package com.forbit.sultanr.ui.map;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.forbit.sultanr.R;
import com.forbit.sultanr.markerAnimation.LatLngInterpolator;
import com.forbit.sultanr.markerAnimation.MarkerAnimation;
import com.forbit.sultanr.models.Client;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.models.Geo;
import com.forbit.sultanr.ui.login.LoginActivity;
import com.forbit.sultanr.ui.main.MainActivity;
import com.forbit.sultanr.ui.monthlyReport.MonthlyActivity;
import com.forbit.sultanr.ui.report.ReportActivity;
import com.forbit.sultanr.utils.AppPreference;
import com.forbit.sultanr.utils.Constant;
import com.forbit.sultanr.utils.PrebaseActivity;
import com.github.anastr.speedviewlib.TubeSpeedometer;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapActivity extends PrebaseActivity implements OnMapReadyCallback, View.OnClickListener, MapContract.View {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Marker mMarker;
    private String deviceID;

    private LatLng currentLocation;
    private Device currentDevice;
    private Device intentDevice;
    private int carIconSize;
    private boolean isVehicleLoad;

    // Clan
    private LinearLayout gt_device;

    private FloatingActionMenu fabHam;
    private com.github.clans.fab.FloatingActionButton fabReport, fabMonthlyReport;
    private TextView tvdriver_name , tvdriver_phone , tvignition , tvspeed , tvcharging , tvvoltage_level , tvfuel_line , tv_reg;

    private TextView tvDriverName;
    private ImageView ivIndicator;
    private ImageView logout;
    private CircleImageView ivDriverImage;
    private RelativeLayout rlBottomContainer, rlRoot;
    private int bottomContainerHeight;
    private boolean isOpen = false;
    private MapPresenter mPresenter;

    private TextView tvTitle;

    private void moveVehicle() {

        LatLng latLng = new LatLng(currentDevice.getGeo().getLat(), currentDevice.getGeo().getLng());
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(carIconSize, carIconSize)));

        if (getDistance(latLng, currentLocation) > 2) { // change 10m to 2
            MarkerAnimation.animateMarkerToGB(mMarker, latLng, new LatLngInterpolator.Spherical());
            mMarker.setTitle(getAddress(latLng));
            currentLocation = latLng;
        }
    }

    private double getDistance(LatLng latLng1, LatLng latLng2) {
        Location loc1 = new Location("");
        Location loc2 = new Location("");

        if (latLng1 != null && latLng2 != null) {
            loc1.setLatitude(latLng1.latitude);
            loc1.setLongitude(latLng1.longitude);

            loc2.setLatitude(latLng2.latitude);
            loc2.setLongitude(latLng2.longitude);
        }

        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mPresenter = new MapPresenter(this);
        intentDevice = (Device) getIntent().getSerializableExtra(Constant.DEVICE);
        deviceID = intentDevice.getId();
        currentDevice = new Device();
        carIconSize = (int) getResources().getDimension(R.dimen.car_icon_size);

        setupToolbar();

        //Todo Remove Later

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //polyLineList = new ArrayList<>();

        mapFragment.getMapAsync(MapActivity.this);

        mPresenter.initView();


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("JJJJJJ","OnStart Called");



        if (mMap != null) {
            mPresenter.start();
        }
    }

    @Override
    public void onStop() {
        mPresenter.stop();
        super.onStop();
    }



    @Override
    public void initView() {
        fabHam = findViewById(R.id.menu);
        fabReport = findViewById(R.id.fab_report);
        fabMonthlyReport = findViewById(R.id.fab_monthly_report);

        fabReport.setOnClickListener(this);
        fabMonthlyReport.setOnClickListener(this);

//        speedometer = findViewById(R.id.tubeSpeedMeter);
//        speedometer.setWithEffects3D(true);
//        speedometer.setAlpha(0.9f);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.digital_7);

//        speedometer.setSpeedTextTypeface(typeface);
//        //speedometer.setSpeedTextTypeface();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            speedometer.setBackgroundColor(getResources().getColor(android.R.color.transparent, null));
//            speedometer.setSpeedometerColor(android.R.color.holo_orange_dark);
//        } else {
//            speedometer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//            speedometer.setSpeedometerColor(android.R.color.holo_orange_dark);
//        }

        // tvModel = findViewById(R.id.model);
        tvDriverName = findViewById(R.id.driver_name);
        ivIndicator = findViewById(R.id.indicator);
        gt_device = findViewById(R.id.gt_device);

        // tvModel = findViewById(R.id.model);
        tvdriver_name = findViewById(R.id.driver_name);
        tvdriver_phone = findViewById(R.id.driver_phone);
        tv_reg = findViewById(R.id.vehicleregno);
        tvcharging = findViewById(R.id.charging);
        tvfuel_line = findViewById(R.id.fuel_line);
        tvignition = findViewById(R.id.ignition);
        tvvoltage_level = findViewById(R.id.voltage_level);
        tvspeed = findViewById(R.id.speed);

        logout = findViewById(R.id.btnlogout);
        logout.setOnClickListener(this);

        rlBottomContainer = findViewById(R.id.bottom_container);
        rlRoot = findViewById(R.id.root);
        ivIndicator.setOnClickListener(this);
        String driverName = intentDevice.getDriver_name();
        String driverPhone = intentDevice.getDriver_phone();

        String regNumber = intentDevice.getRegistration_number();


        if(intentDevice.getId().length()>10){
            gt_device.setVisibility(View.VISIBLE);
        }



        if(driverName !=null && !driverName.equals("")){
            tvdriver_name.setText(driverName);
        }
        if(driverPhone !=null ){
            tvdriver_phone.setText(driverPhone);
        }

        if(regNumber !=null){
            tv_reg.setText(regNumber);
        }

        if(intentDevice.getGeo()!=null){
            updateInfo(intentDevice.getGeo());
        }

        rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //get Card Height
                int rootHeight = rlRoot.getHeight();
                rlBottomContainer.setY(rootHeight - getResources().getDimension(R.dimen.indicator_dim));
                bottomContainerHeight = rlBottomContainer.getHeight();
                rlRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvTitle = findViewById(R.id.title);
        tvTitle.setText(intentDevice.getRegistration_number());
    }



    private void updateInfo(Geo geo){
        if(geo.getFuel_line() !=null ){
            tvfuel_line.setText(geo.getFuel_line());
        }
        if(geo.getCharging() !=null ){
            tvcharging.setText(geo.getCharging());
        }
        if(geo.getVoltage_level() !=null ){
            tvvoltage_level.setText(geo.getVoltage_level());
        }

        if(geo.getAcc() !=null){
            tvignition.setText(geo.getAcc());
        }
        tvspeed.setText(String.valueOf(geo.getSpeed()).concat(" KMPH"));

    }
    @Override
    public void setVehicleData(Geo geo) {
        currentDevice.setGeo(geo);
        currentLocation = new LatLng(geo.getLat(), geo.getLng());
        if (!isVehicleLoad) {
            gotoVehicleLocation(currentLocation);
        }
    }

    @Override
    public void updateCurrentVehicle(Geo geo) {
        currentDevice.setGeo(geo);
//        speedometer.speedTo((float) geo.getSpeed(), 1000);
        moveVehicle();
        updateInfo(geo);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorDialog(String message) {
    }

    @Override
    public void logout() {
        AppPreference.getInstance(getApplicationContext()).clear();
        finish();
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Listening From Firebase Database
        mPresenter.startListenFromDevice(deviceID);

        Log.d("JJJJJ", deviceID);

    }

    private void gotoVehicleLocation(LatLng latLng) {
        isVehicleLoad = true;
        MarkerOptions option;

        if (getAddress(latLng) != null) {
            option = new MarkerOptions().position(latLng).title(getAddress(latLng));
        } else {
            option = new MarkerOptions().position(latLng).title("Vehicle Location");
        }

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(18).tilt(30).build();
        mMarker = mMap.addMarker(option);
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(carIconSize, carIconSize)));
        mMarker.setAnchor(0.5f, 0.5f);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        speedometer.speedTo((float) currentDevice.getGeo().getSpeed(), 1000);

    }

    private Bitmap getCarBitmap(int newWidth, int newHeight) {
        Bitmap bitmap = null;

        switch (intentDevice.getVehicle_type()) {
            case 1:
                if (currentDevice.getGeo().getAcc().equals("ON")) {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_green);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_red);
                }
                break;

            case 2:
                if (currentDevice.getGeo().getAcc().equals("ON")) {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.bike_green);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.bike_red);
                }
                break;

            case 3:
                if (currentDevice.getGeo().getAcc().equals("ON")) {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.micro_green);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.micro_red);
                }
                break;

            case 4:
                if (currentDevice.getGeo().getAcc().equals("ON")) {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.bus_green);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.bus_red);
                }
                break;

            case 5:
                if (currentDevice.getGeo().getAcc().equals("ON")) {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.truck_green);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.truck_red);
                }
                break;

            case 6:
                if (currentDevice.getGeo().getAcc().equals("ON")) {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.cng_green);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.cng_red);
                }
                break;

            case 7:
                if (currentDevice.getGeo().getAcc().equals("ON")) {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ship_green);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ship_red);
                }
                break;

            case 8:
                if (currentDevice.getGeo().getAcc().equals("ON")) {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tractor_green);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.tractor_red);
                }
                break;

            default:
                if (currentDevice.getGeo().getAcc().equals("ON")) {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_green);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_red);
                }
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }

    private String getAddress(LatLng latLng) {
        String address = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public int getPixelFromDp(float dp) {
        Resources resources = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
        return px;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fab_report:
                Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.DEVICE, intentDevice);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.fab_monthly_report:
                Intent mIntent = new Intent(getApplicationContext(), MonthlyActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Constant.DEVICE, intentDevice);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                break;

            case R.id.indicator:
                animate();
                break;

            case R.id.btnlogout:
                logout();
                break;
        }
    }

    private void animate() {
        final float animatedHeight = bottomContainerHeight - getResources().getDimension(R.dimen.indicator_dim);
        ValueAnimator animator = ValueAnimator.ofFloat(0, animatedHeight);

        final int currentY = (int) rlBottomContainer.getY();
        final float currentRotation = ivIndicator.getRotation();

        if (!isOpen) {
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    ivIndicator.setRotation(currentRotation - animatedValue * 180 / animatedHeight);
                    rlBottomContainer.setY(currentY - animatedValue);
                    rlBottomContainer.requestLayout();
                }
            });
            isOpen = true;

        } else {
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();

                    ivIndicator.setRotation(currentRotation + animatedValue * 180 / animatedHeight);
                    rlBottomContainer.setY(currentY + animatedValue);
                    rlBottomContainer.requestLayout();
                }
            });

            isOpen = false;
        }
        animator.start();
    }

    private LatLng getLatLong(String lat, String lng) {
        double lati = (double) Long.parseLong(lat, 16) / 1800000;
        double longi = (double) Long.parseLong(lng, 16) / 1800000;

        return new LatLng(lati, longi);
    }
    public void onBackPressed() {
        finishAffinity();
    }
}
