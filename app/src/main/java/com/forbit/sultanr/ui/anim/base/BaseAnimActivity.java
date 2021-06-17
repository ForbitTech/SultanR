package com.forbit.sultanr.ui.anim.base;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.forbit.sultanr.R;
import com.forbit.sultanr.markerAnimation.LatLngInterpolator;
import com.forbit.sultanr.markerAnimation.MarkerAnimation;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.utils.MyUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class BaseAnimActivity extends AppCompatActivity implements OnMapReadyCallback {

    public List<Location> locationList;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;

    private int iconSize;
    private Marker mMarker;

    private PolylineOptions polylineOptions,darkPolyLineOption;
    private Polyline grayPolyLine,darkPolyLine;

    private MyThread myThread;

    public int vType=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide Status Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_base_anim);

        iconSize = (int) getResources().getDimension(R.dimen.car_icon_size);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //polyLineList = new ArrayList<>();

        mapFragment.getMapAsync(BaseAnimActivity.this);

        initData();
    }

    public void setLocations(List<Location> locationList){
        this.locationList=locationList;
    }

    public abstract void initData();



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        //gotoFirstLatLong();

        if(this.locationList.size()>0){
            animate();
        }

    }

    public void animate() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
       /* for (LatLng x : latLongList) {
            builder.include(x);
        }*/

        final List<LatLng> tempList = new ArrayList<>();

        for (int i = 0; i<locationList.size();i++){

            LatLng latLng = new LatLng(locationList.get(i).getGeo().getLat(),locationList.get(i).getGeo().getLng());
            builder.include(latLng);
            tempList.add(latLng);

        }



        LatLngBounds bounds = builder.build();
        final CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);

        try {
            this.mMap.animateCamera(mCameraUpdate);
        } catch (IllegalStateException e) {
            // layout not yet initialized
            final View mapView = mapFragment.getView();
            if (mapView.getViewTreeObserver().isAlive()) {
                mapView.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @SuppressWarnings("deprecation")
                            @SuppressLint("NewApi")
                            // We check which build version we are using.
                            @Override
                            public void onGlobalLayout() {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                    mapView.getViewTreeObserver()
                                            .removeGlobalOnLayoutListener(this);
                                } else {
                                    mapView.getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(this);
                                }
                                mMap.animateCamera(mCameraUpdate);
                            }
                        });
            }
        }
       // mMap.animateCamera(mCameraUpdate);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.GRAY);
        polylineOptions.width(4);
        polylineOptions.startCap(new SquareCap());
        polylineOptions.endCap(new SquareCap());
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.addAll(tempList);

        grayPolyLine = mMap.addPolyline(polylineOptions);

        darkPolyLineOption = new PolylineOptions();
        darkPolyLineOption.width(3);
        darkPolyLineOption.color(Color.RED);
        darkPolyLineOption.startCap(new SquareCap());
        darkPolyLineOption.endCap(new SquareCap());
        darkPolyLineOption.jointType(JointType.ROUND);

        darkPolyLine = mMap.addPolyline(darkPolyLineOption);

        // Add a Marker at the Start of the Path
        mMap.addMarker(new MarkerOptions()
                .position(tempList.get(0)));

        // Poly Line Animator
        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
        polylineAnimator.setDuration(tempList.size()*100);
        polylineAnimator.setInterpolator(new LinearInterpolator());
        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                List<LatLng> points = grayPolyLine.getPoints();
                int percentValue = (int) valueAnimator.getAnimatedValue();
                int size = points.size();
                int newPoints = (int) (size * (percentValue / 100.0f));
                List<LatLng> p = points.subList(0, newPoints);
                darkPolyLine.setPoints(p);

                if(valueAnimator.getAnimatedFraction()==1){
                    mMap.addMarker(new MarkerOptions()
                            .position(tempList.get(tempList.size() - 1)));
                }
            }
        });


        polylineAnimator.start();


        mMarker = mMap.addMarker(new MarkerOptions().position(tempList.get(0))
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(iconSize,iconSize))));



        mMap.moveCamera(CameraUpdateFactory
                .newCameraPosition
                        (new CameraPosition.Builder()
                                .target(tempList.get(0))
                                .zoom(15.5f)
                                .build()));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View view = null;

                if(marker.getTag()!=null){
                    int value = (int) marker.getTag();
                    // Getting view from the layout file info_window_layout
                    view = getLayoutInflater().inflate(R.layout.info_window, null);
                    TextView tvAddress = view.findViewById(R.id.address);
                    TextView tvspeed = view.findViewById(R.id.speed);
                    TextView tvAcc = view.findViewById(R.id.acc);

                    Location location = locationList.get(value);
                    LatLng latLng = new LatLng(location.getGeo().getLat(),location.getGeo().getLng());

                    tvAddress.setText(getAddress(latLng));
                    tvspeed.setText(MyUtil.getTwoDecimalFormat(location.getGeo().getSpeed()));

                    tvAcc.setText(location.getGeo().getAcc());



                }
                return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        myThread = new MyThread(tempList); // Update here
    }


    private Bitmap getCarBitmap(int newWidth, int newHeight) {

        Bitmap bitmap = null;

        // Log.d("MANGGG",vType+"");

        switch (vType){
            case 0:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_green);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_green);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.bike_green);
                break;

            case 3:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.micro_green);
                break;

            case 4:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.bus_green);
                break;

            case 5:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.truck_green);
                break;

            case 6:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.cng_green);
                break;

            case 7:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ship_green);
                break;

            case 8:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.tractor_green);
                break;

            default:
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_green);
        }


       /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_green);*/

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


    class MyThread extends Thread {
        List<LatLng> latLngList;

        int size,counter,carAnimationTime,markerAnimationTime;
        private Handler handler;

        boolean isRunning;


        public MyThread(List<LatLng> latLngList){
            this.latLngList = latLngList;
            this.size = latLngList.size();
            this.counter=0;
            this.carAnimationTime=500;
            this.markerAnimationTime=300;
            this.handler = new Handler();
            isRunning =true;
            this.start();
        }

        public void setCarAnimationTime(int time){
            this.carAnimationTime=time;
        }

        public void setMarkerAnimationTime(int time){
            this.markerAnimationTime=time;
        }

        @Override
        public void run() {

            while (isRunning){
                try {
                    Thread.sleep(carAnimationTime);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MarkerAnimation.myMapAnim(mMarker, latLngList.get(counter), new LatLngInterpolator.Spherical(),carAnimationTime);

                            /*Log.d("JJJJ","KK");*/
                        }
                    });

                    if(counter==size-2){
                        isRunning=false;
                    }

                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = latLngList.size()-1;i>=0;i--){
                try {
                    Thread.sleep(markerAnimationTime);
                    final LatLng x = latLngList.get(i);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            Marker marker =mMap.addMarker(new MarkerOptions().position(x));
                            marker.setTag(latLngList.indexOf(x));
                            //marker.showInfoWindow();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        public void stopThread(){
            isRunning=false;
            try {
                this.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String getAddress(LatLng latLng){
        String address = null;
        Geocoder geocoder  =new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
}
