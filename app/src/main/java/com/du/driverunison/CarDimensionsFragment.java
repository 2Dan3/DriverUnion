package com.du.driverunison;

import static com.du.driverunison.CarDetailedFragment.CHASSIS_SHAPE;
import static com.du.driverunison.CarDetailedFragment.MAKER_NAME;
import static com.du.driverunison.CarDetailedFragment.MODEL_NAME;
import static com.du.driverunison.CarDetailedFragment.YEARS_OF_MANUFACTURE_RANGE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.du.driverunison.model.CarGeneralSpecs;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarDimensionsFragment extends Fragment {
    private CarGeneralSpecs carGeneralSpecs;
    private String makerName;
    private String modelName;
    private String chassisShape;
    private String yearsRange;

    public CarDimensionsFragment() {}
    public static CarDimensionsFragment newInstance(String carMakerName, String carModelName, String carChassisShape, String carYearSpanManufactured) {
        CarDimensionsFragment fragment = new CarDimensionsFragment();
        Bundle args = new Bundle();
        args.putString(MAKER_NAME, carMakerName);
        args.putString(MODEL_NAME, carModelName);
        args.putString(CHASSIS_SHAPE, carChassisShape);
        args.putString(YEARS_OF_MANUFACTURE_RANGE, carYearSpanManufactured);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            this.makerName = args.getString(MAKER_NAME, "N/A");
            this.modelName = args.getString(MODEL_NAME, "N/A");
            this.chassisShape = args.getString(CHASSIS_SHAPE, "N/A");
            this.yearsRange = args.getString(YEARS_OF_MANUFACTURE_RANGE, "N/A");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_dimensions, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFAB(view);
        loadModelGeneralSpecs(view);
    }

    private void loadModelGeneralSpecs(View view) {
        DatabaseReference fullModelSpecRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(makerName).child(modelName).child(chassisShape).child(yearsRange).child("general specs");
        fullModelSpecRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot fullModelSpecSnap) {
                carGeneralSpecs = fullModelSpecSnap.getValue(CarGeneralSpecs.class);
                setupUI(view);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setupUI(View view) {
        ((TextView) view.findViewById(R.id.detailed_model_length)).setText(carGeneralSpecs.length);
        ((TextView) view.findViewById(R.id.detailed_model_width)).setText(carGeneralSpecs.width);
        ((TextView) view.findViewById(R.id.detailed_model_height)).setText(carGeneralSpecs.height);
        ((TextView) view.findViewById(R.id.detailed_model_wheelbase)).setText(carGeneralSpecs.wheelbase);
        ((TextView) view.findViewById(R.id.detailed_model_trunk)).setText(carGeneralSpecs.trunk);
    }
    private void setupFAB(View view) {

//        FloatingActionButton fabMain = new FloatingActionButton(getContext());
//        FloatingActionsMenu fam = new FloatingActionsMenu(getContext());
////        fabMain
//        fam.addButton(fab);
    }
}