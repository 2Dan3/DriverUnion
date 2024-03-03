package com.du.driverunison.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CarModelRecyclerAdapter extends RecyclerView.Adapter<CarModelRecyclerAdapter.ViewHolder> {
    private ArrayList<Model> models;
    private View.OnClickListener mClickListener;
    public void setClickListener(View.OnClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public CarModelRecyclerAdapter(ArrayList<Model> models){
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

    public class ViewHolder extends RecyclerView.ViewHolder implements FetchImageTask.FetchImageTaskCallback {
        private final TextView tvModelName;
        private final ImageView ivModelPhoto;
        private final CardView modelOptionContainer;

        public ViewHolder(View view) {
            super(view);
            tvModelName = view.findViewById(R.id.tv_car_model_name);
            ivModelPhoto = view.findViewById(R.id.iv_car_model);
            modelOptionContainer = view.findViewById(R.id.model_option_container);
        }

        @Override
        public void onResultReceived(Bitmap result) {
            if (result != null && ivModelPhoto != null)
                ivModelPhoto.setImageBitmap(result);
        }
        public void embedData(Model loadingModel, int position){
            tvModelName.setText(loadingModel.getName());
            loadModelImage(loadingModel);
        }
        public void loadModelImage(Model loadingModel){
//           TODO
//            new FetchImageTask(this).execute(loadingModel.getManufacturerName(), loadingModel.getName());
//              /
            StorageReference manufacturerRef = FirebaseStorage.getInstance("gs://driver-union-1753f.appspot.com").getReference();
            manufacturerRef.child(loadingModel.getManufacturerName()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    for (StorageReference prefix : listResult.getItems()) {
//                        Log.d("PREFIX:", prefix.toString());
                        if (prefix.toString().contains(String.format("%s_%s", loadingModel.getManufacturerName(), loadingModel.getName()))) {
                            prefix.getBytes(Long.MAX_VALUE).addOnSuccessListener(
                                    bytes -> {
                                        onResultReceived(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            });
                            break;
                        }
                    }
                }
            });
        }
    }
}
