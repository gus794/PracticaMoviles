package com.gustavogutierrez.pruebaconexonapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.gustavogutierrez.demo05.utils.ViewStatus
import com.gustavogutierrez.demo05.utils.checkConnection
import com.gustavogutierrez.demo05.utils.viewStatus
import com.gustavogutierrez.pruebaconexonapi.data.RemoteDataSource
import com.gustavogutierrez.pruebaconexonapi.data.TaskRepository
import com.gustavogutierrez.pruebaconexonapi.databinding.ActivityMainBinding
import com.gustavogutierrez.pruebaconexonapi.models.TasksItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val TAG = MainActivity::class.java.simpleName

    private var taskList: List<TasksItem> = emptyList()
    private var finishedTaskList: List<TasksItem> = emptyList()
    private var idTrabajador: Int = 0

    private val vm: MainViewModel by viewModels {
        val tasksDataSource = RemoteDataSource()
        val tasksRepository = TaskRepository(tasksDataSource)
        MainViewModelFactory(tasksRepository)
    }

    // Adaptador para la lista de palabras
    private val adapter: TasksAdapter by lazy {
        TasksAdapter(
            listenerDetail = {
                TaskDetail.navigate(this, it)
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if(idTrabajador != 0){
            populateTasks()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mToolbar.inflateMenu(R.menu.menu)

        showLoginDialog()

        if (checkConnection(this)) {
            binding.tvNoConnection.visibility = View.GONE
        } else {
            binding.tvNoConnection.visibility = View.VISIBLE
        }

        binding.recyclerView.adapter = adapter
    }

    private fun showLoginDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.login, null)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Login")
            .setView(dialogView)
            .setPositiveButton("Login") { dialog, _ ->
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                // Aquí puedes manejar el login, por ejemplo, llamando a tu API
                lifecycleScope.launch {
                    vm.login(email, password)
                    vm.login.catch {
                        Toast.makeText(
                            this@MainActivity, "Usuario incorrecto", Toast.LENGTH_SHORT
                        ).show()
                        showLoginDialog()
                    }.collect{
                        idTrabajador = it.idTrabajador
                        vm.fetchPendingTasks(it.idTrabajador.toString(), password)
                        vm.fetchFinishedTasks(it.idTrabajador.toString(), password)
                        populateTasks()
                    }
                }
            }
            .setCancelable(false)
            .create()

        dialog.show()
    }

    private fun populateTasks() {
        lifecycleScope.launch {
            combine(
                vm.currentPendingTasks,
                vm.currentFinishedtasks
            ) { pending, finished ->
                adapter.submitList(emptyList())
                taskList = pending
                finishedTaskList = finished
                if(viewStatus == ViewStatus.FINISHED) adapter.submitList(finishedTaskList)
                else if(viewStatus == ViewStatus.PENDING) adapter.submitList(taskList)
            }.catch {
                Toast.makeText(
                    this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
            }.collect()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun filterPriority(priority: String){
        lifecycleScope.launch {
            vm.byPriority(idTrabajador.toString(),priority)
            vm.currentByPriority.catch {
                Toast.makeText(
                    this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
            }.collect{
                adapter.submitList(emptyList())
                taskList = it
                adapter.submitList(taskList)
            }
        }
    }

    // Método onStart sobreescrito
    override fun onStart() {
        super.onStart()
        binding.mToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.opt_logout->{
                    showLoginDialog()
                    true
                }
                R.id.opt_priority->{
                    if(viewStatus == ViewStatus.PENDING){
                        lifecycleScope.launch {
                            vm.orderByPriority(idTrabajador.toString())
                            vm.currentOrderTasks.catch {
                                Toast.makeText(
                                    this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                            }.collect{
                                adapter.submitList(emptyList())
                                taskList = it
                                adapter.submitList(taskList)
                            }
                        }
                    }
                    true
                }
                R.id.opt_choose_priority_1->{
                    if(viewStatus == ViewStatus.PENDING){
                        filterPriority("1")
                    }
                    true
                }
                R.id.opt_choose_priority_2->{
                    if(viewStatus == ViewStatus.PENDING){
                        filterPriority("2")
                    }
                    true
                }
                R.id.opt_choose_priority_3->{
                    if(viewStatus == ViewStatus.PENDING){
                        filterPriority("3")
                    }
                    true
                }
                R.id.opt_choose_priority_4->{
                    if(viewStatus == ViewStatus.PENDING){
                        filterPriority("4")
                    }
                    true
                }
                else -> false
            }
        }

        // Se asigna la funcionalidad correspondiente al swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            adapter.submitList(emptyList())
            lifecycleScope.launch {
                vm.currentPendingTasks.collect{
                    taskList = it
                    adapter.submitList(taskList)
                }
            }
            binding.swipeRefresh.isRefreshing = false
        }

        // Se asigna la funcionalidad correspondiente al menú de la bottom navigation view
        binding.btmNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.opt_pending -> {
                    viewStatus = ViewStatus.PENDING
                    binding.swipeRefresh.isEnabled = true
                    adapter.submitList(emptyList())
                    adapter.submitList(taskList)
                    true
                }
                R.id.opt_finished -> {
                    viewStatus = ViewStatus.FINISHED
                    binding.swipeRefresh.isEnabled = false
                    adapter.submitList(emptyList())
                    adapter.submitList(finishedTaskList)
                    true
                }
                else -> true
            }
        }
    }
}