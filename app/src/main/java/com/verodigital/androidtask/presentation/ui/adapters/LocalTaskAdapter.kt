package com.verodigital.androidtask.presentation.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.verodigital.androidtask.R
import com.verodigital.androidtask.data.datasource.Task
import com.verodigital.androidtask.util.getProgressDrawable

/*
class LocalTaskAdapter(
    //private var taskList: ArrayList<Tasks>,

    ) : RecyclerView.Adapter<LocalTaskAdapter.TaskViewHolder>() {


    fun updateTasks(
        taskListDetails: List<Tasks>,
    ) {
        taskList.clear()
        taskList.addAll(taskListDetails)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
    )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(
            taskList[position]
        )
    }

    override fun getItemCount(): Int = taskList.size

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val taskName = view.findViewById<TextView>(R.id.taskName)
        private val taskTitle = view.findViewById<TextView>(R.id.taskTitle)
        private val description = view.findViewById<TextView>(R.id.description)
        private val colorCode = view.findViewById<ImageView>(R.id.colorImageView)
        private val progressDrawable = getProgressDrawable(view.context)
        private var v = view;

        fun bind(
            task: Tasks
        ) {
            this.taskName.text = task.task
            this.taskTitle.text = task.title
            this.description.text = task.description
            if (task.colorCode?.isNotBlank()!! && task.colorCode?.isNotEmpty()!!){
                this.colorCode.setBackgroundColor(android.graphics.Color.parseColor(task.colorCode))
            }


        }
    }


}

 */
