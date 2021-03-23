/// Sign in Activity

package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.Model.Users;
import com.example.bloodbank.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    ActivityMainBinding binding;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     binding=   ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Sign In");


        //initialize objects
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(MainActivity.this);

        progressDialog.setTitle("Singingin");
        progressDialog.setMessage("please Wait");



        //google sign in
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        //google btn click
        binding.googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });








        // dont have acccount btn click

        binding.DontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });
        //end dont have account click btn






        //on click at log in btn
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean empty  =  false;

                if(binding.etemail.getText().toString().trim().length() == 0){
                    empty = true;
                    binding.etemail.setError("Please enter your registerd email");
                }
                if(binding.etpassword.getText().toString().trim().length() == 0){
                    empty = true;
                    binding.etpassword.setError("Please enter your registerd password");
                }
                if(empty == false){
                    Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_SHORT).show();

                    progressDialog.show();
                    auth.signInWithEmailAndPassword
                            (binding.etemail.getText().toString(),binding.etpassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful())
                                    {

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    String id= task.getResult().getUser().getUid();
                                                    //getting values from database

                                                    String emailFromDB = snapshot.child(id).child("email").getValue(String.class);
                                                    String passwordFromDB = snapshot.child(id).child("password").getValue(String.class);
                                                    String usernameFromDB = snapshot.child(id).child("name").getValue(String.class);
                                                    String phonenoFromDB = snapshot.child(id).child("phoneno").getValue(String.class);

                                                    Intent in = new Intent(MainActivity.this,SelectDonateAndReciveActivity.class);
                                                    in.putExtra("Username",usernameFromDB);
                                                    in.putExtra("Email",emailFromDB);
                                                    in.putExtra("Phone",phonenoFromDB);
                                                    in.putExtra("Password",passwordFromDB);
                                                    startActivity(in);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
            }
        });
            //if get user siged in
        if(auth.getCurrentUser()!=null){
            Intent intent= new Intent(MainActivity.this,SelectDonateAndReciveActivity.class);
            startActivity(intent);
        }







    }

    //googleSignin
    int RC_SIGN_IN=54;
    private void signIn()
    {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }
    //start firebaseAuthWithGoogle()
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //getting data from google
                            Users users= new Users();

                            users.setUserId(user.getUid());
                            users.setName(user.getDisplayName());
                            users.setProfilepic(user.getPhotoUrl().toString());
                            users.setEmail(user.getEmail());
                            users.setPhoneno(user.getPhoneNumber());



                            //Seeting database
                            database.getReference().child("Users").child(user.getUid()).setValue(users);

                            Intent i = new Intent(MainActivity.this,SelectDonateAndReciveActivity.class);

                            startActivity(i);
                            Toast.makeText(MainActivity.this, "Sign in with google", Toast.LENGTH_SHORT).show();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //  Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }

                        // ...
                    }
                });
    }
    //googleSign in end

    //off back click
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Back press disable,", Toast.LENGTH_SHORT).show();
        return false;
    }
    //end brace off back click

}