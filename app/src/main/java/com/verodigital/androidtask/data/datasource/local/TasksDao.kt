package com.verodigital.androidtask.data.datasource.local

import android.service.autofill.FieldClassification.Match
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.verodigital.androidtask.data.datasource.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TasksDao {
    @Throws(Exception::class)
    @Query("SELECT * from tasks order by task")
    fun getTasks(): Flow<List<Tasks>>

    @Throws(Exception::class)
    @Query("SELECT * from tasks where task = :task")
    fun getTaskByTaskName(task: String): Flow<List<Tasks>>

    @Throws(Exception::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<Tasks>)

    @Throws(Exception::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tasks: Tasks)

}