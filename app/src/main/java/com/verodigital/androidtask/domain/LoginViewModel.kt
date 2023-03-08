package com.verodigital.androidtask.domain

import androidx.lifecycle.ViewModel
import com.verodigital.androidtask.data.datasource.remote.api.LoginResponse
import com.verodigital.androidtask.data.repository.LoginRepository
import com.verodigital.androidtask.data.repository.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {

    suspend fun getLoginDetails(): Flow<LoginResponse> {
        return loginRepository.getLoginDetails()
    }



}