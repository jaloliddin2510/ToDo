package com.example.todo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.todo.databinding.AddActivityBinding

@Suppress("DEPRECATION")
class AddActivity : AppCompatActivity() {

    private lateinit var binding: AddActivityBinding
    private val toDoList = mutableListOf<ToDoModel>()
    private var index = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
        initData()
    }
    private fun initData() {
        toDoList.clear()
        toDoList.addAll(PreferencesHelper.getToDoList(this))
        val data = intent.getSerializableExtra("edit") as? ToDoModel
        binding.etToDo.setText(data?.task ?: "")
        binding.checkBoxTask.isChecked = data?.isImportant?:false
        index = intent.getIntExtra("index", -1)

    }

    private fun onClick() {
        with(binding) {
            addToolbar.setNavigationOnClickListener {
                finish()
            }
            checkNewData.setOnClickListener {
                if (etToDo.text.isNullOrEmpty() || etToDo.text.isNullOrBlank()) {
                    return@setOnClickListener Toast.makeText(this@AddActivity, "Buni bajarish imkonsiz", Toast.LENGTH_SHORT).show()
                }
                if (isSimilar(toDoList, etToDo.text.toString())) {
                    return@setOnClickListener Toast.makeText(this@AddActivity, "Buni bajarish imkonsiz", Toast.LENGTH_SHORT).show()
                } else {
                    saveToDoList()
                }
            }
        }
    }

    fun isSimilar(list: List<ToDoModel>, title: String): Boolean {
        return list.any { it.task == title }
    }

    private fun saveToDoList() {
        val title = binding.etToDo.text.toString()
        val toDoModel = ToDoModel(title, binding.checkBoxTask.isChecked)
        if (index != -1) {
            toDoList[index] = toDoModel
        } else {
            toDoList.add(toDoModel)
        }
        PreferencesHelper.saveToDo(toDoList, this)
        finish()
    }


}