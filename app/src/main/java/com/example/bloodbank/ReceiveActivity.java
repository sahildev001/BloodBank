package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.bloodbank.Adapter.Adapter;
import com.example.bloodbank.Model.Donors;
import com.example.bloodbank.databinding.ActivityReceiveBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ReceiveActivity extends AppCompatActivity {
    ActivityReceiveBinding binding;
    RecyclerView recyclerView;
    Adapter adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityReceiveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView = findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Donors> options =
                new FirebaseRecyclerOptions.Builder<Donors>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("donars"), Donors.class)
                        .build();
        adapter = new Adapter(options,context);
        recyclerView.setAdapter(adapter);


    }

    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    //  Similarly, the stopListening() call removes the event listener and all data in the adapter. Call this method when the containing Activity or Fragment stops:

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}