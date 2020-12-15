package com.prank.sexygirlcallprank.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.constant.ConstantMethod;
import com.prank.sexygirlcallprank.helper.Prefs;

import java.util.Arrays;
import java.util.Collections;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private String name, mono;
    private int imgAvtar, videoUri;
    private ImageView imageProfile;
    private TextView txtName, textViewNumber, txtInterest;
    private InterstitialAd mInterstitialAd;
    private LinearLayout bannerContainer;
    private AdView adView;
    private KProgressHUD hud;
    public String[] intrestList = {"Dating, Sex Chat", "Friendship, Kiss", "Loveship, LoveChat", "Chatting, Sexy Chat",
            "Dating, Dirty Chat", "Dirty Call, Dirty Sex"};

    InterstitialAdListener interstitialAdListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        try {
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    imgAvtar = R.drawable.vid1;
                    mono = "";
                    videoUri = R.raw.vid1;
                    name = "Lara";
                } else {

                    imgAvtar = extras.getInt(ConstantMethod.KEY_VIDEO_AVTAR);
                    videoUri = extras.getInt(ConstantMethod.KEY_VIDEO_URI);
                    mono = extras.getString(ConstantMethod.KEY_NUMBER);
                    name = extras.getString(ConstantMethod.KEY_VIDEO_NAME);
                }
            } else {

                imgAvtar = (int) savedInstanceState.getSerializable(ConstantMethod.KEY_VIDEO_AVTAR);
                videoUri = (int) savedInstanceState.getSerializable(ConstantMethod.KEY_VIDEO_URI);
                name = (String) savedInstanceState.getSerializable(ConstantMethod.KEY_VIDEO_NAME);
                mono = (String) savedInstanceState.getSerializable(ConstantMethod.KEY_NUMBER);
            }
            txtName = findViewById(R.id.textViewName);
            txtInterest = findViewById(R.id.txtInterest);
            ImageView imgBack = findViewById(R.id.imgBack);
            imageProfile = findViewById(R.id.imageProfile);
            LinearLayout linearNumber = findViewById(R.id.linearNumber);
            textViewNumber = findViewById(R.id.textViewNumber);
            Button btnChat = findViewById(R.id.btnChat);
            Button btnCall = findViewById(R.id.btnCall);
            bannerContainer = findViewById(R.id.bannerContainer);
            linearNumber.setOnClickListener(this);
            btnChat.setOnClickListener(this);
            btnCall.setOnClickListener(this);
            imgBack.setOnClickListener(this);
            setupData();
            showBannerAd();
            changeStatusBarColor();
            loadFullscreenAd();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        }
    }

    private void setupData() {

        try {
            Collections.shuffle(Arrays.asList(intrestList));
            for (String s : intrestList) {
                txtInterest.setText(s);
            }
            txtName.setText(name);
            textViewNumber.setText(mono);
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .priority(Priority.HIGH);
            Glide.with(this)
                    .load(imgAvtar)
                    .apply(options)
                    .thumbnail(0.9f)
                    .error(R.drawable.user_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearNumber:
            case R.id.textViewNumber:
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("number", textViewNumber.getText().toString());
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                    }
                    Toast.makeText(this, "Number Copied", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btnChat:
                if (ConstantMethod.isConnected(this)!=0) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.destroy();
                    }
                    Intent mIntent = new Intent(ProfileActivity.this, ChatActivity.class);
                    mIntent.putExtra(ConstantMethod.KEY_VIDEO_URI, videoUri);
                    mIntent.putExtra(ConstantMethod.KEY_VIDEO_AVTAR, imgAvtar);
                    mIntent.putExtra(ConstantMethod.KEY_VIDEO_NAME, name);
                    startActivity(mIntent);

                } else {
                    Toast.makeText(ProfileActivity.this, String.format("%s", getString(R.string.txt_no_internet)), Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.btnCall:
                if (ConstantMethod.isConnected(this)!=0) {
                    Intent intent = new Intent(ProfileActivity.this, ExoPlayerActivity.class);
                    intent.putExtra(ConstantMethod.KEY_VIDEO_URI, videoUri);
                    intent.putExtra(ConstantMethod.KEY_VIDEO_AVTAR, imgAvtar);
                    intent.putExtra(ConstantMethod.KEY_VIDEO_NAME, name);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProfileActivity.this, String.format("%s", getString(R.string.txt_no_internet)), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    private void loadFullscreenAd() {

        mInterstitialAd = new InterstitialAd(ProfileActivity.this, Prefs.getString(ConstantMethod.INTRESTIAL_ID, getString(R.string.Ad_Interstitial)));
        interstitialAdListener= new InterstitialAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
              hud =  KProgressHUD.create(ProfileActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Showing Ads...")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hud.dismiss();
                        showFullScreenAds();
                    }
                }, 1500);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }
        };
        mInterstitialAd.loadAd(
                mInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    public void showFullScreenAds() {
        if (mInterstitialAd != null) {
            if (mInterstitialAd.isAdLoaded()) {
                mInterstitialAd.show();
            }
        }else {
          loadFullscreenAd();
        }
    }

    private void showBannerAd() {
        adView = new AdView(this, Prefs.getString(ConstantMethod.BANNER_ID, getString(R.string.Ad_Banner)), AdSize.BANNER_HEIGHT_50);
        bannerContainer.addView(adView);
        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }

        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
        }

        super.onDestroy();
    }
}
