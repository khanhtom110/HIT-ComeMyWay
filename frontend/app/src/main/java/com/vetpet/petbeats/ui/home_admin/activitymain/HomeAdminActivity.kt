package com.vetpet.petbeats.ui.home_admin.activitymain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.ActivityHomeAdminBinding
import com.example.VetPet.databinding.ActivityHomeClinicBinding

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupController()
        setupBottomNav()
        hideDestination()
    }

    private fun setupController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHomeAdminFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupBottomNav() {
        binding.bottomAdminNav.setupWithNavController(navController)
    }

    private fun hideDestination() {
        navController.addOnDestinationChangedListener { controller, destination, bundle ->
            // Fix lỗi không hiện màu ở Bottom Nav
            when (destination.id) {
                R.id.addClinicAdminFragment -> {
                    binding.bottomAdminNav.menu.findItem(R.id.listAppointmentAdminFragment)?.isChecked = true
                }
                R.id.addClinicSuccessAdminFragment -> {
                    binding.bottomAdminNav.menu.findItem(R.id.listAppointmentAdminFragment)?.isChecked = true
                }
            }
        }
    }
}