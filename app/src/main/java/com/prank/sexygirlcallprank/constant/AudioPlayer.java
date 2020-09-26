package com.prank.sexygirlcallprank.constant;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {
    private MediaPlayer mMediaPlayer;

    public void stop() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void play(Context c, int rid) {
        try {
            stop();
            mMediaPlayer = MediaPlayer.create(c, rid);
            mMediaPlayer.setOnCompletionListener(mediaPlayer -> stop());

            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
