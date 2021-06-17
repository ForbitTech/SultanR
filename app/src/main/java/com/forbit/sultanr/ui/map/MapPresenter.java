package com.forbit.sultanr.ui.map;

import android.util.Log;

import androidx.annotation.NonNull;

import com.forbit.sultanr.firebase.MyDatabaseRef;
import com.forbit.sultanr.models.Geo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapPresenter implements MapContract.Presenter {
    private MapContract.View mView;
    private DatabaseReference deviceRef;

    public MapPresenter(MapContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initView() {
        mView.initView();
    }

    @Override
    public void start() {
        if (deviceRef != null) {
            deviceRef.addChildEventListener(listener);
            Log.d("JJJJJJ", "Reference Added");
        }
    }

    @Override
    public void stop() {
        if (deviceRef != null) {
            deviceRef.removeEventListener(listener);
        }
    }

    @Override
    public void startListenFromDevice(String deviceId) {
        this.deviceRef = MyDatabaseRef.getInstance().getDeviceRef().child(deviceId);
        deviceRef.addChildEventListener(listener);

    }

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.getValue() != null) {
                if (dataSnapshot.getKey().equals("geo")) {
                    Geo geo = dataSnapshot.getValue(Geo.class);
                    mView.setVehicleData(geo);

                    Log.d("JJJJJJ", "onChildAdded");
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.getKey().equals("geo")) {
                Geo geo = dataSnapshot.getValue(Geo.class);
                mView.updateCurrentVehicle(geo);

                Log.d("JJJJJJ", "Called");
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("JJJJJJ", " Error Called");
        }
    };
}
