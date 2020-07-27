package com.codesroots.mac.cards.presentaion.splash

import androidx.appcompat.app.AppCompatActivity
import com.codesroots.mac.cards.presentaion.MainActivity
import android.content.Intent
import android.view.MotionEvent
import android.media.MediaPlayer
import com.codesroots.mac.cards.R

import android.widget.VideoView
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.view.WindowManager
import com.codesroots.mac.cards.presentaion.login.LoginActivity
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View


class SplashActivity : AppCompatActivity (){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);

        try {
            val video = findViewById<View>(R.id.videoView) as VideoView
            video.setVideoPath("android.resource://" + packageName + "/" + R.raw.splash);

            video.setOnCompletionListener { jump() }
            video.start()
        } catch (ex: Exception) {
            jump()
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        jump()
        return true
    }

    private fun jump() {
        if (isFinishing)
            return
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


}