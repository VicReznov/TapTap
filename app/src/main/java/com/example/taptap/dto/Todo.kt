package com.example.taptap.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todoTable") // 테이블 이름
class Todo(
    // 컬럼에 들어갈 이름
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0, // 기본키, id값 자동 증가
    @ColumnInfo(name = "title") val title: String, // 제목
    @ColumnInfo(name = "content") val content: String, // 내용
    @ColumnInfo(name = "timestamp") val timestamp: String, // 생성/수정 날짜
    @ColumnInfo(name = "isChecked") var isChecked: Boolean // 체크박스(할 일 완료) 여부
    ): Serializable { // Intent에 객체를 담기 위해 Serializable을 상속받음
    }