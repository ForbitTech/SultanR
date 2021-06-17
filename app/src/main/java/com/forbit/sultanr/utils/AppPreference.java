package com.forbit.sultanr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.forbit.sultanr.models.Client;

public class AppPreference {

    private static final String SP_NAME="SSSPPP";
    private static final String CODE="CODE";
    private static final String PASSWORD="PASSWORD";
    private static final String LOGIN="LOGIN";

    private static AppPreference appPreference = null;
    SharedPreferences sp;

    private AppPreference(Context context) {
        sp = context.getSharedPreferences(SP_NAME,context.MODE_PRIVATE);
    }

    public static AppPreference getInstance(Context context){
        if(appPreference==null){
            appPreference = new AppPreference(context);
        }
        return appPreference;
    }


    public boolean isLogin(){
        boolean login = sp.getBoolean(LOGIN,false);
        return login;
    }

    public void setLogin(boolean value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(LOGIN,value);
        editor.apply();
    }
    public Client getClient(){
        String code = sp.getString(CODE,null);
        String pass = sp.getString(PASSWORD,null);
        return new Client(code,pass);
    }


    public void setClient(Client client){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CODE,client.getCode());
        editor.putString(PASSWORD,client.getPassword());
        editor.apply();
    }

    public void clear(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }

}
