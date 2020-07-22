package com.codesroots.mac.cards.presentaion.splash

import androidx.appcompat.app.AppCompatActivity
import com.codesroots.mac.cards.presentaion.MainActivity
import android.content.Intent
import android.view.MotionEvent
import android.media.MediaPlayer
import com.codesroots.mac.cards.R

import android.widget.VideoView
import android.os.Bundle
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import com.codesroots.mac.cards.presentaion.login.LoginActivity


class SplashActivity : AppCompatActivity (){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val videoHolder = VideoView(this)
            setContentView(videoHolder)
            val video = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash)
            videoHolder.setVideoURI(video)
            videoHolder.setOnCompletionListener { jump() }
            videoHolder.start()
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