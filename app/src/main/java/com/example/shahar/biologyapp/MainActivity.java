package com.example.shahar.biologyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.annotations.NotNull;

/**
 * This Activity is the main activity of the project
 * It includes sighing in to Firebase and is a leading point to other activities such as the Guest Activity and the SighUP Activity
 *
 * @author Shahar Chen
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    protected FirebaseAuth mAuth;
    Button btnSignIn, btnGoToSignUpActivity, btnGoToGuestActivity, btnSignOut, btnGoToContent;
    View viewPasswordContainer, viewEmailContainer;
    TextView tvUserEmail, tvInstructions;
    EditText password, email;
    String currentPassword;
    ImageView img;
    boolean allowed = false;
    FirebaseUser currentUser;

    SharedPreferences sp;


    public static final String TAG = "mainactivity";

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Initialize Firebase Auth */
        mAuth = FirebaseAuth.getInstance();

        /** saves the manager passwords (not the users password- the general one) */
        sp = getSharedPreferences("manager_password", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", "garden");
        editor.commit();

        viewEmailContainer = findViewById(R.id.emailContainer);
        viewPasswordContainer = findViewById(R.id.passwordContainer);
        btnSignIn = (Button) findViewById(R.id.btSighIn);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnGoToContent = (Button) findViewById(R.id.btnGoToContent);
        password = (EditText) findViewById(R.id.etPassword);
        email = (EditText) findViewById(R.id.etInputEmail);
        tvUserEmail = (TextView) findViewById(R.id.tvUserEmail);
        tvInstructions = (TextView) findViewById(R.id.tvInstructions);
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnGoToContent.setOnClickListener(this);

        currentPassword = "garden";
        img = (ImageView) findViewById(R.id.imgDecoration);
        btnGoToSignUpActivity = (Button) findViewById(R.id.btToSighUp);
        btnGoToSignUpActivity.setOnClickListener(this);
        btnGoToGuestActivity = (Button) findViewById(R.id.btGuest);
        btnGoToGuestActivity.setOnClickListener(this);
        currentPassword = sp.getString("password", null);

    }

    /**
     * Check if user is signed in (non-null) and update UI accordingly
     */
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            btnSignOut.setVisibility(View.VISIBLE);
            btnGoToContent.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
            btnGoToGuestActivity.setVisibility(View.GONE);
            btnGoToSignUpActivity.setVisibility(View.GONE);
            viewPasswordContainer.setVisibility(View.GONE);
            viewEmailContainer.setVisibility(View.GONE);
            tvUserEmail.setText("שלום " + currentUser.getEmail());
            tvUserEmail.setVisibility(View.VISIBLE);
            tvInstructions.setVisibility(View.GONE);
        } else {
            tvUserEmail.setText("");
            password.setText("");
            tvUserEmail.setVisibility(View.GONE);
            tvInstructions.setVisibility(View.VISIBLE);
            viewPasswordContainer.setVisibility(View.VISIBLE);
            viewEmailContainer.setVisibility(View.VISIBLE);
            btnGoToGuestActivity.setVisibility(View.VISIBLE);
            btnGoToSignUpActivity.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnGoToContent.setVisibility(View.GONE);
        }
    }

    /**
     * This method accepts the user's email and password
     * This method sighs in the user as a manager
     *
     * @return an object
     */
    public void signIn() {

        Log.d(TAG, " entered signIn");
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    /** Sign in success, update UI with the signed-in user's information */
                    Log.d("mainactivity", "signInWithEmail:success");
                    currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                Log.d("mainactivity", "signIn not successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NotNull Exception e) { //when failed

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    /** notifyUser("Invalid password"); */
                    Log.d(TAG, "invalid password");
                    showErrorDialog("invalid password");
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    /** notifyUser("Incorrect email address"); */
                    Log.d(TAG, "Incorrect email address");
                    showErrorDialog("Incorrect email address");
                } else {
                    /** notifyUser(e.getLocalizedDescription()); */
                    Log.d(TAG, "something that is not the email or password is wrong");
                    showErrorDialog("something that is not the email or password is wrong");
                }

            }
        });
    }


    private void showErrorDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnSignIn.getId()) {
            if (currentUser == null) //if the user is not sighed in
                if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText())) {
                    showErrorDialog("Invalid email or password");
                } else {
                    signIn();
                }
        }

        if (v.getId() == btnSignOut.getId()) {
            mAuth.signOut();
            updateUI(null);
        }

        if (v.getId() == btnGoToContent.getId()) {
            startGuestActivity();
        }

        /** move to sign up activity */
        if (v.getId() == btnGoToSignUpActivity.getId()) {
            Intent in = new Intent(getApplicationContext(), SignUp_Activity.class);
            if (currentUser != null)
                in.putExtra("allowed", true);
            startActivity(in);
        }

        /** move to the guest activity */
        if (v.getId() == btnGoToGuestActivity.getId()) {
            Intent in = new Intent(getApplicationContext(), Activity_Guest.class);
            startActivity(in);
        }
    }

    private void startGuestActivity() {
        Intent in = new Intent(MainActivity.this, Activity_Guest.class);
        in.putExtra("allowed", true);
        startActivity(in);
    }


}



