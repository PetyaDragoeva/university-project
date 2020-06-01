package com.example.tasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Switch showTaskSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.taskListView);
        showTaskSwitch = findViewById(R.id.showTaskSwitch);

        showTaskSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTasks();
            }
        });

        loadTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks(){

        final List<Task> tasks = showTaskSwitch.isChecked() ? Task.listAll(Task.class) : Task.find(Task.class, "completed = ?", "0");

        TaskAdapter adapter = new TaskAdapter(this, tasks);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.view_task, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                final TextView viewTitle = promptsView.findViewById(R.id.viewTaskTitle);
                viewTitle.setText(tasks.get(position).title);

                final TextView viewDescription = promptsView.findViewById(R.id.viewTaskDescription);
                viewDescription.setText(tasks.get(position).description);

                final TextView viewDate = promptsView.findViewById(R.id.viewTaskDate);
                viewDate.setText(tasks.get(position).getFormatDate());

                final TextView viewTime = promptsView.findViewById(R.id.viewTaskTime);
                viewTime.setText(tasks.get(position).getFormatTime(tasks.get(position).begin)
                        + " - " + tasks.get(position).getFormatTime(tasks.get(position).end));

                final TextView viewPriority = promptsView.findViewById(R.id.viewTaskPriority);
                viewPriority.setText(tasks.get(position).getFormatPriority());

                final TextView viewNotification = promptsView.findViewById(R.id.viewTaskNotification);
                viewNotification.setText(tasks.get(position).notification ? "Yes" : "No");


                builder.setView(promptsView);
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                        intent.putExtra("task_id", tasks.get(position).getId());
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Complete task")
                        .setMessage("Mark task as completed?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Task task = tasks.get(position);
                                task.completed = "1";
                                task.save();
                                loadTasks();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Task task = tasks.get(position);
                                task.completed = "0";
                                task.save();
                                loadTasks();
                            }
                        })
                        .show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
