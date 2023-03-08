package com.verodigital.androidtask.data.repository

import com.verodigital.androidtask.data.datasource.remote.api.LoginResponse
import com.verodigital.androidtask.data.datasource.remote.api.LoginService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginService: LoginService
) {
    val creds: HashMap<String, String> = HashMap()
    suspend fun getLoginDetails(): Flow<LoginResponse> {
        creds.put("username", "365")
        creds.put("password", "1")
        val getLoginResponse = loginService.login(creds)
        return { getLoginResponse }.asFlow()
    }

    suspend fun getAccessToken(): String {
        var authorizationHeader: String = ""
        getLoginDetails().collectLatest { it ->
            authorizationHeader = it.oauth.tokenType + " " + it.oauth.accessToken
        }
        return authorizationHeader
    }

}