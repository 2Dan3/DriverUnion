package com.du.driverunison.util;

import static com.du.driverunison.ChassisSelectionFragment.CABRIOLET;
import static com.du.driverunison.ChassisSelectionFragment.COUPE;
import static com.du.driverunison.ChassisSelectionFragment.ESTATE;
import static com.du.driverunison.ChassisSelectionFragment.FASTBACK;
import static com.du.driverunison.ChassisSelectionFragment.HATCHBACK;
import static com.du.driverunison.ChassisSelectionFragment.MPV;
import static com.du.driverunison.ChassisSelectionFragment.PICKUP;
import static com.du.driverunison.ChassisSelectionFragment.SEDAN;
import static com.du.driverunison.ChassisSelectionFragment.SUV;

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
                case ESTATE:
                    ivChassisShape.setImageResource(R.mipmap.car_estate_shape);
                    break;
                case HATCHBACK:
                    ivChassisShape.setImageResource(R.mipmap.car_hb_shape);
                    break;
                case SEDAN:
                    ivChassisShape.setImageResource(R.mipmap.car_sedan_shape);
                    break;
                case COUPE:
                    ivChassisShape.setImageResource(R.mipmap.car_coupe_shape);
                    break;
                case SUV:
                    ivChassisShape.setImageResource(R.mipmap.car_suv_shape);
                    break;
                case MPV:
                    ivChassisShape.setImageResource(R.mipmap.car_mpv_shape);
                    break;
                case PICKUP:
                    ivChassisShape.setImageResource(R.mipmap.car_pickup_shape);
                    break;
                case FASTBACK:
                    ivChassisShape.setImageResource(R.mipmap.car_fb_shape);
                    break;
                case CABRIOLET:
                    ivChassisShape.setImageResource(R.mipmap.car_cabrio_shape);
                    break;
            }
        }
    }
}
