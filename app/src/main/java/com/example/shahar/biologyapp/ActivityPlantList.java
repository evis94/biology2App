package com.example.shahar.biologyapp;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This Activity manages the plants information
 * The data is saved using Firebase real time database
 * @author  Shahar Chen
 * @version 1.0
 */
public class ActivityPlantList extends AppCompatActivity implements AdapterView.OnItemLongClickListener, View.OnClickListener {

    ListView list_plants;
    PlantsAdapter PlantAdapter; //adapter
    static int plant_index_arr=0;
    Button add;
    public static final int GET_FROM_GALLERY=1;
    private static int RESULT_LOAD_IMAGE = 1;
    final int MY_CAMERA_REQUEST_CODE=1;

    Dialog plant_details_dialog;
    EditText plant_name,plant_place,plant_watering,plant_type, plant_state;
    Button EditPlant, save_details;
    ImageButton img;

    DatabaseReference plantsDbRef;

    TextView plant_message;

    //Service stuff
    ReceiverTimer myReceiver;
    public static final String FILTER_ACTION_KEY = "any_key";
    int hourCount=0;
    boolean saveFlag=false;
    SharedPreferences sp1;

    boolean allowed_inPlants=false;
    private ArrayList<Plant> arrPlants;
    private Plant plant_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        /** pulls the information of if the user is a manager from the intent*/
        Bundle data= getIntent().getExtras();
        if(data!=null)
            allowed_inPlants=data.getBoolean("allowed");



        View fromPlantList = getLayoutInflater().inflate(R.layout.listplants,null);
        plant_message=(TextView)fromPlantList.findViewById(R.id.tvPlantReminder);

        Log.d("--------------", "got to plant list activity");

        sp1=getSharedPreferences("time",0);

        plantsDbRef =FirebaseDatabase.getInstance().getReference("Plants");

        list_plants=(ListView)findViewById(R.id.lvList);
        list_plants.setOnItemLongClickListener(this);

//        initializeDataFromDB();
        addChildEventListener();

      //service stuff
        sp1=getSharedPreferences("time",0);
        SharedPreferences.Editor editor=sp1.edit();
        editor.putInt("count",hourCount);
        editor.commit();

        if(plant_index_arr!=0) {
            Intent intent = new Intent(this, ServiceTimer.class);
            int send = sp1.getInt("count", 0);
            intent.putExtra("count", send);
            startService(intent);
        }

        add=(Button)findViewById(R.id.btAddPlantToList);
        add.setOnClickListener(this);

        //start dialog
        plant_details_dialog= new Dialog(this);
        plant_details_dialog.setContentView(R.layout.single_plant_page);
        plant_details_dialog.setTitle("The plant's details");
        plant_details_dialog.setCancelable(true);

        plant_name=(EditText)plant_details_dialog.findViewById(R.id.etPlantName);
        plant_place=(EditText)plant_details_dialog.findViewById(R.id.etPosition);
        plant_state=(EditText)plant_details_dialog.findViewById(R.id.etPhysicalSituation);
        plant_watering=(EditText)plant_details_dialog.findViewById(R.id.etWatering);
        plant_type=(EditText)plant_details_dialog.findViewById(R.id.etPlantType);
        img=(ImageButton)plant_details_dialog.findViewById(R.id.btimgDecoration);
        img.setOnClickListener(this);
        EditPlant=(Button)plant_details_dialog.findViewById(R.id.btEditPlant);
        EditPlant.setOnClickListener(this);
        save_details=(Button)plant_details_dialog.findViewById(R.id.btSavePlantDetails);
        save_details.setOnClickListener(this);

        //end dialog





