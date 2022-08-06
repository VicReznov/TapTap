package com.example.taptap.config

import android.app.Application
import com.example.taptap.repository.TodoRepository

// 앱 실행될 때 단 한번만 실행되도록 함
// 앱 실행과 동시에 Repository 초기화를 통해 데이터베이스가 없을 경우 새로 빌드하도록 함
// -> Manifests를 수정해줘야함
class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()

        TodoRepository.initilize(this)
    }
}