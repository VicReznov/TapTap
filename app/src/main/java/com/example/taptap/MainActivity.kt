package com.example.taptap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taptap.adapter.TodoAdapter
import com.example.taptap.databinding.ActivityMainBinding
import com.example.taptap.dto.Todo
import com.example.taptap.viewmodel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // binding 변수로 xml의 변수에 접근
    lateinit var todoViewModel: TodoViewModel
    lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // binding 변수로 xml의 변수에 접근
        setContentView(binding.root) // binding 변수로 xml의 변수에 접근

        todoAdapter = TodoAdapter(this)
        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        // LiveData 이기 때문에 observe 를 통해 변화된 값을 감지할 수 있다.
        // 이를 이용해 값이 변경되면 todoAdapter 에 반영하여 데이터가 업데이트 되도록 한다
        todoViewModel.todoList.observe(this){
            todoAdapter.update(it)
        }

        // + 모양의 프로팅버튼에 연결
        binding.mainFbtAdd.setOnClickListener {
            val intent = Intent(this, EditTodoActivity::class.java).apply {
                putExtra("type", "ADD") // ADD: 추가 라는 뜻
            }
            requestActivity.launch(intent) // EditTodoActivity 의 setResult 에서 데이터가 넘어옴
        }

        binding.mainRv.layoutManager = LinearLayoutManager(this)
        binding.mainRv.adapter = todoAdapter // 리사이클러뷰에 todoAdapter 연결

        todoAdapter.setItemCheckBoxClickListener(object : TodoAdapter.ItemCheckBoxClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = todoViewModel.getOne(itemId) // 클릭된 아이템의 정보 가져옴
                    todo.isChecked = !todo.isChecked
                    todoViewModel.update(todo)
                }
            }
        })

        // 아이템을 눌렀을 때 수정 화면으로 넘어감
        todoAdapter.setItemClickListener(object: TodoAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {
                Toast.makeText(this@MainActivity, "$itemId", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = todoViewModel.getOne(itemId)

                    val intent = Intent(this@MainActivity, EditTodoActivity::class.java).apply {
                        putExtra("type", "EDIT")
                        putExtra("item", todo)
                    }
                    requestActivity.launch(intent)
                }
            }
        })
    }

    // intent를 통해 EditTodoActivity에서 넘어올 데이터를 처리함
    // 넘어온 데이터들은 it.data에 담겨져있다.
    // 데이터베이스에 저장하는 작업은 시간이 오래 걸리기 때문에 메인스레드가 아닌 코루틴의 IO스레드에서 이 작업을 수행하도록 함
    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        // RESULT_OK 데이터가 넘어왔다면
        if(it.resultCode == RESULT_OK){
            val todo = it.data?.getSerializableExtra("todo") as Todo

            when(it.data?.getIntExtra("flag", -1)){
                // 추가
                0 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        // insert 작업은 todoViewModel에서 담당한다.
                        todoViewModel.insert(todo)
                    }
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                // 수정
                1 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        todoViewModel.update(todo)
                    }
                    Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 앱 우상단에 메뉴표시가 나타남
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }
    // 앱 우상단의 메뉴에 나타는 아이템의 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.menu_item_delete -> {
                Toast.makeText(this, "삭제", Toast.LENGTH_SHORT).show()
                todoViewModel.todoList.value!!.forEach{
                    if (it.isChecked){
                        todoViewModel.delete(it)
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}