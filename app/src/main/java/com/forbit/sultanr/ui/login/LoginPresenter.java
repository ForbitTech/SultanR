package com.forbit.sultanr.ui.login;

import android.util.Log;

import com.forbit.sultanr.api.ApiClient;
import com.forbit.sultanr.api.ServiceGenerator;
import com.forbit.sultanr.models.Client;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.ui.main.MainContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View lView;
    public LoginPresenter(LoginContract.View lView) {
        this.lView = lView;
    }

    @Override
    public boolean validate(String code, String password) {

        lView.clearError();

        if(code.equals("")){
            lView.showError(1,"Empty Code not Allowed");
            return false;
        }
        if(password.equals("")){
            lView.showError(2,"Empty Password not Allowed");
            return false;
        }

        return true;
    }

    @Override
    public void login(Client client) {
        lView.showProgressDialog();
        ApiClient apiClient = ServiceGenerator.createService(ApiClient.class);

        apiClient.getDevice(client)
                .enqueue(new Callback<Device>() {
                    @Override
                    public void onResponse(Call<Device> call, Response<Device> response) {
                        lView.hideProgressDialog();
                        if(response.isSuccessful()){
                            Device device = response.body();
                            if(device != null){
                                lView.startMapActivity(device,client);
                            }else{
                                lView.showToast("Vehicle Not Found");
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Device> call, Throwable t) {
                        lView.hideProgressDialog();
                        lView.showToast("Vehicle Not Found");
                    }
                });
    }
}
