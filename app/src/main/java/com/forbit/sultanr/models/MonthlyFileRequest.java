package com.forbit.sultanr.models;

public class MonthlyFileRequest {
    private Device device;
    private int month;
    private int year;

    public MonthlyFileRequest(Device device, int month, int year) {
        this.device = device;
        this.month = month;
        this.year = year;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
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
}
