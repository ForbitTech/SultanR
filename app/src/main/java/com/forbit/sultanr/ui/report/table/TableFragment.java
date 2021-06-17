package com.forbit.sultanr.ui.report.table;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.Span;
import com.forbit.sultanr.ui.report.ReportActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment implements TableContract.View {

    private RecyclerView rvDistance;
    private DAdapter adapter;

    private TablePresenter mPresenter;


    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof ReportActivity){
            ReportActivity reportActivity = (ReportActivity) getActivity();
            int vType = reportActivity.getVehicleType();
            mPresenter = new TablePresenter(this,vType);
            adapter = new DAdapter(getContext(),vType);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvDistance = view.findViewById(R.id.rv_travel_distance);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvDistance.setLayoutManager(manager);
        rvDistance.addItemDecoration(new DividerItemDecoration(getContext(),manager.getOrientation()));
        rvDistance.setAdapter(adapter);
    }

    public void updateData(List<Location> locationList){
        mPresenter.processLocations(locationList);
    }


    @Override
    public void clearPreviousRecord() {
        adapter.clearPreviousRecord();
    }

    @Override
    public void updateSpan(Span span) {
        adapter.updateSpan(span);
    }
}
