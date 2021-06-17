package com.forbit.sultanr.ui.monthlyReport;


import android.util.Log;

import com.forbit.sultanr.api.ApiClient;
import com.forbit.sultanr.api.ServiceGenerator;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.models.MonthlyData;
import com.forbit.sultanr.models.MonthlyFileRequest;
import com.forbit.sultanr.models.MonthlyRequestBody;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthlyPresenter implements MonthlyContract.Presenter {
    private MonthlyContract.View mView;

    public MonthlyPresenter(MonthlyContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getMonthlyData(String deviceId, int vehicleType, int month, int year) {
        mView.showDialog();

        MonthlyRequestBody body = new MonthlyRequestBody(deviceId, vehicleType, month, year);
        ApiClient client = ServiceGenerator.createService(ApiClient.class);
        client.getMonthlyData("hgghghghjhgj", body)
                .enqueue(new Callback<List<MonthlyData>>() {
                    @Override
                    public void onResponse(Call<List<MonthlyData>> call, Response<List<MonthlyData>> response) {

                        mView.hideDialog();

                        if (response.isSuccessful()) {
                            List<MonthlyData> dataList = response.body();
                            mView.updateAdapter(dataList);
                        } else if (response.code() == 401) {
                            mView.showToast("Invalid Token");
                        } else if (response.code() == 402) {
                            mView.showToast("Missing Token");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<MonthlyData>> call, Throwable t) {
                        mView.hideDialog();
                        mView.showToast("Faied " + t.getMessage());
                    }
                });


    }

    @Override
    public void getMonthlyFile(Device device, int month, int year) {

        MonthlyFileRequest request = new MonthlyFileRequest(device, month, year);
        ApiClient client = ServiceGenerator.createService(ApiClient.class);
        client.getMonthlyPdf("hhghghghgh",request)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {

                            String filePath = mView.saveFile(response.body());

                            if (filePath != null) {
                                mView.showToast("File Saved Successfully");
                                mView.openFile(filePath);
                            } else {
                                mView.showToast("Failed to Save the File");
                            }

                        }else if (response.code() == 401) {
                            mView.showToast("Invalid Token");
                        } else if (response.code() == 402) {
                            mView.showToast("Missing Token");
                        }else{
                            Log.d("HHHHHH","Something Else"+"");
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                       // mView.showToast("Called ");
                        Log.d("HHHHHH",t.getMessage()+"");
                    }
                });


    }

    @Override
    public void updateMonthText() {
        mView.updateMonthText();
    }

    @Override
    public void updateFuelAndDistance() {
        mView.updateFuelAndDistance();
    }
}
