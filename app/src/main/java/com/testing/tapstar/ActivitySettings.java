package com.testing.tapstar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.Objects;

public class ActivitySettings extends AppCompatActivity
{
    private ImageView ivSettingsBG;
    private ImageView ivEasy;
    private ImageView ivMedium;
    private ImageView ivHard;
    private ImageView ivSoundOff;
    private ImageView ivSoundOn;
    private TextView tvHighScore;
    private Button btnSave;
    private Button btnCancel;
    private String difficulty;
    private String sound;
    private String hs;
    private boolean stopBlink;
    private boolean clickable;
    private AdView avSettingsBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_settings);

        Shared.transitioning = false;

        avSettingsBottom = findViewById(R.id.avSettingsBottom);

        AdRequest adRequest = new AdRequest.Builder().build();
        avSettingsBottom.loadAd(adRequest);

        tvHighScore = findViewById(R.id.tvHighScore);
        ivSettingsBG = findViewById(R.id.ivSettingsBG);
        btnSave = findViewById(R.id.btnSave);
        ivEasy = findViewById(R.id.ivEasyText);
        ivMedium = findViewById(R.id.ivMediumText);
        ivHard = findViewById(R.id.ivHardText);
        ivSoundOff = findViewById(R.id.ivSoundOff);
        ivSoundOn = findViewById(R.id.ivSoundOn);
        btnCancel = findViewById(R.id.btnCancel);

        TapStarDB tapStarDB = new TapStarDB(this);
        Cursor cursor;

        cursor = tapStarDB.fetch();
        difficulty = cursor.getString(1);
        sound = cursor.getString(2);
        hs = cursor.getString(3);

        Shared.sound = sound;

        Shared.saveIsClicked = false;
        Shared.cancelIsClicked = false;
        stopBlink = false;
        clickable = true;

        tvHighScore.setText(hs);

        Glide.with(this).asGif().load(R.drawable.starysky_anim).into(ivSettingsBG);

        btnSave.setOnClickListener(view ->
        {
            if (!Shared.saveIsClicked)
            {
                Sound.playButtonClick(this);
                Sound.stopCurrentBGM();
                stopBlink = true;
                clickable = false;
                Shared.transitioning = true;
                Shared.saveIsClicked = true;
                Shared.cancelIsClicked = true;
                btnSave.setBackgroundResource(R.drawable.asset_button_save2);
                tapStarDB.updateData(difficulty, sound, Integer.parseInt(hs));

                final Handler handler = new Handler();
                handler.postDelayed(() ->
                {
                    btnSave.setBackgroundResource(R.drawable.asset_button_save1);
                    Intent intended = new Intent(ActivitySettings.this, ActivityTransition.class);

                    intended.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    Shared.aClass = ActivityHome.class;

                    startActivity(intended);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }, 50);
            }
        });

        btnCancel.setOnClickListener(view ->
        {
            if (!Shared.cancelIsClicked)
            {
                Sound.playButtonClick(this);
                stopBlink = true;
                clickable = false;
                Shared.transitioning = true;
                Shared.cancelIsClicked = true;
                Shared.saveIsClicked = true;
                btnCancel.setBackgroundResource(R.drawable.asset_button_cancel2);

                final Handler handler = new Handler();
                handler.postDelayed(() ->
                {
                    btnCancel.setBackgroundResource(R.drawable.asset_button_cancel1);
                    Intent intended = new Intent(ActivitySettings.this, ActivityTransition.class);

                    intended.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    Shared.aClass = ActivityHome.class;

                    startActivity(intended);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }, 50);
            }
        });

        Blink(difficulty);

        if (Objects.equals(sound, "ON"))
        {
            ivSoundOn.setImageResource(R.drawable.asset_sound_on2);
        }
        else if (Objects.equals(sound, "OFF"))
        {
            ivSoundOff.setImageResource(R.drawable.asset_sound_off2);
        }

        ivEasy.setOnClickListener(view ->
        {
            if (clickable)
            {
                Sound.playButtonClick(this);
                difficulty = "EASY";
            }
        });

        ivMedium.setOnClickListener(view ->
        {
            if (clickable)
            {
                Sound.playButtonClick(this);
                difficulty = "MEDIUM";
            }
        });

        ivHard.setOnClickListener(view ->
        {
            if (clickable)
            {
                Sound.playButtonClick(this);
                difficulty = "HARD";
            }
        });

        ivSoundOff.setOnClickListener(view ->
        {
            if (clickable)
            {
                Sound.playButtonClick(this);
                sound = "OFF";
                ivSoundOff.setImageResource(R.drawable.asset_sound_off2);
                ivSoundOn.setImageResource(R.drawable.asset_sound_on);
            }
        });

        ivSoundOn.setOnClickListener(view ->
        {
            if (clickable)
            {
                Sound.playButtonClick(this);
                sound = "ON";
                ivSoundOn.setImageResource(R.drawable.asset_sound_on2);
                ivSoundOff.setImageResource(R.drawable.asset_sound_off);
            }
        });
    }

    public void Blink(String localDiff)
    {
        final Handler handler = new Handler();
        handler.postDelayed(() ->
        {
            ImageView image;

            if (Objects.equals(localDiff, "EASY"))
            {
                image = ivEasy;
                ivMedium.setVisibility(View.VISIBLE);
                ivHard.setVisibility(View.VISIBLE);
            }
            else if (Objects.equals(localDiff, "MEDIUM"))
            {
                image = ivMedium;
                ivEasy.setVisibility(View.VISIBLE);
                ivHard.setVisibility(View.VISIBLE);
            }
            else
            {
                image = ivHard;
                ivEasy.setVisibility(View.VISIBLE);
                ivMedium.setVisibility(View.VISIBLE);
            }

            if (image.getVisibility() == View.VISIBLE)
            {
                image.setVisibility(View.INVISIBLE);
            }
            else
            {
                image.setVisibility(View.VISIBLE);
            }

            if (!stopBlink)
            {
                Blink(difficulty);
            }
        }, 200);
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
