package com.example.taptap.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taptap.dto.Todo

// CRUD 작성
@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dto: Todo)

    // 모든 항목 불러옴
    @Query("select * from todoTable")
    fun list(): LiveData<MutableList<Todo>> // LiveData를 사용해 추가/수정/삭제에 의해 변화하는 값에 대해 즉시 반영이 가능하도록 한다.

    @Query("select * from todoTable where id = (:id)")
    fun selectOne(id: Long): Todo

    @Update
    suspend fun update(dto: Todo)

    @Delete
    fun delete(dto: Todo)
}