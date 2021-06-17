package com.forbit.sultanr.models;

public class RBody {

    private String id;
    private String device_time;

    public RBody() {
    }

    public RBody(String id, String device_time) {
        this.id = id;
        this.device_time = device_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_time() {
        return device_time;
    }

    public void setDevice_time(String device_time) {
        this.device_time = device_time;
    }
}
