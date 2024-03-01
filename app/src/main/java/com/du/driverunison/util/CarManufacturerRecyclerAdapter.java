package com.du.driverunison.util;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.du.driverunison.R;
import com.du.driverunison.model.Manufacturer;

import java.util.ArrayList;

public class CarManufacturerRecyclerAdapter extends RecyclerView.Adapter<CarManufacturerRecyclerAdapter.ViewHolder> {
    private ArrayList<Manufacturer> manufacturers;
    private View.OnClickListener mClickListener;
    public void setClickListener(View.OnClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public CarManufacturerRecyclerAdapter(ArrayList<Manufacturer> manufacturers){
        this.manufacturers = manufacturers;
    }

    public void setManufacturers(ArrayList<Manufacturer> manufacturers){
        this.manufacturers = manufacturers;
        notifyDataSetChanged();
    }

    public Manufacturer getManufacturer(int pos) {
        return manufacturers.get(pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.car_manufacturer_item, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.embedData(manufacturers.get(position), position);
        viewHolder.manufacturerOptionContainer.setOnClickListener(mClickListener);
        viewHolder.manufacturerOptionContainer.setTag(viewHolder);
    }
    @Override
    public int getItemCount() {
        return manufacturers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements FetchImageTask.FetchImageTaskCallback{

        private final TextView tvManufacturerName;
        private final ImageView ivManufacturerLogo;
        private final CardView manufacturerOptionContainer;

        public ViewHolder(View view) {
            super(view);
            tvManufacturerName = view.findViewById(R.id.tv_car_manufacturer_name);
            ivManufacturerLogo = view.findViewById(R.id.iv_car_manufacturer_logo);
            manufacturerOptionContainer = view.findViewById(R.id.manufacturer_option_container);
        }

        @Override
        public void onResultReceived(Bitmap result) {
            if (ivManufacturerLogo != null)
                ivManufacturerLogo.setImageBitmap(result);
        }

        public void embedData(Manufacturer loadingManufacturer, int position){

            tvManufacturerName.setText(loadingManufacturer.getName());
            new FetchImageTask(this).execute(loadingManufacturer.getName());
//
//            switch (loadingChassisOption){
//                case "estate":
//                    ivChassisShape.setImageResource(R.mipmap.car_estate_shape);
//                    break;
//                case "hatchback":
//                    ivChassisShape.setImageResource(R.mipmap.car_hb_shape);
//                    break;
//                case "sedan":
//                    ivChassisShape.setImageResource(R.mipmap.car_sedan_shape);
//                    break;
//                case "coupe":
//                    ivChassisShape.setImageResource(R.mipmap.car_coupe_shape);
//                    break;
//                case "suv":
//                    ivChassisShape.setImageResource(R.mipmap.car_suv_shape);
//                    break;
//                case "mpv":
//                    ivChassisShape.setImageResource(R.mipmap.car_mpv_shape);
//                    break;
//                case "pickup":
//                    ivChassisShape.setImageResource(R.mipmap.car_pickup_shape);
//                    break;
//            }

//            tvRankOrderNum.setText(String.valueOf(position + 1));
//            tvUsername.setText(loadingPlayer.getUsername());
//            tvCurrentRank.setText( String.valueOf(loadingPlayer.getPointsCurrentRank()) );
//            tvStars.setText( String.valueOf(loadingPlayer.getStars()) );
////            Todo default profile pic ->  loaded img parse
//            imgPicture.setImageResource(R.mipmap.ic_battle_bird);
        }
    }
}
