package com.forbit.sultanr.ui.anim.controlAnimation;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;


import com.forbit.sultanr.R;
import com.forbit.sultanr.markerAnimation.LatLngInterpolator;
import com.forbit.sultanr.markerAnimation.MarkerAnimation;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RBody;
import com.forbit.sultanr.utils.Constant;
import com.forbit.sultanr.utils.MyUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ControlAnimationActivity extends AppCompatActivity
        implements ControlAnimationContract.View, OnMapReadyCallback, View.OnClickListener {

    private ControlAnimationPresenter mPresenter;

    private int v_type;
    private String id;
    private Date date;

    private GoogleMap mMap;

    private MyEmmiter myEmmiter;

    private Marker mMarker;

    private int carIconSize;

    private AppCompatImageButton btnPlay,btnPause,btnFaster;

    private TextView tvAddress,tvSpeed,tvAcc;
    private ImageView ivIndicator;

    private RelativeLayout rlContainer,rlRoot;
    private CardView mCard;

    private int cardHeight;
    private boolean isExpand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_control_animation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        date = (Date) getIntent().getSerializableExtra(Constant.SELECTED_DATE);
        v_type = getIntent().getIntExtra(Constant.V_TYPE,1);
        id = getIntent().getStringExtra(Constant.DEVICE_ID);

        this.mPresenter = new ControlAnimationPresenter(this,v_type);

        carIconSize = (int) getResources().getDimension(R.dimen.car_icon_size);

        btnPlay = findViewById(R.id.play);
        btnPause = findViewById(R.id.pause);
        btnFaster = findViewById(R.id.faster);

        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnFaster.setOnClickListener(this);

        rlContainer = findViewById(R.id.rl_container);
        rlRoot = findViewById(R.id.root);
        mCard = findViewById(R.id.card);

        tvAddress = findViewById(R.id.address);
        tvSpeed = findViewById(R.id.speed);
        tvAcc = findViewById(R.id.acc);
        ivIndicator = findViewById(R.id.indicator);

        ivIndicator.setOnClickListener(this);

        Log.d("HHHHH",cardHeight+"");


        /*ViewTreeObserver viewTreeObserver = rlContainer.getViewTreeObserver();

        viewTreeObserver.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View view, View view1) {
                rlContainer.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
                cardHeight = mCard.getHeight();

                Log.d("HHHHH",cardHeight+"");
            }
        });*/

        rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardHeight = mCard.getHeight();
                Log.d("HHHHH",cardHeight+"");

                rlRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlContainer.getLayoutParams();
                //params.

                rlContainer.setY(-cardHeight);
            }
        });





    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myEmmiter!=null){
            myEmmiter.pause();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        RBody requestBody = new RBody();
        requestBody.setDevice_time(MyUtil.getReqDate(date));
        requestBody.setId(id);
        mPresenter.getLocations(requestBody);
    }

    @Override
    public void updateCamera(List<Location> locationList) {
        Log.d("RASHIN",locationList.size()+"");

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        PolylineOptions polylineOptions = new PolylineOptions();

        for (Location x: locationList){
            LatLng latLng = new LatLng(x.getGeo().getLat(),x.getGeo().getLng());
            polylineOptions.add(latLng);
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
        mMap.animateCamera(mCameraUpdate);

        // Initiate Emmiter Class

        addMarker(locationList.get(0));
        locationList.remove(0);



        polylineOptions.color(Color.BLACK);
        polylineOptions.width(6);
        polylineOptions.startCap(new SquareCap());
        polylineOptions.endCap(new SquareCap());
        polylineOptions.jointType(JointType.ROUND);

        mMap.addPolyline(polylineOptions);

        if(myEmmiter==null){
            myEmmiter = new MyEmmiter(this,locationList);
        }


    }

    public void updateLocation(Location location,int duration){
        Log.d("RASHIN","Location "+location.getGeo().getLat()+" , "+location.getGeo().getLng());

        updateMarker(location,duration);
        /*if(mMarker==null){
            addMarker(location);
        }else {

        }*/
    }

    private void addMarker(Location location) {
        LatLng latLng = new LatLng(location.getGeo().getLat(),location.getGeo().getLng());
        MarkerOptions option = new MarkerOptions().position(latLng).title(location.getId());
        //CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).tilt(30).build();
        mMarker = mMap.addMarker(option);
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(carIconSize,carIconSize,v_type)));
        mMarker.setAnchor(0.5f,0.5f);

        updateUi(location);
    }

    private void updateMarker(Location location,int duration){

        LatLng latLng = new LatLng(location.getGeo().getLat(),location.getGeo().getLng());
        //mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(carIconSize,carIconSize,v_type)));
        MarkerAnimation.animateMarkerWithTime(mMarker, latLng,duration, new LatLngInterpolator.Spherical());

        updateUi(location);
    }

    private void updateUi(Location location){
        String address = getAddress(location);

        if(address!=null){
            tvAddress.setText(address);
        }

        tvSpeed.setText(String.valueOf(location.getGeo().getSpeed()));
        tvAcc.setText(location.getGeo().getAcc());

    }


    private Bitmap getCarBitmap(int newWidth, int newHeight, int vehicletype) {


        Bitmap bitmap = null;

        switch (vehicletype){

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

    @Override
    public void onClick(View view) {
        if(view==btnPlay){
            if(myEmmiter!=null){
                myEmmiter.resume();
            }
        }else if(view==btnPause){
            if(myEmmiter!=null){
                myEmmiter.pause();
            }
        }else if(view==btnFaster){
            if(myEmmiter!=null){
                myEmmiter.faster2x();
            }
        }else if(view==ivIndicator){
            animate();
        }
    }

    private void animate(){
        ValueAnimator animator = ValueAnimator.ofFloat(1,cardHeight);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);

        int initialCardY = (int) rlContainer.getY();


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int value;
            int rotation;
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                value = (int) ((cardHeight)*valueAnimator.getAnimatedFraction());
                if(isExpand){
                    rotation = (int) ((1-valueAnimator.getAnimatedFraction())*180);
                    rlContainer.setTranslationY(initialCardY-value);

                }else{
                    rotation = (int) (180*valueAnimator.getAnimatedFraction());
                    rlContainer.setTranslationY(initialCardY+value);
                }

                ivIndicator.setRotation(rotation);

                if(valueAnimator.getAnimatedFraction()==1){
                    isExpand = !isExpand;
                }


            }
        });

        animator.start();



    }

    private String getAddress(Location location){

        LatLng latLng = new LatLng(location.getGeo().getLat(),location.getGeo().getLng());

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
