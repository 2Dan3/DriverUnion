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

import java.io.File;
import java.util.ArrayList;

public class CarManufacturerSelectionFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarManufacturerRecyclerAdapter adapter;
    private ArrayList<Manufacturer> manufacturers;

    public CarManufacturerSelectionFragment() { }

    public static CarManufacturerSelectionFragment newInstance() {
        CarManufacturerSelectionFragment fragment = new CarManufacturerSelectionFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.manufacturers = new ArrayList<>();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
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
//      TODO load all existing manufacturers from Database / Server

//      MOCK-DATA for testing
        this.manufacturers.add(new Manufacturer("Mazda", R.mipmap.car_manufacturer_logo));
        this.manufacturers.add(new Manufacturer("BMW", R.mipmap.car_manufacturer_logo2));
        this.manufacturers.add(new Manufacturer("Alfa Romeo", R.mipmap.car_manufacturer_logo7));
        this.manufacturers.add(new Manufacturer("Lexus", R.mipmap.car_manufacturer_logo3));
        this.manufacturers.add(new Manufacturer("Porsche", R.mipmap.car_manufacturer_logo4));
        this.manufacturers.add(new Manufacturer("Honda", R.mipmap.car_manufacturer_logo5));
        this.manufacturers.add(new Manufacturer("Hyundai", R.mipmap.car_manufacturer_logo6));
//      TODO on loaded -> call adapter.setManufacturers();
    }
    private void onItemClick(View view) {
        CarManufacturerRecyclerAdapter.ViewHolder viewHolder = (CarManufacturerRecyclerAdapter.ViewHolder) view.getTag();
        int position = viewHolder.getAdapterPosition();
        String selectedManufacturerName = adapter.getManufacturer(position).getName();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.car_view_container, CarModelSelectionFragment.newInstance(selectedManufacturerName)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
    }
}