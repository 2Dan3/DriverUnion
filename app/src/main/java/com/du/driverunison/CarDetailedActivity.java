package com.du.driverunison;

import android.os.Bundle;

import com.du.driverunison.model.Motorization;
import com.du.driverunison.util.CarManufacturerRecyclerAdapter;
import com.du.driverunison.util.CarSpecMotorizationRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.driverunison.databinding.ActivityCarDetailedBinding;

import java.util.ArrayList;

public class CarDetailedActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityCarDetailedBinding binding;
    public static final String LENGTH = "length";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String WHEELBASE = "wheelbase";
    public static final String MAKER_AND_MODEL_NAME = "model_name";
    public static final String CHASSIS_SHAPE = "chassis_shape";
    public static final String YEARS_OF_MANUFACTURE_RANGE = "years";

    private String length;
    private String width;
    private String height;
    private String wheelbase;
    private String makerAndModelName;
    private String chassisShape;
    private String yearsRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCarDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        final Bundle args = getIntent().getExtras();
        this.length = args.getString(LENGTH, "N/A");
        this.width = args.getString(WIDTH, "N/A");
        this.wheelbase = args.getString(WHEELBASE, "N/A");
        this.height = args.getString(HEIGHT, "N/A");
        this.makerAndModelName = args.getString(MAKER_AND_MODEL_NAME, "N/A");
        this.chassisShape = args.getString(CHASSIS_SHAPE, "N/A");
        this.yearsRange = args.getString(YEARS_OF_MANUFACTURE_RANGE, "N/A");

        setupUI();

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_detailed);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener((View.OnClickListener) view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show());
    }

    private void setupUI() {
        binding.detailedModelName.setText(String.format("%s (%s)" ,this.makerAndModelName, this.chassisShape));
        binding.detailedModelYears.setText(this.yearsRange);
        binding.detailedModelLength.setText(this.length);
        binding.detailedModelWidth.setText(this.width);
        binding.detailedModelHeight.setText(this.height);
        binding.detailedModelWheelbase.setText(this.wheelbase);
//        todo temporary workaround refactor, having implemented real images fetch from img-server
//          binding.detailedModelIv.setImageURI();
        int imgRes;
        if ("BMW M3".equalsIgnoreCase(this.makerAndModelName))
            imgRes = R.mipmap.car_default_filler2;
        else if ("Mazda 6".equalsIgnoreCase(this.makerAndModelName))
            imgRes = R.mipmap.car_default_filler;
        else if ("Mazda cx60".equalsIgnoreCase(this.makerAndModelName))
            imgRes = R.mipmap.car_default_filler4;
        else if ("Mazda 3".equalsIgnoreCase(this.makerAndModelName))
            imgRes = R.mipmap.car_default_filler3;
        else if ("BMW X5 M".equalsIgnoreCase(this.makerAndModelName))
            imgRes = R.mipmap.car_default_filler5;
        else
            imgRes = R.mipmap.car_coupe_shape;

        binding.detailedModelIv.setImageResource(imgRes);

        loadAndShowMotorizationSpecs();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_detailed);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void loadAndShowMotorizationSpecs() {
        ArrayList<Motorization> motorizations = loadMotorizationSpecs();
        RecyclerView recyclerView = binding.recyclerMotorSpecsCarDetailed;
        CarSpecMotorizationRecyclerAdapter adapter = new CarSpecMotorizationRecyclerAdapter(motorizations);
        //Todo
        // adapter.setClickListener(this::onItemClick);

        recyclerView.setLayoutManager(new LinearLayoutManager(CarDetailedActivity.this));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Motorization> loadMotorizationSpecs() {
        ArrayList<Motorization> motorizations = new ArrayList<>();
        motorizations.add(new Motorization("3,3 e-Skyactiv D (254HP) Mild Hybrid AWD Automatic", "i6"));
        motorizations.add(new Motorization("3,3 e-Skyactiv D (200HP) Mild Hybrid Automatic", "i6"));
        motorizations.add(new Motorization("2,5 e-Skyactiv (328HP) Plug-in Hybrid AWD Automatic", "i4"));
//        motorizations.add(new Motorization("2,5 e-Skyactiv (328HP) Plug-in Hybrid AWD Automatic", "i4"));
//        motorizations.add(new Motorization("2,5 e-Skyactiv (328HP) Plug-in Hybrid AWD Automatic", "i4"));
//        motorizations.add(new Motorization("2,5 e-Skyactiv (328HP) Plug-in Hybrid AWD Automatic", "i4"));
//        motorizations.add(new Motorization("2,5 e-Skyactiv (328HP) Plug-in Hybrid AWD Automatic", "i4"));
        return motorizations;
    }
}