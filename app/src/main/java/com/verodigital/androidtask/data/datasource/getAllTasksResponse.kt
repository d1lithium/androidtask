package com.verodigital.androidtask.data.datasource

import com.google.gson.annotations.SerializedName
import com.verodigital.androidtask.data.datasource.local.Task

data class getAllTasksResponse(
    @field:SerializedName("") val taskResponse: List<Task>
)
