package com.gustavogutierrez.pruebaconexonapi

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gustavogutierrez.demo05.utils.checkConnection
import com.gustavogutierrez.pruebaconexonapi.data.RemoteDataSource
import com.gustavogutierrez.pruebaconexonapi.data.TaskRepository
import com.gustavogutierrez.pruebaconexonapi.databinding.ActivityTaskDetailBinding
import com.gustavogutierrez.pruebaconexonapi.models.TasksItem
import kotlinx.coroutines.launch


class TaskDetail : AppCompatActivity() {
    private lateinit var binding: ActivityTaskDetailBinding

    private val vm: MainViewModel by viewModels {
        val tasksDataSource = RemoteDataSource()
        val tasksRepository = TaskRepository(tasksDataSource)
        MainViewModelFactory(tasksRepository)
    }

    companion object {
        const val TASK_ITEM = "TASK_ITEM"

        fun navigate(activity: AppCompatActivity, task: TasksItem) {
            val intent = Intent(activity, TaskDetail::class.java).apply {
                putExtra(TASK_ITEM, task)
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

        if (checkConnection(this)) {
            var task: TasksItem?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                task = intent.getParcelableExtra(TASK_ITEM, TasksItem::class.java)
            } else {
                task = intent.getParcelableExtra(TASK_ITEM)
            }

            Log.i("TAG", "onCreate: $task")

            task?.let {
                binding.tvTiempo.text = it.tiempo.toString()
                binding.tvDescripcion.text = it.descripcion
                binding.tvCategoria.text = it.categoria
                binding.tvPrioridad.text = it.prioridad.toString()
                binding.tvTrabajador.text = it.trabajador?.nombre ?: "N/A"
                binding.tvFechaInicio.text = it.fechaInicio
                binding.tvFechaFin.text = it.fechaFin
                binding.tvCodTrabajo.text = it.codTrabajo

                if (it.fechaFin == null) {
                    binding.btnAction.visibility = View.VISIBLE
                    binding.btnAction.setOnClickListener {
                        Log.i("TAG", "onCreate: btnAction")
                        showTimeInputDialog(task)
                    }
                }
            }
        }
    }

    private fun showTimeInputDialog(task: TasksItem) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ingrese el tiempo")

        val input = EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setView(input)

        builder.setPositiveButton("Confirmar", null)
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val inputValue = input.text.toString()
                if (inputValue.isNotEmpty()) {
                    val tiempo = inputValue.toDoubleOrNull()
                    if (tiempo != null) {
                        lifecycleScope.launch {
                            vm.finishTask(task.codTrabajo, tiempo)
                        }
                        dialog.dismiss()
                    } else {
                        Log.e("TaskDetail", "Invalid input for time: $inputValue")
                    }
                } else {
                    Toast.makeText(this, "El campo de tiempo no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }
}
