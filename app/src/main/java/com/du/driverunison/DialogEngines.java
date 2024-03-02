package com.du.driverunison;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.driverunison.model.Motorization;
import com.du.driverunison.util.CarSpecMotorizationRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DialogEngines extends Dialog {
    private String manufacturer, model, chassisShape, yearsOfManufacture;
    private ArrayList<Motorization> manuals;
    private ArrayList<Motorization> automatics;
    private ArrayList<Motorization> adapterMotorizations;
    private CarSpecMotorizationRecyclerAdapter adapter;
    private AppCompatToggleButton[] toggleButtons;

    public DialogEngines(Context context,
                         String manufacturer, String model, String chassisShape, String yearsOfManufacture) {
        super(context);
        this.manufacturer = manufacturer;
        this.model = model;
        this.chassisShape = chassisShape;
        this.yearsOfManufacture = yearsOfManufacture;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState != null
                ? savedInstanceState
                : new Bundle());

        // Inflate layout into View
        View view
                = LayoutInflater.from(getContext())
                .inflate(R.layout.engine_specs, null);

        // Set the dialog's content view
        setContentView(view);

        // Touching outside cancels Dialog
        setCanceledOnTouchOutside(true);

        // Back button cancels Dialog
        setCancelable(true);

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
