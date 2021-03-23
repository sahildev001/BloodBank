package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.bloodbank.Model.Donors;
import com.example.bloodbank.databinding.ActivityDonateBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class DonateActivity extends AppCompatActivity {
    ActivityDonateBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    private FusedLocationProviderClient client;

    Double lat , lng ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //start location work
        requestPermissio();
        client = LocationServices.getFusedLocationProviderClient(this);

        super.onCreate(savedInstanceState);
        binding= ActivityDonateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Donate");
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference().child("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String id = auth.getCurrentUser().getUid();
                    String usernameFromDB = snapshot.child(id).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(id).child("email").getValue(String.class);
                    String phoneNoFromDB = snapshot.child(id).child("phoneno").getValue(String.class);

                    binding.tvUsername.setText(usernameFromDB);
                    binding.etDonarName.setText(usernameFromDB);
                    binding.etDonarEmail.setText(emailFromDB);
                    binding.etDonarPhoneno.setText(phoneNoFromDB);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnSetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //location work start




                if (ActivityCompat.checkSelfPermission(DonateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DonateActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DonateActivity.this,new String[]{ACCESS_FINE_LOCATION},1);
                    return;
                }
                boolean empty = false;
                if(binding.etDonarName.getText().toString().trim().length() == 0){
                    empty = true;
                    binding.etDonarName.setError("Please Enter your name");

                }

                if(binding.etDonarEmail.getText().toString().trim().length() == 0){
                    empty = true;
                    binding.etDonarEmail.setError("please Enter your email");

                }

                if(binding.etDonarPhoneno.getText().toString().trim().length() == 0){
                    empty = true;
                    binding.etDonarPhoneno.setError("Please Enter your PhoneNo.");

                }

                if(binding.etDonarBloodGroup.getText().toString().trim().length() == 0 ){
                    empty = true;
                    binding.etDonarBloodGroup.setError("Please enter your Blood group");

                }

                if(!(binding.etDonarBloodGroup.getText().toString()).equals("A+") &&
                        !(binding.etDonarBloodGroup.getText().toString()).equals("A-") &&
                        !(binding.etDonarBloodGroup.getText().toString()).equals("AB+") &&
                        !(binding.etDonarBloodGroup.getText().toString()).equals("AB-") &&
                        !(binding.etDonarBloodGroup.getText().toString()).equals("B-") &&
                        !(binding.etDonarBloodGroup.getText().toString()).equals("B+") &&
                        !(binding.etDonarBloodGroup.getText().toString()).equals("O-") &&
                        !(binding.etDonarBloodGroup.getText().toString()).equals("O+")
                ){
                    empty = true;
                    binding.etDonarBloodGroup.setError("please enter valid Blood Group A-,A+,AB-,AB+,B-,B+,O-,O+");

                }



                if(binding.etDoanrAddress.getText().toString().trim().length() == 0){
                    empty = true;
                    binding.etDoanrAddress.setError("Please Enter your Address");

                }
                if(empty == false){

                    client.getLastLocation().addOnSuccessListener(DonateActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {

                                binding.tvLoc.setText(location.toString());
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                                //outside code
                                String uid = binding.etDonarPhoneno.getText().toString();
                                Donors donors = new Donors(binding.etDonarName.getText().toString(),
                                        binding.etDonarEmail.getText().toString(),
                                        binding.etDonarPhoneno.getText().toString(),
                                        binding.etDonarBloodGroup.getText().toString(),
                                        binding.etDoanrAddress.getText().toString(),
                                        lat, lng);



                                Toast.makeText(DonateActivity.this, "DATA saved", Toast.LENGTH_SHORT).show();
                                //end outside code
                                binding.btnDoante.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //database instance
                                        database = FirebaseDatabase.getInstance();
                                        String ides = binding.etDonarPhoneno.getText().toString();
                                      DatabaseReference  reference1 = database.getReference("donars");
                                        reference1.child(ides).setValue(donors);
                                        //database end instance


                                        Toast.makeText(DonateActivity.this, "Donated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DonateActivity.this, SelectDonateAndReciveActivity.class);
                                        startActivity(intent);

                                    }
                                });

                            }

                        }
                    });
                    //End location workfi

                }

            }
        });



    }
    //start
    private void requestPermissio(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }//end
}