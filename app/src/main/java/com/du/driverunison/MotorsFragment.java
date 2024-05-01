package com.du.driverunison;

import static com.du.driverunison.CarDetailedFragment.CHASSIS_SHAPE;
import static com.du.driverunison.CarDetailedFragment.MAKER_NAME;
import static com.du.driverunison.CarDetailedFragment.MODEL_NAME;
import static com.du.driverunison.CarDetailedFragment.YEARS_OF_MANUFACTURE_RANGE;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.du.driverunison.model.Motorization;
import com.du.driverunison.util.CarSpecMotorizationRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MotorsFragment extends Fragment {
    private String manufacturer, model, chassisShape, yearsOfManufacture;
    private ArrayList<Motorization> manuals;
    private ArrayList<Motorization> automatics;
    private ArrayList<Motorization> adapterMotorizations;
    private CarSpecMotorizationRecyclerAdapter adapter;
    private AppCompatToggleButton[] toggleButtons;

    public MotorsFragment() {}
    public static MotorsFragment newInstance(String manufacturer, String model, String chassisShape, String yearsOfManufacture) {
        MotorsFragment fragment = new MotorsFragment();
        Bundle args = new Bundle();
        args.putString(MAKER_NAME, manufacturer);
        args.putString(MODEL_NAME, model);
        args.putString(CHASSIS_SHAPE, chassisShape);
        args.putString(YEARS_OF_MANUFACTURE_RANGE, yearsOfManufacture);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            manufacturer = getArguments().getString(MAKER_NAME);
            model = getArguments().getString(MODEL_NAME);
            chassisShape = getArguments().getString(CHASSIS_SHAPE);
            yearsOfManufacture = getArguments().getString(YEARS_OF_MANUFACTURE_RANGE);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_motors, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadMotorizationSpecs(view);

        toggleButtons = new AppCompatToggleButton[]{view.findViewById(R.id.toggle_engine_manual_transmissions), view.findViewById(R.id.toggle_engine_automatic_transmissions)};

        manuals = new ArrayList<>();
        automatics = new ArrayList<>();
        adapterMotorizations = new ArrayList<>();
    }

    private void loadMotorizationSpecs(View view) {

        DatabaseReference enginesRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(manufacturer).child(model).child(chassisShape).child(yearsOfManufacture).child("engines");
        enginesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot transmissionType) {
                for (DataSnapshot transmissionEnginesAvailable : transmissionType.getChildren()) {
                    String transmission = transmissionEnginesAvailable.getKey();
                    boolean added;
                    for (DataSnapshot engine: transmissionEnginesAvailable.getChildren()){
                        String name = engine.getKey();
                        String layout = engine.getValue(String.class);
                        Motorization motorSpec = new Motorization(name, layout, transmission);
                        added = "manual".equals(transmission) ? manuals.add(motorSpec) : automatics.add(motorSpec);
                    }
//                    }
                }
                showMotorizationSpecs(view);
                setupTransmissionOptionToggles();
            }
            private void showMotorizationSpecs(View view) {
                RecyclerView recyclerView = view.findViewById(R.id.recycler_motor_specs_car_detailed);
                adapter = new CarSpecMotorizationRecyclerAdapter(adapterMotorizations);
                //Todo
                // adapter.setClickListener(this::onItemClick);

                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void setupTransmissionOptionToggles(){
        for (AppCompatToggleButton btn : toggleButtons) {
            btn.setOnCheckedChangeListener( (buttonView, isToggled) -> {
                if (isToggled) {
                    buttonView.setTypeface(null, Typeface.BOLD);
                    buttonView.setTextColor(getContext().getResources().getColor(R.color.white));
                }
                else {
                    buttonView.setTypeface(null, Typeface.NORMAL);
                    buttonView.setTextColor(getContext().getResources().getColor(R.color.white_smoked));
                }

                if (toggleButtons[0].isChecked() == toggleButtons[1].isChecked()){
                    adapterMotorizations.clear();
                    if (isToggled){
                        adapterMotorizations.addAll(manuals);
                        adapterMotorizations.addAll(automatics);
                    }
                    adapter.setMotorizations(adapterMotorizations);

                }else if (toggleButtons[0].isChecked()){
                    adapter.setMotorizations(manuals);
                }else if (toggleButtons[1].isChecked()){
                    adapter.setMotorizations(automatics);
                }
            });
        }
//        toggleButtons[0].setChecked(true);
    }
}