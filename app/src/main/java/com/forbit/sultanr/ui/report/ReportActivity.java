package com.forbit.sultanr.ui.report;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;

import com.forbit.sultanr.R;
import com.forbit.sultanr.firebase.MyDatabaseRef;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RBody;
import com.forbit.sultanr.ui.anim.controlAnimation.ControlAnimationActivity;
import com.forbit.sultanr.ui.report.chart.ChartFragment;
import com.forbit.sultanr.ui.report.dialog.UpdateMilageListener;
import com.forbit.sultanr.ui.report.dialog.UpdateMileageFragment;
import com.forbit.sultanr.ui.report.status.StatusFragment;
import com.forbit.sultanr.ui.report.table.TableFragment;
import com.forbit.sultanr.utils.Constant;
import com.forbit.sultanr.utils.MyUtil;
import com.forbit.sultanr.utils.PrebaseActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportActivity extends PrebaseActivity implements View.OnClickListener,ReportContract.View {

    private ProgressDialog mProgressDialog;

    private Device device;
    private ReportPresenter mPresenter;
    private RBody requestBody;


    private Date selectedDate;

    private TextView tvDistance,tvTravelTime,tvFuel,tvStatus,tvNext,tvPrev;

    private double totalDistance;
    private CardView cAnim,cFuel;

    private List<Location> locationList;

    private ViewPager mViewPager;

    private ViewPagerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        device = (Device) getIntent().getSerializableExtra(Constant.DEVICE);
        selectedDate = new Date();
        mPresenter = new ReportPresenter(this,device);
        initView();

        requestBody = new RBody();
        requestBody.setDevice_time(MyUtil.getReqDate(selectedDate));
        requestBody.setId(device.getId());

        mPresenter.requestForData(requestBody);

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
    public void updateTitle() {
        getSupportActionBar().setTitle("Report On "+MyUtil.getStringDate(selectedDate));
    }


    @Override
    public void updateFuel(){
        if(device.getMileage()==0){
            tvFuel.setText("Update Mileage");
        }else{
            double fuel = totalDistance/ device.getMileage()/1000;
            tvFuel.setText(MyUtil.getTwoDecimalFormat(fuel)+" Lit");
        }
    }

    @Override
    public void updateUI(List<Location> locationList) {
        this.locationList = locationList;
        updateDistance(MyUtil.getDistance(locationList,device.getVehicle_type()));
        updateFuel();

        updateData(mViewPager.getCurrentItem());

        //adapter.getItem(mViewPager.getCurrentItem())
    }

    @Override
    public void updateDistance(double distance) {
        totalDistance = distance;
        tvDistance.setText(MyUtil.getTwoDecimalFormat(totalDistance/1000)+" km");
        //updateFragment();
    }

    private void updateData(int position){
        Fragment fragment = adapter.getItem(position);

        if(fragment instanceof TableFragment){
            TableFragment tf = (TableFragment) fragment;

            if(this.locationList != null){
                tf.updateData(this.locationList);
            }

        }else if(fragment instanceof ChartFragment){
            ChartFragment cf = (ChartFragment) fragment;

            if(this.locationList != null){
                cf.updateData(this.locationList);
            }
        }else if(fragment instanceof StatusFragment){
            StatusFragment sf = (StatusFragment) fragment;
            sf.updateData(locationList);
        }
    }


    @Override
    public void updateTravelTime(String travelTime) {
        if(travelTime==null){
            tvTravelTime.setText("0 hr 0 min");
        }else{
            tvTravelTime.setText(travelTime);
        }
    }

    public int getVehicleType(){
       return device.getVehicle_type();
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInfoDialog() {
        /*final InfoDialog infoDialog = InfoDialog.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CONTENT,"Unable to Show Report before a Week");
        infoDialog.setArguments(bundle);
        infoDialog.setListener(new DialogClickListener() {
            @Override
            public void positiveButtonClick() {
                infoDialog.dismiss();
            }
        });

        infoDialog.show(getSupportFragmentManager(),"Hello");*/
    }

    @Override
    public void startLoginActivity() {
       /* finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));*/
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this, R.style.MyTheme);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void initView() {
        cAnim = findViewById(R.id.anim);
        cFuel = findViewById(R.id.fuel);

        cAnim.setOnClickListener(this);
        cFuel.setOnClickListener(this);

        tvDistance = findViewById(R.id.tvDistance);
        tvTravelTime = findViewById(R.id.tv_running_time);
        tvFuel = findViewById(R.id.tv_fuel_consumption);
        tvStatus = findViewById(R.id.status);
        tvNext = findViewById(R.id.next);
        tvPrev = findViewById(R.id.prev);

        tvNext.setOnClickListener(this);
        tvPrev.setOnClickListener(this);

        mViewPager = findViewById(R.id.view_pager);
        setupViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateData(position);
                updateStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPresenter.initialize();
        mPresenter.updateTitle();
        mPresenter.updateFuel();

        updateStatus(mViewPager.getCurrentItem());

    }

    private void updateStatus(int position){
        switch (position){
            case 0:
                tvStatus.setText("Hourly Travel Distance");
                tvPrev.setVisibility(View.GONE);
                tvNext.setVisibility(View.VISIBLE);
                break;

            case 1:
                tvStatus.setText("Distance Chart");
                tvPrev.setVisibility(View.VISIBLE);
                tvNext.setVisibility(View.VISIBLE);
                break;

            case 2:
                tvStatus.setText("Off-On Status");
                tvPrev.setVisibility(View.VISIBLE);
                tvNext.setVisibility(View.GONE);
                break;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        if(adapter==null){
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
        }
        adapter.addFragment(new TableFragment(), "Table Fragment");
        adapter.addFragment(new ChartFragment(), "Chart Fragment");
        adapter.addFragment(new StatusFragment(), "Status Fragment");
        //adapter.addFragment(new TransactionSummeryFragment(), "Transaction Summery");
        viewPager.setAdapter(adapter);
    }




    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.anim:
                if(totalDistance>1000){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.SELECTED_DATE,  selectedDate);
                    bundle.putInt(Constant.V_TYPE,device.getVehicle_type());
                    bundle.putString(Constant.DEVICE_ID,device.getId());

                    Intent intent = new Intent(getApplicationContext(), ControlAnimationActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                break;

            case R.id.fuel:

                UpdateMileageFragment updateMileageFragment = new UpdateMileageFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble(Constant.MILEAGE, device.getMileage());
                updateMileageFragment.setArguments(bundle);
                updateMileageFragment.setListener(new UpdateMilageListener() {
                    @Override
                    public void update(final double value) {
                        Toast.makeText(ReportActivity.this, ""+value, Toast.LENGTH_SHORT).show();

                        MyDatabaseRef.getInstance()
                                .getDeviceRef().child(device.getId())
                                .child("mileage")
                                .setValue(value, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        device.setMileage(value);
                                        updateFuel();
                                    }
                                });
                    }
                });

                updateMileageFragment.show(getFragmentManager(),"UPDATE");

                break;

            case R.id.next:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1,true);
                break;
            case R.id.prev:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1,true);
                break;

        }



    }

    private void openCalender() {
        DatePickerBuilder builder = new DatePickerBuilder(this, calendar -> {
            selectedDate = calendar.get(0).getTime();
            mPresenter.updateTitle();
            requestBody.setDevice_time(MyUtil.getReqDate(selectedDate));
            mPresenter.requestForData(requestBody);
        }).pickerType(CalendarView.ONE_DAY_PICKER).date(Calendar.getInstance())
                ;

        DatePicker datePicker = builder.build();
        datePicker.show();
    }


    // Option Menu
    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.report_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.menu_calendar:
                openCalender();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
