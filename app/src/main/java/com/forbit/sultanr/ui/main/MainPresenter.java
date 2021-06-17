package com.forbit.sultanr.ui.main;

import android.content.SharedPreferences;
import android.util.Log;

import com.forbit.sultanr.api.ApiClient;
import com.forbit.sultanr.api.ServiceGenerator;
import com.forbit.sultanr.models.Client;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.ui.login.LoginContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;

    }


    @Override
    public void ButtonClick() {

    }

    @Override
    public void startLoginActivity() {
        mView.startLoginActivity();
    }

    @Override
    public void getDevice(Client client) {
        ApiClient apiClient = ServiceGenerator.createService(ApiClient.class);

        mView.showProgressDialog();
        apiClient.getDevice(client)
                .enqueue(new Callback<Device>() {
                    @Override
                    public void onResponse(Call<Device> call, Response<Device> response) {
                        mView.hideProgressDialog();
                        if(response.isSuccessful()){
                            Device device = response.body();

                            if(device !=null){
                                mView.startMapActivity(device);
                            }else{
                                mView.showToast("Vehicle Not Found");
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Device> call, Throwable t) {
                        mView.hideProgressDialog();

                        mView.showToast("Error Happen in Parsing Device");
                    }
                });
    }
}

