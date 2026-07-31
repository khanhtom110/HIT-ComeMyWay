package com.vetpet.petbeats.ui.home_clinic.activitymain

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.ActivityHomeBinding
import com.example.VetPet.databinding.ActivityHomeClinicBinding

class HomeClinicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeClinicBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupController()
        setupBottomNav()
        hideDestination()
    }

    private fun setupController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHomeClinicFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupBottomNav() {
        binding.bottomClinicNav.setupWithNavController(navController)
    }

    private fun hideDestination() {
        navController.addOnDestinationChangedListener { controller, destination, bundle ->
            // Fix lỗi không hiện màu ở Bottom Nav
            when (destination.id) {
                R.id.scheduleListFragment -> {
                    binding.bottomClinicNav.menu.findItem(R.id.appointmentScheduleFragment)?.isChecked = true
                }
                R.id.appointmentDetailWait -> {
                    binding.bottomClinicNav.menu.findItem(R.id.appointmentScheduleFragment)?.isChecked = true
                }
                R.id.appointmentDetailReceive -> {
                    binding.bottomClinicNav.menu.findItem(R.id.appointmentScheduleFragment)?.isChecked = true
                }
                R.id.appointmentDetailRefuse -> {
                    binding.bottomClinicNav.menu.findItem(R.id.appointmentScheduleFragment)?.isChecked = true
                }

            }
        }
    }
}
