package com.du.driverunison;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.du.driverunison.databinding.ActivityCarDetailedBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class CarDetailedActivity extends FragmentActivity {

//    private AppBarConfiguration appBarConfiguration;
    private ActivityCarDetailedBinding binding;
    public static final String MAKER_NAME = "maker_name";
    public static final String MODEL_NAME = "model_name";
    public static final String CHASSIS_SHAPE = "chassis_shape";
    public static final String TARGET_YEARS_OF_MANUFACTURE_RANGE = "years";
    public static final String EXISTING_GEN_YEAR_SPANS = "gens";

    private String makerName;
    private String modelName;
    private String chassisShape;
    private String targetYearsRange;
    private ArrayList<String> existingGenYearSpans;
    private ViewPager2 viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private TextView tvPrevGen;
    private TextView tvNextGen;
    private LinearLayout layoutPrevGen;
    private LinearLayout layoutNextGen;
    private TabLayout layoutTabbed;

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
        this.targetYearsRange = args.getString(TARGET_YEARS_OF_MANUFACTURE_RANGE, null);
        this.existingGenYearSpans = args.getStringArrayList(EXISTING_GEN_YEAR_SPANS);

//       TODO check NAV & del auto-gen fragments (First- & Second-)
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_detailed);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//          .
//        binding.fab.setOnClickListener((View.OnClickListener) view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show());

        tvPrevGen = binding.tvPrevGen;
        tvNextGen = binding.tvNextGen;
        layoutPrevGen = binding.prevGenLayout;
        layoutNextGen = binding.nextGenLayout;
        layoutTabbed = binding.tabLayoutCarDetailed;
        if (existingGenYearSpans.size() > 1)
            tvPrevGen.setText(existingGenYearSpans.get(existingGenYearSpans.size()-2));
        else {
            tvPrevGen.setText("No older models");
            layoutNextGen.setVisibility(View.INVISIBLE);
            layoutTabbed.setVisibility(View.INVISIBLE);
        }
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = binding.pagerCarDetailed;
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                updateUI(position);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateUI(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
            private void updateUI(int position) {
                if (existingGenYearSpans.size() > 1){
                    if (position == 0){
                        layoutPrevGen.setVisibility(View.INVISIBLE);
                        layoutNextGen.setVisibility(View.VISIBLE);
                        tvNextGen.setText(existingGenYearSpans.get(position + 1));
                    } else if (position == existingGenYearSpans.size() - 1) {
                        layoutNextGen.setVisibility(View.INVISIBLE);
                        layoutPrevGen.setVisibility(View.VISIBLE);
                        tvPrevGen.setText(existingGenYearSpans.get(position - 1));
                    }else {
                        tvPrevGen.setText(existingGenYearSpans.get(position - 1));
                        layoutPrevGen.setVisibility(View.VISIBLE);
                        tvNextGen.setText(existingGenYearSpans.get(position + 1));
                        layoutNextGen.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        TabLayout tabLayout = binding.tabLayoutCarDetailed;
        TabLayoutMediator tlm = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {

        });
        tlm.attach();

//        Initially the newest gen of model is displayed, unless pre-requested otherwise from CarDetailedActivity Intent
        int targetCarGenerationItem = targetYearsRange == null ? existingGenYearSpans.size() - 1 : existingGenYearSpans.indexOf(targetYearsRange);
        viewPager.setCurrentItem(targetCarGenerationItem, false);
    }

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
//  TODO check Nav controls and auto-config and, if unnecessary - del this
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_car_detailed);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}