package com.du.driverunison;

import static com.du.driverunison.CarDetailedActivity.CHASSIS_SHAPE;
import static com.du.driverunison.CarDetailedActivity.MAKER_NAME;
import static com.du.driverunison.CarDetailedActivity.MODEL_NAME;
import static com.du.driverunison.CarDetailedActivity.TARGET_YEARS_OF_MANUFACTURE_RANGE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CarImageFragment extends Fragment {
    private static final String IMAGE_NUMBER = "image_num";
    public static final String[] IMAGE_ANGLES = {"", "_rear", "_interior", "_boot"};
    private String makerName;
    private String modelName;
    private String chassisShape;
    private String yearsRange;
    private int imageNum;
    private ImageView ivCarModel;

    public CarImageFragment() {}
    public static CarImageFragment newInstance(String carMaker, String carModel, String carChassis, String carYearsRange, int imageNum) {
        CarImageFragment fragment = new CarImageFragment();
        Bundle args = new Bundle();
        args.putString(MAKER_NAME, carMaker);
        args.putString(MODEL_NAME, carModel);
        args.putString(CHASSIS_SHAPE, carChassis);
        args.putString(TARGET_YEARS_OF_MANUFACTURE_RANGE, carYearsRange);
        args.putInt(IMAGE_NUMBER, imageNum);
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
            this.yearsRange = args.getString(TARGET_YEARS_OF_MANUFACTURE_RANGE, "N/A");
            this.imageNum = args.getInt(IMAGE_NUMBER, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car_image, container, false);

        ivCarModel = v.findViewById(R.id.iv_car_angles);
        loadModelImage();

        return v;
    }

    private void loadModelImage() {
//        new FetchImageTask(this).execute(makerName, modelName, chassisShape, yearsRange);

    StorageReference storageRef = FirebaseStorage.getInstance("gs://driver-union-1753f.appspot.com").getReference();
    storageRef.child(makerName).child(String.format("%s_%s_%s_%s%s.png", makerName, modelName, chassisShape, yearsRange, IMAGE_ANGLES[imageNum])).getBytes(Long.MAX_VALUE).addOnSuccessListener(
            bytes -> onResultReceived(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
    }
    public void onResultReceived(Bitmap result) {
        if (result != null && ivCarModel != null) {
            ivCarModel.setImageBitmap(result);
        }
    }
}