package com.testing.tapstar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ActivityHome extends AppCompatActivity
{
    private ImageView ivHome;
    private Button btnPlay;
    private ImageView ivSettings;
    private ImageView ivTitle;
    private int rotate;
    private TapStarDB tapStarDB;
    private AdView avHomeBottom;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_home);

        Shared.transitioning = false;
        Shared.playIsClicked = false;
        Shared.pauseIsClicked = false;
        Shared.resumeIsClicked = false;
        Shared.retryIsClicked = false;
        Shared.settingsIsClicked = false;

        avHomeBottom = findViewById(R.id.avHomeBottom);

        MobileAds.initialize(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        avHomeBottom.loadAd(adRequest);

        ivTitle = findViewById(R.id.ivTitle);
        ivHome = findViewById(R.id.ivHome);
        btnPlay = findViewById(R.id.btnPlay);
        ivSettings = findViewById(R.id.ivSettings);

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params2.topMargin = (int) (Space.getScreenHeight() * 0.30);
        params2.height = (int) (Space.getScreenWidth() * 0.35);
        params2.width = (int) (Space.getScreenWidth() * 0.6);
        params2.addRule(RelativeLayout.CENTER_HORIZONTAL);

        ivTitle.setLayoutParams(params2);
        Glide.with(this).asGif().load(R.drawable.tapstar_title_anim).into(ivTitle);

        Glide.with(this).asGif().load(R.drawable.starysky_anim).into(ivHome);

        tapStarDB = new TapStarDB(this);

        if (tapStarDB.fetch().getCount() == 0)
        {
            tapStarDB.insertData("EASY", "ON", 0);
        }

        Cursor cursor = tapStarDB.fetch();
        Shared.sound = cursor.getString(2);

        Sound.playHomeSoundtrack(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = (int) (Space.getScreenHeight() * 0.72);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        btnPlay.setLayoutParams(params);

        btnPlay.setWidth((int)(Space.getScreenWidth() * 0.3));
        btnPlay.setHeight((int)(Space.getScreenWidth() * 0.15));

        btnPlay.setOnClickListener(view ->
        {
            if (!Shared.playIsClicked)
            {
                Sound.playButtonClick(this);
                Shared.transitioning = true;
                Shared.playIsClicked = true;
                Sound.stopCurrentBGM();
                btnPlay.setBackgroundResource(R.drawable.asset_button_play2);

                final Handler handler = new Handler();
                handler.postDelayed(() ->
                {
                    btnPlay.setBackgroundResource(R.drawable.asset_button_play1);
                    Intent intended = new Intent(ActivityHome.this, ActivityTransition.class);

                    intended.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    Shared.aClass = ActivityGameplay.class;

                    startActivity(intended);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }, 50);
            }
        });

        Glide.with(this).asGif().load(R.drawable.settings_icon_anim).into(ivSettings);
        ivSettings.getLayoutParams().height = (int)(Space.getScreenWidth() * 0.2);
        ivSettings.getLayoutParams().width = (int)(Space.getScreenWidth() * 0.2);

        rotate = 0;
        final Handler handlerSettings = new Handler();
        handlerSettings.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                rotate+=3;
                if (rotate == Integer.MAX_VALUE - 1)
                {
                    rotate = 0;
                }
                ivSettings.setRotation(rotate);
                handlerSettings.postDelayed(this, 50);
            }
        }, 50);

        ivSettings.setOnClickListener(view ->
        {
            if (!Shared.settingsIsClicked)
            {
                Sound.playButtonClick(this);
                Shared.settingsIsClicked = true;
                Shared.transitioning = true;
                Intent intended = new Intent(ActivityHome.this, ActivityTransition.class);

                intended.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                Shared.aClass = ActivitySettings.class;

                startActivity(intended);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (!Shared.transitioning)
        {
            Sound.pauseCurrentBGM();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!Shared.transitioning)
        {
            Sound.startCurrentBGM();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}