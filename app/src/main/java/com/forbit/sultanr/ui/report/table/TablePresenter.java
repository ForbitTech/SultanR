package com.forbit.sultanr.ui.report.table;

import android.util.Log;


import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.Span;
import com.forbit.sultanr.utils.MyUtil;

import java.util.List;

import rx.Observable;

public class TablePresenter implements TableContract.Presenter {
    private TableContract.View mView;
    private int vehicleType;

    public TablePresenter(TableContract.View mView,int vehicleType) {
        this.mView = mView;
        this.vehicleType = vehicleType;
    }

    @Override
    public void processLocations(List<Location> locationList) {
        mView.clearPreviousRecord();
        Observable.from(locationList)
                .groupBy(location->location.getDate().getHour())
                .flatMap(Observable::toList)
                .subscribe(group->  process(group));
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

        mView.updateSpan(span);



        Log.d("YYYYY",firstTime);


    }
}
