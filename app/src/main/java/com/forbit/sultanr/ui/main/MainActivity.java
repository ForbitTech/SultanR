package com.forbit.sultanr.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Client;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.ui.login.LoginActivity;
import com.forbit.sultanr.ui.login.LoginContract;
import com.forbit.sultanr.ui.map.MapActivity;
import com.forbit.sultanr.utils.AppPreference;
import com.forbit.sultanr.utils.Constant;
import com.forbit.sultanr.utils.PrebaseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends PrebaseActivity implements MainContract.View {

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!AppPreference.getInstance(getApplicationContext()).isLogin()){
            mPresenter.startLoginActivity();
        }else{
            mPresenter.getDevice(AppPreference.getInstance(getApplicationContext()).getClient());
        }
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void startMapActivity(Device device) {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra(Constant.DEVICE, device);
        startActivity(intent);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
