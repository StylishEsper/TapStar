package com.testing.tapstar;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.util.concurrent.ThreadLocalRandom;

public class StarBit
{
    private ImageView ivStarBit;
    private int width;
    private int height;
    private float weight;
    private int x;
    private int y;
    private int rotate;
    private boolean disintegrated;
    private boolean stay;
    private boolean goRight;
    private boolean goUp;
    private float incDown;

    public ImageView getImage()
    {
        return this.ivStarBit;
    }

    public StarBit(Context context, float weight, ImageView parentStar)
    {
        ivStarBit = new ImageView(context);

        Glide.with(context).asGif().load(R.drawable.star_anim_yellow).into(ivStarBit);

        width = (int)(Space.getScreenWidth() * 0.05);
        height = width;

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width, height);
        ivStarBit.setLayoutParams(lp);

        this.weight = weight;
        int force = ThreadLocalRandom.current().nextInt(0, 4);

        if (force == 0)
        {
            incDown = weight;
        }
        else if (force == 1)
        {
            incDown = weight * 1.5F;
        }
        else if (force == 2)
        {
            incDown = weight * 2F;
        }
        else
        {
            incDown = weight * 2.2F;
        }

        x = ThreadLocalRandom.current().nextInt((int)parentStar.getTranslationX(),
                (int)parentStar.getTranslationX() + parentStar.getLayoutParams().width);
        y = ThreadLocalRandom.current().nextInt((int)parentStar.getTranslationY(),
                (int)parentStar.getTranslationY() + parentStar.getLayoutParams().height);

        ivStarBit.setTranslationX(x);
        ivStarBit.setTranslationY(y);

        int leftOrRight = (int)Math.round(Math.random());

        if (leftOrRight == 1)
        {
            goRight = true;
        }
        else
        {
            goRight = false;
        }

        goUp = true;

        rotate = 0;

        disintegrated = false;
        stay = false;
    }

    public void fall()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!Shared.pauseIsClicked && !stay)
                {
                    if (goUp)
                    {
                        y -= incDown;
                    }
                    else
                    {
                        y += incDown;

                        if (incDown < weight)
                        {
                            incDown++;
                        }
                    }

                    if (goUp && incDown > 0)
                    {
                        incDown--;
                    }
                    else
                    {
                        goUp = false;
                    }

                    if (goRight)
                    {
                        x += weight;
                    }
                    else
                    {
                        x -= weight;
                    }

                    rotate+=20;
                    ivStarBit.setTranslationY(y);
                    ivStarBit.setTranslationX(x);
                    ivStarBit.setRotation(rotate);
                    handler.postDelayed(this,0);
                }
                else
                {
                    if (!disintegrated)
                    {
                        handler.postDelayed(this,0);
                    }
                }
            }
        }, 0);
    }

    public void checkCollision(ImageView object)
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (ivStarBit.getTranslationX() <= 0)
                {
                    goRight = true;
                }
                else if (ivStarBit.getTranslationX() >= Space.getScreenWidth() - width)
                {
                    goRight = false;
                }

                if (!Shared.retryIsClicked && !Shared.homeIsClicked)
                {
                    if (Space.collision(ivStarBit, object) && !disintegrated)
                    {
                        stay = true;
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> disintegrate(), 2000);
                    }

                    if (!disintegrated)
                    {
                        handler.postDelayed(this, 0);
                    }
                }
            }
        }, 0);
    }

    public void disintegrate()
    {
        disintegrated = true;
        ivStarBit.setVisibility(View.GONE);
    }
}
