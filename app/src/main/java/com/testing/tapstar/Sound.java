package com.testing.tapstar;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound
{
    private static MediaPlayer currentBGM;
    private static MediaPlayer mp;

    public static void stopCurrentBGM()
    {
        if (Shared.sound.equals("ON"))
        {
            currentBGM.stop();
        }
    }

    public static void startCurrentBGM()
    {
        if (Shared.sound.equals("ON"))
        {
            currentBGM.start();
        }
    }

    public static void pauseCurrentBGM()
    {
        if (Shared.sound.equals("ON"))
        {
            currentBGM.pause();
        }
    }

    public static boolean getIsPlaying()
    {
        return currentBGM.isPlaying();
    }

    public static void playHomeSoundtrack(Context context)
    {
        if (Shared.sound.equals("ON"))
        {
            mp = MediaPlayer.create(context, R.raw.soundtrack_home);

            if (currentBGM != null)
            {
                if (!currentBGM.isPlaying())
                {
                    currentBGM = mp;
                    currentBGM.setLooping(true);
                    currentBGM.start();
                }
            }
            else
            {
                currentBGM = mp;
                currentBGM.setLooping(true);
                currentBGM.start();
            }
        }
    }

    public static void playGameplaySoundtrack(Context context)
    {
        if (Shared.sound.equals("ON"))
        {
            if (currentBGM != null)
            {
                if (!currentBGM.isPlaying())
                {
                    mp = MediaPlayer.create(context, R.raw.soundtrack_gameplay);
                    currentBGM = mp;
                    currentBGM.setLooping(true);
                    currentBGM.start();
                }
            }
        }
    }

    public static void playButtonClick(Context context)
    {
        if (Shared.sound.equals("ON"))
        {
            mp = MediaPlayer.create(context, R.raw.sound_button_click);
            mp.start();
            completion();
        }
    }

    public static void playHeal(Context context)
    {
        if (Shared.sound.equals("ON"))
        {
            mp = MediaPlayer.create(context, R.raw.sound_heal);
            mp.start();
            completion();
        }
    }

    public static void playGameOver(Context context)
    {
        if (Shared.sound.equals("ON"))
        {
            mp = MediaPlayer.create(context, R.raw.sound_gameover);
            mp.start();
            completion();
        }
    }

    public static void playDamaged(Context context)
    {
        if (Shared.sound.equals("ON"))
        {
            mp = MediaPlayer.create(context, R.raw.sound_damaged);
            mp.start();
            completion();
        }
    }

    public static void playStarBurst(Context context)
    {
        if (Shared.sound.equals("ON"))
        {
            mp = MediaPlayer.create(context, R.raw.sound_starburst);
            mp.start();
            completion();
        }
    }

    public static void completion()
    {
        mp.setOnCompletionListener(MediaPlayer::release);
    }
}
