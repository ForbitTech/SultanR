package com.forbit.sultanr.api;

import com.forbit.sultanr.models.Client;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.MonthlyData;
import com.forbit.sultanr.models.MonthlyFileRequest;
import com.forbit.sultanr.models.MonthlyRequestBody;
import com.forbit.sultanr.models.RBody;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiClient {

    @POST("/api/api/devices/client")
    Call<Device> getDevice(@Body Client client);

    @POST("/api/api/locations/daily")
    Call<List<Location>> getDailyLocations(@Header("Authorization") String token, @Body RBody data);

    @POST("/api/api/locations/monthly")
    Call<List<MonthlyData>> getMonthlyData(@Header("Authorization") String token, @Body MonthlyRequestBody monthlyRequestBody);

    @POST("/api/api/locations/monthly/pdf")
    Call<ResponseBody> getMonthlyPdf (@Header("Authorization") String token,@Body MonthlyFileRequest monthlyFileRequest);

}
