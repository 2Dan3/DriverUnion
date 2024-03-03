package com.du.driverunison;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.du.driverunison.model.CarGeneralSpecs;
import com.du.driverunison.model.CarSafetySpecs;
import com.du.driverunison.util.FetchImageTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarDetailedFragment extends Fragment implements FetchImageTask.FetchImageTaskCallback{
    public static final String MAKER_NAME = "maker_name";
    public static final String MODEL_NAME = "model_name";
    public static final String CHASSIS_SHAPE = "chassis_shape";
    public static final String YEARS_OF_MANUFACTURE_RANGE = "years";
    private String makerName;
    private String modelName;
    private String chassisShape;
    private String yearsRange;
    private ImageView ivCarModel;
    private CarGeneralSpecs carGeneralSpecs;
    private CarSafetySpecs carSafetySpecs;
    private DialogEngines dialogEngines;
    private DialogSafetySpecs dialogSafetySpecs;

    public CarDetailedFragment() {}

    public static CarDetailedFragment newInstance(String carMakerName, String carModelName, String carChassisShape, String carYearSpanManufactured) {
        CarDetailedFragment fragment = new CarDetailedFragment();
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

            loadModelGeneralSpecs();
            new FetchImageTask(this).execute(makerName, modelName, chassisShape, yearsRange);
            loadModelSafetyRatings();
        }
    }
    private void loadModelGeneralSpecs() {
        DatabaseReference fullModelSpecRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(makerName).child(modelName).child(chassisShape).child(yearsRange).child("general specs");
        fullModelSpecRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot fullModelSpecSnap) {
                carGeneralSpecs = fullModelSpecSnap.getValue(CarGeneralSpecs.class);
                setupUI(getView());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void loadModelSafetyRatings() {
        DatabaseReference fullModelSpecRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(makerName).child(modelName).child(chassisShape).child(yearsRange).child("safety");
        fullModelSpecRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot fullModelSafetySnap) {
                carSafetySpecs = fullModelSafetySnap.getValue(CarSafetySpecs.class);
                if (carSafetySpecs != null)
                    showSafetyRatingOverall(getView());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_detailed, container, false);

//        setupUI(v);
        ((TextView)v.findViewById(R.id.detailed_model_years)).setText(yearsRange);
        ((TextView) v.findViewById(R.id.detailed_model_name)).setText(String.format("%s %s (%s)", this.makerName, this.modelName, this.chassisShape));

        return v;
    }

    private void setupUI(View view) {
//        todo temporary workaround refactor, having implemented real images fetch from img-server
//          binding.detailedModelIv.setImageURI();
        int imgManufacturerRes;
        switch (this.makerName){
            case "BMW":
                imgManufacturerRes = R.mipmap.car_manufacturer_logo2;
                break;
            case "Alfa Romeo":
                imgManufacturerRes = R.mipmap.car_manufacturer_logo7;
                break;
            case "Mazda":
                imgManufacturerRes = R.mipmap.car_manufacturer_logo;
                break;
            default:
                imgManufacturerRes = R.mipmap.car_coupe_shape;
        }

        CardView cardDistributor,
                cardUsedCars,
                cardCarParts,
                cardPowertrains,
                cardForum;
        if (view != null) {
            ivCarModel = view.findViewById(R.id.detailed_model_iv);
            ((ImageView) view.findViewById(R.id.iv_distributor_logo)).setImageResource(imgManufacturerRes);
            ((TextView) view.findViewById(R.id.detailed_model_name)).setText(String.format("%s %s (%s)", this.makerName, this.modelName, this.chassisShape));
            ((TextView) view.findViewById(R.id.detailed_model_years)).setText(this.yearsRange);
            ((TextView) view.findViewById(R.id.detailed_model_length)).setText(carGeneralSpecs.length);
            ((TextView) view.findViewById(R.id.detailed_model_width)).setText(carGeneralSpecs.width);
            ((TextView) view.findViewById(R.id.detailed_model_height)).setText(carGeneralSpecs.height);
            ((TextView) view.findViewById(R.id.detailed_model_wheelbase)).setText(carGeneralSpecs.wheelbase);
            ((TextView) view.findViewById(R.id.detailed_model_trunk)).setText(carGeneralSpecs.trunk);
            cardDistributor = view.findViewById(R.id.card_distributor);
            cardUsedCars = view.findViewById(R.id.card_used_cars);
            cardCarParts = view.findViewById(R.id.card_car_parts);
            cardPowertrains = view.findViewById(R.id.card_powertrains);
            cardForum = view.findViewById(R.id.card_forums);
        }
        else {
            ivCarModel = getActivity().findViewById(R.id.detailed_model_iv);
            ((ImageView)getActivity().findViewById(R.id.iv_distributor_logo)).setImageResource(imgManufacturerRes);
            ((TextView)getActivity().findViewById(R.id.detailed_model_name)).setText(String.format("%s %s (%s)", this.makerName, this.modelName, this.chassisShape));
            ((TextView)getActivity().findViewById(R.id.detailed_model_years)).setText(this.yearsRange);
            ((TextView)getActivity().findViewById(R.id.detailed_model_length)).setText(carGeneralSpecs.length);
            ((TextView)getActivity().findViewById(R.id.detailed_model_width)).setText(carGeneralSpecs.width);
            ((TextView)getActivity().findViewById(R.id.detailed_model_height)).setText(carGeneralSpecs.height);
            ((TextView)getActivity().findViewById(R.id.detailed_model_wheelbase)).setText(carGeneralSpecs.wheelbase);
            ((TextView)getActivity().findViewById(R.id.detailed_model_trunk)).setText(carGeneralSpecs.trunk);
            cardDistributor = getActivity().findViewById(R.id.card_distributor);
            cardUsedCars = getActivity().findViewById(R.id.card_used_cars);
            cardCarParts = getActivity().findViewById(R.id.card_car_parts);
            cardPowertrains = getActivity().findViewById(R.id.card_powertrains);
            cardForum = getActivity().findViewById(R.id.card_forums);
        }

        cardDistributor.setOnClickListener(this::toNewCarsSeller);
        cardUsedCars.setOnClickListener(this::toUsedCarsSeller);
        cardCarParts.setOnClickListener(this::toCarPartsSeller);
        cardPowertrains.setOnClickListener(this::onClickShowEngines);
        cardForum.setOnClickListener(this::toForum);
    }
    private void showSafetyRatingOverall(View view) {
        ImageView[] ivSafetyRatingStars;
        View viewSafetyRating;
        if (view != null) {
            ivSafetyRatingStars = new ImageView[]{view.findViewById(R.id.iv_star_1), view.findViewById(R.id.iv_star_2), view.findViewById(R.id.iv_star_3), view.findViewById(R.id.iv_star_4), view.findViewById(R.id.iv_star_5)};
            viewSafetyRating = view.findViewById(R.id.layout_star_rating);
        }else{
            ivSafetyRatingStars = new ImageView[]{getActivity().findViewById(R.id.iv_star_1), getActivity().findViewById(R.id.iv_star_2), getActivity().findViewById(R.id.iv_star_3), getActivity().findViewById(R.id.iv_star_4), getActivity().findViewById(R.id.iv_star_5)};
            viewSafetyRating = getActivity().findViewById(R.id.layout_star_rating);
        }

        for (int starViewIndex = 0; starViewIndex < ivSafetyRatingStars.length; starViewIndex++) {

            ivSafetyRatingStars[starViewIndex].setImageResource(R.drawable.ic_star_full);

            if (starViewIndex + 1 == carSafetySpecs.getStars())
                break;
        }
        viewSafetyRating.setVisibility(View.VISIBLE);
        viewSafetyRating.setOnClickListener(this::onClickShowSafetyRatingsDetailed);
    }
    private void onClickShowEngines(View v) {
        if (dialogEngines == null)
            dialogEngines = new DialogEngines(getContext(), makerName, modelName, chassisShape, yearsRange);

        dialogEngines.show();
    }
    private void onClickShowSafetyRatingsDetailed(View v) {
        if (dialogSafetySpecs == null)
            dialogSafetySpecs = new DialogSafetySpecs(getContext(), carSafetySpecs);

        dialogSafetySpecs.show();
    }

    private void toCarPartsSeller(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.prodajadelova.rs"));
        startActivity(browserIntent);
    }

    private void toUsedCarsSeller(View v) {
        final String URL_ENCODING_FILL = "%5";
        String[] startEndYears = yearsRange.split("-");
        String yearFrom = startEndYears[0].trim();
        String yearTo = startEndYears.length == 2 ? yearsRange.split("-")[1].trim() : "";
        String websiteURL = String.format("https://www.polovniautomobili.com/auto-oglasi/pretraga?brand=%s&model%sB%sD=%s&price_to=&year_from=%s&year_to=%s&chassis%sB%sD=&showOldNew=all&submit_1=&without_price=1", makerName.toLowerCase().replace(" ", "-"), URL_ENCODING_FILL, URL_ENCODING_FILL, modelName.replace(" ", "-").toLowerCase(), yearFrom, yearTo, URL_ENCODING_FILL, URL_ENCODING_FILL);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
        startActivity(browserIntent);
    }

    private void toNewCarsSeller(View v) {
//        TODO change when real Official distributer URL loading is implemented
        String websiteURL = makerName.equals("Mazda") ? "http://www.mazda.rs" : makerName.equals("BMW") ? "http://www.bmw.rs" : "http://www.alfaromeosrbija.rs";

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
        startActivity(browserIntent);
    }
    private void toForum(View v){
        Toast.makeText(getContext(), "Forums are currently unavailable. Please try again later!", Toast.LENGTH_SHORT).show();
//        Todo
    }
    public void onResultReceived(Bitmap result) {
        if (result != null && ivCarModel != null) {
            ivCarModel.setImageBitmap(result);
        }
    }
}