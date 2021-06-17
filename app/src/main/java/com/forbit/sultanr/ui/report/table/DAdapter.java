package com.forbit.sultanr.ui.report.table;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Span;
import com.forbit.sultanr.ui.anim.hourly.HourlyAnimActivy;
import com.forbit.sultanr.utils.Constant;
import com.forbit.sultanr.utils.MyUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IMATPC-12 on 24-Apr-18.
 */

public class DAdapter extends RecyclerView.Adapter<DAdapter.DistanceHolder> {
    private Context context;
    private LayoutInflater inflater;

    //private FrequencyDistribution frequencyDistribution;

    private List<Span> spanList;

    private int vType;



    public DAdapter(Context context, int vType) {
        this.vType= vType;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        spanList = MyUtil.getSpanList(context);
        //currentSpanNumber = MyUtil.getCurrentSpanNumber();
    }

    public void clear(){
        this.spanList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addSpan(Span span){
        spanList.add(span);
        int pos = spanList.indexOf(span);
        notifyItemInserted(pos);

    }

    public void updateSpan(Span span){
        spanList.set(span.getSpanNo(),span);
        notifyItemChanged(span.getSpanNo());
    }

    public void clearPreviousRecord(){
        for (Span x:spanList){
            x.setLocationList(new ArrayList<>());
            spanList.set(x.getSpanNo(),x);
        }

        notifyDataSetChanged();
    }


    @Override
    public DistanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_distance,parent,false);

        return new DistanceHolder(view);
    }

    @Override
    public void onBindViewHolder(DistanceHolder holder, int position) {

        Span span = spanList.get(position);

        int hour = span.getSpanNo()+1;

        String suffix="";

        if(hour==1 || hour==21){
            suffix="st";
        }else if(hour==2){
            suffix="nd";
        }else if(hour==3){
            suffix="rd";
        }else{
            suffix="th";
        }

        holder.tvHour.setText(hour+suffix+" hour");

        //double distance = span.getFrequency()*totalDistance/totalFraquency/1000;
        double distance = span.getDistance()/1000;



       String[] times = span.getTime().split(" to ");

       holder.tvStartTime.setText(times[0]);
       holder.tvEndTime.setText(times[1]);


       holder.tvDistance.setText(MyUtil.getTwoDecimalFormat(distance).concat(" KM"));




    }

    @Override
    public int getItemCount() {
        return spanList.size();
    }

    class DistanceHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvHour,tvDistance,tvStartTime,tvEndTime;

        public DistanceHolder(View itemView) {
            super(itemView);

            tvHour = itemView.findViewById(R.id.hour);
            tvDistance = itemView.findViewById(R.id.distance);
            tvStartTime = itemView.findViewById(R.id.start_time);
            tvEndTime = itemView.findViewById(R.id.end_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Span span = spanList.get(getAdapterPosition());

            if(span.getDistance()>=1000){
                Intent intent = new Intent(context, HourlyAnimActivy.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.LOCATION_LIST, (Serializable) span.getLocationList());
                bundle.putInt(Constant.V_TYPE,vType);
                intent.putExtras(bundle);

                context.startActivity(intent);
            }




        }
    }
}
