package com.forbit.sultanr.ui.report.status;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.VehicleStatus;
import com.forbit.sultanr.ui.anim.trip.TripAnimActivity;
import com.forbit.sultanr.ui.report.ReportActivity;
import com.forbit.sultanr.utils.Constant;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment implements StatusContract.View {

    private StatusPresenter mPresenter;
    private StatusAdapter adapter;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new StatusPresenter(this);

        if(getActivity() instanceof ReportActivity){
            ReportActivity reportActivity = (ReportActivity) getActivity();
            int vType = reportActivity.getVehicleType();
            adapter = new StatusAdapter(this,vType);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView rvStatus = view.findViewById(R.id.rv_status);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvStatus.setLayoutManager(manager);
        rvStatus.addItemDecoration(new DividerItemDecoration(getContext(),manager.getOrientation()));
        rvStatus.setAdapter(adapter);
    }

    public void updateData(List<Location> locationList){
        if(locationList !=null){
            mPresenter.processLocations(locationList);
        }

    }


    public void startTripAnimActivity(List<Location> locationList){
        if(locationList.size()>1){
            Intent intent = new Intent(getContext(), TripAnimActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.LOCATION_LIST, (Serializable) locationList);
            bundle.putInt(Constant.V_TYPE,getVehicleType());
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Toast.makeText(getContext(), "No Data Found to Animate Car", Toast.LENGTH_SHORT).show();
        }
    }

    private int getVehicleType(){
        int veTY = 1;
        if(getActivity() instanceof ReportActivity){
            ReportActivity ra = (ReportActivity) getActivity();
            veTY =ra.getVehicleType();
        }
        return veTY;
    }


    @Override
    public void updateStatus(List<VehicleStatus> vehicleStatusList) {
        adapter.updateStatusList(vehicleStatusList);
    }
}
