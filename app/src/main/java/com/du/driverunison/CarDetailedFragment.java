package com.du.driverunison;

import static com.du.driverunison.CarImageFragment.IMAGE_ANGLES;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.du.driverunison.model.CarGeneralSpecs;
import com.du.driverunison.model.CarSafetySpecs;
import com.du.driverunison.util.FetchImageTask;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CarDetailedFragment extends Fragment implements FetchImageTask.FetchImageTaskCallback{
    public static final String MAKER_NAME = "maker_name";
    public static final String MODEL_NAME = "model_name";
    public static final String CHASSIS_SHAPE = "chassis_shape";
    public static final String YEARS_OF_MANUFACTURE_RANGE = "years";
    private static final int IMAGES_TOTAL_PER_CAR = IMAGE_ANGLES.length;
    private String makerName;
    private String modelName;
    private String chassisShape;
    private String yearsRange;
    private ImageView ivCarMaker;
    private ViewPager2 modelImagePager;
    private ViewPager modelDataPager;
    private TabLayout modelDataTabLayout;
    private CarDetailedFragment.ScreenSlidePagerAdapter pagerAdapter;
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
//            loadModelImage();
            loadModelSafetyRatings();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return CarImageFragment.newInstance(makerName, modelName, chassisShape, yearsRange, position);
        }

        @Override
        public int getItemCount() {
            return IMAGES_TOTAL_PER_CAR;
        }
    }

//    private void loadModelImage() {
////        new FetchImageTask(this).execute(makerName, modelName, chassisShape, yearsRange);
//
//        StorageReference storageRef = FirebaseStorage.getInstance("gs://driver-union-1753f.appspot.com").getReference();
//        storageRef.child(makerName).child(String.format("%s_%s_%s_%s.png", makerName, modelName, chassisShape, yearsRange)).getBytes(Long.MAX_VALUE).addOnSuccessListener(
//                bytes -> onResultReceived(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
//    }
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

        setupImages(v);

        setupDataSpecs(v);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI(view);
    }

    private void setupDataSpecs(View v) {
        TabPagerAdapter adapter = new TabPagerAdapter(CarDetailedFragment.this);
        modelDataPager = v.findViewById(R.id.pager_car_data);
        modelDataPager.setAdapter(adapter);
        modelDataTabLayout = v.findViewById(R.id.tab_layout_car_data);

        modelDataTabLayout.setupWithViewPager(modelDataPager);

//        Set up with Tab Icons
        for (int i = 0; i < modelDataTabLayout.getTabCount(); i++) {
            modelDataTabLayout.getTabAt(i).setIcon(adapter.getIcon(i));
        }
    }

    private void setupImages(View v) {
        ivCarMaker = v.findViewById(R.id.iv_distributor_logo);
        ivCarMaker.setAlpha(200);
        loadManufacturerImage();

        // Instantiate a ViewPager2 and a PagerAdapter.
        modelImagePager = v.findViewById(R.id.pager_car_images);
//        Check line:
        pagerAdapter = new ScreenSlidePagerAdapter(getActivity());
        modelImagePager.setAdapter(pagerAdapter);
        modelImagePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

        });
        TabLayout tabLayout = v.findViewById(R.id.tab_layout_car_images);
        TabLayoutMediator tlm = new TabLayoutMediator(tabLayout, modelImagePager, (tab, position) -> {

        });
        tlm.attach();

//        Initially the exterior front of model is displayed (1st image)
        modelImagePager.setCurrentItem(0, false);
    }

    private void setupUI(View view) {
        CardView cardDistributor;

        FloatingActionButton
                cardUsedCars,
                cardCarParts,
//                cardPowertrains,
                cardForum;

//        ivCarModel = view.findViewById(R.id.detailed_model_iv);
        ((TextView) view.findViewById(R.id.detailed_model_name)).setText(String.format("%s %s (%s)", this.makerName, this.modelName, this.chassisShape));
        ((TextView) view.findViewById(R.id.detailed_model_years)).setText(this.yearsRange);
//        ((TextView) view.findViewById(R.id.detailed_model_length)).setText(carGeneralSpecs.length);
//        ((TextView) view.findViewById(R.id.detailed_model_width)).setText(carGeneralSpecs.width);
//        ((TextView) view.findViewById(R.id.detailed_model_height)).setText(carGeneralSpecs.height);
//        ((TextView) view.findViewById(R.id.detailed_model_wheelbase)).setText(carGeneralSpecs.wheelbase);
//        ((TextView) view.findViewById(R.id.detailed_model_trunk)).setText(carGeneralSpecs.trunk);
        cardDistributor = view.findViewById(R.id.card_distributor);
        cardUsedCars = view.findViewById(R.id.card_used_cars);
        cardCarParts = view.findViewById(R.id.card_car_parts);
        cardForum = view.findViewById(R.id.card_forums);
//        cardPowertrains = view.findViewById(R.id.card_powertrains);
        cardDistributor.setOnClickListener(this::toNewCarsSeller);
        cardUsedCars.setOnClickListener(this::toUsedCarsSeller);
        cardCarParts.setOnClickListener(this::toCarPartsSeller);
        cardForum.setOnClickListener(this::toForum);
    }

    private void loadManufacturerImage() {
//        new FetchImageTask(this).execute(makerName);

        StorageReference storageRef = FirebaseStorage.getInstance("gs://driver-union-1753f.appspot.com").getReference();
        storageRef.child(makerName).child(String.format("%s.png", makerName)).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                onResultReceived(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
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
//    private void onClickShowEngines(View v) {
//        if (dialogEngines == null)
//            dialogEngines = new DialogEngines(getContext(), makerName, modelName, chassisShape, yearsRange);
//
//        dialogEngines.show();
//    }
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
        String websiteURL = makerName.equals("Mazda") ? "http://www.mazda.rs" : makerName.equals("BMW") ? "http://www.bmw.rs" : makerName.equals("Alfa Romeo") ? "http://www.alfaromeosrbija.rs" : makerName.equals("Peugeot") ? "http://www.peugeot.rs" : "";

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
        startActivity(browserIntent);
    }
    private void toForum(View v){
        Toast.makeText(getContext(), "Forums are currently unavailable. Please try again later!", Toast.LENGTH_SHORT).show();
//        Todo
    }
    public void onResultReceived(Bitmap result) {
        if (result != null && ivCarMaker != null)
            ivCarMaker.setImageBitmap(result);
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter {
        private final String[] titles = {getString(R.string.vehicle_dimensions), getString(R.string.vehicle_motors)};
        private final int[] iconsRes = {R.drawable.ic_car_dimen, R.drawable.ic_engine_block};
        private CarDetailedFragment fragment;

        public TabPagerAdapter(CarDetailedFragment frag) {
            super(frag.getActivity().getSupportFragmentManager());
            fragment = frag;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return CarDimensionsFragment.newInstance(makerName, modelName, chassisShape, yearsRange);
                case 1:
                    return MotorsFragment.newInstance(makerName, modelName, chassisShape, yearsRange);
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
        public int getIcon(int i) {
            return iconsRes[i];
        }
    }
}