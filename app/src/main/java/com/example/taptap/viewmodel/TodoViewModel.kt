package com.example.taptap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taptap.dto.Todo
import com.example.taptap.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ViewModel : 액티비티의 라이프사이클과 별개로 돌아가기 때문에 데이터의 유지 및 공유가 가능
// ViewModel에서 CRUD를 사용해 액티비티의 이동이 있어도 동일하게 값을 불러올 수 있도록 함
class TodoViewModel: ViewModel() {
    val todoList: LiveData<MutableList<Todo>>
    private val todoRepository: TodoRepository = TodoRepository.get()

    init {
        todoList = todoRepository.list()
    }

    fun getOne(id: Long) = todoRepository.getTodo(id)

    fun insert(dto: Todo) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.insert(dto)
    }

    fun update(dto: Todo) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.update(dto)
    }

    fun delete(dto: Todo) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.delete(dto)
    }
}