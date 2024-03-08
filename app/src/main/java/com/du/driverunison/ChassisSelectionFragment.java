package com.du.driverunison;

import static com.du.driverunison.CarDetailedActivity.CHASSIS_SHAPE;
import static com.du.driverunison.CarDetailedActivity.EXISTING_GEN_YEAR_SPANS;
import static com.du.driverunison.CarDetailedActivity.MAKER_NAME;
import static com.du.driverunison.CarDetailedActivity.MODEL_NAME;
import static com.du.driverunison.CarDetailedActivity.YEARS_OF_MANUFACTURE_RANGE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.driverunison.util.ChassisOptionRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChassisSelectionFragment extends Fragment {
    public static final String ESTATE = "estate";
    public static final String SEDAN = "sedan";
    public static final String HATCHBACK = "hatchback";
    public static final String CABRIOLET = "cabriolet";
    public static final String FASTBACK = "fastback";
    public static final String COUPE = "coupe";
    public static final String SUV = "suv";
    public static final String MPV = "mpv";
    public static final String PICKUP = "pickup";
    private static final String[] AVAILABLE_CHASSIS_SHAPES = new String[]{ESTATE, SEDAN, HATCHBACK, CABRIOLET, FASTBACK, COUPE, SUV, MPV, PICKUP};
    private static final String MANUFACTURER = "manufacturer_name";
    private String manufacturerName;
    private static final String MODEL = "model_name";
    private String modelName;
    private RecyclerView recyclerView;
    private ChassisOptionRecyclerAdapter adapter;
    private ArrayList<String> chassisOptions;

    public ChassisSelectionFragment() {}
    public static ChassisSelectionFragment newInstance(String manufacturerName, DataSnapshot carModelData, ArrayList<String> availableChassisShapes) {
        ChassisSelectionFragment fragment = new ChassisSelectionFragment();
        Bundle args = new Bundle();

        for (String chassisShape : availableChassisShapes) {
            args.putBoolean(chassisShape, true);
        }

        args.putString(MANUFACTURER, manufacturerName);
        args.putString(MODEL, carModelData.getKey());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.chassisOptions = new ArrayList<>();

        final Bundle args = getArguments();

        if (args != null) {
            for (String carShape : AVAILABLE_CHASSIS_SHAPES) {
                if (args.get(carShape) != null && args.getBoolean(carShape)) {
                    this.chassisOptions.add(carShape);
                }
            }

            this.manufacturerName = args.getString(MANUFACTURER);
            this.modelName = args.getString(MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chassis_selection, container, false);

        recyclerView = v.findViewById(R.id.recycler_chassis_selection);
        adapter = new ChassisOptionRecyclerAdapter(this.chassisOptions);

        adapter.setClickListener(this::onItemClick);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return v;
    }

    private void onItemClick(View view) {
        ChassisOptionRecyclerAdapter.ViewHolder viewHolder = (ChassisOptionRecyclerAdapter.ViewHolder) view.getTag();
        int position = viewHolder.getAdapterPosition();
        String selectedChassisShape = adapter.getChassisOption(position);

//        Todo fix : temporary test values for "years", refactor when real years are chosen from UI
        String selectedManufactureYears = "M3".equals(modelName) ? "2007-2013" : "6".equals(modelName) ? "2018-" : "3".equals(modelName) ? "2019-2024" : "CX-60".equals(modelName) ? "2022-" : "X5 M".equals(modelName) ? "2023-" : "Giulia".equals(modelName) ? "2022-" : "N/A";

        DatabaseReference gensRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(manufacturerName).child(modelName).child(selectedChassisShape);
        gensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<String> genYearSpans = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot carGensSnapshot) {
                for (DataSnapshot carGenData : carGensSnapshot.getChildren()) {
                    String genYearSpan = carGenData.getKey();
                    genYearSpans.add(genYearSpan);
                }

                getActivity().runOnUiThread(() -> {
                    Intent detailedCarViewIntent = new Intent(getActivity(), CarDetailedActivity.class);
                    Bundle args = new Bundle();

                    args.putString(MAKER_NAME, manufacturerName);
                    args.putString(MODEL_NAME, modelName);
                    args.putString(CHASSIS_SHAPE, selectedChassisShape);
                    args.putString(YEARS_OF_MANUFACTURE_RANGE, selectedManufactureYears);
                    args.putStringArrayList(EXISTING_GEN_YEAR_SPANS, genYearSpans);

                    detailedCarViewIntent.putExtras(args);
                    startActivity(detailedCarViewIntent);
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}