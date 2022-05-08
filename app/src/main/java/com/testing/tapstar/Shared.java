package com.testing.tapstar;

import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Shared
{
    static Class aClass;
    static boolean pauseIsClicked = false;
    static boolean homeIsClicked = false;
    static boolean resumeIsClicked = false;
    static boolean retryIsClicked = false;
    static boolean playIsClicked = false;
    static boolean settingsIsClicked = false;
    static boolean saveIsClicked = false;
    static boolean cancelIsClicked = false;
    static boolean transitioning = false;
    static long score = 0;
    static boolean hit = false;
    static String sound = "ON";

    public static void TextViewTranslation(TextView textView, int fromX, int toX, int fromY, int toY, long duration, int repeat, boolean visible)
    {
        TranslateAnimation r = new TranslateAnimation(fromX, toX, fromY, toY);
        r.setDuration(duration);
        r.setRepeatCount(repeat);
        textView.startAnimation(r);

        if (visible)
        {
            textView.setVisibility(View.VISIBLE);
        }
        else
        {
            textView.setVisibility(View.GONE);
        }
    }

    public static void ImageViewTranslation(ImageView imageView, int fromX, int toX, int fromY, int toY, long duration, int repeat, boolean visible)
    {
        TranslateAnimation r = new TranslateAnimation(fromX, toX, fromY, toY);
        r.setDuration(duration);
        r.setRepeatCount(repeat);
        imageView.startAnimation(r);

        if (visible)
        {
            imageView.setVisibility(View.VISIBLE);
        }
        else
        {
            imageView.setVisibility(View.GONE);
        }
    }
}
