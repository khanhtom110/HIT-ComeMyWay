package com.vetpet.petbeats.data.repository.repository_nodejs

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.data.remote.api.api_nodejs.ApiAuthAdmin
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeAdminRepository @Inject constructor(
    private val apiAuthAdmin: ApiAuthAdmin
): BaseRepository()  {

}