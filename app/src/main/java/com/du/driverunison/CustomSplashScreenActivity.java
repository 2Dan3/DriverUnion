package com.du.driverunison;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.window.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

//  todo
//      public class CustomSplashScreenActivity extends AppCompatActivity implements IThemeHandler, ICallbackCarrier {
public class CustomSplashScreenActivity extends AppCompatActivity {

    public static final int SPLASH_TIMEOUT = 50;
    private Intent iMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setupTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        iMainActivity = new Intent(CustomSplashScreenActivity.this, MainActivity.class);

//        if (sharedPreferences.contains(USERNAME)) {
//
//            String savedUsernameOrEmail = sharedPreferences.getString(USERNAME, "");
//            String savedPassword = sharedPreferences.getString(PASSWORD, "");
//
//            FirebaseUser foundPlayer = AuthHandler.Login.execute(savedUsernameOrEmail, savedPassword, this, this, false);
//        }else{
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(iMainActivity);
                    finish();
                }
            }, SPLASH_TIMEOUT);
//        }
    }

//    @Override
//    public void onResult(FirebaseUser foundPlayer) {
//        if (foundPlayer != null) {
////                iMainActivity.putExtra(USERNAME, foundPlayer.getUsername());
////                iMainActivity.putExtra(EMAIL, foundPlayer.getEmail());
////                iMainActivity.putExtra("picture", foundPlayer.getImageURI());
//            Toast.makeText(CustomSplashScreenActivity.this, "Dobro do\u0161li, " + foundPlayer.getDisplayName(), Toast.LENGTH_SHORT).show();
////                Toast.makeText(CustomSplashScreenActivity.this, "Dobro do\u0161li, " + foundPlayer.getUsername(), Toast.LENGTH_SHORT).show();
//        }
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                startActivity(iMainActivity);
//                finish();
//            }
//        }, SPLASH_TIMEOUT);
//    }
}