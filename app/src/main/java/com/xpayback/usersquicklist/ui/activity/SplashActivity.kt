package com.xpayback.usersquicklist.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.xpayback.usersquicklist.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Launch a coroutine to delay and then start the main activity
        lifecycleScope.launch {
            delay(1500)

            val intent = Intent(this@SplashActivity, UsersActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}