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
        // 넘어온 데이터가 ADD라면
        if(type.equals("ADD")){
            binding.editTodoSaveBtn.text = "추가하기"
        }
        // 넘어온 데이터가 ADD가 아니라면
        else {
            todo = intent.getSerializableExtra("item") as Todo?
            binding.editTodoTitle.setText(todo!!.title)
            binding.editTodoContent.setText(todo!!.content)
            binding.editTodoSaveBtn.text = "수정하기"
        }

        // 추가하기 버튼을 눌렀을 때 이벤트 처리
        binding.editTodoSaveBtn.setOnClickListener {
            // 제목, 내용, 현재 시각
            val title = binding.editTodoTitle.text.toString()
            val content = binding.editTodoContent.text.toString()
            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())

            // 추가할 때
            if(type.equals("ADD")){
                if(title.isNotEmpty() && content.isNotEmpty()){ // 작성하지 않은 항목이 있는지 확인
                    Toast.makeText(this, "저장 버튼 눌림", Toast.LENGTH_SHORT).show()
                    // 투두 객체 만듦 ( 투두: DTO)
                    val todo = Todo(0, title, content, currentDate, false) // id는 자동으로 증가하기 때문에 0 넣어줌
                    // intent에 투두 객체와 flag(추가인지 수정인지 구분)를 담아 MainActivity로 넘김
                    val intent = Intent().apply{
                        putExtra("todo", todo)
                        putExtra("flag", 0) // 0 -> "추가", 1 -> "수정"
                    }
                    setResult(RESULT_OK, intent) // MainActivity의 requestActivity로 데이터 넘겨줌
                    finish() // 현재 액티비티 종료
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