package com.vetpet.petbeats.ui.home_user.activitymain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.VetPet.R
import com.example.VetPet.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupController()
        setupBottomNav()
        hideDestination()
    }

    private fun setupController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHomeUserFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupBottomNav() {
        binding.bottomUserNav.setupWithNavController(navController)
    }

    private fun hideDestination() {
        navController.addOnDestinationChangedListener { controller, destination, bundle ->
            // Fix lỗi không hiện màu ở Bottom Nav
            when (destination.id) {
                R.id.searchFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.bookFragment)?.isChecked = true
                }
                R.id.resultSearchFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.bookFragment)?.isChecked = true
                }
                R.id.informationRoomFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.bookFragment)?.isChecked = true
                }
                R.id.historyListAllFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.bookFragment)?.isChecked = true
                }
                R.id.calendarFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.bookFragment)?.isChecked = true
                }
                R.id.successAppointFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.bookFragment)?.isChecked = true
                }
                R.id.confirmAppointmentFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.bookFragment)?.isChecked = true
                }
                R.id.editCalendarFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.bookFragment)?.isChecked = true
                }
            }
        }
    }
}