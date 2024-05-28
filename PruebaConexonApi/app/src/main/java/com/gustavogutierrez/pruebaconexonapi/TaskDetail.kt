package com.gustavogutierrez.pruebaconexonapi

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gustavogutierrez.demo05.utils.checkConnection
import com.gustavogutierrez.demo07.data.RemoteDataSource
import com.gustavogutierrez.pruebaconexonapi.data.TaskRepository
import com.gustavogutierrez.pruebaconexonapi.databinding.ActivityTaskDetailBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine


class TaskDetail : AppCompatActivity() {
    private lateinit var binding: ActivityTaskDetailBinding
    private val vm: MainViewModel by viewModels {
        val tasksDataSource = RemoteDataSource()
        val tasksRepository = TaskRepository(tasksDataSource)
        MainViewModelFactory(tasksRepository)
    }

    companion object {
        const val TASK_ID = "TASK_ID"

        fun navigate(activity: AppCompatActivity, id: String = "") {

            val intent = Intent(activity, TaskDetail::class.java).apply {
                putExtra(TASK_ID, id)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            activity.startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
            )

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        /*setContentView(R.layout.activity_task_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        if (checkConnection(this)) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    vm.currentPendingTasks.f
                }
            }
        } else {
            Toast.makeText(this@DetailShowActivity, "", Toast.LENGTH_SHORT).show()
        }
    }
}