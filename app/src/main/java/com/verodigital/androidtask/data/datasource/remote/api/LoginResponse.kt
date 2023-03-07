package com.verodigital.androidtask.data.datasource.remote.api

import com.google.gson.annotations.SerializedName

// To parse the JSON, install Klaxon and do:
//
//   val welcome2 = Welcome2.fromJson(jsonString)



data class LoginResponse (
    val oauth: Oauth,
    val userInfo: UserInfo,
    val permissions: List<String>,
    val apiVersion: String,
    val showPasswordPrompt: Boolean
) {

}

data class Oauth (
    @field:SerializedName("access_token")
    val accessToken: String,

    @field:SerializedName("expires_in")
    val expiresIn: Long,

    @field:SerializedName("token_type")
    val tokenType: String,

    val scope: Any? = null,

    @field:SerializedName("refresh_token")
    val refreshToken: String
)

data class UserInfo (
    val personalNo: Long,
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val active: Boolean,
    val businessUnit: String
)

