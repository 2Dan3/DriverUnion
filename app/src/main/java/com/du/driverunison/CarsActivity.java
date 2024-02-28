package com.du.driverunison;

import android.os.Bundle;

import com.du.driverunison.model.Manufacturer;
import com.du.driverunison.util.CarManufacturerRecyclerAdapter;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import com.du.driverunison.databinding.ActivityCarManufacturersBinding;

import java.util.ArrayList;

public class CarsActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
    private ActivityCarManufacturersBinding binding;

//    private RecyclerView recyclerView;
//    private CarManufacturerRecyclerAdapter adapter;
//    private ArrayList<Manufacturer> manufacturers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCarManufacturersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_manufacturers);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show());

        getSupportFragmentManager().beginTransaction().replace(R.id.car_view_container, CarManufacturerSelectionFragment.newInstance()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_manufacturers);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//
//    private void getCarManufacturers() {
////      TODO load all existing manufacturers from Database / Server
////      MOCK-DATA for testing
//        this.manufacturers.add(new Manufacturer("Mazda", R.mipmap.car_manufacturer_logo));
//        this.manufacturers.add(new Manufacturer("BMW", R.mipmap.car_manufacturer_logo2));
//        this.manufacturers.add(new Manufacturer("Lexus", R.mipmap.car_manufacturer_logo3));
//        this.manufacturers.add(new Manufacturer("Porsche", R.mipmap.car_manufacturer_logo4));
//        this.manufacturers.add(new Manufacturer("Honda", R.mipmap.car_manufacturer_logo5));
//        this.manufacturers.add(new Manufacturer("Hyundai", R.mipmap.car_manufacturer_logo6));
////      TODO on loaded -> call adapter.setManufacturers();
//    }
}