package com.example.shahar.biologyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * This activity is used as a main point of transportation to the different activities of this project
 * Through this activity the both the managers and normal users can proceed to the tasks activity, plantlist activity and to the gallery
 * In addition, the manager users can also access the schedule activity through this activity
 * The gallery button will lead the user to an outside website
 * @author  Shahar Chen
 * @version 1.0
 */
public class Activity_Guest extends AppCompatActivity implements View.OnClickListener {

    /** The  button that leads to the PlantList Activity */
    Button plants;
    /** The  button that leads to the Schedule Activity */
    Button schedule;
    /** The  button that leads to the Tasks Activity */
    Button tasks;
    /** The  button that leads to the gallery */
    Button gallery;
    /** a flag to save if the user is a manager */
    boolean allowed_inGuest= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        plants=(Button)findViewById(R.id.btPlants);
        schedule=(Button)findViewById(R.id.btSchedule);
        tasks=(Button)findViewById(R.id.btTasks);
        gallery=(Button)findViewById(R.id.btGallery);
        plants.setOnClickListener(this);
        schedule.setOnClickListener(this);
        tasks.setOnClickListener(this);
        gallery.setOnClickListener(this);

        Bundle data=getIntent().getExtras();
        if(data!=null)
            allowed_inGuest=data.getBoolean("allowed");


    }

    @Override
    public void onClick(View v) {

        /**
         *  The user will be transported to the PlantList Activity
         */
        if(v.getId()==plants.getId())
        {
            Intent in=new Intent(getApplicationContext(),ActivityPlantList.class);
            Log.d("------------------","got intent to move to plants");
            in.putExtra("allowed",allowed_inGuest);
            startActivity(in);

        }
        /**
         *  The user will be transported to the Schedule Activity if they are sighed in as a manager
         *  An alert message will be shown to a non manager user that pushed this button
         */
        if(v.getId()==schedule.getId())
        {
            if(allowed_inGuest==true) {
                Intent in = new Intent(getApplicationContext(), Activity_Schedule.class);
                Log.d("------------------", "got intent to move to schedule");
                in.putExtra("allowed", allowed_inGuest);
                startActivity(in);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sign in as manager to access this function");
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
        /**
         *  The user will be transported to the Tasks Activity
         */
        if(v.getId()==tasks.getId())
        {
            Intent in=new Intent(getApplicationContext(),ActivityTasks.class);
            Log.d("------------------","got intent to move to tasks");
            in.putExtra("allowed",allowed_inGuest);
            startActivity(in);

        }
        /**
         *  The user will be transported to an outside website called Instagram
         */
        if(v.getId()==gallery.getId())
        {
            Intent in=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/mor_school_garden/"));
            startActivity(in);
        }

    }
}
