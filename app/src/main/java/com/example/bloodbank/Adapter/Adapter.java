package com.example.bloodbank.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.Model.Donors;
import com.example.bloodbank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Adapter extends FirebaseRecyclerAdapter<Donors,Adapter.ViewHolder> {
Context context;
    public Adapter(@NonNull FirebaseRecyclerOptions<Donors> options, Context context) {
        super(options);
      //  this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position, @NonNull Donors model) {
        holder.textdoanrname.setText(model.getName());
        holder.textdonaremail.setText(model.getEmail());
        holder.textdonorphoneno.setText(model.getPhoneNo());
        holder.textdonorbloodgroup.setText(model.getBloodGroup());
        holder.textdonaraddress.setText(model.getAddress());
        holder.latt.setText(String.valueOf(model.getLat()));
        holder.lann.setText(String.valueOf(model.getLng()));
        String Latitude,Longitude;
        Latitude= model.getLat().toString();
        Longitude= model.getLng().toString();

        holder.textdonorphoneno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String posted_by= model.getPhoneNo();
                String uri = "tel:" + posted_by.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                holder.textdonorphoneno.getContext().startActivity(intent);
            }
        });

        holder.textdonaremail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = model.getEmail();
                try{
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + mail));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "I need Blood From blood bank");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    holder.textdonaremail.getContext().startActivity(intent);
                }catch(ActivityNotFoundException e){
                    //TODO smth
                }

            }
        });

        holder.btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String address = "google.navigation:q="+Latitude+","+Longitude+"&mode=l";
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));




                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(holder.btnLocate.getContext().getPackageManager()) != null) {
                    holder.btnLocate.getContext().startActivity(mapIntent);
                }
                else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recivehelp,parent,false);
        return  new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textdoanrname,textdonaremail,textdonorphoneno,textdonorbloodgroup,textdonaraddress,latt,lann;
        Button btnLocate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textdoanrname = itemView.findViewById(R.id.textdonarname);
            textdonaremail = itemView.findViewById(R.id.tvEmail);
            textdonorphoneno = itemView.findViewById(R.id.tvPhoneNo);
            textdonorbloodgroup = itemView.findViewById(R.id.textBloodGroup);
            textdonaraddress = itemView.findViewById(R.id.tvAddress);
            latt = itemView.findViewById(R.id.latt);
            lann = itemView.findViewById(R.id.lng);

            btnLocate = itemView.findViewById(R.id.locatebtn);




        }
    }
}
