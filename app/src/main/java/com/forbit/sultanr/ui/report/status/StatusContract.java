package com.forbit.sultanr.ui.report.status;



import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.VehicleStatus;

import java.util.List;

public interface StatusContract {

    interface Presenter{
        void processLocations(List<Location> locationList);
    }

    interface View{
        void updateStatus(List<VehicleStatus> vehicleStatusList);
    }
}
