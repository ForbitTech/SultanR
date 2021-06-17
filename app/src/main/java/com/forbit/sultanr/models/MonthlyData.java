package com.forbit.sultanr.models;

public class MonthlyData {

    private ID _id;
    private double distance;
    private int idle_time;
    private int congestion_time;
    private int running_time;
    private int total_time;
    private String start_time;
    private String end_time;


    public MonthlyData() {
    }

    public ID get_id() {
        return _id;
    }

    public void set_id(ID _id) {
        this._id = _id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getIdle_time() {
        return idle_time;
    }

    public void setIdle_time(int idle_time) {
        this.idle_time = idle_time;
    }

    public int getCongestion_time() {
        return congestion_time;
    }

    public void setCongestion_time(int congestion_time) {
        this.congestion_time = congestion_time;
    }

    public int getRunning_time() {
        return running_time;
    }

    public void setRunning_time(int running_time) {
        this.running_time = running_time;
    }

    public int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public class ID{
        private int day;
        private int month;
        private int year;



        public ID() {
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getDate(){
            return String.format("%02d",this.day) +"-"+ String.format("%02d",this.month)+"-"+this.year;
        }
    }
}
