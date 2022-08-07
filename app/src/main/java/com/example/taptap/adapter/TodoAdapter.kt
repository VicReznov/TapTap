package com.example.taptap.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taptap.R
import com.example.taptap.dto.Todo

class TodoAdapter(val context: Context): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var list = mutableListOf<Todo>()
    private lateinit var itemClickListener: ItemClickListener

    inner class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var title = itemView.findViewById<TextView>(R.id.item_title)
        var timestamp = itemView.findViewById<TextView>(R.id.item_timeStamp)
        var checkbox = itemView.findViewById<CheckBox>(R.id.item_checkBox)

        fun onBind(data: Todo){
            title.text = data.title
            timestamp.text = data.timestamp
            checkbox.isChecked = data.isChecked

            if(data.isChecked){
                title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG
            }

            checkbox.setOnClickListener {
               itemCheckBoxClickListener.onClick(it, layoutPosition, list[layoutPosition].id)
            }

            itemView.setOnClickListener {
                itemClickListener.onClick(it, layoutPosition, list[layoutPosition].id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_list, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int){
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int{
        return list.size
    }

    fun update(newList: MutableList<Todo>){
        this.list = newList
        notifyDataSetChanged()
    }

    interface ItemCheckBoxClickListener{
        fun onClick(view: View, position: Int, itemId: Long)
    }

    private lateinit var itemCheckBoxClickListener: ItemCheckBoxClickListener

    fun setItemCheckBoxClickListener(itemCheckBoxClickListener: ItemCheckBoxClickListener){
        this.itemCheckBoxClickListener = itemCheckBoxClickListener
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, itemId: Long)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

}