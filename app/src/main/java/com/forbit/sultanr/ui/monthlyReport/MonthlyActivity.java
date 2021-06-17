package com.forbit.sultanr.ui.monthlyReport;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forbit.sultanr.BuildConfig;
import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.models.MonthlyData;
import com.forbit.sultanr.utils.Constant;
import com.forbit.sultanr.utils.MyUtil;
import com.forbit.sultanr.utils.PrebaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MonthlyActivity extends PrebaseActivity implements MonthlyContract.View, View.OnClickListener {
    private static final int READ_WRITE_PERMISSION=3000;

    private MonthlyPresenter mPresenter;
    private Device device;
    private TextView tvMonth,tvFuel,tvDistance;
    private int currentMonth,currentYear;
    private MonthlyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        this.device = (Device) getIntent().getSerializableExtra(Constant.DEVICE);
        mPresenter = new MonthlyPresenter(this);

        this.currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        this.currentYear = Calendar.getInstance().get(Calendar.YEAR);
        adapter = new MonthlyAdapter(this,device);
        initView();
    }

    private void initView() {
        setupToolbar();
        TextView tvTitle = findViewById(R.id.title);
        TextView tvNext = findViewById(R.id.next);
        TextView tvPrev = findViewById(R.id.prev);
        tvTitle.setText("Monthly Report");

        tvNext.setOnClickListener(this);
        tvPrev.setOnClickListener(this);

        tvMonth = findViewById(R.id.month);
        tvFuel = findViewById(R.id.fuel);
        tvDistance = findViewById(R.id.distance);

        AppCompatImageView btnDownload = findViewById(R.id.download);
        btnDownload.setOnClickListener(this);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        mPresenter.getMonthlyData(device.getId(),device.getVehicle_type(),currentMonth,currentYear);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.next:
                increase();
                mPresenter.getMonthlyData(device.getId(),device.getVehicle_type(),currentMonth,currentYear);
                break;

            case R.id.prev:
                decrease();
                mPresenter.getMonthlyData(device.getId(),device.getVehicle_type(),currentMonth,currentYear);
                break;

            case R.id.download:
                requestFileAfterPermission();
                break;
        }

    }

    private void increase(){
        currentMonth++;
        if(currentMonth>11){
            currentYear++;
            currentMonth= currentMonth%12;
        }
    }

    private void decrease(){
        currentMonth--;
        if(currentMonth<0){
            currentYear--;
            currentMonth= currentMonth+12;
        }
    }


    @AfterPermissionGranted(READ_WRITE_PERMISSION)
    private void requestFileAfterPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getApplicationContext(), perms)) {
            mPresenter.getMonthlyFile(device,currentMonth,currentYear);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "App need to Permission for Location",
                    READ_WRITE_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void hideDialog() {
        hideProgressDialog();
    }

    @Override
    public void updateMonthText() {
        tvMonth.setText(MyUtil.getMonthYear(getStartDate()));
    }

    @Override
    public void updateFuelAndDistance() {
        tvFuel.setText(MyUtil.getTwoDecimalFormat(adapter.getTotalFuel()).concat(" Lit"));
        tvDistance.setText(MyUtil.getTwoDecimalFormat(adapter.getTotalDistance()).concat(" KM"));
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String saveFile(ResponseBody body) {
        String file = Environment.getExternalStorageDirectory().getPath()+ File.separator+getString(R.string.app_name);
        File dir = new File(file);

        if(!dir.exists()){
            dir.mkdirs();
        }

        String fileName = generateFileName();
        File myFile = new File(file, fileName);


        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            byte[] fileReader = new byte[4096];

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(myFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);

                fileSizeDownloaded += read;
            }

            outputStream.flush();
            return myFile.getPath();

        } catch (IOException e) {

            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void openFile(String path) {
        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Uri uri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID+".fileprovider",file);

        target.setDataAndType(uri,"application/pdf");

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            PackageManager pm = getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                startActivity(intent);
            }
            //startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            showToast("Please Download a PDF Reader");
        }
    }

    @Override
    public void updateAdapter(List<MonthlyData> dataList) {
        adapter.update(dataList);
        mPresenter.updateMonthText();
        mPresenter.updateFuelAndDistance();
    }

    private Date getStartDate(){
        Calendar cal = new GregorianCalendar(currentYear,currentMonth,1);
        return  cal.getTime();
    }

    private String getMonthText(){
        return getResources().getStringArray(R.array.month_array)[currentMonth];
    }

    private String generateFileName(){
        return getMonthText()+"-"+currentYear+".pdf";
    }
}