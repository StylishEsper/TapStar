package com.testing.tapstar;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.util.concurrent.ThreadLocalRandom;

public class Heart
{
    private ImageView ivHeart;
    private int width;
    private int height;
    private float weight;
    private int x;
    private int y;
    private boolean goRight;
    private boolean destroyed;
    private int increment;
    private Context context;

    public ImageView getImage()
    {
        return this.ivHeart;
    }

    public boolean isDestroyed()
    {
        return this.destroyed;
    }

    public Heart(Context context, float weight)
    {
        this.context = context;
        ivHeart = new ImageView(context);

        try
        {
            Glide.with(context).asBitmap().load(R.drawable.asset_heart).into(ivHeart);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        height = (int)(Space.getScreenWidth() * 0.08);
        width = (int)(Space.getScreenWidth() * 0.09);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width, height);
        ivHeart.setLayoutParams(lp);

        this.weight = weight;

        x = ThreadLocalRandom.current().nextInt(0, Space.getScreenWidth() - width);
        y = -height;

        int leftOrRight = (int)Math.round(Math.random());

        if (leftOrRight == 1)
        {
            goRight = true;
        }
        else
        {
            goRight = false;
        }

        ivHeart.setTranslationX(x);
        ivHeart.setTranslationY(y);

        destroyed = false;
    }

    public void fall()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!Shared.pauseIsClicked && !destroyed)
                {
                    y += weight;

                    if (goRight)
                    {
                        x += 10;
                    }
                    else
                    {
                        x -= 10;
                    }

                    ivHeart.setTranslationY(y);
                    ivHeart.setTranslationX(x);
                    handler.postDelayed(this,0);
                }
                else
                {
                    if (!destroyed)
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
                if (ivHeart.getTranslationX() <= 0)
                {
                    goRight = true;
                }
                else if (ivHeart.getTranslationX() >= Space.getScreenWidth() - width)
                {
                    goRight = false;
                }

                if (Space.collision(ivHeart, object))
                {
                    broken(true);
                }
                else
                {
                    handler.postDelayed(this, 0);
                }
            }
        }, 0);
    }

    public void broken(boolean isBroken)
    {
        destroyed = true;

        increment = 0;

        if (isBroken)
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if (ivHeart.getVisibility() == View.VISIBLE)
                    {
                        ivHeart.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        ivHeart.setVisibility(View.VISIBLE);
                    }

                    if (increment != 10)
                    {
                        handler.postDelayed(this, 50);
                    }
                    else
                    {
                        ivHeart.setVisibility(View.GONE);
                    }

                    increment++;
                }
            }, 50);
        }
        else
        {
            Glide.with(context).asBitmap().load(R.drawable.asset_plus_heart).into(ivHeart);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    ivHeart.setTranslationY(ivHeart.getTranslationY() - 10);

                    if (increment != 10)
                    {
                        handler.postDelayed(this, 50);
                    }
                    else
                    {
                        ivHeart.setVisibility(View.GONE);
                    }

                    increment++;
                }
            }, 50);
        }
    }
}
