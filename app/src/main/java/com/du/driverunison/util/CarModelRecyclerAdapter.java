package com.du.driverunison.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.du.driverunison.R;
import com.du.driverunison.model.Model;

import java.util.ArrayList;

public class CarModelRecyclerAdapter extends RecyclerView.Adapter<CarModelRecyclerAdapter.ViewHolder> {
    private ArrayList<Model> models;
    private View.OnClickListener mClickListener;
    public void setClickListener(View.OnClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public CarModelRecyclerAdapter(ArrayList<Model> models){
//        models.add(new Model("6", 0));
//        models.add(new Model("3", 0));
        this.models = models;
    }

    public void setModels(ArrayList<Model> models){
        this.models = models;
        notifyDataSetChanged();
    }

    public Model getModel(int pos) {
        return models.get(pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.car_model_item, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.embedData(models.get(position), position);
        viewHolder.modelOptionContainer.setOnClickListener(mClickListener);
        viewHolder.modelOptionContainer.setTag(viewHolder);
    }
    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvModelName;
        private final ImageView ivModelPhoto;
        private final CardView modelOptionContainer;

        public ViewHolder(View view) {
            super(view);
            tvModelName = view.findViewById(R.id.tv_car_model_name);
            ivModelPhoto = view.findViewById(R.id.iv_car_model);
            modelOptionContainer = view.findViewById(R.id.model_option_container);
        }

        public void embedData(Model loadingModel, int position){
            tvModelName.setText(loadingModel.getName());
            ivModelPhoto.setImageResource(loadingModel.getPhoto());
        }
    }
}
