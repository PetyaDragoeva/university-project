package com.example.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_items, parent, false);
        }

        TextView taskName = convertView.findViewById(R.id.taskName);
        taskName.setText(task.title);

        TextView taskDescription = convertView.findViewById(R.id.taskDate);
        taskDescription.setText(task.getFormatDate());

        TextView taskPriority = convertView.findViewById(R.id.taskPriority);
        taskPriority.setText(task.getFormatPriority());

        TextView taskTime = convertView.findViewById(R.id.taskTime);
        taskTime.setText(task.getFormatTime(task.begin) + " - " + task.getFormatTime(task.end));

        return convertView;
    }
}
