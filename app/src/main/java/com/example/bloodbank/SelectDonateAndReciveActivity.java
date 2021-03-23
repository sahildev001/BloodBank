package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bloodbank.databinding.ActivitySelectDonateAndReciveBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectDonateAndReciveActivity extends AppCompatActivity {
    ActivitySelectDonateAndReciveBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectDonateAndReciveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("DonateBlood And Save A Life");

        auth= FirebaseAuth.getInstance();

        database= FirebaseDatabase.getInstance();

//        Intent in =  getIntent();
//        String username = in.getStringExtra("Username");


     //   binding.tvusername.setText(username);

        DatabaseReference reference = database.getReference().child("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String id = auth.getCurrentUser().getUid();
                    String usernameFromDB = snapshot.child(id).child("name").getValue(String.class);
                    binding.tvusername.setText(usernameFromDB);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        //set on donate clicked
        binding.tvdoante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SelectDonateAndReciveActivity.this,DonateActivity.class);
                startActivity(in);
            }
        });

        //set on volunterr clicked
        binding.tvVolenteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SelectDonateAndReciveActivity.this, ReceiveActivity.class);
                startActivity(in);
            }
        });


    }


    //off back click
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Back press disable,Please click top right menu for Log out", Toast.LENGTH_SHORT).show();
        return false;
    }
    //end brace off back click

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.logout:
                auth.signOut();
                Intent in = new Intent(SelectDonateAndReciveActivity.this,MainActivity.class);
                startActivity(in);
                break;
            case R.id.setting:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}