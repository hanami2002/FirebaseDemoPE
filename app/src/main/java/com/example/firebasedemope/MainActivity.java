package com.example.firebasedemope;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    List<Contact> list;
    EditText edtName,edtEmail,edtCompany,edtAddress,edtUrl,edtId;

    private FirebaseDatabase db;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtName=findViewById(R.id.edt_name);
        edtEmail=findViewById(R.id.edt_email);
        edtCompany=findViewById(R.id.edt_company);
        edtAddress=findViewById(R.id.edt_address);
        edtUrl=findViewById(R.id.edt_url);
        edtId=findViewById(R.id.edt_id);
        db=FirebaseDatabase.getInstance();
        ref=db.getReference("contact");
    }
    public void getRecyclerView(List<Contact> list){
        RecyclerView recyclerView= findViewById(R.id.rcv_contact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ContactAdapter(list, new ContactAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(Contact contact) {
                opDialogUpdate(contact);
            }
        }, new ContactAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Contact contact) {
                deleteContact(contact);
            }
        }));
    }

    private void deleteContact(Contact contact) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Confirm")
                .setMessage("Do you want to delete")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.child(contact.getId()+"").removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(MainActivity.this,"Delete successfull",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    private void opDialogSearch(){
        Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.search_dialog);
        Window window= dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);
        EditText name= dialog.findViewById(R.id.edt_namesearch);
        EditText email=dialog.findViewById(R.id.edt_emailsearch);
        Button btn= dialog.findViewById(R.id.btnSearch);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataList(name.getText().toString(),email.getText().toString());
                getRecyclerView(list);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void opDialogUpdate(Contact contact) {
        Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.contact_dialog);
        Window window= dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);

        EditText id= dialog.findViewById(R.id.editTextID);
        id.setText(String.valueOf(contact.getId()));
        id.setEnabled(false);
        EditText name= dialog.findViewById(R.id.editTextName);
        name.setText(contact.getName());
        EditText email= dialog.findViewById(R.id.editTextEmail);
        email.setText(contact.getEmail());
        EditText address= dialog.findViewById(R.id.editTextAddress);
        address.setText(contact.getAddress());
        EditText url= dialog.findViewById(R.id.editTextURL);
        url.setText(contact.getUrl());
        EditText company= dialog.findViewById(R.id.editTextCompany);
        company.setText(contact.getCompany());
        Button btnCancel=dialog.findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnUpdate=dialog.findViewById(R.id.buttonUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updateData = new HashMap<>();
                contact.setName(name.getText().toString());
                contact.setUrl(url.getText().toString());
                contact.setCompany(company.getText().toString());
                contact.setAddress(address.getText().toString());
                contact.setEmail(email.getText().toString());

                updateData.put("name", contact.getName());
                updateData.put("email", contact.getEmail());
                updateData.put("address", contact.getAddress());
                updateData.put("company", contact.getCompany());
                updateData.put("url", contact.getUrl());
                ref.child(contact.getId()+"").updateChildren(updateData, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(MainActivity.this,"Update successfull",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }
    public  void getDataList(String name,String email){

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list=new ArrayList<>();
                for (DataSnapshot contactSnapshot : snapshot.getChildren()) {
                    Contact contact = contactSnapshot.getValue(Contact.class);
                    if(name.isEmpty()&&email.isEmpty()){
                        list.add(contact);
                    }else if(name.isEmpty()&&!email.isEmpty()){
                        if(contact.getEmail().equals(email)){
                            list.add(contact);
                        }
                    } else if (!name.isEmpty()&&email.isEmpty()) {
                        if(contact.getName().equals(name)){
                            list.add(contact);
                        }
                    }else {
                        if(contact.getName().equals(name)&&contact.getEmail().equals(email)){
                            list.add(contact);
                        }
                    }


                }
                getRecyclerView(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Getdata fail",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onGetData(View view) {
        getDataList("","");
    }
    public void onAddData(View view) {
        try{
            Contact contact = new Contact();
            contact.setId(Integer.parseInt(edtId.getText().toString()));
            contact.setName(edtName.getText().toString());
            contact.setEmail(edtEmail.getText().toString());
            contact.setAddress(edtAddress.getText().toString());
            contact.setUrl(edtUrl.getText().toString());
            contact.setCompany(edtCompany.getText().toString());
            ref.child(contact.getId()+"").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(MainActivity.this,"The ID already exists",Toast.LENGTH_SHORT).show();
                    } else {
                        ref.child(contact.getId()+"").setValue(contact);
                        Toast.makeText(MainActivity.this,"Add successfull",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this,"Add fail",Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex){
            Toast.makeText(MainActivity.this,"Add fail",Toast.LENGTH_SHORT).show();
        }

    }

    public void openSearch(View view) {
        opDialogSearch();
    }
}