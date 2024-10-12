package com.example.letterboxie.utilities

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class SwipeGesture(private val backgroundColor : Int, private val actionIcon : Int)
    : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    lateinit var onSwipe : (Int) -> Unit

    override fun onMove(recyclerView : RecyclerView, viewHolder : RecyclerView.ViewHolder,
                        target : RecyclerView.ViewHolder) : Boolean {
        return false
    }

    override fun onSwiped(viewHolder : RecyclerView.ViewHolder, direction : Int) {
        val position = viewHolder.bindingAdapterPosition
        if (position != RecyclerView.NO_POSITION) onSwipe(position)
    }

    override fun onChildDraw(c : Canvas, recyclerView : RecyclerView, viewHolder : RecyclerView.ViewHolder,
        dX : Float, dY : Float, actionState : Int, isCurrentlyActive : Boolean) {
        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addBackgroundColor(backgroundColor).addActionIcon(actionIcon).create().decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}