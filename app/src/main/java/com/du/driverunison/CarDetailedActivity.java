package com.du.driverunison;

import android.os.Bundle;

import com.du.driverunison.util.CarSpecMotorizationRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.du.driverunison.databinding.ActivityCarDetailedBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class CarDetailedActivity extends FragmentActivity {

//    private AppBarConfiguration appBarConfiguration;
    private ActivityCarDetailedBinding binding;
//    public static final String LENGTH = "length";
//    public static final String WIDTH = "width";
//    public static final String HEIGHT = "height";
//    public static final String WHEELBASE = "wheelbase";
//    public static final String TRUNK_SIZE = "trunk";
    public static final String MAKER_NAME = "maker_name";
    public static final String MODEL_NAME = "model_name";
    public static final String CHASSIS_SHAPE = "chassis_shape";
    public static final String YEARS_OF_MANUFACTURE_RANGE = "years";
    public static final String EXISTING_GEN_YEAR_SPANS = "gens";

//    private String length;
//    private String width;
//    private String height;
//    private String wheelbase;
//    private String trunkSize;
    private String makerName;
    private String modelName;
    private String chassisShape;
    private String yearsRange;
    private ArrayList<String> existingGenYearSpans;
    private CarSpecMotorizationRecyclerAdapter adapter;
    private ViewPager2 viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCarDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);

        final Bundle args = getIntent().getExtras();
        this.makerName = args.getString(MAKER_NAME, "N/A");
        this.modelName = args.getString(MODEL_NAME, "N/A");
        this.chassisShape = args.getString(CHASSIS_SHAPE, "N/A");
        this.yearsRange = args.getString(YEARS_OF_MANUFACTURE_RANGE, "N/A");
        this.existingGenYearSpans = args.getStringArrayList(EXISTING_GEN_YEAR_SPANS);
//        this.length = args.getString(LENGTH, "N/A");
//        this.width = args.getString(WIDTH, "N/A");
//        this.wheelbase = args.getString(WHEELBASE, "N/A");
//        this.height = args.getString(HEIGHT, "N/A");
//        this.trunkSize = args.getString(TRUNK_SIZE, "N/A");

//        setupUI();

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_detailed);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener((View.OnClickListener) view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show());

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = binding.pagerCarDetailed;
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = binding.tabLayoutCarDetailed;
//        tabLayout.setupWithViewPager(viewPager);
        TabLayoutMediator tlm = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {

        });
        tlm.attach();

//        Initially the newest gen of model is displayed
        viewPager.setCurrentItem(existingGenYearSpans.size() - 1, false);
    }

//    @Override
//    public void onBackPressed() {
//        if (viewPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//        }
//    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return CarDetailedFragment.newInstance(makerName, modelName, chassisShape, existingGenYearSpans.get(position));
        }

        @Override
        public int getItemCount() {
            return existingGenYearSpans.size();
        }
    }

//    private void setupUI() {
//        binding.detailedModelName.setText(String.format("%s %s (%s)", this.makerName, this.modelName, this.chassisShape));
//        binding.detailedModelYears.setText(this.yearsRange);
//        binding.detailedModelLength.setText(this.length);
//        binding.detailedModelWidth.setText(this.width);
//        binding.detailedModelHeight.setText(this.height);
//        binding.detailedModelWheelbase.setText(this.wheelbase);
////        todo temporary workaround refactor, having implemented real images fetch from img-server
////          binding.detailedModelIv.setImageURI();
//        int imgRes;
//        switch (this.modelName){
//            case "M3":
//                imgRes = R.mipmap.car_default_filler2;
//                break;
//            case "6":
//                imgRes = R.mipmap.car_default_filler;
//                break;
//            case "3":
//                imgRes = R.mipmap.car_default_filler3;
//                break;
//            case "CX-60":
//                imgRes = R.mipmap.car_default_filler4;
//                break;
//            case "X5 M":
//                imgRes = R.mipmap.car_default_filler5;
//                break;
//            default:
//                imgRes = R.mipmap.car_coupe_shape;
//        }
//        binding.detailedModelIv.setImageResource(imgRes);
//
//        loadMotorizationSpecs();
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_detailed);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//    private void loadMotorizationSpecs() {
//        ArrayList<Motorization> motorizations = new ArrayList<>();
////        motorizations.add(new Motorization("3,3 e-Skyactiv D (254HP) Mild Hybrid AWD Automatic", "i6"));
////        motorizations.add(new Motorization("3,3 e-Skyactiv D (200HP) Mild Hybrid Automatic", "i6"));
////        motorizations.add(new Motorization("2,5 e-Skyactiv (328HP) Plug-in Hybrid AWD Automatic", "i4"));
//        DatabaseReference enginesRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(makerName).child(modelName).child(chassisShape).child(yearsRange).child("engines");
//        enginesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot transmissionType) {
//                for (DataSnapshot transmissionEnginesAvailable : transmissionType.getChildren()) {
////                    Toast.makeText(CarDetailedActivity.this, transmissionData.toString(), Toast.LENGTH_SHORT).show();
//                    String transmission = transmissionEnginesAvailable.getKey();
////                    if (engineBasicData.exists()) {
//                        for (DataSnapshot engine: transmissionEnginesAvailable.getChildren()){
//                            String name = engine.getKey();
//                            String layout = engine.getValue(String.class);
//                            Motorization motorSpec = new Motorization(name, layout, transmission);
//                            motorizations.add(motorSpec);
//                        }
////                    }
//                }
//                showMotorizationSpecs();
//            }
//            private void showMotorizationSpecs() {
//                RecyclerView recyclerView = binding.recyclerMotorSpecsCarDetailed;
//                adapter = new CarSpecMotorizationRecyclerAdapter(motorizations);
//                //Todo
//                // adapter.setClickListener(this::onItemClick);
//
//                recyclerView.setLayoutManager(new LinearLayoutManager(CarDetailedActivity.this));
//                recyclerView.setAdapter(adapter);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
}