package com.example.shahar.biologyapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @author  Shahar Chen
 * @version 1.0
 */
public class TasksAdapter extends ArrayAdapter<Task> {

    Context context;
    List<Task> objects;

    /**
     *
     * @param context
     * @param resource
     * @param textViewResourceId
     * @param objects
     */
    public TasksAdapter(Context context, int resource, int textViewResourceId, List<Task> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
    }

    /**
     * This method connects between the adapter to the correct layout
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.list_tasks,parent,false);

        EditText task_body=(EditText)view.findViewById(R.id.etTheTask);
        TextView isComplete=(TextView)view.findViewById(R.id.tvIsFinished);
        Button save= (Button)view.findViewById(R.id.btSaveTask);
        Task task_object = objects.get(position);

        task_body.setText(String.valueOf(task_object.getTask()));
        isComplete.setText(String.valueOf(task_object.isDoneMessage()));

        return view;
    }



}
