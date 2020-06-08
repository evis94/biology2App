package com.example.shahar.biologyapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This Activity manages the schedule of the garden
 * The data is saved using Firebase real time database
 * @author  Shahar Chen
 * @version 1.0
 */
public class Activity_Schedule extends AppCompatActivity implements View.OnClickListener {

    /** a button to open the editing option */
    Button editSchedule;
    /**
     * F= Friday, Th= Thursday, W= Wednesday, Tue= Tuesday, M= Monday, S= Sunday, H= number of hour (what hour it is)
     * the numbers indicate the hours
     */
    EditText F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11;
    EditText Th1, Th2, Th3, Th4, Th5, Th6, Th7, Th8, Th9, Th10, Th11;
    EditText W1, W2, W3, W4, W5, W6, W7, W8, W9, W10, W11;
    EditText Tue1, Tue2, Tue3, Tue4, Tue5, Tue6, Tue7, Tue8, Tue9, Tue10, Tue11;
    EditText M1, M2, M3, M4, M5, M6, M7, M8, M9, M10, M11;
    EditText S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11;
    EditText H1, H2, H3, H4, H5, H6, H7, H8, H9, H10, H11;
    /**
     *An array that holds all of the schedules
     * row first, then column
     */
    Schedule[][] schedule_arr = new Schedule[11][6];
    /** an array that holds all of the Edit Text objects of the hours*/
    EditText[] hours_arr = new EditText[11];
    /** an array that holds all of the Edit Text objects of the days*/
    EditText[][] for_editing_premissions = new EditText[11][7];

    EditText friday, thursday, wednesday, tuesday, monday, sunday;

    boolean isAllowedtoEdit, flag;

