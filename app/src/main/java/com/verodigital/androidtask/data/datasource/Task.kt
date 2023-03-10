package com.verodigital.androidtask.data.datasource

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tasks")
data class Task (

    @PrimaryKey @NonNull @SerializedName("task"          ) var task                               : String,
    @SerializedName("title"                              ) var title                              : String?  = null,
    @SerializedName("description"                        ) var description                        : String?  = null,
    @SerializedName("sort"                               ) var sort                               : String?  = null,
    @SerializedName("wageType"                           ) var wageType                           : String?  = null,
    @SerializedName("BusinessUnitKey"                    ) var BusinessUnitKey                    : String?  = null,
    @SerializedName("businessUnit"                       ) var businessUnit                       : String?  = null,
    @SerializedName("parentTaskID"                       ) var parentTaskID                       : String?  = null,
    @SerializedName("preplanningBoardQuickSelect"        ) var preplanningBoardQuickSelect        : String?  = null,
    @SerializedName("colorCode"                          ) var colorCode                          : String?  = null,
    @SerializedName("workingTime"                        ) var workingTime                        : String?  = null,
    @SerializedName("isAvailableInTimeTrackingKioskMode" ) var isAvailableInTimeTrackingKioskMode : Boolean? = null



)
