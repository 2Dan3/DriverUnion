package com.du.driverunison;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.du.driverunison.model.Manufacturer;
import com.du.driverunison.util.CarManufacturerRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarManufacturerSelectionFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarManufacturerRecyclerAdapter adapter;
    private ArrayList<Manufacturer> manufacturers;

    public CarManufacturerSelectionFragment() { }

    public static CarManufacturerSelectionFragment newInstance() {
        CarManufacturerSelectionFragment fragment = new CarManufacturerSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.manufacturers = new ArrayList<>();
        getCarManufacturers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_manufacturer_selection, container, false);

        recyclerView = v.findViewById(R.id.recycler_car_manufacturer_selection);
        adapter = new CarManufacturerRecyclerAdapter(manufacturers);
        adapter.setClickListener(this::onItemClick);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return v;
    }

    private void getCarManufacturers() {

        DatabaseReference manufacturersRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("brands");
        manufacturersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> manufacturersNames = (ArrayList<String>) snapshot.getValue();
                ArrayList<Manufacturer> manufacturers = new ArrayList<>();

                for (String manufacturerName : manufacturersNames) {
                    manufacturers.add(new Manufacturer(manufacturerName, 0));
                }
                adapter.setManufacturers(manufacturers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void onItemClick(View view) {
        CarManufacturerRecyclerAdapter.ViewHolder viewHolder = (CarManufacturerRecyclerAdapter.ViewHolder) view.getTag();
        int position = viewHolder.getAdapterPosition();
        String selectedManufacturerName = adapter.getManufacturer(position).getName();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.car_view_container, CarModelSelectionFragment.newInstance(selectedManufacturerName)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
    }
}