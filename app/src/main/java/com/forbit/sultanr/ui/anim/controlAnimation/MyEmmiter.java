package com.forbit.sultanr.ui.anim.controlAnimation;

import android.util.Log;


import com.forbit.sultanr.models.Location;

import java.util.List;

public class MyEmmiter implements Runnable {

    private ControlAnimationActivity activity;
    private boolean isRunning;
    private List<Location> locationList;

    private Thread mThread;
    private int index;
    private int animationDuration = 3000;


    public MyEmmiter(ControlAnimationActivity activity, List<Location> locationList) {
        this.activity = activity;
        this.locationList = locationList;
    }

    @Override
    public void run() {

        while (isRunning){

            if(index<locationList.size()){
                Location location = locationList.get(index);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.updateLocation(location,animationDuration);
                    }
                });

                index++;

                try {
                    Thread.sleep(animationDuration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                Log.d("RASHIN","ELSE CALLED");
                this.index=0;
                pause();
            }

        }

    }

    public void resume(){
        if(mThread==null){
            mThread = new Thread(this);
            isRunning= true;
            mThread.start();
        }
    }

    public void pause(){
        isRunning=false;
        this.animationDuration=3000;

       /* while (true){
            try {

                mThread.join();
                Log.d("RASHIN","Thread JOIN");
                // After Join Break the Loop
                break;
            } catch (InterruptedException e) {
                Log.d("RASHIN","Thread EXCEPTION "+e.getMessage());
                e.printStackTrace();
            }
        }*/

        mThread=null;

        Log.d("RASHIN","Thread NULL");


    }

    public void faster2x(){
        this.animationDuration = this.animationDuration/2;
    }
}
