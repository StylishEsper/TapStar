package com.testing.tapstar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityTransition extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_transition);

        final Handler handler = new Handler();
        handler.postDelayed(() ->
        {
            Intent intended;

            if (Shared.aClass == null)
            {
                intended = new Intent(ActivityTransition.this, ActivityHome.class);
            }
            else
            {
                intended = new Intent(ActivityTransition.this, Shared.aClass);
            }

            intended.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intended);
            overridePendingTransition(0, android.R.anim.fade_out);
            finish();
        }, 1000);
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        finishActivity(0);
    }
}
