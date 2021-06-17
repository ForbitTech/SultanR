package com.forbit.sultanr.ui.report.status;

import android.util.Log;


import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.VehicleStatus;

import java.util.ArrayList;
import java.util.List;

public class StatusPresenter implements StatusContract.Presenter {
    private StatusContract.View mView;

    public StatusPresenter(StatusContract.View mView) {
        this.mView = mView;

    }

    @Override
    public void processLocations(List<Location> locationList) {

        List<VehicleStatus> vehicleStatusList = new ArrayList<>();

        for (Location location:locationList){
            if(locationList.indexOf(location)==0){
                VehicleStatus vehicleStatus = new VehicleStatus();
                vehicleStatus.setStatus(location.getGeo().getAcc());
                vehicleStatus.addLocation(location);
                vehicleStatusList.add(vehicleStatus);
            }else{
                if(vehicleStatusList.get(vehicleStatusList.size()-1).getStatus().equals(location.getGeo().getAcc())){
                    vehicleStatusList.get(vehicleStatusList.size()-1).addLocation(location);
                }else{
                    VehicleStatus vehicleStatus = new VehicleStatus();
                    vehicleStatus.setStatus(location.getGeo().getAcc());
                    vehicleStatus.addLocation(location);
                    vehicleStatusList.add(vehicleStatus);
                }
            }
        }

        mView.updateStatus(vehicleStatusList);

        Log.d("HHHHH",vehicleStatusList.size()+"");

    }
}
