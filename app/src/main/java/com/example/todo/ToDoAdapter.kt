package com.example.todo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ItemTodoBinding


@Suppress("DEPRECATION")
class ToDoAdapter(
    private val onClick: (position: Int, isFinish: Boolean) -> Unit,
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    var onLongClick: ((toDoModel: ToDoModel) -> Unit)? = null
    val dataList = mutableListOf<ToDoModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadData(newList: List<ToDoModel>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }

    fun swapOnMoved(fromPosition: Int, toPosition: Int, context: Context) {
        val currentData = dataList[fromPosition]
        val targetData = dataList[toPosition]
        dataList[fromPosition] = targetData
        dataList[toPosition] = currentData
        PreferencesHelper.saveToDo(dataList, context)
    }

    fun swapOnRemove(position: Int, context: Context) {
        dataList.removeAt(position)
        PreferencesHelper.saveToDo(dataList, context)
    }

    fun search(text: String?, context: Context) {
        text?.let {
            PreferencesHelper.getToDoList(context).toMutableList()
                .filter { it.task.lowercase().startsWith(text.lowercase()) }.let {
                loadData(it)
            }
        } ?: loadData(PreferencesHelper.getToDoList(context))
    }


    inner class ToDoViewHolder(
        private val binding: ItemTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoModel: ToDoModel) {
            binding.tvCheckBox.text = toDoModel.task
            binding.tvCheckBox.isChecked = toDoModel.isFinished
            binding.ivImportant.visibility =
                if (toDoModel.isImportant) View.VISIBLE else View.INVISIBLE
            binding.llFinish.visibility = if (toDoModel.isFinished) View.VISIBLE else View.INVISIBLE
            binding.tvCheckBox.setOnClickListener {

            }
            binding.root.setOnLongClickListener {
                onLongClick?.invoke(toDoModel)
                true
            }
        }

    }


    fun notifyItemDataChanged(position: Int, context: Context, isChecked: Boolean) {
        dataList[position].isImportant = isChecked
        PreferencesHelper.saveToDo(dataList, context)
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
            ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(dataList[position])
    }


}