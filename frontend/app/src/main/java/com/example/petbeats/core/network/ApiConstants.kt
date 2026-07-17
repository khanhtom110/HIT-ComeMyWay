package com.example.petbeats.core.network

object ApiConstants {

    //Auth
    const val BASE_URL = "https://hit-comemyway-api.onrender.com/"
    const val LOGIN = "api/v1/auth/login"
    const val REGISTER = "api/v1/auth/register"
    const val LOGOUT = "api/v1/auth/logout"
    const val FORGOTPASSWORD = "/api/v1/auth/forgot-password"
    const val RESETOTP = "/api/v1/auth/verify-otp"
    const val REGISTEROTP = "/api/v1/auth/verify-register"
    const val RESETPASSWORD = "/api/v1/auth/reset-password"
    const val REFRESH = "/api/v1/auth/refresh"


    //Search
    const val LOCATION = "/api/v1/public/suggestions/location"
    const val CLINICID = "/api/v1/public/clinics/{clinicId}"
    const val SEARCH = "/api/v1/public/clinics/search"


    //Calendar
    const val CREATAPPOINTMENT = "/api/v1/user/appointments"
    const val EDITAPPOINTMENT = "/api/v1/user/appointments/update/{appointmentId}"
    const val TAKEBOOKING = "/api/v1/public/clinics/{clinicId}/booking"
    const val TAKEAPPOINTMENTID = "/api/v1/user/appointments/detail/{id}"
    const val CANCELAPPOINTMENT = "/api/v1/user/appointments/cancel/{appointmentId}"

    //Notify
    const val FIREBASE = "/api/v1/user/device-token"

}