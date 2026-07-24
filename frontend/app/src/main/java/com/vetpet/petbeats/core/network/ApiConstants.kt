package com.vetpet.petbeats.core.network

object ApiConstants {

    //Servers
    const val BASE_URL = "https://hit-comemyway-api.onrender.com/"


    //Authentication
    const val LOGOUT = "api/v1/auth/logout"
    const val RESETOTP = "/api/v1/auth/verify-otp"
    const val REGISTEROTP = "/api/v1/auth/verify-register"
    const val RESETPASSWORD = "/api/v1/auth/reset-password"
    const val REGISTER = "api/v1/auth/register"
    const val REFRESH = "/api/v1/auth/refresh"
    const val LOGIN = "api/v1/auth/login"
    const val FORGOTPASSWORD = "/api/v1/auth/forgot-password"


    //AppointmentChild
    const val CREATAPPOINTMENT = "/api/v1/user/appointments"
    const val EDITAPPOINTMENT = "/api/v1/user/appointments/update/{appointmentId}"
    const val CANCELAPPOINTMENT = "/api/v1/user/appointments/cancel/{appointmentId}"
    const val TAKEAPPOINTMENTID = "/api/v1/user/appointments/detail/{id}"
    const val TAKEAPPOINTMENTIDCLINIC = "/api/v1/clinic/appointments/detail/{id}"



    //ClinicController
    const val UPDATEPROFILE = "/api/v1/clinic/update-profile"
    const val COMPLETEPROFILE = "/api/v1/clinic/complete-profile"
    const val CHANGEPASSWORD = "/api/v1/clinic/change-password"
    const val REJECTAPPOINTMENTPOST = "/api/v1/clinic/appointments/{appointmentId}/reject"
    const val CONFIRMAPPOINTMENTPOST = "/api/v1/clinic/appointments/{appointmentId}/confirm"
    const val PENDINGAPPOINTMENTGET = "/api/v1/clinic/appointments/pending"
    const val REJECTAPPOINTMENTGET = "/api/v1/clinic/appointments/rejected"
    const val CONFIRMAPPOINTMENTGET = "/api/v1/clinic/appointments/comfirmed"


    //SearchClinicController
    const val LOCATION = "/api/v1/public/suggestions/location"
    const val CLINICID = "/api/v1/public/clinics/{clinicId}"
    const val TAKEBOOKING = "/api/v1/public/clinics/{clinicId}/booking"
    const val SEARCH = "/api/v1/public/clinics/search"


    //User
    const val FIREBASE = "/api/v1/user/device-token"


    //Media
    const val UPLOAD = "/api/v1/media/upload"

}