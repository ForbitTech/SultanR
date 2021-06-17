package com.forbit.sultanr.ui.monthlyReport;

import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.models.MonthlyData;

import java.util.List;

import okhttp3.ResponseBody;

public interface MonthlyContract {

    interface Presenter{
        void getMonthlyData(String deviceId, int vehicleType, int month, int year);
        void getMonthlyFile(Device device, int month, int year);

        void updateMonthText();
        void updateFuelAndDistance();
    }

    interface View{
        void showDialog();
        void hideDialog();
        void updateMonthText();
        void updateFuelAndDistance();
        void showToast(String message);
        String saveFile(ResponseBody body);
        void openFile(String path);
        void updateAdapter(List<MonthlyData> dataList);
    }
}
