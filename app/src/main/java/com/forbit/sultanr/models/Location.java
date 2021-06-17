package com.forbit.sultanr.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Location implements Serializable {

    private String _id;
    private String id;
    private Geo geo;
    private GeoDate date;

    public Location() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public GeoDate getDate() {
        return date;
    }

    public void setDate(GeoDate date) {
        this.date = date;
    }

    public class GeoDate implements Serializable {
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;

        public GeoDate() {
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        @NonNull
        @Override
        public String toString() {
            return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
        }

        public Date getDateTime(){
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,day,hour,minute,second);
            return calendar.getTime();
        }
    }

}
