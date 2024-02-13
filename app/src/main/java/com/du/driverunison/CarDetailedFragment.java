package com.du.driverunison;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.du.driverunison.model.Motorization;
import com.du.driverunison.util.CarSpecMotorizationRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarDetailedFragment extends Fragment {
    public static final String LENGTH = "length";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String WHEELBASE = "wheelbase";
    public static final String TRUNK_SIZE = "trunk";
    public static final String MAKER_NAME = "maker_name";
    public static final String MODEL_NAME = "model_name";
    public static final String CHASSIS_SHAPE = "chassis_shape";
    public static final String YEARS_OF_MANUFACTURE_RANGE = "years";
    private String length;
    private String width;
    private String height;
    private String wheelbase;
    private String trunkSize;
    private String makerName;
    private String modelName;
    private String chassisShape;
    private String yearsRange;
    private CarSpecMotorizationRecyclerAdapter adapter;

    public CarDetailedFragment() {}

    public static CarDetailedFragment newInstance(String carLength, String carWidth, String carWheelbase, String carHeight, String carMakerName, String carModelName, String carChassisShape, String carYearSpanManufactured, String carTrunkSize) {
        CarDetailedFragment fragment = new CarDetailedFragment();
        Bundle args = new Bundle();
        args.putString(LENGTH, carLength);
        args.putString(WIDTH, carWidth);
        args.putString(WHEELBASE, carWheelbase);
        args.putString(HEIGHT, carHeight);
        args.putString(MAKER_NAME, carMakerName);
        args.putString(MODEL_NAME, carModelName);
        args.putString(CHASSIS_SHAPE, carChassisShape);
        args.putString(YEARS_OF_MANUFACTURE_RANGE, carYearSpanManufactured);
        args.putString(TRUNK_SIZE, carTrunkSize);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            this.length = args.getString(LENGTH, "N/A");
            this.width = args.getString(WIDTH, "N/A");
            this.wheelbase = args.getString(WHEELBASE, "N/A");
            this.height = args.getString(HEIGHT, "N/A");
            this.makerName = args.getString(MAKER_NAME, "N/A");
            this.modelName = args.getString(MODEL_NAME, "N/A");
            this.chassisShape = args.getString(CHASSIS_SHAPE, "N/A");
            this.yearsRange = args.getString(YEARS_OF_MANUFACTURE_RANGE, "N/A");
            this.trunkSize = args.getString(TRUNK_SIZE, "N/A");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_detailed, container, false);

        setupUI(v);

        return v;
    }

    private void setupUI(View view) {
        ((TextView)view.findViewById(R.id.detailed_model_name)).setText(String.format("%s %s (%s)", this.makerName, this.modelName, this.chassisShape));
        ((TextView)view.findViewById(R.id.detailed_model_years)).setText(this.yearsRange);
        ((TextView)view.findViewById(R.id.detailed_model_length)).setText(this.length);
        ((TextView)view.findViewById(R.id.detailed_model_width)).setText(this.width);
        ((TextView)view.findViewById(R.id.detailed_model_height)).setText(this.height);
        ((TextView)view.findViewById(R.id.detailed_model_wheelbase)).setText(this.wheelbase);
        ((TextView)view.findViewById(R.id.detailed_model_trunk)).setText(this.trunkSize);
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
        ((ImageView)view.findViewById(R.id.detailed_model_iv)).setImageResource(imgRes);

        loadMotorizationSpecs(view);
    }

    private void loadMotorizationSpecs(View view) {
        ArrayList<Motorization> motorizations = new ArrayList<>();

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
                showMotorizationSpecs(view);
            }
            private void showMotorizationSpecs(View view) {
                RecyclerView recyclerView = view.findViewById(R.id.recycler_motor_specs_car_detailed);
                adapter = new CarSpecMotorizationRecyclerAdapter(motorizations);
                //Todo
                // adapter.setClickListener(this::onItemClick);

                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}