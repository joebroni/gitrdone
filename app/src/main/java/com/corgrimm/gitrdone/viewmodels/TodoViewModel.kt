package com.corgrimm.gitrdone.viewmodels

import androidx.lifecycle.*
import com.corgrimm.gitrdone.model.Todo
import com.corgrimm.gitrdone.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    val allTodos: LiveData<List<Todo>> = repository.allTodos.asLiveData()

    fun insert(todoName: String) {
        viewModelScope.launch {
            repository.insert(Todo(name = todoName, isDone = false))
        }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
}
