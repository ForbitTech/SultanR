package com.forbit.sultanr.ui.anim.controlAnimation;



import com.forbit.sultanr.api.ApiClient;
import com.forbit.sultanr.api.ServiceGenerator;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RBody;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlAnimationPresenter implements ControlAnimationContract.Presenter {

    private ControlAnimationContract.View mView;
    private int vehicleType;

    public ControlAnimationPresenter(ControlAnimationContract.View mView,int vehicleType) {
        this.mView = mView;
        this.vehicleType = vehicleType;
    }

    @Override
    public void getLocations(RBody rBody) {
        ApiClient client = ServiceGenerator.createService(ApiClient.class);

        client.getDailyLocations("jhjhkjhkdhjf",rBody)
                .enqueue(new Callback<List<Location>>() {
                    @Override
                    public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                        if(response.isSuccessful()){
                            List<Location> myLocations = new ArrayList<>();

                            if(vehicleType<5){
                                for (Location x: response.body()){
                                    if(x.getGeo().getAcc().equals("ON")){
                                        myLocations.add(x);
                                    }
                                }

                            }else{
                                myLocations.addAll(response.body());
                            }
                            if(myLocations.size()>2){
                                mView.updateCamera(myLocations);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Location>> call, Throwable t) {

                    }
                });
    }
}
