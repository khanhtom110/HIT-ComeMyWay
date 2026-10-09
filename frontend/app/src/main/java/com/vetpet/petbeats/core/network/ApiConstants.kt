package com.vetpet.petbeats.core.network

object ApiConstants {

    //Servers
    const val SPRING_BASE_URL = "http://56.10.115.197:8080"
    const val NODE_BASE_URL = "http://56.10.115.197:3000/"


    //Admin
    const val CREATECLINIC = "/api/v1/admin/create-clinic"


    //Authentication
    const val LOGOUT = "/api/v1/user/logout"
    const val RESETOTP = "/api/v1/auth/verify-otp"
    const val REGISTEROTP = "/api/v1/auth/verify-register"
    const val RESETPASSWORD = "/api/v1/auth/reset-password"
    const val REGISTER = "/api/v1/auth/register"
    const val REFRESH = "/api/v1/auth/refresh"
    const val LOGIN = "/api/v1/auth/login"
    const val FORGOTPASSWORD = "/api/v1/auth/forgot-password"


    //AppointmentChild
    const val CREATAPPOINTMENT = "/api/v1/user/appointments"
    const val EDITAPPOINTMENT = "/api/v1/user/appointments/update/{appointmentId}"
    const val CANCELAPPOINTMENT = "/api/v1/user/appointments/cancel/{appointmentId}"
    const val TAKEAPPOINTMENTID = "/api/v1/user/appointments/detail/{id}"
    const val TAKEAPPOINTMENTIDCLINIC = "/api/v1/clinic/appointments/detail/{id}"


    //Pet Locket
    const val DELETEPOSTLOCKET = "/api/v1/user/locket/delete-post/{postId}"
    const val CREATEPOSTLOCKET = "/api/v1/user/locket/create-post"
    const val GETMYLOCKET = "/api/v1/user/locket/my-posts"
    const val GETFEEDSLOCKET = "/api/v1/user/locket/feed"


    //Friendship
    const val REJECTFRIENDLOCKET = "/api/v1/user/locket/reject-request/{requestId}"
    const val SENDFRIENDLOCKET = "/api/v1/user/locket/add-friend"
    const val ACCEPTFRIENDLOCKET = "/api/v1/user/locket/accept-request/{requestId}"
    const val GETPENDINGFRIENDLOCKET = "/api/v1/user/locket/pending"
    const val GETMYFRIENDLOCKET = "/api/v1/user/locket/my-friend"
    const val FINDFRIEND = "/api/v1/user/locket/find-friend/{locketLink}"
    const val UNFRIEND = "/api/v1/user/locket/unfriend/{friendId}"


    //Clinic Posts
    const val CLINICPOST = "/api/v1/public/clinic-posts"
    const val CLINICPOSTDETAIL = "/api/v1/public/clinic-posts/{id}"


    //ClinicController
    const val UPDATEPROFILE = "/api/v1/clinic/update-profile"
    const val COMPLETEPROFILE = "/api/v1/clinic/complete-profile"
    const val CHANGEPASSWORD = "/api/v1/clinic/change-password"
    const val REJECTAPPOINTMENTPOST = "/api/v1/clinic/appointments/{appointmentId}/reject"
    const val CONFIRMAPPOINTMENTPOST = "/api/v1/clinic/appointments/{appointmentId}/confirm"
    const val PENDINGAPPOINTMENTGET = "/api/v1/clinic/appointments/pending"
    const val REJECTAPPOINTMENTGET = "/api/v1/clinic/appointments/rejected"
    const val CONFIRMAPPOINTMENTGET = "/api/v1/clinic/appointments/comfirmed"
    const val GETPROFILE = "/api/v1/clinic/profile"


    //SearchClinicController
    const val LOCATION = "/api/v1/public/suggestions/location"
    const val CLINICID = "/api/v1/public/clinics/{clinicId}"
    const val TAKEBOOKING = "/api/v1/public/clinics/{clinicId}/booking"
    const val SEARCH = "/api/v1/public/clinics/search"


    //User
    const val FIREBASE = "/api/v1/user/device-token"
    const val LOCKETLINK = "/api/v1/user/locket-link"


    //Media
    const val UPDATEPROFILEUSER = "/api/v1/user/update-profile/{id}"
    const val UPLOAD = "/api/v1/media/upload"
    const val PROFILEUSER = "/api/v1/user/profile"
    const val CHANGEPASSWORDUSER = "/api/v1/user/change-password"


    //Chatbot
    const val CHATBOT = "/api/v1/chat"


    //Health
    const val HEALTH = "/health"
    const val LIVE = "/health/live"
    const val READY = "/health/ready"


    //Clinic posts
    const val CLINICPOSTIDNODEJS = "/api/v1/public/clinic-posts/{id}"
    const val CLINICPOSTNODEJS = "/api/v1/clinic/posts"
    const val CLINICDELETE = "/api/v1/clinic/posts/{id}"
    const val CLINICPOSTGLOBAL = "/api/v1/public/clinic-posts"


    //Admin
    const val CLINICPOSTADMIN = "/api/v1/admin/clinic-posts"
    const val APPROVEADMIN = "/api/v1/admin/clinic-posts/{id}/approve"
    const val STATISTICADMIN = "/api/v1/admin/statistics"
}