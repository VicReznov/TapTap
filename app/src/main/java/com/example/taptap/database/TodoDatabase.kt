package com.example.taptap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taptap.dao.TodoDao
import com.example.taptap.dto.Todo

@Database(entities = arrayOf(Todo::class), version = 1)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}