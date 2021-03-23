package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Model.Users;
import com.example.bloodbank.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    TextView AlreadyAcc;
    EditText name,email,phone,password;
    Button signup;
    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Register");



        //Already acc click

        binding.tvAlreadyHaveAccount.setOnClickListener(v -> {
            Intent in = new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(in);
        });//end already acc


        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(SignUpActivity.this);









        //setting signUP button
        signup = findViewById(R.id.btn_signUP);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize fail flag
                boolean failFlag = false ;


                //if statent to cheack edit text should not be empty

                if (binding.etName.getText().toString().trim().length() == 0) {
                    failFlag = true;
                    binding.etName.setError("Please enter your name");


                }
                 if (binding.etPhoneno.getText().toString().trim().length() == 0) {
                    failFlag =true;
                    binding.etPhoneno.setError("Please enter your phone number");
                }
                if (binding.etEmail.getText().toString().trim().length() == 0) {
                    failFlag =true;
                    binding.etEmail.setError("please enter your email");
                }
              if (binding.etPassword.getText().toString().trim().length() == 0) {
                    failFlag =true;
                    binding.etPassword.setError("please enter your password");
                }
                if (failFlag == false){


                    //enrt


                        progressDialog.show();
                        auth.createUserWithEmailAndPassword
                                (binding.etEmail.getText().toString(), binding.etPassword.getText().toString()).
                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressDialog.dismiss();
                                        if(task.isSuccessful()){

                                            Users user=new Users(binding.etName.getText().toString(),binding.etEmail.getText().toString(),binding.etPhoneno.getText().toString(),binding.etPassword.getText().toString());
                                            String id=task.getResult().getUser().getUid();
                                            database.getReference().child("Users").child(id).setValue(user);
                                            Toast.makeText(SignUpActivity.this, "User created sucessfully", Toast.LENGTH_SHORT).show();

                                            Intent in = new Intent(SignUpActivity.this,MainActivity.class);
                                            startActivity(in);
                                        }
                                        else {
                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }) ;


                    //end

                }
                //end if


            }
        });












    }

    //off back click
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Back press disable,", Toast.LENGTH_SHORT).show();
        return false;
    }
    //end brace off back click
}