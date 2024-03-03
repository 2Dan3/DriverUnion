package com.du.driverunison;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.driverunison.model.CarSafetySpecs;
import com.du.driverunison.model.Motorization;
import com.du.driverunison.util.CarSpecMotorizationRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DialogSafetySpecs extends Dialog{
    private final CarSafetySpecs safetySpecs;
    private CarSpecMotorizationRecyclerAdapter adapter;
    public DialogSafetySpecs(Context context,
                             CarSafetySpecs safetySpecs) {
        super(context);
        this.safetySpecs = safetySpecs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState != null
                ? savedInstanceState
                : new Bundle());

        // Inflate layout into View
        View view
                = LayoutInflater.from(getContext())
                .inflate(R.layout.safety_specs, null);

        // Set the dialog's content view
        setContentView(view);

        // Touching outside cancels Dialog
        setCanceledOnTouchOutside(true);

        // Back button cancels Dialog
        setCancelable(true);

        showSafetySpecs(view);
    }
    private void showSafetySpecs(View view) {
        TextView tvYearOfTesting = view.findViewById(R.id.tv_eval_year);
        TextView tvAdultSafety = view.findViewById(R.id.tv_adult_safety);
        TextView tvChildSafety = view.findViewById(R.id.tv_child_safety);
        TextView tvVulnerableSafety = view.findViewById(R.id.tv_vulnerable_safety);
        TextView tvActiveSafety = view.findViewById(R.id.tv_active_safety);

        tvYearOfTesting.setText(String.format("%d %s", safetySpecs.getEvalYear(), "Safety Ratings"));
        tvAdultSafety.setText(String.format("%d%s", safetySpecs.getAdultOccupant(), "%"));
        tvChildSafety.setText(String.format("%d%s", safetySpecs.getChildOccupant(), "%"));
        tvVulnerableSafety.setText(String.format("%d%s", safetySpecs.getVulnerableRoadUsers(), "%"));
        tvActiveSafety.setText(String.format("%d%s", safetySpecs.getSafetyAssist(), "%"));
    }
}
