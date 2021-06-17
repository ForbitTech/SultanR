package com.forbit.sultanr.ui.monthlyReport;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Device;
import com.forbit.sultanr.models.MonthlyData;
import com.forbit.sultanr.utils.MyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthlyAdapter extends RecyclerView.Adapter<MonthlyAdapter.MonthlyHolder> {

    private Context context;
    private List<MonthlyData> monthlyDataList;
    private LayoutInflater inflater;
    private Device device;

    private int hideContainerHeight;

    public MonthlyAdapter(Context context, Device device) {
        this.context = context;
        this.device = device;
        this.monthlyDataList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MonthlyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_monthly_data,parent,false);
        return new MonthlyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyHolder holder, int position) {
        MonthlyData data = monthlyDataList.get(position);
        holder.bind(data);
    }

    public void addData(MonthlyData data){
        monthlyDataList.add(data);
        int position = monthlyDataList.indexOf(data);
        notifyItemInserted(position);

    }

    public void clear(){
        monthlyDataList.clear();
        notifyDataSetChanged();
    }

    private double fuelRequired(MonthlyData data){
        double travelDiatance = data.getDistance()/1000;
        double runningConsumption = travelDiatance/device.getMileage();

        if(device.getCongestion_consumption()!=0){
            double x = Double.parseDouble(String.valueOf(data.getCongestion_time()))/3600;
            double congesCon = x * device.getCongestion_consumption();
            return runningConsumption+congesCon;
        }

        return runningConsumption;
    }

    public double getTotalDistance(){
        double retDist = 0;
        for (MonthlyData x: monthlyDataList){
            retDist = retDist + x.getDistance();
        }

        return (retDist)/1000;
    }

    public double getTotalFuel(){
        double fuel = 0;

        for (MonthlyData x: monthlyDataList){
            fuel = fuel + fuelRequired(x);
        }

        return fuel;


    }

    public void setHideContainerHeight(int height){
        if(height!=0 && hideContainerHeight==0){
            hideContainerHeight = height;
        }
    }

    public void update(List<MonthlyData> monthlyData){
        clear();
        this.monthlyDataList = monthlyData;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return monthlyDataList.size();
    }

    class MonthlyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDate,tvDistance,tvStartTime,tvStopTime,tvIdleTime,tvRunningTime,tvCongestionTime,tvFuel;
        LinearLayout mHideLayout;
        LinearLayout.LayoutParams params;
        ImageView ivArrow;

        private CardView mCardView;
        private boolean isExpand;


        public MonthlyHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.date);
            tvDistance = itemView.findViewById(R.id.distance);
            tvStartTime = itemView.findViewById(R.id.start_time);
            tvStopTime = itemView.findViewById(R.id.stop_time);
            tvIdleTime = itemView.findViewById(R.id.idle_time);
            tvRunningTime = itemView.findViewById(R.id.running_time);
            tvCongestionTime = itemView.findViewById(R.id.congestion_time);
            ivArrow = itemView.findViewById(R.id.arrow);
            mHideLayout = itemView.findViewById(R.id.hide_container);
            tvFuel = itemView.findViewById(R.id.fuel);
            mCardView = itemView.findViewById(R.id.card);

            mCardView.setOnClickListener(this);


            params = (LinearLayout.LayoutParams) mHideLayout.getLayoutParams();

            if(hideContainerHeight<=0){
                ViewTreeObserver observer = mHideLayout.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHideLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        setHideContainerHeight(mHideLayout.getMeasuredHeight());
                        collapse();
                    }
                });
            }

        }


        public void bind(MonthlyData data){

            if(hideContainerHeight>0){
                collapse();
                isExpand= false;
                ivArrow.setRotation(0);
            }

            tvDate.setText(String.format("%02d", data.get_id().getDay()));
            double km = data.getDistance()/1000;
            tvDistance.setText(MyUtil.getTwoDecimalFormat(km)+" KM");
            tvFuel.setText(MyUtil.getTwoDecimalFormat(fuelRequired(data))+ " Litre");

            if(data.getStart_time()!=null){
                tvStartTime.setText(getTime(data.getStart_time()));
            }
            if(data.getEnd_time()!=null){
                tvStopTime.setText(getTime(data.getEnd_time()));
            }

            tvIdleTime.setText(getStringTime(data.getIdle_time()));
            tvRunningTime.setText(getStringTime(data.getRunning_time()));
            tvCongestionTime.setText(getStringTime(data.getCongestion_time()));

        }

        private void animate(){

            ValueAnimator animator = ValueAnimator.ofFloat(1,hideContainerHeight);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value;
                    int rotation;

                    if(isExpand){
                        value = (int) (hideContainerHeight*(1-valueAnimator.getAnimatedFraction()));
                        rotation = (int) ((1-valueAnimator.getAnimatedFraction())*180);
                    }else {
                        value = (int) (hideContainerHeight*valueAnimator.getAnimatedFraction());
                        rotation = (int) (180*valueAnimator.getAnimatedFraction());
                    }

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,value);
                    mHideLayout.setLayoutParams(params);
                    ivArrow.setRotation(rotation);
                    mHideLayout.requestLayout();

                    if(valueAnimator.getAnimatedFraction()==1){
                        isExpand = !isExpand;
                    }
                }
            });

            animator.start();
        }


        private String getTime(String dateStr){
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            SimpleDateFormat output = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            Date d = null;
            String retStr = null;

            try {
                d= input.parse(dateStr);
                d.setTime(d.getTime()+6*3600*1000);
                retStr = output.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return retStr;
        }

        private String getStringTime(int time){
            int hour=0;
            int min=0;
            int sec=0;

            if(time>3600){
                hour = (int) time/3600;
            }

            int remainingTime = time%3600;

            if(remainingTime>60){
                min = remainingTime/60;
            }
            sec = remainingTime%60;

            if(time>3600){
                return hour+" hrs "+min+" min";
            }

            return min+" min "+sec+" sec";
        }

        private void collapse(){
            params = (LinearLayout.LayoutParams) mHideLayout.getLayoutParams();
            params.height = 0;
            mHideLayout.setLayoutParams(params);
            mHideLayout.requestLayout();
        }

        @Override
        public void onClick(View view) {
            animate();
        }
    }
}
