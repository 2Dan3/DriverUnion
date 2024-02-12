package com.du.driverunison.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.du.driverunison.R;
import com.du.driverunison.model.Model;
import com.du.driverunison.model.Motorization;

import java.util.ArrayList;

public class CarSpecMotorizationRecyclerAdapter extends RecyclerView.Adapter<CarSpecMotorizationRecyclerAdapter.ViewHolder> {
    private ArrayList<Motorization> motorizations;
    private View.OnClickListener mClickListener;
    public void setClickListener(View.OnClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public CarSpecMotorizationRecyclerAdapter(ArrayList<Motorization> motorizations){
//        motorizations.add(new Motorization());
//        motorizations.add(new Motorization());
        this.motorizations = motorizations;
    }
    public void setMotorizations(ArrayList<Motorization> motorizations){
        this.motorizations = motorizations;
        notifyDataSetChanged();
    }
    public Motorization getMotorization(int pos) {
        return motorizations.get(pos);
    }

    @NonNull
    @Override
    public CarSpecMotorizationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_motor_spec_item, parent, false);
        return new CarSpecMotorizationRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarSpecMotorizationRecyclerAdapter.ViewHolder viewHolder, int position) {
        viewHolder.embedData(motorizations.get(position), position);
        viewHolder.motorSpecOptionContainer.setOnClickListener(mClickListener);
        viewHolder.motorSpecOptionContainer.setTag(viewHolder);
    }

    @Override
    public int getItemCount() {
        return motorizations.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvMotorSpecName;
        private final ImageView ivMotorSpec;
        private final CardView motorSpecOptionContainer;

        public ViewHolder(View view) {
            super(view);
            tvMotorSpecName = view.findViewById(R.id.tv_car_motor_spec_name);
            ivMotorSpec = view.findViewById(R.id.iv_car_motor_spec);
            motorSpecOptionContainer = view.findViewById(R.id.motor_spec_option_container);
        }

        public void embedData(Motorization loadingMotorization, int position){
            tvMotorSpecName.setText(loadingMotorization.getName());
            ivMotorSpec.setImageResource(loadingMotorization.getPhoto());
        }
    }
}
