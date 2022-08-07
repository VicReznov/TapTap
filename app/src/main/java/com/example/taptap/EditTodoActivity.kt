package com.example.taptap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taptap.databinding.ActivityEditTodoBinding
import com.example.taptap.databinding.ActivityMainBinding
import com.example.taptap.dto.Todo
import java.text.SimpleDateFormat

class EditTodoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditTodoBinding
    private var todo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // MainActivity의 floatingbutton에서 ADD가 넘어올 때와 아닐 때를 구분하여 받음
        val type = intent.getStringExtra("type")

        if(type.equals("ADD")){
            binding.editTodoSaveBtn.text = "추가하기"
        } else {
            todo = intent.getSerializableExtra("item") as Todo?
            binding.editTodoTitle.setText(todo!!.title)
            binding.editTodoContent.setText(todo!!.content)
            binding.editTodoSaveBtn.text = "수정하기"
        }

        binding.editTodoSaveBtn.setOnClickListener {
            val title = binding.editTodoTitle.text.toString()
            val content = binding.editTodoContent.text.toString()
            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())

            // 추가할 때
            if(type.equals("ADD")){
                if(title.isNotEmpty() && content.isNotEmpty()){
                    Toast.makeText(this, "저장 버튼 눌림", Toast.LENGTH_SHORT).show()

                    val todo = Todo(0, title, content, currentDate, false)
                    val intent = Intent().apply{
                        putExtra("todo", todo)
                        putExtra("flag", 0) // 0 -> "추가", 1 -> "수정"
                    }
                    setResult(RESULT_OK, intent) // MainActivity의 requestActivity로 데이터 넘겨줌
                    finish()
                }
            }
            // 수정할 때
            else {
                if(title.isNotEmpty() && content.isNotEmpty()){
                    val todo = Todo(todo!!.id, title, content, currentDate, todo!!.isChecked)

                    val intent = Intent().apply {
                        putExtra("todo", todo)
                        putExtra("flag", 1)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

        }
    }
}