package com.example.taptap.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.taptap.database.TodoDatabase
import com.example.taptap.dto.Todo
import java.lang.IllegalStateException

private const val DATABASE_NAME = "todo-database.db"
class TodoRepository  private constructor(context: Context){

    // 데이터베이스 빌드
    private val database: TodoDatabase = Room.databaseBuilder(
        context.applicationContext,
        TodoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val todoDao = database.todoDao()

    fun list(): LiveData<MutableList<Todo>> = todoDao.list()

    fun getTodo(id: Long): Todo = todoDao.selectOne(id)

    fun insert(dto: Todo) = todoDao.insert(dto)

    suspend fun update(dto: Todo) = todoDao.update(dto)

    fun delete(dto: Todo) = todoDao.delete(dto)

    // 클래스가 생성될 때 메모리에 적재되면서 동시에 생성하는 객체
    // 데이터베이스 생성 및 초기화를 담당
    companion object {
        private var INSTANCE: TodoRepository? = null

        fun initilize(context: Context) {
            if(INSTANCE == null){
                INSTANCE = TodoRepository(context)
            }
        }

        fun get(): TodoRepository {
            return INSTANCE ?:
            throw IllegalStateException("TodoRepository must be initialized")
        }

    }
}