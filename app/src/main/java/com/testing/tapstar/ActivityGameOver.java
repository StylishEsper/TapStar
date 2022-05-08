package com.testing.tapstar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class ActivityGameOver extends AppCompatActivity
{
    private Button btnRetry;
    private Button btnHome;
    private TextView tvScore;
    private ImageView ivGameOver;
    private ImageView ivNew;
    private TapStarDB tapStarDB;
    private InterstitialAd iaGameOverFull;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overlay_gameover);

        Shared.retryIsClicked = true;
        Shared.homeIsClicked = true;

        final Handler waitForAd = new Handler();
        waitForAd.postDelayed(() ->
        {
            Shared.retryIsClicked = false;
            Shared.homeIsClicked = false;
        }, 3000);

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-8785133418509207/8694608355", adRequest, new InterstitialAdLoadCallback()
        {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd)
            {
                iaGameOverFull = interstitialAd;
                iaGameOverFull.show(ActivityGameOver.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
            {
                iaGameOverFull = null;
            }
        });

        Sound.playGameOver(this);
        Shared.transitioning = false;
        Shared.hit = false;

        ivNew = findViewById(R.id.ivNew);
        ivGameOver = findViewById(R.id.txtGameOver);
        tvScore = findViewById(R.id.tvGOScore);
        btnRetry = findViewById(R.id.btnRetry);
        btnHome = findViewById(R.id.btnGOHome);

        tapStarDB = new TapStarDB(this);
        Cursor cursor = tapStarDB.fetch();
        String difficulty = cursor.getString(1);
        String sound = cursor.getString(2);
        String hs = cursor.getString(3);

        ivGameOver.getLayoutParams().height = (int)(Space.getScreenHeight()*0.2);
        ivGameOver.getLayoutParams().width = (int)(Space.getScreenWidth()*0.5);

        String textToSet = "SCORE: " + Shared.score;

        tvScore.setText(textToSet);
        tvScore.setVisibility(View.VISIBLE);

        Shared.TextViewTranslation(tvScore, -700, 0, 0, 0, 800L, 0, true);
        Shared.ImageViewTranslation(ivNew, -700, 0, 0, 0, 800L, 0, false);

        if (Shared.score > Integer.parseInt(hs))
        {
            tapStarDB.updateData(difficulty, sound, Shared.score);
            ivNew.setVisibility(View.VISIBLE);
        }

        btnRetry.setOnClickListener(view ->
        {
            if (!Shared.retryIsClicked)
            {
                Sound.playButtonClick(this);
                Shared.transitioning = true;
                Shared.retryIsClicked = true;
                Shared.homeIsClicked = true;
                btnRetry.setBackgroundResource(R.drawable.asset_button_retry2);

                final Handler handler = new Handler();
                handler.postDelayed(() ->
                {
                    btnRetry.setBackgroundResource(R.drawable.asset_button_retry1);
                    Intent intended = new Intent(ActivityGameOver.this, ActivityTransition.class);

                    intended.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    Shared.aClass = ActivityGameplay.class;

                    startActivity(intended);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }, 50);
            }
        });

        btnHome.setOnClickListener(view ->
        {
            if (!Shared.homeIsClicked)
            {
                Sound.playButtonClick(this);
                Sound.stopCurrentBGM();
                Shared.transitioning = true;
                Shared.homeIsClicked = true;
                Shared.retryIsClicked = true;
                btnHome.setBackgroundResource(R.drawable.asset_button_home2);

                final Handler handler = new Handler();
                handler.postDelayed(() ->
                {
                    btnHome.setBackgroundResource(R.drawable.asset_button_home1);
                    Intent intended = new Intent(ActivityGameOver.this, ActivityTransition.class);

                    intended.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    Shared.aClass = ActivityHome.class;

                    startActivity(intended);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 50);
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

    }
}
