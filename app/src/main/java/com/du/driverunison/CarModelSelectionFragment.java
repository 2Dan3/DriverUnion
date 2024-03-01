package com.du.driverunison;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.du.driverunison.model.Model;
import com.du.driverunison.util.CarModelRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarModelSelectionFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarModelRecyclerAdapter adapter;
    private ArrayList<Model> models;
    private static final String MANUFACTURER = "manufacturer_name";
    private String manufacturerName;

    public CarModelSelectionFragment() {}

    public static CarModelSelectionFragment newInstance(String manufacturerName) {
        CarModelSelectionFragment fragment = new CarModelSelectionFragment();

        Bundle args = new Bundle();
        args.putString(MANUFACTURER, manufacturerName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.models = new ArrayList<>();
        this.adapter = new CarModelRecyclerAdapter(models);

        if (getArguments() != null) {
            this.manufacturerName = getArguments().getString(MANUFACTURER);
        }

        getCarModels();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_model_selection, container, false);

        recyclerView = v.findViewById(R.id.recycler_car_model_selection);
//        adapter = new CarModelRecyclerAdapter(models);

        adapter.setClickListener(this::onItemClick);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return v;
    }

    private void onItemClick(View view) {
        CarModelRecyclerAdapter.ViewHolder viewHolder = (CarModelRecyclerAdapter.ViewHolder) view.getTag();
        int position = viewHolder.getAdapterPosition();
        String selectedModelName = adapter.getModel(position).getName();

        final ArrayList<String> chassisShapesAvailable = new ArrayList<>();

        DatabaseReference modelRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(manufacturerName).child(selectedModelName);
        modelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot carModelSnapshot) {
                for (DataSnapshot chassisData : carModelSnapshot.getChildren()) {
                    String chassisShape = chassisData.getKey();
//                    Toast.makeText(getActivity(), chassisShape, Toast.LENGTH_SHORT).show();
                    chassisShapesAvailable.add(chassisShape);
                }
                getActivity().runOnUiThread(() -> {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.car_view_container, ChassisSelectionFragment.newInstance(manufacturerName, carModelSnapshot, chassisShapesAvailable)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void getCarModels() {
//        models.add(new Model("M3", 0));
//        models.add(new Model("6", 0));
//        models.add(new Model("3", 0));
//        adapter.setModels(models);
        DatabaseReference manufacturerRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(manufacturerName);
        manufacturerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot carManufacturerSnapshot) {
//                Toast.makeText(getActivity(), "makerSnap: " + carManufacturerSnapshot, Toast.LENGTH_SHORT).show();

                for (DataSnapshot modelData : carManufacturerSnapshot.getChildren()) {
                    String modelName = modelData.getKey();
//                    Toast.makeText(getActivity(), "model: " + modelName, Toast.LENGTH_SHORT).show();
//                    todo fix temporary workaround for :  temporary default photo, until real car model photo is loaded
                    Model newModel = new Model(manufacturerName, modelName, 0);
                    models.add(newModel);
//                    Toast.makeText(getActivity(), "mod: " + newModel, Toast.LENGTH_SHORT).show();
                }
//                adapter.setClickListener(this::onItemClick);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                recyclerView.setAdapter(adapter);
                adapter.setModels(models);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}