package com.forbit.sultanr.ui.report.chart;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.Span;
import com.forbit.sultanr.ui.report.ReportActivity;
import com.forbit.sultanr.utils.MyUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment implements ChartContract.View {

    private BarChart barChart;
    private ChartPresenter mPresenter;

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Span> spanList = MyUtil.getSpanList(getContext());

        if(getActivity() instanceof ReportActivity){
            ReportActivity reportActivity = (ReportActivity) getActivity();
            int vType = reportActivity.getVehicleType();
            mPresenter = new ChartPresenter(this,spanList,vType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        barChart = view.findViewById(R.id.bar_chart);
        //createChart(spanList);
    }

    public void updateData(List<Location> locationList){
        Log.d("JJJJJJJJ",locationList.size()+"");
        mPresenter.processLocations(locationList);
    }

    @Override
    public void updateChart(List<Span> spanList) {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(true);

        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(false);

        //List<String> xAxisLabels = new ArrayList<>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (Span x: spanList) {
            yVals1.add(new BarEntry(x.getSpanNo(), (float) x.getDistance()));
            // xAxisLabels.add(String.valueOf(x.getSpanNo()));
        }

        //IAxisValueFormatter xAxisFormatter = new DateAxisFormatter(xAxisLabels);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        //xAxis.setLabelCount(7);
        xAxis.setSpaceMax(5f);
        xAxis.setSpaceMin(2f);
        //xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = barChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        // Disable Right Axis
        barChart.getAxisRight().setEnabled(false);



        BarDataSet dataSet = new BarDataSet(yVals1,"Hourly Travel Distance");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setDrawValues(false);
        dataSet.setBarBorderWidth(1f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        //data.setValueTypeface(mTfLight);
        data.setBarWidth(.9f);
        barChart.setData(data);
        barChart.animateXY(1000,1000);
        barChart.invalidate();
    }
}
