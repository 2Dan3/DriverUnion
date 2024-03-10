package com.du.driverunison;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.du.driverunison.databinding.ActivityCarManufacturersBinding;

public class CarsActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
    private ActivityCarManufacturersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCarManufacturersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//       TODO
//        setSupportActionBar(binding.toolbar);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_manufacturers);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        getSupportFragmentManager().beginTransaction().replace(R.id.car_view_container, CarManufacturerSelectionFragment.newInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
// TODO
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_manufacturers);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}