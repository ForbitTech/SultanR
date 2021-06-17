package com.forbit.sultanr.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sohel on 07-02-18.
 */

public class MyDatabaseRef {

    private static final String DEVICE_REF="devices";
    private static final String DATA_REF="data";


    private static MyDatabaseRef instance;



    private FirebaseDatabase database;

    private MyDatabaseRef() {
        this.database  = FirebaseDatabase.getInstance();
    }

    public static MyDatabaseRef getInstance() {
        if (instance == null) {
            instance = new MyDatabaseRef();
        }
        return instance;
    }


    public DatabaseReference getDeviceRef(){
        return database.getReference(DEVICE_REF);
    }
    public DatabaseReference getDataRef(String ime){
        return database.getReference(DATA_REF).child(ime);
    }


    public DatabaseReference getRootDataRef(){
        return database.getReference(DATA_REF);
    }

}
