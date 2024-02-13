package com.du.driverunison;

import android.os.Bundle;

import com.du.driverunison.model.Motorization;
import com.du.driverunison.util.CarManufacturerRecyclerAdapter;
import com.du.driverunison.util.CarSpecMotorizationRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.driverunison.databinding.ActivityCarDetailedBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarDetailedActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityCarDetailedBinding binding;
    public static final String LENGTH = "length";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String WHEELBASE = "wheelbase";
    public static final String MAKER_NAME = "maker_name";
    public static final String MODEL_NAME = "model_name";
    public static final String CHASSIS_SHAPE = "chassis_shape";
    public static final String YEARS_OF_MANUFACTURE_RANGE = "years";

    private String length;
    private String width;
    private String height;
    private String wheelbase;
    private String makerName;
    private String modelName;
    private String chassisShape;
    private String yearsRange;
    private CarSpecMotorizationRecyclerAdapter adapter;

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
        this.makerName = args.getString(MAKER_NAME, "N/A");
        this.modelName = args.getString(MODEL_NAME, "N/A");
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
        binding.detailedModelName.setText(String.format("%s %s (%s)", this.makerName, this.modelName, this.chassisShape));
        binding.detailedModelYears.setText(this.yearsRange);
        binding.detailedModelLength.setText(this.length);
        binding.detailedModelWidth.setText(this.width);
        binding.detailedModelHeight.setText(this.height);
        binding.detailedModelWheelbase.setText(this.wheelbase);
//        todo temporary workaround refactor, having implemented real images fetch from img-server
//          binding.detailedModelIv.setImageURI();
        int imgRes;
        switch (this.modelName){
            case "M3":
                imgRes = R.mipmap.car_default_filler2;
                break;
            case "6":
                imgRes = R.mipmap.car_default_filler;
                break;
            case "3":
                imgRes = R.mipmap.car_default_filler3;
                break;
            case "CX-60":
                imgRes = R.mipmap.car_default_filler4;
                break;
            case "X5 M":
                imgRes = R.mipmap.car_default_filler5;
                break;
            default:
                imgRes = R.mipmap.car_coupe_shape;
        }
        binding.detailedModelIv.setImageResource(imgRes);

        loadMotorizationSpecs();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_detailed);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void loadMotorizationSpecs() {
        ArrayList<Motorization> motorizations = new ArrayList<>();
//        motorizations.add(new Motorization("3,3 e-Skyactiv D (254HP) Mild Hybrid AWD Automatic", "i6"));
//        motorizations.add(new Motorization("3,3 e-Skyactiv D (200HP) Mild Hybrid Automatic", "i6"));
//        motorizations.add(new Motorization("2,5 e-Skyactiv (328HP) Plug-in Hybrid AWD Automatic", "i4"));
        DatabaseReference enginesRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(makerName).child(modelName).child(chassisShape).child(yearsRange).child("engines");
        enginesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot transmissionType) {
                for (DataSnapshot transmissionEnginesAvailable : transmissionType.getChildren()) {
//                    Toast.makeText(CarDetailedActivity.this, transmissionData.toString(), Toast.LENGTH_SHORT).show();
                    String transmission = transmissionEnginesAvailable.getKey();
//                    if (engineBasicData.exists()) {
                        for (DataSnapshot engine: transmissionEnginesAvailable.getChildren()){
                            String name = engine.getKey();
                            String layout = engine.getValue(String.class);
                            Motorization motorSpec = new Motorization(name, layout, transmission);
                            motorizations.add(motorSpec);
                        }
//                    }
                }
                showMotorizationSpecs();
            }
            private void showMotorizationSpecs() {
                RecyclerView recyclerView = binding.recyclerMotorSpecsCarDetailed;
                adapter = new CarSpecMotorizationRecyclerAdapter(motorizations);
                //Todo
                // adapter.setClickListener(this::onItemClick);

                recyclerView.setLayoutManager(new LinearLayoutManager(CarDetailedActivity.this));
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}