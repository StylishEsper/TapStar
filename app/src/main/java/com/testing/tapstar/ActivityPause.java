package com.testing.tapstar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityPause extends AppCompatActivity
{
    private Button btnResume;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overlay_paused);

        Shared.transitioning = false;
        Shared.resumeIsClicked = false;

        btnResume = findViewById(R.id.btnResume);
        btnHome = findViewById(R.id.btnHome);

        btnResume.setOnClickListener(view ->
        {
            if (!Shared.resumeIsClicked)
            {
                Sound.playButtonClick(this);
                Shared.transitioning = true;
                Shared.resumeIsClicked = true;
                Shared.pauseIsClicked = false;

                btnResume.setBackgroundResource(R.drawable.asset_button_resume2);

                final Handler handler = new Handler();
                handler.postDelayed(() ->
                {
                    btnResume.setBackgroundResource(R.drawable.asset_button_resume1);
                    finish();

                    overridePendingTransition(0, android.R.anim.fade_out);
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
                Shared.resumeIsClicked = true;
                btnHome.setBackgroundResource(R.drawable.asset_button_home2);

                final Handler handler = new Handler();
                handler.postDelayed(() ->
                {
                    btnHome.setBackgroundResource(R.drawable.asset_button_home1);
                    Intent intended = new Intent(ActivityPause.this, ActivityTransition.class);

                    intended.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    Shared.aClass = ActivityHome.class;

                    startActivity(intended);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
