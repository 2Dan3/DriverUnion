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

import java.util.ArrayList;

public class ChassisOptionRecyclerAdapter extends RecyclerView.Adapter<ChassisOptionRecyclerAdapter.ViewHolder> {
    private ArrayList<String> chassisOptions;
    private View.OnClickListener mClickListener;
    public void setClickListener(View.OnClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public ChassisOptionRecyclerAdapter(ArrayList<String> chassisOptions){
        this.chassisOptions = chassisOptions;
    }

    public void setChassisOptions(ArrayList<String> chassisOptions){
        this.chassisOptions = chassisOptions;
        notifyDataSetChanged();
    }

    public String getChassisOption(int pos) {
        return chassisOptions.get(pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chassis_option_item, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.embedData(chassisOptions.get(position), position);
        viewHolder.chassisOptionContainer.setOnClickListener(mClickListener);
        viewHolder.chassisOptionContainer.setTag(viewHolder);
    }
    @Override
    public int getItemCount() {
        return chassisOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvChassisName;
        private final ImageView ivChassisShape;
        private final CardView chassisOptionContainer;

        public ViewHolder(View view) {
            super(view);
            tvChassisName = view.findViewById(R.id.tv_car_chassis_name);
            ivChassisShape = view.findViewById(R.id.iv_car_chassis_shape);
            chassisOptionContainer = view.findViewById(R.id.chassis_option_container);
        }

        public void embedData(String loadingChassisOption, int position){

            tvChassisName.setText(loadingChassisOption);
            switch (loadingChassisOption){
                case "estate":
                    ivChassisShape.setImageResource(R.mipmap.car_estate_shape);
                    break;
                case "hatchback":
                    ivChassisShape.setImageResource(R.mipmap.car_hb_shape);
                    break;
                case "sedan":
                    ivChassisShape.setImageResource(R.mipmap.car_sedan_shape);
                    break;
                case "coupe":
                    ivChassisShape.setImageResource(R.mipmap.car_coupe_shape);
                    break;
                case "suv":
                    ivChassisShape.setImageResource(R.mipmap.car_suv_shape);
                    break;
                case "mpv":
                    ivChassisShape.setImageResource(R.mipmap.car_mpv_shape);
                    break;
                case "pickup":
                    ivChassisShape.setImageResource(R.mipmap.car_pickup_shape);
                    break;
            }
        }
    }
}
