package com.example.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.todo.databinding.ActivityMainBinding


@Suppress("UNREACHABLE_CODE")
open class MainActivity : AppCompatActivity() {

    private lateinit var listener: Listener
    private lateinit var binding: ActivityMainBinding
    private lateinit var toDoAdapter: ToDoAdapter
    private var itemSearch: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        listener = object : Listener {
            override fun listener1(int: Int,boolean: Boolean) {
                if (!boolean){
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                intent.putExtra("edit", PreferencesHelper.getToDoList(this@MainActivity)[int])
                intent.putExtra("index", int)
                startActivity(intent)}
                else{
                  val list =   PreferencesHelper.getToDoList(this@MainActivity)[int]
                      list.isFinished= true
                    toDoAdapter.loadData(listOf(list))
                }
            }
        }

        initRV()
        onClick()
    }

    private fun View.showKeyboard() {
        WindowInsetsControllerCompat(window, this).show(WindowInsetsCompat.Type.ime())
        requestFocus()
    }

    private fun View.hideKeyboard() {
        WindowInsetsControllerCompat(window, this).hide(WindowInsetsCompat.Type.ime())
    }

    private fun initSearchView() {
        binding.etSearch.visible()
        binding.toolbar.title = ""
        itemSearch?.setVisible(false)
        binding.etSearch.showKeyboard()
        binding.etSearch.onRightDrawableClickListener {
            binding.etSearch.gone()
            itemSearch?.setVisible(true)
            binding.toolbar.title="Vazifalar"
            binding.etSearch.hideKeyboard()
        }
        binding.etSearch.addTextChangedListener {
            val text= it?.toString()
            if (text?.isBlank()==true || text?.isEmpty()==true){
                toDoAdapter.search(null,this )
            }
            toDoAdapter.search(text, this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun EditText.onRightDrawableClickListener(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }

    private fun View.visible() {
        visibility = View.VISIBLE
    }

    private fun View.gone() {
        visibility = View.GONE
    }

    private fun View.inVisible() {
        visibility = View.INVISIBLE
    }

    private var hide: Boolean = true

    private fun initRV() {
        toDoAdapter = ToDoAdapter { position, isChecked ->
            onItemClick(position, isChecked)
        }
        binding.rvMain.adapter = toDoAdapter
        binding.rvMain.setHasFixedSize(false)
        binding.rvMain.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
            )
        )
        ItemTouchHelper(RvHelper(toDoAdapter, this, listener)).attachToRecyclerView(binding.rvMain)
    }

    private fun onItemClick(position: Int, isChecked: Boolean) {
        toDoAdapter.notifyItemDataChanged(position, this, isChecked)
    }

    override fun onResume() {
        super.onResume()
        toDoAdapter.loadData(PreferencesHelper.getToDoList(this))
    }

    private fun onClick() {
        with(binding) {
            addTask.setOnClickListener {
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                intent.putExtra(TODO_TYPE, ToDoEnum.ADD)
                startActivity(intent)
            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_todo_main, menu)
        itemSearch = menu?.findItem(R.id.search)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.delete_all -> {
                PreferencesHelper.removeAll(this)
                onResume()

            }

            R.id.delete_checkable -> {
                val dataList = PreferencesHelper.getToDoList(this).toMutableList()
                dataList.removeIf { it.isFinished }
                PreferencesHelper.saveToDo(dataList, this)
                onResume()
            }

            R.id.sort_time -> {
                val dataList = PreferencesHelper.getToDoList(this).toMutableList()
                dataList.sortBy {
                    it.timeTaskSave
                }
                PreferencesHelper.saveToDo(dataList, this)
                onResume()
            }

            R.id.sort_name -> {
                val dataList = PreferencesHelper.getToDoList(this).toMutableList()
                dataList.sortBy {
                    it.task.lowercase()
                }
                PreferencesHelper.saveToDo(dataList, this)
                onResume()
            }

            R.id.sort_finished_down -> {
                val dataList = PreferencesHelper.getToDoList(this).toMutableList()
                dataList.sortBy {
                    it.isFinished
                }
                PreferencesHelper.saveToDo(dataList, this)
                onResume()
            }

            R.id.sort_finished_up -> {
                val dataList = PreferencesHelper.getToDoList(this).toMutableList()
                dataList.sortWith(compareByDescending { it.isFinished })
                PreferencesHelper.saveToDo(dataList, this)
                onResume()
            }

            R.id.finished_hiden -> {
                if (hide) {
                    hide = false
                } else {
                    hide = true
                }
                hidens()
            }

            R.id.search -> {
                initSearchView()
            }
            R.id.help ->{
                goToUri("t.me/jalol_1025")
            }
            R.id.share ->{
                share()
            }
            R.id.comment ->{
                goToUri("https://play.google.com/store/apps")
            }
            R.id.source_code ->{
                goToUri("https://github.com/jaloliddin2510")
            }
            R.id.info ->{
                info()
            }


        }
        return true
    }
    private fun info(){
        val intent=Intent(this@MainActivity, Info::class.java)
        startActivity(intent)
    }

    //edit delete search
    companion object {
        const val TODO_TYPE = "ToToType"
    }

    private fun hidens() {

        if (hide) {
            val dataList = PreferencesHelper.getToDoList(this).toMutableList()
            PreferencesHelper.saveToDo(dataList, this)
            onResume()
        } else {
            val dataList = PreferencesHelper.getToDoList(this).toMutableList()
            val unfinishedTasks = dataList.filter { !it.isFinished }
            toDoAdapter.loadData(unfinishedTasks)
        }

    }
    fun goToUri(s: String){
        val  uri=Uri.parse(s)
        var intents=Intent(Intent.ACTION_VIEW, uri)
        startActivity(intents)
    }
    private fun share(){
        val appMsg: String ="abc"+"https://play.google.com/store/search?q=discord+talk+chat+and+hang+out&c=apps"
        val intent=Intent()
        intent.action=Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_LOCALE_LIST, appMsg)
        intent.type="text/plain"
        startActivity(intent)
    }

}