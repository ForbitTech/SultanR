package com.forbit.sultanr.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Client;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.ui.main.MainContract;
import com.forbit.sultanr.ui.main.MainPresenter;
import com.forbit.sultanr.ui.map.MapActivity;
import com.forbit.sultanr.utils.AppPreference;
import com.forbit.sultanr.utils.Constant;
import com.forbit.sultanr.utils.PrebaseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends PrebaseActivity implements View.OnClickListener, LoginContract.View {

    private LoginPresenter lPresenter;
    private MaterialButton lbutton;
    private TextInputLayout ticode, tiPassword;
    private EditText etCode, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        lPresenter = new LoginPresenter(this);
        initView();
    }

    private void initView() {
        ticode = findViewById(R.id.ti_code);
        tiPassword = findViewById(R.id.ti_password);
        etCode = findViewById(R.id.et_code);
        etPassword = findViewById(R.id.et_password);
        lbutton = findViewById(R.id.btnlogin);
        lbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String code = etCode.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean valid = lPresenter.validate(code, password);

        if (!valid) {
            return;
        }

        Client client = new Client();
        client.setCode(code);
        client.setPassword(password);
        lPresenter.login(client);
    }

    @Override
    public void showError(int fieldId, String msg) {
        switch (fieldId) {
            case 1:
                ticode.setError(msg);
                etCode.requestFocus();
                break;

            case 2:
                tiPassword.setError(msg);
                etPassword.requestFocus();
                break;
        }
    }

    @Override
    public void clearError() {
        ticode.setErrorEnabled(false);
        tiPassword.setErrorEnabled(false);
    }

    @Override
    public void startMapActivity(Device device,Client client) {
        AppPreference.getInstance(getApplicationContext()).setClient(client);
        AppPreference.getInstance(getApplicationContext()).setLogin(true);
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra(Constant.DEVICE, device);
        startActivity(intent);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        finishAffinity();
    }
}