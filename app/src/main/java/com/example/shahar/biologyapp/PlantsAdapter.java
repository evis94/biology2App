package com.example.shahar.biologyapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @author  Shahar Chen
 * @version 1.0
 */
public class PlantsAdapter extends ArrayAdapter<Plants> {
    Context context;
    List<Plants> objects;

    /**
     *
     * @param context
     * @param resource
     * @param textViewResourceId
     * @param objects
     */
    public PlantsAdapter(Context context, int resource, int textViewResourceId, List<Plants> objects) {
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
        View view = layoutInflater.inflate(R.layout.listplants,parent,false);

        EditText nameOfPlant=(EditText)view.findViewById(R.id.etPlantName);
        ImageView pic=(ImageView)view.findViewById(R.id.imgPlantPic);
        TextView reminder=(TextView)view.findViewById(R.id.tvPlantReminder);

        Plants plant_obj = objects.get(position);

        pic.setImageBitmap(plant_obj.getPic());
        nameOfPlant.setText(String.valueOf(plant_obj.getName()));
        reminder.setText(String.valueOf(plant_obj.getRemind()));

        return view;
    }
}
