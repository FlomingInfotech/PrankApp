package com.prank.sexygirlcallprank.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Facing;
import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.constant.AudioPlayer;
import com.prank.sexygirlcallprank.constant.ConstantMethod;
import com.prank.sexygirlcallprank.helper.Prefs;

import de.hdodenhof.circleimageview.CircleImageView;


public class ExoPlayerActivity extends AppCompatActivity implements Player.EventListener, View.OnClickListener {
    private AdView adView;
    PlayerView videoFullScreenPlayer;
    String celebName;
    SimpleExoPlayer player;
    DataSource.Factory dataSourceFactory;
    Handler mHandler;
    Handler mHandlerAudio;
    int videoUri = R.raw.vid1, celebAvtar = R.drawable.vid1;
    Runnable mRunnable;
    RelativeLayout relativeMain;
    CameraView cameraViewOne, cameraViewTwo, cameraViewTwoOreo;
    private boolean isMute = true;
    private AudioPlayer audioPlayer;
    ImageView imgSwitchCamera, imgDismiss, imgMute;
    private boolean isPreview = true;
    private TextView textViewName;
    private LinearLayout bannerContainer;
    private CircleImageView imageViewPerson;
    private CardView cardViewFrontCamera, cardViewFrontCameraOreo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        try {
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    videoUri = R.raw.vid1;
                    celebAvtar = R.drawable.vid1;
                    celebName = "Lara";
                } else {
                    videoUri = extras.getInt(ConstantMethod.KEY_VIDEO_URI);
                    celebAvtar = extras.getInt(ConstantMethod.KEY_VIDEO_AVTAR);
                    celebName = extras.getString(ConstantMethod.KEY_VIDEO_NAME);
                }
            } else {
                videoUri = (int) savedInstanceState.getSerializable(ConstantMethod.KEY_VIDEO_URI);
                celebAvtar = (int) savedInstanceState.getSerializable(ConstantMethod.KEY_VIDEO_AVTAR);
                celebName = (String) savedInstanceState.getSerializable(ConstantMethod.KEY_VIDEO_NAME);
            }


            mHandler = new Handler(Looper.getMainLooper());
            mHandlerAudio = new Handler(Looper.getMainLooper());
            audioPlayer = new AudioPlayer();

            changeStatusBarColor();
            initView();
            setupData();
            showBannerAd();


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

    private void initView() {
        cardViewFrontCamera = findViewById(R.id.cardViewFrontCamera);
        textViewName = findViewById(R.id.textViewName);
        imageViewPerson = findViewById(R.id.imageViewPerson);
        videoFullScreenPlayer = findViewById(R.id.videoFullScreenPlayer);
        cameraViewOne = findViewById(R.id.cameraViewOne);
        cameraViewTwo = findViewById(R.id.cameraViewTwo);
        cardViewFrontCameraOreo = findViewById(R.id.cardViewFrontCameraOreo);
        cameraViewTwoOreo = findViewById(R.id.cameraViewTwoOreo);
        relativeMain = findViewById(R.id.relativeMain);
        bannerContainer = findViewById(R.id.bannerContainer);
        imgSwitchCamera = findViewById(R.id.imgSwitchCamera);
        imgDismiss = findViewById(R.id.imgDismiss);
        imgMute = findViewById(R.id.imgMute);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        imgDismiss.setOnClickListener(this);
        imgMute.setOnClickListener(this);
        imgSwitchCamera.setOnClickListener(this);
    }

    private void setupData() {
        try {
            textViewName.setText(celebName);
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .priority(Priority.HIGH);
            Glide.with(this)
                    .load(celebAvtar)
                    .apply(options)
                    .thumbnail(0.9f)
                    .error(R.drawable.user_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPerson);
            mHandlerAudio.postDelayed(() -> audioPlayer.play(ExoPlayerActivity.this, R.raw.ring), 500);
            mHandler.postDelayed(this::setUp
                    , 5000);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isPreview) {
            cameraViewOne.open();
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                cameraViewTwoOreo.open();
            } else {
                cameraViewTwo.open();
            }
        }

    }


    private void setUp() {
        initializePlayer();

        buildMediaSource(videoUri);
    }

    private void buildMediaSource(int mUri) {
        try {
            MediaSource firstSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(RawResourceDataSource.buildRawResourceUri(mUri));
            player.setPlayWhenReady(true);
            player.prepare(firstSource);
            player.addListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializePlayer() {
        try {
            if (player == null) {
                videoFullScreenPlayer.hideController();
                player = ExoPlayerFactory.newSimpleInstance(this);

                String playerInfo = Util.getUserAgent(this, "simpleExoPlayer");
                dataSourceFactory = new DefaultDataSourceFactory(
                        this, playerInfo
                );

                videoFullScreenPlayer.setPlayer(player);
                videoFullScreenPlayer.hideController();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();

        }
    }

    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();

        }
    }

    @Override
    protected void onPause() {
        try {
            audioPlayer.stop();

            if (mRunnable != null) {
                mHandler.removeCallbacks(mRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pausePlayer();


        super.onPause();
        cameraViewOne.close();
        cameraViewTwo.close();
        cameraViewTwoOreo.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        resumePlayer();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }


        releasePlayer();
        super.onDestroy();
        cameraViewOne.destroy();
        cameraViewTwo.destroy();
        cameraViewTwoOreo.destroy();
    }


    @Override
    public void onBackPressed() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            finish();
            pausePlayer();
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
            }
            if (mHandlerAudio != null) {
                mHandlerAudio.removeCallbacksAndMessages(null);
            }
            Animatoo.animateSlideRight(ExoPlayerActivity.this);
        }, 100);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {

            case Player.STATE_BUFFERING:
                break;
            case Player.STATE_ENDED:
                player.seekTo(0);
                player.setPlayWhenReady(true);
                break;
            case Player.STATE_IDLE:

                break;
            case Player.STATE_READY:
                cameraViewOne.close();
                cameraViewOne.destroy();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    cardViewFrontCameraOreo.setVisibility(View.VISIBLE);
                    cardViewFrontCamera.setVisibility(View.GONE);
                    cameraViewTwoOreo.open();

                } else {
                    cardViewFrontCameraOreo.setVisibility(View.GONE);
                    cardViewFrontCamera.setVisibility(View.VISIBLE);
                    cameraViewTwo.open();

                }
                videoFullScreenPlayer.setVisibility(View.VISIBLE);
                isPreview = false;
                audioPlayer.stop();
                imgMute.setClickable(true);
                break;
        }

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgDismiss:
                onBackPressed();
                break;
            case R.id.imgSwitchCamera:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    if (isPreview) {
                        if (cameraViewOne != null) {
                            if (cameraViewOne.getFacing() == Facing.FRONT) {
                                cameraViewOne.setFacing(Facing.BACK);
                            } else {
                                cameraViewOne.setFacing(Facing.FRONT);
                            }
                        }
                    } else {
                        if (cameraViewTwoOreo != null) {
                            if (cameraViewTwoOreo.getFacing() == Facing.FRONT) {
                                cameraViewTwoOreo.setFacing(Facing.BACK);
                            } else {
                                cameraViewTwoOreo.setFacing(Facing.FRONT);
                            }
                        }
                    }
                } else {
                    if (isPreview) {
                        if (cameraViewOne != null) {
                            if (cameraViewOne.getFacing() == Facing.FRONT) {
                                cameraViewOne.setFacing(Facing.BACK);
                            } else {
                                cameraViewOne.setFacing(Facing.FRONT);
                            }
                        }
                    } else {
                        if (cameraViewTwo != null) {
                            if (cameraViewTwo.getFacing() == Facing.FRONT) {
                                cameraViewTwo.setFacing(Facing.BACK);
                            } else {
                                cameraViewTwo.setFacing(Facing.FRONT);
                            }
                        }
                    }
                }
                break;
            case R.id.imgMute:

                if (isMute) {
                    setMute(true);
                    isMute = false;
                } else {
                    setMute(false);
                    isMute = true;
                }
                break;
        }
    }

    private void showBannerAd() {
        adView = new AdView(ExoPlayerActivity.this, Prefs.getString(ConstantMethod.BANNER_ID, getString(R.string.Ad_Banner)), AdSize.BANNER_HEIGHT_50);
        bannerContainer.addView(adView);
        adView.loadAd(adView.buildLoadAdConfig().build());

    }


    public void setMute(boolean toMute) {

        if (toMute) {
            ConstantMethod.setImageBackground(this, R.drawable.ic_mute, imgMute);
            if (player != null) {
                player.setVolume(0f);
            }
        } else {
            ConstantMethod.setImageBackground(this, R.drawable.ic_un_mute, imgMute);
            if (player != null) {
                player.setVolume(1f);
            }

        }
    }


}

