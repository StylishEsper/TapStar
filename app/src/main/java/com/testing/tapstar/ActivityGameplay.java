package com.testing.tapstar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.Objects;

public class ActivityGameplay extends AppCompatActivity
{
    private ImageView floor;
    private ImageView ivCity;
    private ImageView ivLife1;
    private ImageView ivLife2;
    private ImageView ivLife3;
    private Button btnPause;
    private TextView tvScore;
    private TapStarDB tapStarDB;
    private int starWeight;
    private int bitWeight;
    private int heartWeight;
    private int incFloor;
    private int alpha;
    private long score;
    private long spawnTime;
    private String difficulty;
    private boolean backwards;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_gameplay);

        Shared.transitioning = false;

        Sound.playGameplaySoundtrack(this);

        Shared.pauseIsClicked = false;
        Shared.homeIsClicked = false;
        Shared.retryIsClicked = false;

        tvScore = findViewById(R.id.tvScore);
        ivCity = findViewById(R.id.givCity);
        ivLife1 = findViewById(R.id.life1);
        ivLife2 = findViewById(R.id.life2);
        ivLife3 = findViewById(R.id.life3);
        btnPause = findViewById(R.id.btnPause);
        floor = findViewById(R.id.floor2);

        Glide.with(this).asGif().load(R.drawable.city_anim).into(ivCity);

        double lifeHeight = Space.getScreenWidth() * 0.07;
        double lifeWidth = Space.getScreenWidth() * 0.08;

        ivLife1.getLayoutParams().height = (int)(lifeHeight);
        ivLife1.getLayoutParams().width = (int)(lifeWidth);

        ivLife2.getLayoutParams().height = (int)(lifeHeight);
        ivLife2.getLayoutParams().width = (int)(lifeWidth);

        ivLife2.setTranslationX((float)(ivLife2.getTranslationX() - lifeWidth - 20));

        ivLife3.getLayoutParams().height = (int)(lifeHeight);
        ivLife3.getLayoutParams().width = (int)(lifeWidth);

        ivLife3.setTranslationX((float)((ivLife3.getTranslationX() - lifeWidth - 20) * 2));

        tapStarDB = new TapStarDB(this);
        Cursor cursor = tapStarDB.fetch();
        difficulty = cursor.getString(1);

        starWeight = 25;
        heartWeight = 25;
        bitWeight = 15;

        if (Space.getScreenHeight() >= 500 && Space.getScreenHeight() <= 1500)
        {
            starWeight = 12;
            heartWeight = 10;
            bitWeight = 8;
        }

        if (Space.getScreenHeight() >= 1500 && Space.getScreenHeight() <= 2500)
        {
            starWeight = 18;
            heartWeight = 15;
            bitWeight = 12;
        }

        if (Space.getScreenHeight() >= 2500 && Space.getScreenHeight() <= 3500)
        {
            starWeight = 25;
            heartWeight = 25;
            bitWeight = 15;
        }

        if (Objects.equals(difficulty, "EASY"))
        {
            spawnTime = 1000;
        }
        else if (Objects.equals(difficulty, "HARD"))
        {
            starWeight = starWeight + 10;
            spawnTime = 650;
        }
        else
        {
            starWeight = starWeight + 5;
            spawnTime = 800;
        }

        btnPause.setHeight((int)(Space.getScreenWidth() * 0.1));
        btnPause.setWidth((int)(Space.getScreenWidth() * 0.2));

        btnPause.setOnClickListener(view ->
        {
            if (!Shared.pauseIsClicked)
            {
                Sound.playButtonClick(this);
                Shared.transitioning = true;
                Shared.pauseIsClicked = true;
                btnPause.setBackgroundResource(R.drawable.asset_button_pause2);

                final Handler handler = new Handler();
                handler.postDelayed(() ->
                {
                    btnPause.setBackgroundResource(R.drawable.asset_button_pause1);
                    Intent intended = new Intent(ActivityGameplay.this, ActivityPause.class);

                    startActivity(intended);
                    overridePendingTransition(0, android.R.anim.fade_out);
                }, 50);
            }
        });

        incFloor = 0;
        alpha = 255;
        backwards = false;

        final Handler handlerFloor = new Handler();
        handlerFloor.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!Shared.pauseIsClicked)
                {
                    floor.getBackground().setAlpha(alpha);

                    if (alpha == 255)
                    {
                        backwards = true;
                    }
                    else if (alpha == 0)
                    {
                        backwards = false;
                    }

                    if (backwards)
                    {
                        alpha -= 15;
                    }
                    else
                    {
                        alpha += 15;
                    }

                    if (incFloor == 5)
                    {
                        incFloor++;
                        startGame();
                        hitCheck();
                    }
                    else if (incFloor < 5)
                    {
                        incFloor++;
                    }
                }

                if (!Shared.homeIsClicked)
                {
                    handlerFloor.postDelayed(this, 40);
                }
            }
        }, 1000);
    }

    public void startGame()
    {
        final Handler starSpawner = new Handler();
        starSpawner.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!Shared.pauseIsClicked)
                {
                    spawnStar();
                }

                if (!Shared.homeIsClicked)
                {
                    starSpawner.postDelayed(this, spawnTime);
                }
            }
        }, spawnTime);

        final Handler heartSpawner = new Handler();
        heartSpawner.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!Shared.pauseIsClicked)
                {
                    spawnHeart();
                }

                if (!Shared.homeIsClicked)
                {
                    heartSpawner.postDelayed(this, 20000);
                }
            }
        }, 20000);
    }

    public Star spawnStar()
    {
        Star star = new Star(this, starWeight);
        addContentView(star.getImage(), star.getImage().getLayoutParams());
        star.fall();
        star.checkCollision(floor);

        star.getImage().setOnClickListener(view ->
        {
            if (!star.isDestroyed())
            {
                Sound.playStarBurst(this);
                star.burst(true);
                score++;
                tvScore.setText("SCORE: " + score);

                for (int i = 0; i < 4; i++)
                {
                    StarBit starBit = new StarBit(this, bitWeight, star.getImage());
                    addContentView(starBit.getImage(), starBit.getImage().getLayoutParams());
                    starBit.fall();
                    starBit.checkCollision(floor);
                }
            }
        });

        return star;
    }

    public Heart spawnHeart()
    {
        Heart heart = new Heart(this, heartWeight);
        addContentView(heart.getImage(), heart.getImage().getLayoutParams());
        heart.fall();
        heart.checkCollision(floor);

        heart.getImage().setOnClickListener(view ->
        {
            if (!heart.isDestroyed())
            {
                heart.broken(false);
                heal();
            }
        });

        return heart;
    }

    public void heal()
    {
        Sound.playHeal(this);

        if (ivLife1.getVisibility() == View.VISIBLE && ivLife2.getVisibility() == View.VISIBLE &&
                ivLife3.getVisibility() == View.INVISIBLE)
        {
            ivLife3.setVisibility(View.VISIBLE);
        }
        else if (ivLife1.getVisibility() == View.VISIBLE && ivLife2.getVisibility() == View.INVISIBLE &&
                ivLife3.getVisibility() == View.INVISIBLE)
        {
            ivLife2.setVisibility(View.VISIBLE);
        }
    }

    public void hitCheck()
    {
        final Handler hitChecker = new Handler();
        hitChecker.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!Shared.pauseIsClicked)
                {
                    if (Shared.hit)
                    {
                        Shared.hit = false;

                        if (ivLife1.getVisibility() == View.VISIBLE && ivLife2.getVisibility() == View.VISIBLE &&
                                ivLife3.getVisibility() == View.VISIBLE)
                        {
                            ivLife3.setVisibility(View.INVISIBLE);
                        }
                        else if (ivLife1.getVisibility() == View.VISIBLE && ivLife2.getVisibility() == View.VISIBLE &&
                                ivLife3.getVisibility() == View.INVISIBLE)
                        {
                            ivLife2.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            ivLife1.setVisibility(View.INVISIBLE);
                            Shared.score = score;
                            Shared.pauseIsClicked = true;
                            Shared.homeIsClicked = true;
                            gameOver();
                        }
                    }
                }

                if (!Shared.homeIsClicked)
                {
                    hitChecker.postDelayed(this, 0);
                }
            }
        }, 0);
    }

    public void gameOver()
    {
        Intent intended = new Intent(ActivityGameplay.this, ActivityGameOver.class);

        Shared.TextViewTranslation(tvScore, 0, -700, 0, 0, 800L, 0, false);

        startActivity(intended);
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @Override
    protected void onPause()
    {
        if (!Shared.transitioning)
        {
            Sound.pauseCurrentBGM();
            Shared.pauseIsClicked = true;
        }
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!Shared.transitioning)
        {
            Shared.pauseIsClicked = false;
            Sound.startCurrentBGM();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (!Shared.pauseIsClicked)
        {
            Shared.pauseIsClicked = true;
            Intent intended = new Intent(ActivityGameplay.this, ActivityPause.class);

            startActivity(intended);

            overridePendingTransition(0, android.R.anim.fade_out);
        }

        Toast.makeText(this, "Tap once more to exit.", Toast.LENGTH_SHORT).show();
    }
}
