package com.corgrimm.gitrdone.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.corgrimm.gitrdone.model.Todo
import com.corgrimm.gitrdone.viewmodels.TodoViewModel

@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    var newTodo by remember { mutableStateOf("") }
    val todoList by viewModel.allTodos.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            TextField(
                value = newTodo,
                onValueChange = { newTodo = it },
                modifier = Modifier.weight(1f),
                label = { Text("New To-do") }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                if (newTodo.isNotEmpty()) {
                    viewModel.insert(newTodo)
                    newTodo = ""
                }
            }) {
                Text("Add")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TodoList(todoList = todoList, onDelete = { viewModel.delete(it) })
    }
}

@Composable
fun TodoList(todoList: List<Todo>, onDelete: (Todo) -> Unit) {
    Column {
        todoList.forEach { todo ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(todo.name)
                IconButton(onClick = { onDelete(todo) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}