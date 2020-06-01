package com.example.tasks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TaskActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;
    private EditText beginEditText;
    private EditText endEditText;
    private Spinner prioritySpinner;
    private CheckBox notificCheckBox;

    private Button btnSave;
    private Button btnCancel;
    private long task_id;

    int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        beginEditText = findViewById(R.id.beginEditText);
        endEditText = findViewById(R.id.endEditText);

        notificCheckBox = findViewById(R.id.notificationCheckbox);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        dateEditText.setOnClickListener(timeClickListener);
        beginEditText.setOnClickListener(timeClickListener);
        endEditText.setOnClickListener(timeClickListener);

        btnSave.setOnClickListener(btnClickListener);
        btnCancel.setOnClickListener(btnClickListener);

        prioritySpinner = findViewById(R.id.prioritySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        Intent intent = getIntent();
        task_id = intent.getLongExtra("task_id", 0);

        if(task_id > 0){
            loadTask();
        }
    }

    private void loadTask(){
        Task task = Task.findById(Task.class, task_id);

        titleEditText.setText(task.title);
        descriptionEditText.setText(task.description);
        dateEditText.setText(task.getFormatDate());
        dateEditText.setTag(task.date);
        beginEditText.setText(task.getFormatTime(task.begin));
        beginEditText.setTag(task.begin);
        endEditText.setText(task.getFormatTime(task.end));
        endEditText.setTag(task.end);
        prioritySpinner.setSelection(task.priority);
        notificCheckBox.setChecked(task.notification);
    }

    private View.OnClickListener timeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final Calendar c = Calendar.getInstance();

            if(v.getId() == R.id.dateEditText){

                if(dateEditText.getText().toString().equals("")){
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    String[] arr = dateEditText.getText().toString().split("/");
                    mDay = Integer.parseInt(arr[0]);
                    mMonth = Integer.parseInt(arr[1]) - 1;
                    mYear = Integer.parseInt(arr[2]);
                }


                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                dateEditText.setTag(c.getTimeInMillis());

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            } else {
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                c.set(Calendar.HOUR, hourOfDay);
                                c.set(Calendar.MINUTE, minute);

                                if(v.getId() == R.id.beginEditText){
                                    beginEditText.setText(hourOfDay + ":" + minute);
                                    beginEditText.setTag(c.getTimeInMillis());
                                } else {
                                    endEditText.setText(hourOfDay + ":" + minute);
                                    endEditText.setTag(c.getTimeInMillis());
                                }
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        }
    };

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnCancel){
                finish();
                return;
            }

            if(titleEditText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Title is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if(dateEditText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Date is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if(beginEditText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Begin is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if(endEditText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "End is required", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = task_id == 0 ? new Task() : Task.findById(Task.class, task_id);

            task.title = titleEditText.getText().toString();
            task.description = descriptionEditText.getText().toString();
            task.date = dateEditText.getTag().toString();
            task.begin = beginEditText.getTag().toString();
            task.end = endEditText.getTag().toString();
            task.priority = prioritySpinner.getSelectedItemPosition();
            task.notification = notificCheckBox.isChecked();
            task.completed = "0";

            task.save();

            finish();
        }
    };
}
