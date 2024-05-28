package com.gustavogutierrez.pruebaconexonapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gustavogutierrez.pruebaconexonapi.databinding.ItemTaskBinding
import com.gustavogutierrez.pruebaconexonapi.models.TasksItem

// Adaptador para la lista de palabras
class TasksAdapter(
    private val listenerDetail: (TasksItem) -> Unit
): ListAdapter<TasksItem, TasksAdapter.TasksViewHolder>(TasksDiffCallback()) {
    inner class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemTaskBinding.bind(view)
        // Método para tenlazar los datos de una palabra con la vista de la lista
        fun bind(task: TasksItem) {
            binding.tvDesc.text = task.descripcion
            binding.tvCat.text = task.categoria
            itemView.setOnClickListener {
                listenerDetail(task)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }
    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

// Clase que implementa DiffUtil.ItemCallback para comparar las palabras en el adaptador
class TasksDiffCallback : DiffUtil.ItemCallback<TasksItem>() {
    // Método para verificar si dos palabras son la misma (tienen el mismo ID)
    override fun areItemsTheSame(oldItem: TasksItem, newItem: TasksItem): Boolean {
        return oldItem.codTrabajo == newItem.codTrabajo
    }
    // Método para verificar si dos palabras tienen los mismos contenidos
    override fun areContentsTheSame(oldItem: TasksItem, newItem: TasksItem): Boolean {
        return oldItem.codTrabajo == newItem.codTrabajo &&
                oldItem.descripcion == newItem.descripcion &&
                oldItem.fechaInicio == newItem.fechaInicio &&
                oldItem.prioridad == newItem.prioridad &&
                oldItem.categoria == newItem.categoria
    }
}
