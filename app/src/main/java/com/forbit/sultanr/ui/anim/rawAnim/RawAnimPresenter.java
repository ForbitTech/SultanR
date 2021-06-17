package com.forbit.sultanr.ui.anim.rawAnim;


import com.forbit.sultanr.api.ApiClient;
import com.forbit.sultanr.api.ServiceGenerator;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RawAnimPresenter implements RawAnimContract.Presenter {
    private RawAnimContract.View mView;

    public RawAnimPresenter(RawAnimContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getLocations(RBody rBody) {
        ApiClient client = ServiceGenerator.createService(ApiClient.class);


        client.getDailyLocations("hjjhghjgh",rBody)
                .enqueue(new Callback<List<Location>>() {
                    @Override
                    public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                        if(response.isSuccessful()){
                            List<Location> locationList = response.body();
                            if(locationList.size()>2){
                                mView.updateLocations(locationList);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Location>> call, Throwable t) {

                    }
                });
    }
}