        if(allowed_inPlants==false)
            EditPlant.setEnabled(false);
        else
            EditPlant.setEnabled(true);





    }

    private void addChildEventListener() {

        plantsDbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Plant plant = dataSnapshot.getValue(Plant.class);
                if (PlantAdapter == null){
                    arrPlants = new ArrayList<>();
                    PlantAdapter = new PlantsAdapter(ActivityPlantList.this,0,0,arrPlants);
                    list_plants.setAdapter(PlantAdapter);
                }
                PlantAdapter.add(plant);
                PlantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Plant plant = dataSnapshot.getValue(Plant.class);
                PlantAdapter.remove(plant);
                PlantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeDataFromDB() {


        arrPlants = new ArrayList<>();
        plantsDbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Plant p = ds.getValue(Plant.class);
                    arrPlants.add(p);
                }

                if (PlantAdapter == null){
                    PlantAdapter = new PlantsAdapter(ActivityPlantList.this,0,0,arrPlants);
                    list_plants.setAdapter(PlantAdapter);
                }else{
                    PlantAdapter.setPlants(arrPlants);
                    PlantAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * This method takes a picture or pulls one from the user's phone's gallery using two other methods that it calls
     */
    public void AddPic()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Where would you like to take the picture from?");
        builder.setCancelable(true);
        builder.setNegativeButton("Take a picture now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    String [] permission = {Manifest.permission.CAMERA};
                    ActivityCompat.requestPermissions(getParent(),permission,MY_CAMERA_REQUEST_CODE);
                }
                else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,0);
                }
            }
            });

        builder.setPositiveButton("From my gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Intent in = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(in, RESULT_LOAD_IMAGE);
                startActivityForResult(new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),GET_FROM_GALLERY);
            }
        });
    }

    /**
     * This method displays an alert message to the user
     */
    public void OpenDialog()
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

    /**
     * This method pulls a picture from the user's phone's gallery
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0)//if it's from the camera
        {
            if (resultCode == RESULT_OK) {
                img.setImageBitmap((Bitmap) data.getExtras().get("data"));
            }
        }
        if ((requestCode == RESULT_LOAD_IMAGE) && (resultCode == RESULT_OK && null != data)) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String  picPath = cursor.getString(columnIndex); //contains the path of selected Image
            cursor.close();
            img.setImageDrawable(Drawable.createFromPath(picPath));
        }
    }

    /**
     * This method takes a picture
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);

            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }}

    /**
     * This method gives a manager user the option to delete or edit the plant's information
     * If the user id not a manager an alert message will be displayed to the user
     * This method also opens a dialog with the plant's information
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return false
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


        if(allowed_inPlants==true)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete the plant's information?");
            builder.setCancelable(true);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PlantAdapter.remove(PlantAdapter.getItem(position));
                    PlantAdapter.notifyDataSetChanged();
                    arrPlants.remove(arrPlants.indexOf(PlantAdapter.getItem(position)));

                    for (int j=arrPlants.indexOf(PlantAdapter.getItem(position)); j<plant_index_arr-1;j++)
                    {
                        arrPlants.set(j,arrPlants.get(j+1));
                    }
                    plant_index_arr--;

                }
            });
            builder.setNeutralButton("view the plant's information", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    plant_details_dialog.show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            hourCount=0;

        }
        else
        {
           OpenDialog();
            plant_details_dialog.show();
        }

        return false;
    }


    /**
     * This is a private a method that is only used by the application
     * The methods uses a service to time how many hours has passed which is saved using SharePreference
     */
    private void TimeIt()
    {
        Intent intent = new Intent(this, ServiceTimer.class);
        int send= sp1.getInt("count",0);
        Log.d("--- time it 's send",String.valueOf(send));
        intent.putExtra("count",send);
        if(saveFlag==true) {
            intent.putExtra("flag", true);
            Log.d("-------","save flag is true");
        }
        else
            intent.putExtra("flag",false);
        startService(intent);

    }

    /**
     * This method creates a new ReceiverTimer object to be used by the broadcast receiver
     */
    private void setReceiver() {
        myReceiver = new ReceiverTimer();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FILTER_ACTION_KEY);

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);

    }

    @Override
    protected void onStart() {
        setReceiver();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    /**
     * @author  Shahar Chen
     * @version 1.0
     */
    public class ReceiverTimer extends BroadcastReceiver {
        /**
         * This method uses broadcast receiver to update how many hours past since the last time the information was edited
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("broadcastMessage");
            plant_message.setText("");
            if (saveFlag==false)
                plant_message.setText(plant_message.getText() + "\n" + message);
            else {
                stopService(intent);
                plant_message.setText( "Hours that past since the last edit: 0" );
                saveFlag=false;
            }


            int send= sp1.getInt("count",0);
            plant_details.setRemind(String.valueOf(send));
            Log.d("-----inside class send",String.valueOf(sp1.getInt("count",0)));
            SharedPreferences.Editor editor = sp1.edit();
            editor.putInt("count",send+1);
            editor.commit();
            TimeIt();

        }
    }



    @Override
    public void onClick(View view) {

        /**
         * Creates a new plant object and adds it to the listview if the user is a manager
         * If the user is not a manager an alert message will be displayed
         */
        if(view.getId()==add.getId())
        {

            if(allowed_inPlants==true) {
               addPlantToDB();
//                AddPic();
            }
            else{
                OpenDialog();
            }
        }


        else if (view.getId()==EditPlant.getId())
        {
            /**
             * opens the plant's information to editing and updates its data if the user is a manager
             * If the user is not a manager an alert message will be displayed
             */
            if(allowed_inPlants==true)
            {
                SharedPreferences.Editor editor=sp1.edit();
                editor.putInt("count",0);
                saveFlag=true;
                editor.commit();
                plant_message.setText( "Hours since the last edit: 0" );

                plant_name.setEnabled(true);
                plant_place.setEnabled(true);
                plant_state.setEnabled(true);
                plant_watering.setEnabled(true);
                plant_type.setEnabled(true);
                plant_name=(EditText)plant_details_dialog.findViewById(R.id.etPlantName);
                plant_place=(EditText)plant_details_dialog.findViewById(R.id.etPosition);
                plant_state=(EditText)plant_details_dialog.findViewById(R.id.etPhysicalSituation);
                plant_watering=(EditText)plant_details_dialog.findViewById(R.id.etWatering);
                plant_type=(EditText)plant_details_dialog.findViewById(R.id.etPlantType);

//                RetrieveData();


            }
            else
            {
                OpenDialog();
            }
        }

        else if(view.getId()==img.getId())
        {
            /**
             * adds a picture to the plant if the user is a manager
             * If the user is not a manager an alert message will be displayed
             */
            if(allowed_inPlants==true)
            {
               AddPic();
            }
            else
            {
               OpenDialog();

            }

        }
        else if (view.getId()==save_details.getId())
        {
            plant_details_dialog.dismiss();

        }

    }


    /*********************************************************************************************/

    private void addPlantToDB(){

        plant_details_dialog.show();

        Plant plant = new Plant(plant_name.getText().toString(),
                plant_place.getText().toString(),
                plant_watering.getText().toString(),
                plant_type.getText().toString(),
                plant_state.getText().toString(),
                "");

        clearPlantdetailsDialogEditTextValues();

        String key = plantsDbRef.push().getKey();
        plant.setKey(key);

        plantsDbRef.child(key).setValue(plant);
    }

    private void clearPlantdetailsDialogEditTextValues() {

        plant_name.setText("");
        plant_place.setText("");
        plant_watering.setText("");
        plant_type.setText("");
        plant_state.setText("");
    }


}
