package com.example.todo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.AddActivityBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@Suppress("DEPRECATION")
class RvHelper(
    private val toDoAdapter: ToDoAdapter,
    private val context: Context,
    private val listener: Listener
) : ItemTouchHelper.SimpleCallback(

    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
) {
    private lateinit var binding: AddActivityBinding
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val accessOnMove = ItemTouchHelper.DOWN or ItemTouchHelper.UP
        val accessOnSwipeRight = ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        return makeMovementFlags(accessOnMove, accessOnSwipeRight)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        toDoAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        toDoAdapter.swapOnMoved(viewHolder.adapterPosition, target.adapterPosition, context)
        return true
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        when (direction) {
            ItemTouchHelper.LEFT -> {
                toDoAdapter.swapOnRemove(viewHolder.adapterPosition, context)
                toDoAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
            ItemTouchHelper.RIGHT ->{
                val position = viewHolder.adapterPosition
                val import=PreferencesHelper.getToDoList(context)[position]
                listener.listener1(position)
                toDoAdapter.notifyItemChanged(position)
            }
        }
    }
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val recyclerViewSwipeDecorator = RecyclerViewSwipeDecorator.Builder(
            c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
        )

        recyclerViewSwipeDecorator
            .addSwipeLeftBackgroundColor(Color.RED)
            .addSwipeLeftActionIcon(R.drawable.delete_svgrepo_com)
            .create()
            .decorate()
        val recyclerViewSwipeDecorator1 = RecyclerViewSwipeDecorator.Builder(
            c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
        )

        recyclerViewSwipeDecorator1
            .addSwipeRightBackgroundColor(Color.BLUE)
            .addSwipeRightActionIcon(R.drawable.baseline_mode_edit_24)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }



}