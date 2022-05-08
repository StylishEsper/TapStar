package com.testing.tapstar;

import android.content.res.Resources;
import android.graphics.Rect;
import android.widget.ImageView;

public class Space
{
    public static int getScreenWidth()
    {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight()
    {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static boolean collision(ImageView object1, ImageView object2)
    {
        Rect object1Rect = new Rect();
        object1.getHitRect(object1Rect);

        Rect object2Rect = new Rect();
        object2.getHitRect(object2Rect);

        if (Rect.intersects(object1Rect, object2Rect))
        {
            return true;
        }
        else return false;
    }
}
