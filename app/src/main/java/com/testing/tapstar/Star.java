package com.testing.tapstar;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.util.concurrent.ThreadLocalRandom;

public class Star
{
    private ImageView ivStar;
    private int width;
    private int height;
    private float weight;
    private int x;
    private int y;
    private int rotate;
    private boolean destroyed;
    private Context context;
    private int increment;

    public ImageView getImage()
    {
        return this.ivStar;
    }

    public boolean isDestroyed()
    {
        return this.destroyed;
    }

    public Star(Context context, float weight)
    {
        this.context = context;

        ivStar = new ImageView(context);

        try
        {
            Glide.with(context).asGif().load(R.drawable.star_anim_yellow).into(ivStar);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        width = (int)(Space.getScreenWidth() * 0.15);
        height = width;

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width, height);
        ivStar.setLayoutParams(lp);

        this.weight = weight;

        x = ThreadLocalRandom.current().nextInt(0, Space.getScreenWidth() - width);
        y = -height;

        ivStar.setTranslationX(x);
        ivStar.setTranslationY(y);

        rotate = 0;

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
                    rotate+=50;
                    ivStar.setTranslationY(y);
                    ivStar.setRotation(rotate);
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
                if (!Shared.retryIsClicked && !Shared.homeIsClicked)
                {
                    if (Space.collision(ivStar, object))
                    {
                        Sound.playDamaged(context);
                        Shared.hit = true;
                        burst(false);
                    }
                    else
                    {
                        handler.postDelayed(this, 0);
                    }
                }
            }
        }, 0);
    }

    public void burst(boolean clicked)
    {
        destroyed = true;

        if (clicked)
        {
            ivStar.setRotation(0);
            ivStar.getLayoutParams().height = ivStar.getLayoutParams().height / 2;
            ivStar.getLayoutParams().width = ivStar.getLayoutParams().width / 2;
            Glide.with(context).asDrawable().load(R.drawable.asset_plus_one).into(ivStar);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    ivStar.setTranslationY(ivStar.getTranslationY() - 10);

                    if (increment != 10)
                    {
                        handler.postDelayed(this, 50);
                    }
                    else
                    {
                        ivStar.setVisibility(View.GONE);
                    }

                    increment++;
                }
            }, 50);
        }
        else
        {
            ivStar.setVisibility(View.GONE);
        }
    }
}
