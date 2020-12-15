package com.prank.sexygirlcallprank.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.adapter.ChatAdapter;
import com.prank.sexygirlcallprank.constant.ConstantMethod;
import com.prank.sexygirlcallprank.helper.Prefs;
import com.prank.sexygirlcallprank.model.ChatOne;

import java.util.ArrayList;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    InterstitialAdListener interstitialAdListener;
    private KProgressHUD hud;
    public String[] messageList =
            {"how are you?", "what?", "can I call you?", "what is your number?", "What is Your Name?", "I love You", "where are your from?",
                    "Your favourite place?", "Nope", "You have girlfriends?", "You know my number?", "you want to my number?", "No", "Yes",
                    "Hi handsome, how are you today?", "Hi boy, good to see you here", "Hi, how are you?", "Hi, how is your day?",
                    " Good morning handsome!", "Hi,I'm glad that you found me!", "I really want to have sax with you, let's meet now in my apartment!",
                    "I'm a real sax bomb in the bed, try me out!",
                    "I will go buy some condos, meet me on my place in an hour, ok baby?",
                    "Meet me in 2 hours, but remember it is only a sax, nothing more!",
                    "You are so lovely",
                    "yes please",
                    "how are you?"};


    public ImageView profile_image;

    public TextView txtName;

    public String celebName;
    public ListView listViewChat;

    public Button btnSend;
    private TextView online;
    public EditText editMsg;
    public ChatAdapter f17149y;
    int celebAvtar = R.drawable.vid1;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        try {
            loadFullscreenAd();
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    celebAvtar = R.drawable.vid1;
                    celebName = "Lara";
                } else {
                    celebAvtar = extras.getInt(ConstantMethod.KEY_VIDEO_AVTAR);
                    celebName = extras.getString(ConstantMethod.KEY_VIDEO_NAME);
                }
            } else {
                celebAvtar = (int) savedInstanceState.getSerializable(ConstantMethod.KEY_VIDEO_AVTAR);
                celebName = (String) savedInstanceState.getSerializable(ConstantMethod.KEY_VIDEO_NAME);
            }


            txtName = findViewById(R.id.textViewName);
            ImageView imgBack = findViewById(R.id.imgBack);


            listViewChat = findViewById(R.id.listView);
            btnSend = findViewById(R.id.btn_send);
            editMsg = findViewById(R.id.et_message);

            findViewById(R.id.send_message_layout);


            profile_image = findViewById(R.id.profile_image);
            online = findViewById(R.id.online);


            f17149y = new ChatAdapter(ChatActivity.this, new ArrayList<>());
            listViewChat.setAdapter(f17149y);

            btnSend.setOnClickListener(new C4018b());


            imgBack.setOnClickListener(v -> onBackPressed());

            setupProfile();
            changeStatusBarColor();

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

    private void setupProfile() {

        try {
            txtName.setText(celebName);
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .priority(Priority.HIGH);
            Glide.with(this)
                    .load(celebAvtar)
                    .apply(options)
                    .thumbnail(0.9f)
                    .error(R.drawable.user_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class C4018b implements View.OnClickListener {


        public class C4019a implements Runnable {
            C4019a() {
            }

            public void run() {
                int nextInt = new Random().nextInt(messageList.length);
                ChatActivity chatActivity = ChatActivity.this;
                String str = chatActivity.messageList[nextInt];
                chatActivity.mo11668b(str);

            }
        }


        public void onClick(View view) {
            try {

                String obj = editMsg.getText().toString().trim();
                if (!ConstantMethod.isEmpty(obj)) {
                    mo11669c(obj);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> online.setText(R.string.lbl_type), 900);

                    if (count <= 3) {
                        count++;
                        new Handler(Looper.getMainLooper()).postDelayed(() -> online.setText(R.string.lbl_online), 2000);
                        new Handler(Looper.getMainLooper()).postDelayed(new C4019a()
                                , 3000);
                    } else {
                        count = 0;
                        new Handler(Looper.getMainLooper()).postDelayed(() -> online.setText(R.string.lbl_online), 4000);
                        new Handler(Looper.getMainLooper()).postDelayed(new C4019a()
                                , 5000);
                    }
                    editMsg.setText("");
                    ChatActivity chatActivity2 = ChatActivity.this;
                    chatActivity2.listViewChat.setSelection(chatActivity2.f17149y.getCount() - 1);
                    if (count <= 3) {
                        count++;
                    } else {
                        count = 0;

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private void loadFullscreenAd() {

        mInterstitialAd = new InterstitialAd(ChatActivity.this, Prefs.getString(ConstantMethod.INTRESTIAL_ID, getString(R.string.Ad_Interstitial)));
        interstitialAdListener= new InterstitialAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

                hud =  KProgressHUD.create(ChatActivity.this)
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
    public final void mo11669c(String str) {
        f17149y.add(new ChatOne(str, true, false));
    }

    public final void mo11668b(String str) {
        f17149y.add(new ChatOne(str, false, false));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
        }
        super.onDestroy();
    }
}
