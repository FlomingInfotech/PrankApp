package com.prank.sexygirlcallprank.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.constant.ConstantMethod;
import com.prank.sexygirlcallprank.helper.Prefs;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    private ImageView imageView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }
                });
        imageView = findViewById(R.id.imageView);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        changeStatusBarColor();
        setupConst();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        }
    }


    private void setupData() {
        if (ConstantMethod.isConnected(this) != 0) {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            final HashMap<String, Object> defaultMap = new HashMap<>();
            defaultMap.put(ConstantMethod.ADMOB_ID, getResources().getString(R.string.ADMOB_ID));
            defaultMap.put(ConstantMethod.INTRESTIAL_ID, getResources().getString(R.string.Ad_Interstitial));
            defaultMap.put(ConstantMethod.BANNER_ID, getResources().getString(R.string.Ad_Banner));


            mFirebaseRemoteConfig.setDefaultsAsync(defaultMap);
            checkService();

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
            alertDialogBuilder.setTitle("No Internet Connection");
            alertDialogBuilder
                    .setMessage("Please connect internet and try again")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setupData();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    private void checkService() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(SplashActivity.this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            setupConst();
                        } else {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
    }

    private void setupConst() {
        Prefs.putString(ConstantMethod.BANNER_ID, getString(R.string.Ad_Banner));
        Prefs.putString(ConstantMethod.INTRESTIAL_ID, getString(R.string.Ad_Interstitial));
        new Handler().postDelayed(() -> {

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }, 3000);

    }
}
