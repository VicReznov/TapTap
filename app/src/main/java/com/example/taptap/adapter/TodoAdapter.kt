package com.example.taptap.adapter

import android.content.Context
import android.graphics.Paint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
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

        // item_todo_list 의 변수
        var title = itemView.findViewById<TextView>(R.id.item_title)
        var timestamp = itemView.findViewById<TextView>(R.id.item_timeStamp)
        var checkbox = itemView.findViewById<CheckBox>(R.id.item_checkBox)

        // item_todo_list 의 변수에 값을 저장
        fun onBind(data: Todo){
            title.text = data.title
            timestamp.text = data.timestamp
            checkbox.isChecked = data.isChecked

            // 체크 여부에 따라 줄을 긋고 안 긋고를 설정
            if(data.isChecked){
                title.paintFlags = title.paintFlags or STRIKE_THRU_TEXT_FLAG // 취소선 적용
            } else {
                title.paintFlags = title.paintFlags and STRIKE_THRU_TEXT_FLAG.inv() // 취소선 취소
                // A.inv() : A의 비트를 반전
            }

            checkbox.setOnClickListener {
               itemCheckBoxClickListener.onClick(it, layoutPosition, list[layoutPosition].id)
            }

            // item을 누르면
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