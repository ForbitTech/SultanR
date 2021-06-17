package com.forbit.sultanr.ui.login;

import com.forbit.sultanr.models.Client;
import com.forbit.sultanr.models.Device;

public interface LoginContract {

    interface Presenter{
        boolean validate(String code, String password);
        void login(Client client);
    }

    interface View{
        void showError(int fieldId,String msg);
        void clearError();
        void showProgressDialog();
        void hideProgressDialog();
        void startMapActivity(Device device,Client client);
        void showToast(String message);
    }
}
