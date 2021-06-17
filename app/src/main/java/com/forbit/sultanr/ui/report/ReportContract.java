package com.forbit.sultanr.ui.report;




import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RBody;

import java.util.List;

public interface ReportContract {

    interface Presenter{
        void requestForData(RBody rBody);
        void updateTitle();
        void updateFuel();
        void destroy();
        void initialize();
        void showInfoDialog();
    }

    interface View{
        void showDialog();
        void hideDialog();
        //void updateUI(List<RData> rDataList);
        void updateTitle();
        void updateFuel();
        void updateUI(List<Location> locationList);
        void updateDistance(double distance);
        void updateTravelTime(String travelTime);
        void showToast(String message);
        void showInfoDialog();
        void startLoginActivity();
    }
}
