package com.forbit.sultanr.ui.report.status;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.VehicleStatus;
import com.forbit.sultanr.utils.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sohel on 6/7/2018.
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.Statusholder> {
    private StatusFragment statusFragment;
    private List<VehicleStatus> vehicleStatusList;
    private LayoutInflater inflater;
    private int vehicleType;


    public StatusAdapter(StatusFragment statusFragment,int vehicleType) {
        this.statusFragment = statusFragment;
        this.vehicleType = vehicleType;
        this.vehicleStatusList = new ArrayList<>();
        this.inflater = LayoutInflater.from(statusFragment.getContext());
    }

    @Override
    public Statusholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_status,parent,false);
        return new Statusholder(view);
    }

    @Override
    public void onBindViewHolder(Statusholder holder, int position) {
         VehicleStatus vehicleStatus = vehicleStatusList.get(position);
         holder.bind(vehicleStatus);
    }

    public void clear(){
        this.vehicleStatusList = new ArrayList<>();

    }

    public void updateStatusList(List<VehicleStatus> vehicleStatusList){
        this.vehicleStatusList = vehicleStatusList;
        notifyDataSetChanged();
    }

    public void addVehicleStatus(VehicleStatus vehicleStatus){
        vehicleStatusList.add(vehicleStatus);
        int pos = vehicleStatusList.indexOf(vehicleStatus);
        notifyItemInserted(pos);
    }

    @Override
    public int getItemCount() {
        return vehicleStatusList.size();
    }

    class Statusholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvStartTime,tvEndTime,tvDuration,tvStatus,tvDistance;

        public Statusholder(View itemView) {
            super(itemView);

            tvStartTime = itemView.findViewById(R.id.start_time);
            tvEndTime = itemView.findViewById(R.id.end_time);
            tvDuration = itemView.findViewById(R.id.duration);
            tvDistance = itemView.findViewById(R.id.distance);
            tvStatus = itemView.findViewById(R.id.acc);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           statusFragment.startTripAnimActivity(vehicleStatusList.get(getAdapterPosition()).getLocationList());
        }

        public void bind(VehicleStatus vehicleStatus){
            if(vehicleStatus.getLocationList().size()>1){
                Location.GeoDate firstDate= vehicleStatus.getLocationList().get(0).getDate();
                Location.GeoDate lastDate = vehicleStatus.getLocationList().get(vehicleStatus.getLocationList().size()-1).getDate();

                String stTime = MyUtil.getTime(firstDate.getDateTime());
                String endTime = MyUtil.getTime(lastDate.getDateTime());

                tvStartTime.setText(stTime);
                tvEndTime.setText(endTime);

                tvStatus.setText(vehicleStatus.getStatus());

                int duration = (lastDate.getHour()*3600
                        +lastDate.getMinute()*60
                        +lastDate.getSecond())
                        -(firstDate.getHour()*3600+firstDate.getMinute()*60+firstDate.getSecond());

                if(duration>0){
                    String durationStr = getDuration(duration);
                    tvDuration.setText(durationStr);
                }
                double distance;
                if(vehicleType<5 && vehicleStatus.getStatus().equals("OFF")){
                    distance =0;
                }else {
                    distance = MyUtil.getDistance(vehicleStatus.getLocationList())/1000;
                }

//                double distance = MyUtil.getDistance(vehicleStatus.getLocationList())/1000;
                tvDistance.setText(MyUtil.getTwoDecimalFormat(distance).concat(" Km"));

            }else{
                String stTime = MyUtil.getTime(vehicleStatus.getLocationList().get(0).getDate().getDateTime());
                String endTime = MyUtil.getTime(vehicleStatus.getLocationList().get(0).getDate().getDateTime());
                tvStartTime.setText(stTime);
                tvEndTime.setText(endTime);
                tvStatus.setText(vehicleStatus.getStatus());
                tvDuration.setText("0 min 0 sec");
                tvDistance.setText("0.00 Km");
            }
        }
    }


    private String getDuration(int duration){
        String val ="";
        if(duration>=60*60){
            int hour = duration/(60*60);
            int min = duration%(60*60)/60;
            val = hour+" hr "+min+" min";

        }else if(duration<3600 && duration>=60){
            int min = duration/60;
            int sec = (int) (duration-min*60);
            val = min+" min "+sec+" sec";
        }else {
            val ="0 min "+ duration+" sec";
        }
        return val;
    }
}
