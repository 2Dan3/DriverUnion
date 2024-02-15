package com.du.driverunison;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.du.driverunison.model.CarGeneralSpecs;
import com.du.driverunison.util.CarSpecMotorizationRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarDetailedFragment extends Fragment {
//    public static final String LENGTH = "length";
//    public static final String WIDTH = "width";
//    public static final String HEIGHT = "height";
//    public static final String WHEELBASE = "wheelbase";
//    public static final String TRUNK_SIZE = "trunk";
    public static final String MAKER_NAME = "maker_name";
    public static final String MODEL_NAME = "model_name";
    public static final String CHASSIS_SHAPE = "chassis_shape";
    public static final String YEARS_OF_MANUFACTURE_RANGE = "years";
//    private String length;
//    private String width;
//    private String height;
//    private String wheelbase;
//    private String trunkSize;
    private String makerName;
    private String modelName;
    private String chassisShape;
    private String yearsRange;
    private CarSpecMotorizationRecyclerAdapter adapter;
    private CarGeneralSpecs carGeneralSpecs;
    private DialogEngines dialogEngines;

    public CarDetailedFragment() {}

    public static CarDetailedFragment newInstance(/*String carLength, String carWidth, String carWheelbase, String carHeight,*/ String carMakerName, String carModelName, String carChassisShape, String carYearSpanManufactured /*,String carTrunkSize*/) {
        CarDetailedFragment fragment = new CarDetailedFragment();
        Bundle args = new Bundle();
        args.putString(MAKER_NAME, carMakerName);
        args.putString(MODEL_NAME, carModelName);
        args.putString(CHASSIS_SHAPE, carChassisShape);
        args.putString(YEARS_OF_MANUFACTURE_RANGE, carYearSpanManufactured);
//        args.putString(LENGTH, carLength);
//        args.putString(WIDTH, carWidth);
//        args.putString(WHEELBASE, carWheelbase);
//        args.putString(HEIGHT, carHeight);
//        args.putString(TRUNK_SIZE, carTrunkSize);
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
//            this.length = args.getString(LENGTH, "N/A");
//            this.width = args.getString(WIDTH, "N/A");
//            this.wheelbase = args.getString(WHEELBASE, "N/A");
//            this.height = args.getString(HEIGHT, "N/A");
//            this.trunkSize = args.getString(TRUNK_SIZE, "N/A");
            loadModelGeneralSpecs();
        }
    }

    private void setupOnClickListeners(View v) {
        v.findViewById(R.id.btn_show_engines).setOnClickListener(this::makePopup);
//        v.findViewById(R.id.btn_show_info).setOnClickListener(this::makePopup);
        v.findViewById(R.id.btn_show_shopping).setOnClickListener(this::makePopup);
    }

    private void loadModelGeneralSpecs() {
        DatabaseReference fullModelSpecRef = FirebaseDatabase.getInstance("https://driver-union-1753f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cars").child("models").child(makerName).child(modelName).child(chassisShape).child(yearsRange).child("general specs");
        fullModelSpecRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot fullModelSpecSnap) {

//                genYearSpans.add(genYearSpan);
                carGeneralSpecs = fullModelSpecSnap.getValue(CarGeneralSpecs.class);
                setupUI(getView());
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
        setupOnClickListeners(v);

        return v;
    }

    private void setupUI(View view) {
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
                switch (yearsRange) {
                    case "2023-":
                        imgRes = R.mipmap.car_default_filler5;
                        break;
                    case "2019-2023":
                        imgRes = R.mipmap.car_default_filler5_5;
                        break;
                    case "2015-2018":
                        imgRes = R.mipmap.car_default_filler5_2;
                        break;
                    case "2010-2013":
                        imgRes = R.mipmap.car_default_filler5_1;
                        break;
                    default:
                        imgRes = R.mipmap.car_coupe_shape;
                        break;
                }
                break;
            default:
                imgRes = R.mipmap.car_coupe_shape;
        }
        if (view != null) {
            ((ImageView) view.findViewById(R.id.detailed_model_iv)).setImageResource(imgRes);
            ((TextView) view.findViewById(R.id.detailed_model_name)).setText(String.format("%s %s (%s)", this.makerName, this.modelName, this.chassisShape));
            ((TextView) view.findViewById(R.id.detailed_model_years)).setText(this.yearsRange);
            ((TextView) view.findViewById(R.id.detailed_model_length)).setText(carGeneralSpecs.length);
            ((TextView) view.findViewById(R.id.detailed_model_width)).setText(carGeneralSpecs.width);
            ((TextView) view.findViewById(R.id.detailed_model_height)).setText(carGeneralSpecs.height);
            ((TextView) view.findViewById(R.id.detailed_model_wheelbase)).setText(carGeneralSpecs.wheelbase);
            ((TextView) view.findViewById(R.id.detailed_model_trunk)).setText(carGeneralSpecs.trunk);
        }
        else {
            ((ImageView)getActivity().findViewById(R.id.detailed_model_iv)).setImageResource(imgRes);
            ((TextView)getActivity().findViewById(R.id.detailed_model_name)).setText(String.format("%s %s (%s)", this.makerName, this.modelName, this.chassisShape));
            ((TextView)getActivity().findViewById(R.id.detailed_model_years)).setText(this.yearsRange);
            ((TextView)getActivity().findViewById(R.id.detailed_model_length)).setText(carGeneralSpecs.length);
            ((TextView)getActivity().findViewById(R.id.detailed_model_width)).setText(carGeneralSpecs.width);
            ((TextView)getActivity().findViewById(R.id.detailed_model_height)).setText(carGeneralSpecs.height);
            ((TextView)getActivity().findViewById(R.id.detailed_model_wheelbase)).setText(carGeneralSpecs.wheelbase);
            ((TextView)getActivity().findViewById(R.id.detailed_model_trunk)).setText(carGeneralSpecs.trunk);
        }
    }

    private void onClickShowEngines(View v) {
        if (dialogEngines == null)
            dialogEngines = new DialogEngines(getContext(), makerName, modelName, chassisShape, yearsRange);

        dialogEngines.show();
    }
    private void makePopup(View v/* ,Car selectedCar, int modelPosition*/) {
        int menuToBeDisplayedID = 0;
        if (v.getId() == R.id.btn_show_engines)
            menuToBeDisplayedID = R.menu.car_parts_menu;
        else if (v.getId() == R.id.btn_show_info) {
            menuToBeDisplayedID = R.menu.car_info;
        } else if (v.getId() == R.id.btn_show_shopping) {
            menuToBeDisplayedID = R.menu.car_shop;
        }
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(menuToBeDisplayedID, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.item_powertrains)
                        onClickShowEngines(v);
//                    else if (menuItem.getItemId() == R.id.item_problems)
//                        onClickShowCommonProblems(v);
//                    else if (menuItem.getItemId() == R.id.item_discussions)
//                        toForums();
                    else if (menuItem.getItemId() == R.id.item_car_parts)
                        toCarPartsSeller();
                    else if (menuItem.getItemId() == R.id.item_cars_used)
                        toUsedCarsSeller();
                    else if (menuItem.getItemId() == R.id.item_cars_new)
                        toNewCarsSeller();
                    return true;
                }
        );
