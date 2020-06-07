package com.example.shahar.biologyapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

/**
 * This method which inherits from the Main Activity is used to sign up a new manager and to change the manager password
 * The data for the new manager is saved using the Firebase real time database
 * The manager password is saved using SharePreference
 * @author  Shahar Chen
 * @version 1.0
 */
public class SignUp_Activity extends MainActivity implements View.OnClickListener  {

    Button  sigh_up, change_password;
    Dialog pass_change_dialog;
    EditText new_pass, manager_password;
    Button change_pass_button;
    String gottenPassword="";
    FirebaseUser user;
    EditText email_su, password_su;
    boolean failed_to_sighUp= false;
    EditText oldPass;
    boolean allowed_to_change_pass= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigh_up_);
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        change_password=(Button) findViewById(R.id.btPassChange);
        sigh_up=(Button) findViewById(R.id.btSighUp);
        sigh_up.setOnClickListener(this);
        change_password.setOnClickListener(this);
        manager_password=(EditText)findViewById(R.id.etManagerPass);
        gottenPassword= manager_password.getText().toString();

        Log.d("-------HERE",gottenPassword);
        Log.d("------",manager_password.getText().toString());

        email_su=(EditText)findViewById(R.id.etEmailSU);
        password_su=(EditText)findViewById(R.id.etEmailPasswordSU);

        Log.d("------ email",email_su.getText().toString());

        /** pulls the information of if the user is a manager from the intent*/
        Bundle data= getIntent().getExtras();
        if(data!=null)
            allowed_to_change_pass=data.getBoolean("allowed");
        if(allowed_to_change_pass==false)
            change_password.setEnabled(false);




    }

    /**
     * This method accepts an email and a password and signs up a new user a manager
     * The new manager information is saved using Firebase
     */
    public void SignUp() {

            mAuth.createUserWithEmailAndPassword(email_su.getText().toString(), password_su.getText().toString()).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        /** Sign in success, update UI with the signed-in user's information */
                        Log.d("----------------", "createUserWithEmail:success");
                        user = mAuth.getCurrentUser();
                        allowed_to_change_pass=true;
                        change_password.setEnabled(true);
                        failed_to_sighUp=false;
                    } else {
                        /** If sign in fails, display a message to the user: */
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUp_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        failed_to_sighUp=true;

                    }

                }
            });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == sigh_up.getId())
        {
            /** Signs up a new user as a manager if the manager password they wrote is correct and displays an alert message if not */
            if(isPasswordCorrect()==true) {
                SignUp();
                Log.d("-----------------", "after sighup");
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sign up failed. The manager password is incorrect");
                builder.setCancelable(true);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            if(failed_to_sighUp==false)
            {
                /** If the user was signed up as a manager- they are moved to the guest activity. If not an alert message will be displayed */
                Intent in = new Intent(getApplicationContext(), Activity_Guest.class);
                startActivity(in);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sign up failed. The email or password is incorrect");
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
        else if(v.getId()==change_password.getId())
        {
            /**
             * opens a dialog where the user can write in the new manager password
             */
            pass_change_dialog= new Dialog(getApplicationContext());
            pass_change_dialog.setContentView(R.layout.change_manager_password);
            pass_change_dialog.setTitle("Change password");
            pass_change_dialog.setCancelable(true);
            new_pass=(EditText)pass_change_dialog.findViewById(R.id.etNewPassword);
            oldPass=(EditText)pass_change_dialog.findViewById(R.id.etOldPassword);
            change_pass_button=(Button)pass_change_dialog.findViewById(R.id.btChangePassword);
            change_pass_button.setOnClickListener(this);
            pass_change_dialog.show();
        }
        else if((v.getId()==change_pass_button.getId()) &&(sp.getString("password",null)==oldPass.getText().toString()))
        {
            /**
             * Changes the manager password that is saved using SharePreference if the current password that the user put in is correct
             * If it is not an alert message will be displayed to the user
             */
            if(((new_pass.toString()!="")||(new_pass.toString()!= null)))
            {
                sp=getSharedPreferences("manager_password",0);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("password",new_pass.getText().toString());
                editor.commit();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("The new password can't be empty of characters");
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
        else if(sp.getString("password",null)!= oldPass.getText().toString())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("The current password is wrong");
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
     * This method compares the current password (which is saved using SharePreference) and the manager password the user has put in
     * @return "true" if the two passwords are identical and "false" if not
     */
    public boolean isPasswordCorrect()
    {
        Log.d("----- password",sp.getString("password",null));
        if(!manager_password.getText().toString().equals(sp.getString("password",null)))
            return false;
        else
            Log.d("------","passwords match");
        return true;
    }
}
