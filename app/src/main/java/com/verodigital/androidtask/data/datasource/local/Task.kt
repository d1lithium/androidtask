package com.verodigital.androidtask.data.datasource.local

import com.google.gson.annotations.SerializedName

data class Task (

    @SerializedName("task"                               ) var task                               : String?  = null,
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
