package com.example.contactslist.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactslist.Model.ContactsModel;
import com.example.contactslist.R;
import com.example.contactslist.databinding.CustomItemsBinding;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ContactsModel> data;

    public RecyclerAdapter(Context c,ArrayList<ContactsModel> arrayList){
        this.context = c;
        this.data = arrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomItemsBinding binding = CustomItemsBinding.inflate(LayoutInflater.from(context),parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(binding);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContactsModel contactsModel = data.get(position);
        if (contactsModel.getName() != null)
        {
            holder.binding.customItemTvName.setText(contactsModel.getName());
        }
        if (contactsModel.getEmail() != null)
        {
            holder.binding.customItemTvEmail.setText(contactsModel.getEmail());
        }
        if (contactsModel.getNumber() != null)
        {
            holder.binding.customItemTvNumber.setText(contactsModel.getNumber());
        }
        if (contactsModel.getOtherDetails() != null)
        {
            holder.binding.customItemTvOthers.setText(contactsModel.getOtherDetails());
        }
        Bitmap image = null;
        if (contactsModel.getImage() != null)
        {
            image = BitmapFactory.decodeByteArray(contactsModel.getImage(), 0, contactsModel.getImage().length);
            holder.binding.customItemImg.setImageBitmap(image);
        }else {
            image = BitmapFactory.decodeResource(holder.binding.getRoot().getContext().getResources(), R.drawable.icons_person);
            holder.binding.customItemImg.setImageBitmap(image);
        }





    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomItemsBinding binding;
        public MyViewHolder(@NonNull CustomItemsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
