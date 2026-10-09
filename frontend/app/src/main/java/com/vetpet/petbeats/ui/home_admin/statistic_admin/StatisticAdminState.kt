package com.vetpet.petbeats.ui.home_admin.statistic_admin

data class StatisticAdminState (
    val activeClinics: Int = 0,
    val inactiveClinics: Int = 0,
    val totalUsers: Int = 0,


    val isRealtimeConnected: Boolean = false,
    val errorMessage: String = ""
)