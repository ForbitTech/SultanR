package com.forbit.sultanr.models;

public class MonthlyRequestBody {

    private String device_id;
    private int device_type;
    private int year;
    private int month;

    public MonthlyRequestBody() {
    }

    public MonthlyRequestBody(String device_id, int device_type, int month, int year) {
        this.device_id = device_id;
        this.device_type = device_type;
        this.month = month;
        this.year = year;

    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
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

    public int getDevice_type() {
        return device_type;
    }

    public void setDevice_type(int device_type) {
        this.device_type = device_type;
    }
}
