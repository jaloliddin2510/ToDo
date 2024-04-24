package com.example.todo


import java.io.Serializable

class ToDoModel(
    var task: String,
    var isImportant: Boolean=false,
    val timeTaskSave: Long = System.currentTimeMillis(),
    var isFinished: Boolean=false,
): Serializable {
}