//        popupMenu.setForceShowIcon(true);
        popupMenu.show();
    }

    private void toCarPartsSeller() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.prodajadelova.rs"));
        startActivity(browserIntent);
    }

    private void toUsedCarsSeller() {
        final String URL_ENCODING_FILL = "%5";
        String[] startEndYears = yearsRange.split("-");
        String yearFrom = startEndYears[0].trim();
        String yearTo = startEndYears.length == 2 ? yearsRange.split("-")[1].trim() : "";
        String websiteURL = String.format("https://www.polovniautomobili.com/auto-oglasi/pretraga?brand=%s&model%sB%sD=%s&price_to=&year_from=%s&year_to=%s&chassis%sB%sD=&showOldNew=all&submit_1=&without_price=1", makerName.toLowerCase(), URL_ENCODING_FILL, URL_ENCODING_FILL, modelName.replace(" ", "-").toLowerCase(), yearFrom, yearTo, URL_ENCODING_FILL, URL_ENCODING_FILL);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
        startActivity(browserIntent);
    }

    private void toNewCarsSeller() {
//        TODO change when real Official distributer URL loading is implemented
        String websiteURL = makerName.equals("Mazda") ? "http://www.mazda.rs" : "http://www.bmw.rs";

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
        startActivity(browserIntent);
    }
}