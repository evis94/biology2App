package com.example.shahar.biologyapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This Activity manages the tasks that are needed to do in the garden
 * The data is saved using Firebase real time database
 *
 * @author Shahar Chen
 * @version 1.0
 */
public class ActivityTasks extends AppCompatActivity implements AdapterView.OnItemLongClickListener, View.OnClickListener {

    Button addTask;
    Boolean allowed_inTasks;//did this person enter as manager
    ListView tasks_list;
    /**
     * this array holds a list of tasks
     */
    ArrayList<Task> arrTask = new ArrayList<Task>();
    //Task [] t_arr;
    static int index_arr = 0;
    TasksAdapter task_adapter;

    Dialog task_dialog;
    EditText the_task;
    TextView isDone;
    Button save_task;

    Task task_object;

    FirebaseDatabase task_database;
    DatabaseReference task_Ref;
    DatabaseReference task_from_database;


    private void initAdapter(){
        task_adapter = new TasksAdapter(ActivityTasks.this, 0, 0, arrTask);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        task_object = new Task();

        task_database = FirebaseDatabase.getInstance();
        task_from_database = FirebaseDatabase.getInstance().getReference("Tasks");

        addTask = (Button) findViewById(R.id.btAddTask);
        addTask.setOnClickListener(this);
        allowed_inTasks = false;
        tasks_list = (ListView) findViewById(R.id.lvTasksList);
        if (tasks_list == null)
            Log.d("----------", "listView is null");
        initAdapter();
        retrieveData();
        tasks_list.setAdapter(task_adapter);
        tasks_list.setOnItemLongClickListener(this);




        /**
         * Opens a dialog to create a new task
         */
        task_dialog = new Dialog(this, R.style.AlertDialogCustom);
        task_dialog.setContentView(R.layout.tasks_dialog);
        task_dialog.setTitle("The task details");

        task_dialog.setCancelable(true);
        isDone = (TextView) task_dialog.findViewById(R.id.tvIsFinishedDialog);
        the_task = (EditText) task_dialog.findViewById(R.id.etTheTaskDialog);
        save_task = (Button) task_dialog.findViewById(R.id.btSaveTaskDialog);
//        save_task.setOnClickListener(this);
        save_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task_object = new Task(the_task.getText().toString(), false);
                task_object.setTask(the_task.getText().toString()); //to make sure it works because hell knows it didn't before
                Log.d("-----------", the_task.getText().toString());
                task_object.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid()); //setting the uid
                task_Ref = task_database.getReference("Tasks").push(); //creating new column/ adding to the right existing one
                task_object.setKey(task_Ref.getKey()); //setting the key
                task_Ref.setValue(task_object); //setting the value
                task_adapter.add(task_object); //adding to listview
                task_adapter.notifyDataSetChanged(); //notify the lv that the data has changed
                task_Ref = task_database.getReference("Posts/" + task_Ref.getKey());
                ActivityTasks.this.retrieveData();

                task_dialog.dismiss();
            }
        });

        /** pulls the information of if the user is a manager from the intent*/
        Bundle data = getIntent().getExtras();
        if (data != null)
            allowed_inTasks = data.getBoolean("allowed");
    }


    @Override
    public void onClick(View v) {

        /**
         * Adds a new task if the user is a manager
         * If the user isn't a manager an alert message will be displayed
         */
        if (v.getId() == addTask.getId()) {

            if (allowed_inTasks == true) {
                Toast.makeText(this,"clicked to create new task",Toast.LENGTH_SHORT).show();
                task_dialog.show();
//                RetrieveData();
                task_adapter.notifyDataSetChanged();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You need to sign in as manager to access this function");
                builder.setCancelable(true);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }

        }

    }

    /**
     * This method updates the completion status of the task and deletes it
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

        final int pos = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Has the task been completed?");
        builder.setCancelable(true);

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                task_object.setDone(true);
                isDone.setText(task_object.isDoneMessage());
                Log.d("------", task_object.isDoneMessage());
                task_adapter.notifyDataSetChanged();

                task_Ref = task_database.getReference("Posts/" + task_Ref.getKey());
                retrieveData();

                /*
                task_object.setTask(task_object.getTask());
                Task to_edit=task_adapter.getItem(pos);
                DatabaseReference current = FirebaseDatabase.getInstance().getReference("Tasks/" + to_edit.getKey());
                current.setValue(task_object);
                */

            }
        });

        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d("----- isDone problem", task_object.isDoneMessage());
                task_object.setDone(false);
                isDone.setText(task_object.isDoneMessage());
                Log.d("------", task_object.isDoneMessage());
                task_adapter.notifyDataSetChanged();

                task_object.setTask(task_object.getTask());
                Log.d("-----the task", task_object.getTask());
                Task to_edit = task_adapter.getItem(pos);
                DatabaseReference current = FirebaseDatabase.getInstance().getReference("Tasks/" + to_edit.getKey());
                current.setValue(task_object);


            }
        });

        builder.setNeutralButton("delete the task", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Task t = task_adapter.getItem(pos);
                task_adapter.remove(t);
                task_adapter.notifyDataSetChanged();
                DatabaseReference current = FirebaseDatabase.getInstance().getReference("Tasks/" + t.getKey());
                current.removeValue();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }


    /**
     * retrieves the schedule data from the Firebase database and updates the info in the array
     */
    public void retrieveData() {
        //the reference to the Task table- task_from_database
        task_from_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Task task = dataSnapshot.getValue(Task.class);
                    arrTask.add(task);

                }

                task_adapter.notifyDataSetChanged();
                index_arr++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
