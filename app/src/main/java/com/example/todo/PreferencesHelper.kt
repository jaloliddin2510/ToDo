package com.example.todo

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object PreferencesHelper {
    private var sharedPrefs: SharedPreferences? = null
    private val gson by lazy { Gson() }


    private fun getSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(TODO_KEY, Context.MODE_PRIVATE)
    }

    fun saveToDo(toDoList: List<ToDoModel>, context: Context): Unit? {
        return getSharedPrefs(context).edit()?.putString(TODO_KEY, gson.toJson(toDoList))?.apply()
    }

    fun getToDoList(context: Context): List<ToDoModel> {
        return if (!getSharedPrefs(context).getString(TODO_KEY, "").isNullOrEmpty()) {
            println("${getSharedPrefs(context).getString(TODO_KEY, "")}")
            gson.fromJson(getSharedPrefs(context).getString(TODO_KEY, ""),
                object : TypeToken<List<ToDoModel>>() {}.type)
        } else emptyList()

    }

    fun removeAll(context: Context) {
        getSharedPrefs(context).edit().clear().apply()
    }

    private const val TODO_KEY = "todo_Key"
}