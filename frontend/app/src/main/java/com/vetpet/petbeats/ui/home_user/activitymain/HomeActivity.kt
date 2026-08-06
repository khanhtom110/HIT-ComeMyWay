package com.vetpet.petbeats.ui.home_user.activitymain

import android.os.Bundle
import android.view.View
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
        changeColorDestination()
//        hideDestination()
    }

    private fun setupController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHomeUserFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupBottomNav() {
        binding.bottomUserNav.setupWithNavController(navController)
    }

    private fun changeColorDestination() {
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



                R.id.editInformationFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.settingUserFragment)?.isChecked = true
                }
                R.id.editPasswordFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.settingUserFragment)?.isChecked = true
                }



                R.id.locketFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.splashLocketFragment)?.isChecked = true
                }
                R.id.listFriendLocketFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.splashLocketFragment)?.isChecked = true
                }
                R.id.imageMeLocketFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.splashLocketFragment)?.isChecked = true
                }
                R.id.imageEverybodyLocketFragment -> {
                    binding.bottomUserNav.menu.findItem(R.id.splashLocketFragment)?.isChecked = true
                }

            }
        }
    }

    private fun hideDestination() {
        //4.Dặn dò ẩn/hiện thanh điều hướng tùy theo màn hình
        navController.addOnDestinationChangedListener { controller, destination, bundle ->
            if (
                destination.id == R.id.editForgotpasswordSettingFragment ||
                destination.id == R.id.editOtpSettingFragment ||
                destination.id == R.id.editResetPasswordSettingFragment ||
                destination.id == R.id.editPasswordSuccessSettingFragment) {
                binding.bottomUserNav.visibility = View.GONE // Nếu đích đến là màn hình Máy tính -> Giấu thanh điều hướng đi
            }
            else {
                binding.bottomUserNav.visibility = View.VISIBLE // Nếu là các màn hình khác (Home, Blog) -> Hiện thanh điều hướng lên
            }
        }
    }
}