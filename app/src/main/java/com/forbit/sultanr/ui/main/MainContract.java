package com.forbit.sultanr.ui.main;

import com.forbit.sultanr.models.Client;
import com.forbit.sultanr.models.Device;

public interface MainContract {

    public interface Presenter {
        void ButtonClick();
        void startLoginActivity();
        void getDevice(Client client);

    }

    public interface View {
        void startLoginActivity();
        void startMapActivity(Device device);
        void showProgressDialog();
        void hideProgressDialog();
        void showToast(String message);
    }
}
