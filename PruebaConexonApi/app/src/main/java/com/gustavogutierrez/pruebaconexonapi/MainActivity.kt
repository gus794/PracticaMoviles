package com.gustavogutierrez.pruebaconexonapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.gustavogutierrez.demo05.utils.checkConnection
import com.gustavogutierrez.demo07.data.RemoteDataSource
import com.gustavogutierrez.pruebaconexonapi.data.TaskRepository
import com.gustavogutierrez.pruebaconexonapi.databinding.ActivityMainBinding
import com.gustavogutierrez.pruebaconexonapi.models.TasksItem
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val TAG = MainActivity::class.java.simpleName

    private var taskList: List<TasksItem> = emptyList()
    private var finishedTaskList: List<TasksItem> = emptyList()

    private val vm: MainViewModel by viewModels {
        val tasksDataSource = RemoteDataSource()
        val tasksRepository = TaskRepository(tasksDataSource)
        MainViewModelFactory(tasksRepository)
    }

    // Adaptador para la lista de palabras
    private val adapter: TasksAdapter by lazy {
        TasksAdapter(
            listenerDetail = {
                TaskDetail.navigate(this, it.codTrabajo)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            vm.fetchPendingTasks("1", "contrasena123")
            vm.fetchFinishedTasks("1", "contrasena123")
        }

        binding.recyclerView.adapter = adapter
        populateTasks();
    }

    private fun populateTasks() {
        lifecycleScope.launch {
            binding.swipeRefresh.isRefreshing = true
            vm.currentPendingTasks.catch {
                Toast.makeText(
                    this@MainActivity, it.message, Toast.LENGTH_SHORT
                ).show()
            }.collect {
                taskList = it
                Log.i(TAG, "populateTasks: ${it}")
                adapter.submitList(taskList)
                adapter.notifyDataSetChanged()
                binding.swipeRefresh.isRefreshing = false
            }

            vm.currentFinishedtasks.catch {
                Toast.makeText(
                    this@MainActivity, it.message, Toast.LENGTH_SHORT
                ).show()
            }.collect {
                finishedTaskList = it
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    // Método onStart sobreescrito
    override fun onStart() {
        super.onStart()

        // Se asigna la funcionalidad correspondiente al swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            if (checkConnection(this)) {
                binding.tvNoConnection.visibility = View.GONE
            } else {
                binding.tvNoConnection.visibility = View.VISIBLE
            }
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
                    binding.swipeRefresh.isEnabled = true
                    adapter.submitList(emptyList())
                    adapter.submitList(taskList)
                    true
                }
                R.id.opt_finished -> {
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