package com.example.firebasedemope;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import androidx.appcompat.app.AlertDialog;
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>  {
    List<Contact> contactList;
    private OnDeleteClickListener onDeleteClickListener;
    private OnUpdateClickListener onUpdateClickListener;

    public ContactAdapter(List<Contact> contactList, OnDeleteClickListener onDeleteClickListener) {
        this.contactList = contactList;
        this.onDeleteClickListener = onDeleteClickListener;
    }
    public ContactAdapter(List<Contact> contactList, OnUpdateClickListener onUpdateClickListener,
                          OnDeleteClickListener onDeleteClickListener) {
        this.contactList = contactList;
        this.onUpdateClickListener = onUpdateClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Contact contact);
    }
    public interface OnUpdateClickListener {
        void onUpdateClick(Contact contact);
    }

 public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent,false);

        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact=contactList.get(position);
        Glide.with(holder.itemView).load(contactList.get(position).getUrl()).into(holder.img_urlcontact);
        holder.tv_namecontact.setText("name: "+(contact.getName().isEmpty()?"none":contact.getName()));
        holder.tv_emailcontact.setText("email: "+(contact.getEmail().isEmpty()?"none":contact.getEmail()));
        holder.tv_addresscontact.setText("address: "+(contact.getAddress().isEmpty()?"none":contact.getAddress()));
        holder.tv_companycontact.setText("company: "+(contact.getCompany().isEmpty()?"none":contact.getCompany()));
        holder.tv_idcontact.setText("id: "+String.valueOf(contact.getId()));
        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClickListener.onUpdateClick(contact);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(contact);
            }
        });


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public  class ContactHolder extends RecyclerView.ViewHolder{
        ImageView img_urlcontact;
        TextView tv_idcontact;
        TextView tv_namecontact;
        TextView tv_emailcontact;
        TextView tv_addresscontact;
        TextView tv_companycontact;
        CardView contact_card_view;
        Button btn_delete;
        Button btn_update;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            img_urlcontact=itemView.findViewById(R.id.imv_urlcontact);
            tv_idcontact=itemView.findViewById(R.id.tv_idcontact);
            tv_namecontact=itemView.findViewById(R.id.tv_namecontact);
            tv_companycontact=itemView.findViewById(R.id.tv_companycontact);
            tv_addresscontact=itemView.findViewById(R.id.tv_addresscontact);
            tv_emailcontact=itemView.findViewById(R.id.tv_emailcontact);
            contact_card_view=itemView.findViewById(R.id.contact_card_view);
            btn_update=itemView.findViewById(R.id.btn_update);
            btn_delete=itemView.findViewById(R.id.btn_delete);
        }
    }
}
