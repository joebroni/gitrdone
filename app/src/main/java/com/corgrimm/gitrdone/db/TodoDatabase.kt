package com.corgrimm.gitrdone.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.corgrimm.gitrdone.model.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
