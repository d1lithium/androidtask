package com.verodigital.androidtask.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.verodigital.androidtask.data.datasource.Task
import com.verodigital.androidtask.util.DATABASE_NAME

@Database(entities = [Tasks::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase :RoomDatabase() {

    abstract fun tasksDao(): TasksDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}