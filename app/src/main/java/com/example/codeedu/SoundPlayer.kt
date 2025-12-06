package com.example.codeedu


import android.content.Context
import android.media.MediaPlayer


/***
 * Sound player object, easier to implmenet across the other files I created*/
object SoundPlayer {
    private var backgroundMusicPlayer: MediaPlayer? = null

    fun playBackgroundMusic(context: Context) {
        if (backgroundMusicPlayer == null) {
            backgroundMusicPlayer = MediaPlayer.create(context, R.raw.background_music)
            backgroundMusicPlayer?.isLooping = false
            backgroundMusicPlayer?.start()
        }
    }

    fun playSuccessSound(context: Context) {
        MediaPlayer.create(context, R.raw.success_sound)?.start()
    }

}