    FirebaseDatabase database;
    DatabaseReference schedule_Ref, schedule_from_database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);

        friday = (EditText) findViewById(R.id.etFriday);
        thursday = (EditText) findViewById(R.id.etThursday);
        wednesday = (EditText) findViewById(R.id.etWednesday);
        tuesday = (EditText) findViewById(R.id.etTuesday);
        monday = (EditText) findViewById(R.id.etMonday);
        sunday = (EditText) findViewById(R.id.etSunday);
        friday.setEnabled(false);
        thursday.setEnabled(false);
        wednesday.setEnabled(false);
        tuesday.setEnabled(false);
        monday.setEnabled(false);
        sunday.setEnabled(false);


        flag = false;
        isAllowedtoEdit = false;

        /** pulls the information of if the user is a manager from the intent*/
        Bundle data = getIntent().getExtras();
        if (data != null)
            isAllowedtoEdit = data.getBoolean("allowed");


        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();


        schedule_from_database = FirebaseDatabase.getInstance().getReference("schedule");
        schedule_Ref = database.getReference("schedule").push();
        this.RetrieveData();


        editSchedule = (Button) findViewById(R.id.btEditSchedule);
        editSchedule.setOnClickListener(this);

        F1 = (EditText) findViewById(R.id.etF1);
        F2 = (EditText) findViewById(R.id.etF2);
        F3 = (EditText) findViewById(R.id.etF3);
        F4 = (EditText) findViewById(R.id.etF4);
        F5 = (EditText) findViewById(R.id.etF5);
        F6 = (EditText) findViewById(R.id.etF6);
        F7 = (EditText) findViewById(R.id.etF7);
        F8 = (EditText) findViewById(R.id.etF8);
        F9 = (EditText) findViewById(R.id.etF9);
        F10 = (EditText) findViewById(R.id.etF10);
        F11 = (EditText) findViewById(R.id.etF11);

        Th1 = (EditText) findViewById(R.id.etTh1);
        Th2 = (EditText) findViewById(R.id.etTh2);
        Th3 = (EditText) findViewById(R.id.etTh3);
        Th4 = (EditText) findViewById(R.id.etTh4);
        Th5 = (EditText) findViewById(R.id.etTh5);
        Th6 = (EditText) findViewById(R.id.etTh6);
        Th7 = (EditText) findViewById(R.id.etTh7);
        Th8 = (EditText) findViewById(R.id.etTh8);
        Th9 = (EditText) findViewById(R.id.etTh9);
        Th10 = (EditText) findViewById(R.id.etTh10);
        Th11 = (EditText) findViewById(R.id.etTh11);

        W1 = (EditText) findViewById(R.id.etW1);
        W2 = (EditText) findViewById(R.id.etW2);
        W3 = (EditText) findViewById(R.id.etW3);
        W4 = (EditText) findViewById(R.id.etW4);
        W5 = (EditText) findViewById(R.id.etW5);
        W6 = (EditText) findViewById(R.id.etW6);
        W7 = (EditText) findViewById(R.id.etW7);
        W8 = (EditText) findViewById(R.id.etW8);
        W9 = (EditText) findViewById(R.id.etW9);
        W10 = (EditText) findViewById(R.id.etW10);
        W11 = (EditText) findViewById(R.id.etW11);

        Tue1 = (EditText) findViewById(R.id.etTue1);
        Tue2 = (EditText) findViewById(R.id.etTue2);
        Tue3 = (EditText) findViewById(R.id.etTue3);
        Tue4 = (EditText) findViewById(R.id.etTue4);
        Tue5 = (EditText) findViewById(R.id.etTue5);
        Tue6 = (EditText) findViewById(R.id.etTue6);
        Tue7 = (EditText) findViewById(R.id.etTue7);
        Tue8 = (EditText) findViewById(R.id.etTue8);
        Tue9 = (EditText) findViewById(R.id.etTue9);
        Tue10 = (EditText) findViewById(R.id.etTue10);
        Tue11 = (EditText) findViewById(R.id.etTue11);

        M1 = (EditText) findViewById(R.id.etM1);
        M2 = (EditText) findViewById(R.id.etM2);
        M3 = (EditText) findViewById(R.id.etM3);
        M4 = (EditText) findViewById(R.id.etM4);
        M5 = (EditText) findViewById(R.id.etM5);
        M6 = (EditText) findViewById(R.id.etM6);
        M7 = (EditText) findViewById(R.id.etM7);
        M8 = (EditText) findViewById(R.id.etM8);
        M9 = (EditText) findViewById(R.id.etM9);
        M10 = (EditText) findViewById(R.id.etM10);
        M11 = (EditText) findViewById(R.id.etM11);

        S1 = (EditText) findViewById(R.id.etS1);
        S2 = (EditText) findViewById(R.id.etS2);
        S3 = (EditText) findViewById(R.id.etS3);
        S4 = (EditText) findViewById(R.id.etS4);
        S5 = (EditText) findViewById(R.id.etS5);
        S6 = (EditText) findViewById(R.id.etS6);
        S7 = (EditText) findViewById(R.id.etS7);
        S8 = (EditText) findViewById(R.id.etS8);
        S9 = (EditText) findViewById(R.id.etS9);
        S10 = (EditText) findViewById(R.id.etS10);
        S11 = (EditText) findViewById(R.id.etS11);

        H1 = (EditText) findViewById(R.id.etHour1);
        H2 = (EditText) findViewById(R.id.etHour2);
        H3 = (EditText) findViewById(R.id.etHour3);
        H4 = (EditText) findViewById(R.id.etHour4);
        H5 = (EditText) findViewById(R.id.etHour5);
        H6 = (EditText) findViewById(R.id.etHour6);
        H7 = (EditText) findViewById(R.id.etHour7);
        H8 = (EditText) findViewById(R.id.etHour8);
        H9 = (EditText) findViewById(R.id.etHour9);
        H10 = (EditText) findViewById(R.id.etHour10);
        H11 = (EditText) findViewById(R.id.etHour11);


        schedule_arr[0][0] = new Schedule("Friday", H1.getText().toString(), F1.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[0][0]);

        schedule_arr[1][0] = new Schedule("Friday", H2.getText().toString(), F2.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[1][0]);

        schedule_arr[2][0] = new Schedule("Friday", H3.getText().toString(), F3.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[2][0]);

        schedule_arr[3][0] = new Schedule("Friday", H4.getText().toString(), F4.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[3][0]);

        schedule_arr[4][0] = new Schedule("Friday", H5.getText().toString(), F5.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[4][0]);

        schedule_arr[5][0] = new Schedule("Friday", H6.getText().toString(), F6.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[5][0]);

        schedule_arr[6][0] = new Schedule("Friday", H7.getText().toString(), F7.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[6][0]);

        schedule_arr[7][0] = new Schedule("Friday", H8.getText().toString(), F8.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[7][0]);

        schedule_arr[8][0] = new Schedule("Friday", H9.getText().toString(), F9.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[8][0]);

        schedule_arr[9][0] = new Schedule("Friday", H10.getText().toString(), F10.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[9][0]);

        schedule_arr[10][0] = new Schedule("Friday", H11.getText().toString(), F11.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[10][0]);

        /////////////////////////////////////////////////////////////////

        schedule_arr[0][1] = new Schedule("Thursday", H1.getText().toString(), Th1.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[0][1]);

        schedule_arr[1][1] = new Schedule("Thursday", H2.getText().toString(), Th2.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[1][1]);

        schedule_arr[2][1] = new Schedule("Thursday", H3.getText().toString(), Th3.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[2][1]);

        schedule_arr[3][1] = new Schedule("Thursday", H4.getText().toString(), Th4.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[3][1]);

        schedule_arr[4][1] = new Schedule("Thursday", H5.getText().toString(), Th5.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[4][1]);

        schedule_arr[5][1] = new Schedule("Thursday", H6.getText().toString(), Th6.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[5][1]);

        schedule_arr[6][1] = new Schedule("Thursday", H7.getText().toString(), Th7.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[6][1]);

        schedule_arr[7][1] = new Schedule("Thursday", H8.getText().toString(), Th8.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[7][1]);

        schedule_arr[8][1] = new Schedule("Thursday", H9.getText().toString(), Th9.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[8][1]);

        schedule_arr[9][1] = new Schedule("Thursday", H10.getText().toString(), Th10.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[9][1]);

        schedule_arr[10][1] = new Schedule("Thursday", H11.getText().toString(), Th11.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[10][1]);

        ////////////////////////////////////////////////////////////////////////////////

        schedule_arr[0][2] = new Schedule("Wednesday", H1.getText().toString(), W1.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[0][2]);

        schedule_arr[1][2] = new Schedule("Wednesday", H2.getText().toString(), W2.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[1][2]);

        schedule_arr[2][2] = new Schedule("Wednesday", H3.getText().toString(), W3.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        ;
        schedule_Ref.setValue(schedule_arr[2][2]);

        schedule_arr[3][2] = new Schedule("Wednesday", H4.getText().toString(), W4.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[3][2]);

        schedule_arr[4][2] = new Schedule("Wednesday", H5.getText().toString(), W5.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[4][2]);

        schedule_arr[5][2] = new Schedule("Wednesday", H6.getText().toString(), W6.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[5][2]);

        schedule_arr[6][2] = new Schedule("Wednesday", H7.getText().toString(), W7.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[6][2]);

        schedule_arr[7][2] = new Schedule("Wednesday", H8.getText().toString(), W8.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[7][2]);

        schedule_arr[8][2] = new Schedule("Wednesday", H9.getText().toString(), W9.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[8][2]);

        schedule_arr[9][2] = new Schedule("Wednesday", H10.getText().toString(), W10.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[9][2]);

        schedule_arr[10][2] = new Schedule("Wednesday", H11.getText().toString(), W11.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[10][2]);

        //////////////////////////////////////////////////////////////////////////////

        schedule_arr[0][3] = new Schedule("Tuesday", H1.getText().toString(), Tue1.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[0][3]);

        schedule_arr[1][3] = new Schedule("Tuesday", H2.getText().toString(), Tue2.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[1][3]);

        schedule_arr[2][3] = new Schedule("Tuesday", H3.getText().toString(), Tue3.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[2][3]);

        schedule_arr[3][3] = new Schedule("Tuesday", H4.getText().toString(), Tue4.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[3][3]);

        schedule_arr[4][3] = new Schedule("Tuesday", H5.getText().toString(), Tue5.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[4][3]);

        schedule_arr[5][3] = new Schedule("Tuesday", H6.getText().toString(), Tue6.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[5][3]);

        schedule_arr[6][3] = new Schedule("Tuesday", H7.getText().toString(), Tue7.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[6][3]);

        schedule_arr[7][3] = new Schedule("Tuesday", H8.getText().toString(), Tue8.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[7][3]);

        schedule_arr[8][3] = new Schedule("Tuesday", H9.getText().toString(), Tue9.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[8][3]);

        schedule_arr[9][3] = new Schedule("Tuesday", H10.getText().toString(), Tue10.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[9][3]);

        schedule_arr[10][3] = new Schedule("Tuesday", H11.getText().toString(), Tue11.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[10][3]);

        ////////////////////////////////////////////////////////////////////////////

        schedule_arr[0][4] = new Schedule("Monday", H1.getText().toString(), Tue1.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[0][4]);

        schedule_arr[1][4] = new Schedule("Monday", H2.getText().toString(), Tue2.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[1][4]);

        schedule_arr[2][4] = new Schedule("Monday", H3.getText().toString(), Tue3.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[2][4]);

        schedule_arr[3][4] = new Schedule("Monday", H4.getText().toString(), Tue4.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[3][4]);

        schedule_arr[4][4] = new Schedule("Monday", H5.getText().toString(), Tue5.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[4][4]);

        schedule_arr[5][4] = new Schedule("Monday", H6.getText().toString(), Tue6.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[5][4]);

        schedule_arr[6][4] = new Schedule("Monday", H7.getText().toString(), Tue7.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[6][4]);

        schedule_arr[7][4] = new Schedule("Monday", H8.getText().toString(), Tue8.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[7][4]);

        schedule_arr[8][4] = new Schedule("Monday", H9.getText().toString(), Tue9.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[8][4]);

        schedule_arr[9][4] = new Schedule("Monday", H10.getText().toString(), Tue10.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[9][4]);

        schedule_arr[10][4] = new Schedule("Monday", H11.getText().toString(), Tue11.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[10][4]);

        ///////////////////////////////////////////////////////////////////////////////////////////

        schedule_arr[0][5] = new Schedule("Sunday", H1.getText().toString(), S1.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[0][5]);

        schedule_arr[1][5] = new Schedule("Sunday", H2.getText().toString(), S2.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[1][5]);

        schedule_arr[2][5] = new Schedule("Sunday", H3.getText().toString(), S3.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[2][5]);

        schedule_arr[3][5] = new Schedule("Sunday", H4.getText().toString(), S4.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[3][5]);

        schedule_arr[4][5] = new Schedule("Sunday", H5.getText().toString(), S5.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[4][5]);

        schedule_arr[5][5] = new Schedule("Sunday", H6.getText().toString(), S6.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[5][5]);

        schedule_arr[6][5] = new Schedule("Sunday", H7.getText().toString(), S7.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[6][5]);

        schedule_arr[7][5] = new Schedule("Sunday", H8.getText().toString(), S8.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[7][5]);

        schedule_arr[8][5] = new Schedule("Sunday", H9.getText().toString(), S9.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[8][5]);

        schedule_arr[9][5] = new Schedule("Sunday", H10.getText().toString(), S10.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[9][5]);

        schedule_arr[10][5] = new Schedule("Sunday", H11.getText().toString(), S11.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), schedule_Ref.getKey());
        schedule_Ref.setValue(schedule_arr[10][5]);

        ////////////////////////////////////////////////////////

        hours_arr[0] = H1;
        hours_arr[1] = H2;
        hours_arr[2] = H3;
        hours_arr[3] = H4;
        hours_arr[4] = H5;
        hours_arr[5] = H6;
        hours_arr[6] = H7;
        hours_arr[7] = H8;
        hours_arr[8] = H9;
        hours_arr[9] = H10;
        hours_arr[10] = H11;


        //(from before the type "Schedule" was created)
        //to keep guests away from editing
        for_editing_premissions[0][0] = F1;
        for_editing_premissions[1][0] = F2;
        for_editing_premissions[2][0] = F3;
        for_editing_premissions[3][0] = F4;
        for_editing_premissions[4][0] = F5;
        for_editing_premissions[5][0] = F6;
        for_editing_premissions[6][0] = F7;
        for_editing_premissions[7][0] = F8;
        for_editing_premissions[8][0] = F9;
        for_editing_premissions[9][0] = F10;
        for_editing_premissions[10][0] = F11;

        for_editing_premissions[0][1] = Th1;
        for_editing_premissions[1][1] = Th2;
        for_editing_premissions[2][1] = Th3;
        for_editing_premissions[3][1] = Th4;
        for_editing_premissions[4][1] = Th5;
        for_editing_premissions[5][1] = Th6;
        for_editing_premissions[6][1] = Th7;
        for_editing_premissions[7][1] = Th8;
        for_editing_premissions[8][1] = Th9;
        for_editing_premissions[9][1] = Th10;
        for_editing_premissions[10][1] = Th11;

        for_editing_premissions[0][2] = W1;
        for_editing_premissions[1][2] = W2;
        for_editing_premissions[2][2] = W3;
        for_editing_premissions[3][2] = W4;
        for_editing_premissions[4][2] = W5;
        for_editing_premissions[5][2] = W6;
        for_editing_premissions[6][2] = W7;
        for_editing_premissions[7][2] = W8;
        for_editing_premissions[8][2] = W9;
        for_editing_premissions[9][2] = W10;
        for_editing_premissions[10][2] = W11;

        for_editing_premissions[0][3] = Tue1;
        for_editing_premissions[1][3] = Tue2;
        for_editing_premissions[2][3] = Tue3;
        for_editing_premissions[3][3] = Tue4;
        for_editing_premissions[4][3] = Tue5;
        for_editing_premissions[5][3] = Tue6;
        for_editing_premissions[6][3] = Tue7;
        for_editing_premissions[7][3] = Tue8;
        for_editing_premissions[8][3] = Tue9;
        for_editing_premissions[9][3] = Tue10;
        for_editing_premissions[10][3] = Tue11;

        for_editing_premissions[0][4] = M1;
        for_editing_premissions[1][4] = M2;
        for_editing_premissions[2][4] = M3;
        for_editing_premissions[3][4] = M4;
        for_editing_premissions[4][4] = M5;
        for_editing_premissions[5][4] = M6;
        for_editing_premissions[6][4] = M7;
        for_editing_premissions[7][4] = M8;
        for_editing_premissions[8][4] = M9;
        for_editing_premissions[9][4] = M10;
        for_editing_premissions[10][4] = M11;

        for_editing_premissions[0][5] = S1;
        for_editing_premissions[1][5] = S2;
        for_editing_premissions[2][5] = S3;
        for_editing_premissions[3][5] = S4;
        for_editing_premissions[4][5] = S5;
        for_editing_premissions[5][5] = S6;
        for_editing_premissions[6][5] = S7;
        for_editing_premissions[7][5] = S8;
        for_editing_premissions[8][5] = S9;
        for_editing_premissions[9][5] = S10;
        for_editing_premissions[10][5] = S11;

        for_editing_premissions[0][6] = H1;
        for_editing_premissions[1][6] = H2;
        for_editing_premissions[2][6] = H3;
        for_editing_premissions[3][6] = H4;
        for_editing_premissions[4][6] = H5;
        for_editing_premissions[5][6] = H6;
        for_editing_premissions[6][6] = H7;
        for_editing_premissions[7][6] = H8;
        for_editing_premissions[8][6] = H9;
        for_editing_premissions[9][6] = H10;
        for_editing_premissions[10][6] = H11;

        //the user can set the enables to "true" again by pressing the esit button- if they have the authorisation for it
        for (int i = 0; i < schedule_arr.length; i++) {
            hours_arr[i].setEnabled(false);
            close_or_open_Enabled(false);

            }
        }

     /** retrieves the schedule data from the Firebase database and updates the info in the array */
    public void RetrieveData() {
        schedule_from_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (int i = 0; i < schedule_arr.length; i++) {
                        for (int j = 0; j < schedule_arr[i].length; j++) {
                            Schedule schedule = data.getValue(Schedule.class);
                            if (schedule.getKey() == schedule_arr[i][j].getKey())
                                schedule_arr[i][j].setBody(schedule.getBody());
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View view) {

        /** opens the edit option if the user is a manager */
        if (editSchedule.getId() == view.getId()) {
            if (isAllowedtoEdit == true) {
                close_or_open_Enabled(true);
            }

            if (flag == false) {
                editSchedule.setText("Edit the schedule");
                flag = true;
                close_or_open_Enabled(false);


            } else {
                editSchedule.setText("Go back to viewing mode only");
                flag = false;
                notification();

            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
     * This method opens or closes the editing option of the edit text according to the parameter
     * @param need This establishes if the editing option would be opened (true) or closed (false)
     * @return nothing
     * */
    public void close_or_open_Enabled(boolean need) {
        for (int i = 0; i < schedule_arr.length; i++) {
            for (int j = 0; j < schedule_arr[i].length; j++) {
                for_editing_premissions[i][j].setEnabled(need);
            }
        }
    }

    /**
     * This method uses Pending Intent to notify the user every time the schedule is opened to editing
     * @return nothing
     */
    public void notification()
        {
            //making a notification whenever someone opens it up for changes:
            int icon = R.drawable.icon;
            long when = System.currentTimeMillis();
            String title = "The schedule hs been open to editing";
            String ticker = "ticker";

            PendingIntent pendingIntent = PendingIntent.getActivity(Activity_Schedule.this, 0, new Intent(), 0, null);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //O (Oreo) is because the notification channel needs API of 26
                String channelId = "YOUR_CHANNEL_ID";
                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }


            Notification notification = builder.setContentIntent(pendingIntent)
                    .setSmallIcon(icon).setTicker(ticker).setWhen(when)
                    .setAutoCancel(true).setContentTitle(title)
                    .setContentText("Press here to view the changes").build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            notificationManager.notify(1, notification);
            // end of making a notification
        }

    }

