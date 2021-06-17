package com.forbit.sultanr.ui.report;



import com.forbit.sultanr.api.ApiClient;
import com.forbit.sultanr.api.ServiceGenerator;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RBody;
import com.forbit.sultanr.models.RData;
import com.forbit.sultanr.models.VehicleStatus;
import com.forbit.sultanr.utils.MyUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportPresenter implements ReportContract.Presenter {
    private ReportContract.View mView;
    private Device device;

    public ReportPresenter(ReportContract.View mView, Device device) {
        this.mView = mView;
        this.device = device;
    }

    @Override
    public void requestForData(final RBody rBody) {
        mView.showDialog();

        ApiClient client = ServiceGenerator.createService(ApiClient.class);

        client.getDailyLocations("hghgghhggh",rBody)
                .enqueue(new Callback<List<Location>>() {
                    @Override
                    public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                        mView.hideDialog();
                        if(response.isSuccessful()){
                            List<Location> locationList = response.body();

                            mView.updateUI(locationList);
                            getRunningTimeForLocations(locationList);

                        }else if(response.code()==401){
                            mView.showToast("Invalid Token");
                        }else if(response.code()==402){
                            mView.showToast("Token Missing");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Location>> call, Throwable t) {
                        mView.hideDialog();
                        mView.showToast("Error Happen in the Fetching Data");

                    }
                });


    }

    @Override
    public void updateTitle() {
        mView.updateTitle();
    }

    @Override
    public void updateFuel() {
        mView.updateFuel();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void initialize() {
        mView.updateTravelTime(null);
        mView.updateDistance(0);
    }

    @Override
    public void showInfoDialog() {
        mView.showInfoDialog();
    }

    private double getDistance(List<RData> rDataList){
        return MyUtil.getDistanceFrom(rDataList);
    }

    private String getRunningTime(List<RData> rDataList){

        List<VehicleStatus> vehicleStatuses = getVehicleStatusList(rDataList);

        long duration = 0;
        for (VehicleStatus x: vehicleStatuses){
            if (x.getStatus().equals("1")){
                duration = duration+(x.getEndTime()-x.getStartTime());

            }
        }

        duration = duration/1000;

        String val ="";

        if(duration>=3600){
            int hour = (int) (duration/3600);
            int min = (int) (duration%60);

            val = hour+" hr "+min+" min";
        }else if(duration<3600 && duration>=60){
            int min = (int) (duration/60);
            int sec = (int) (duration-min*60);
            val = min+" min "+sec+" sec";
        }else {
            val = duration+" sec";
        }

        return val;
    }

    private List<RData> getFilteredData(List<RData> rDataList ){
        /*List<RData> retList = new ArrayList<>();

        if(rDataList.size()>2){
            RData fd = rDataList.get(0);
            retList.add(fd);

            for (RData x: rDataList){
               if(rDataList.indexOf(x)!=0){
                   double d = Haversine.distance(fd.getLat(),fd.getLng(),x.getLat(),x.getLng());

                   if(d>=THRESHOLD){
                       retList.add(x);
                       fd = x;
                   }

               }
            }
        }

        retList.add(rDataList.get(rDataList.size()-1));*/

        return rDataList;
    }

    private List<VehicleStatus> getVehicleStatusList(List<RData> rDataList){

        RData initialFData = new RData();
        initialFData.setServerTime(MyUtil.getBeginingTime(rDataList.get(0).getServerTime()));
        initialFData.setStatus("0");

        List<VehicleStatus> vehicleStatusList = new ArrayList<>();

        for(int i=0;i<rDataList.size();i++){

            RData rData = rDataList.get(i);

            if(!rData.getStatus().equals(initialFData.getStatus())){
                VehicleStatus vehicleStatus = new VehicleStatus();
                vehicleStatus.setStartTime(initialFData.getServerTime());
                vehicleStatus.setEndTime(rData.getServerTime());
                vehicleStatus.setStatus(initialFData.getStatus());
                vehicleStatusList.add(vehicleStatus);

                initialFData = rData;
            }
        }

        return vehicleStatusList;
    }


    public void getRunningTimeForLocations(List<Location> locationList){

        if(locationList.size()>0){

            long offTime =0;
            long onTime =0;

            long currentTime = MyUtil.getBeginingTime(locationList.get(0).getDate().getDateTime().getTime());
            String currentAcc="OFF";

            for(Location x: locationList){
                if(x.getGeo().getAcc().equals(currentAcc)){
                    if(currentAcc.equals("OFF")){
                        offTime = offTime+(x.getDate().getDateTime().getTime()-currentTime);
                    }else{
                        onTime = onTime+(x.getDate().getDateTime().getTime()-currentTime);
                    }

                }else{

                    if(currentAcc.equals("OFF")){
                        currentAcc="ON";
                    }else{
                        currentAcc="OFF";
                    }
                }

                currentTime = x.getDate().getDateTime().getTime();
            }

            String val ="";
            onTime = onTime/1000; // Convert into Seconds

            if(onTime>=3600){
                int hour = (int) (onTime/3600);
                int min = (int) (onTime%60);

                val = hour+" hr "+min+" min";
            }else if(onTime<3600 && onTime>=60){
                int min = (int) (onTime/60);
                int sec = (int) (onTime-min*60);
                val = min+" min "+sec+" sec";
            }else {
                val = onTime+" sec";
            }

            mView.updateTravelTime(val);

        }else{
            mView.updateTravelTime("0 min 0 sec");
        }


    }

}
