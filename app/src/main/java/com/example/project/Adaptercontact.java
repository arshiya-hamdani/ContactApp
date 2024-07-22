package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Adaptercontact extends RecyclerView.Adapter<Adaptercontact.contactviewholder> {
    private Context context;
    private ArrayList<modelclass> contactlist;
    private DBHelper dbHelper;

    public Adaptercontact(Context context, ArrayList<modelclass> contactlist) {
        this.context = context;
        this.contactlist = contactlist;
        this.dbHelper = new DBHelper(context); // Initialize dbHelper
    }

    @NonNull
    @Override
    public contactviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_contact, parent, false);
        return new contactviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull contactviewholder holder, int position) {
        modelclass mc = contactlist.get(position);
        String id = mc.getId();
        String name = mc.getName();

        holder.contactname.setText(name);
        holder.contactimg.setImageResource(R.drawable.baseline_person_24); // Default image

        holder.frontLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, contactdetails.class);
                    intent.putExtra("contactId", id);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.e("Adaptercontact", "Error starting contact details activity", e);
                }
            }
        });
        holder.numberdial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = mc.getPhone();
                if (!phoneNumber.isEmpty()) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        context.startActivity(callIntent);
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }
                } else {
                    Toast.makeText(context, "Phone number is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.contactedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Addeditcontact.class);
                intent.putExtra("contactId", id);
                context.startActivity(intent);
            }
        });

        holder.contactdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long result = dbHelper.deleteData(id);
                if (result != -1) {
                    Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                    contactlist.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactlist.size();
    }
    public void sortContactsByName() {
        Collections.sort(contactlist, new Comparator<modelclass>() {
            @Override
            public int compare(modelclass c1, modelclass c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
        notifyDataSetChanged();
    }

    public void sortContactsByPhone() {
        Collections.sort(contactlist, new Comparator<modelclass>() {
            @Override
            public int compare(modelclass c1, modelclass c2) {
                return c1.getPhone().compareTo(c2.getPhone());
            }
        });
        notifyDataSetChanged();
    }

    static class contactviewholder extends RecyclerView.ViewHolder {
        ImageView contactimg,numberdial;
        TextView contactname, contactedit, contactdelete;
        RelativeLayout frontLayout;

        public contactviewholder(@NonNull View itemView) {
            super(itemView);
            contactimg = itemView.findViewById(R.id.contactimg);
            contactname = itemView.findViewById(R.id.contact_name);
            contactedit = itemView.findViewById(R.id.contact_edit);
            contactdelete = itemView.findViewById(R.id.contact_delete);
            frontLayout = itemView.findViewById(R.id.front_layout);
            numberdial=itemView.findViewById(R.id.numberdial);
        }
    }
}
