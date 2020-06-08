package com.example.shahar.biologyapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Shahar Chen
 * @version 1.0
 */
public class PlantsAdapter extends ArrayAdapter<Plant> {
    Context context;
    List<Plant> objects;
    LayoutInflater layoutInflater;
    ArrayList<Bitmap> images;

    /**
     *
     * @param context
     * @param resource
     * @param textViewResourceId
     * @param objects
     */
    public PlantsAdapter(Context context, int resource, int textViewResourceId, List<Plant> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
        this.layoutInflater = ((Activity)context).getLayoutInflater();
        createImages();
    }

    private void createImages() {
        images = new ArrayList<>();
        Bitmap cyclamen = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cyclamen);
        Bitmap wheat = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.wheat);
        images.add(cyclamen);
        images.add(wheat);
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

        View view = layoutInflater.inflate(R.layout.listplants,parent,false);
        TextView nameOfPlant=(TextView) view.findViewById(R.id.tvPlantName);
        ImageView pic=(ImageView)view.findViewById(R.id.imgPlantPic);
        TextView reminder=(TextView)view.findViewById(R.id.tvPlantReminder);

        Plant plant_obj = objects.get(position);

        pic.setImageBitmap(images.get(plant_obj.getPicNum()));
        nameOfPlant.setText(plant_obj.getName());
        return view;
    }

    public void setPlants(ArrayList<Plant> arrPlants) {
        objects = arrPlants;
    }
}
