package com.xpayback.usersquicklist.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.xpayback.usersquicklist.R
import com.xpayback.usersquicklist.databinding.ActivityUsersBinding
import com.xpayback.usersquicklist.utils.InternetConnectivityUtils

// Main activity for the application, hosting the navigation component
class UsersActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityUsersBinding
    val internetConnectivityUtils by lazy { InternetConnectivityUtils(this) }

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment_content_new)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar and navigation
        setSupportActionBar(binding.toolbar)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        internetCheckup()
    }

    private fun internetCheckup() {
        internetConnectivityUtils.observe(this) {
            Log.e("TAG", "onCreate: $it")
            if (it) {
                // Internet is available
                binding.clMain.visibility = View.GONE
                binding.container.root.visibility = View.VISIBLE
            } else {
                // Internet is not available
                binding.clMain.visibility = View.VISIBLE
                binding.container.root.visibility = View.INVISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_new)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}