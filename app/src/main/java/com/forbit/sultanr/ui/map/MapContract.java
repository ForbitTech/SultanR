package com.forbit.sultanr.ui.map;


import com.forbit.sultanr.models.Client;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.models.Geo;

public interface MapContract {

    interface Presenter{
        void initView();
        void start();
        void stop();
        void startListenFromDevice(String deviceId);
    }

    interface View{
        void initView();
        void setVehicleData(Geo fireData);
        void updateCurrentVehicle(Geo fireData);
        void showToast(String message);
        void showErrorDialog(String message);
        void logout();

        void startMainActivity();

    }
}
