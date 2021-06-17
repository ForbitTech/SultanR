package com.forbit.sultanr.models;

import java.io.Serializable;

public class Client implements Serializable {

    private String code;
    private String password;

    public Client() {
    }

    public Client(String code,String password){
        this.code = code;
        this.password= password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
