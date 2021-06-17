package com.forbit.sultanr.ui.report.chart;

import android.util.Log;


import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.Span;
import com.forbit.sultanr.utils.MyUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class ChartPresenter implements ChartContract.Presenter {

    private ChartContract.View mView;
    private List<Span> spanList;
    private int vehicleType;

    public ChartPresenter(ChartContract.View mView, List<Span> spanList, int vehicleType) {
        this.mView = mView;
        this.spanList = spanList;
        this.vehicleType = vehicleType;
    }

    @Override
    public void processLocations(List<Location> locationList) {
        clearSpan();
        Observable.from(locationList)
                .groupBy(location->location.getDate().getHour())
                .flatMap(Observable::toList)
                .subscribe(group->  process(group),err-> Log.d("EEE",err.toString()),()->complete());
    }

    private void clearSpan() {
        for (Span x: spanList){
            x.setLocationList(new ArrayList<>());
            spanList.set(x.getSpanNo(),x);
        }
    }

    private void process(List<Location> locationList){

        Location first = locationList.get(0);
        Location last = locationList.get(locationList.size()-1);

        String firstTime = MyUtil.getTime(first.getDate().getDateTime());
        String lastTime = MyUtil.getTime(last.getDate().getDateTime());

        String time = firstTime+" to "+lastTime;

        Span span = new Span(first.getDate().getHour());
        span.setTime(time);
        span.setLocationList(locationList,vehicleType);

        spanList.set(span.getSpanNo(),span);


        //mView.updateSpan(span);

    }

    private void complete(){
        mView.updateChart(spanList);
    }
}
