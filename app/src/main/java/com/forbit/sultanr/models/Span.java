package com.forbit.sultanr.models;




import com.forbit.sultanr.utils.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IMATPC-12 on 20-Feb-18.
 */

public class Span {
    private int spanNo;
    private double distance;
    private String time;
    private List<Location> locationList;


    public Span(int spanNo) {
        this.spanNo=spanNo;
    }

    public int getSpanNo() {
        return spanNo;
    }

    public void setSpanNo(int spanNo) {
        this.spanNo = spanNo;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
        this.distance = MyUtil.getDistance(locationList);
    }

    public void setLocationList(List<Location> locationList, int vehicleType){

        if(vehicleType<5){
            if(this.locationList==null){
                this.locationList = new ArrayList<>();
            }
            for (Location x: locationList){
                if(x.getGeo().getAcc().equals("ON")){
                    this.locationList.add(x);
                }
            }

        }else{
            this.locationList = locationList;
        }

        this.distance = MyUtil.getDistance(this.locationList);
    }
}
