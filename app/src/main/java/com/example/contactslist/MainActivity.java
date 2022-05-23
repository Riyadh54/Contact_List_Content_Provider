package com.example.contactslist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.contactslist.Adapter.RecyclerAdapter;
import com.example.contactslist.Model.ContactsModel;
import com.example.contactslist.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final int REQ_PER_READ_CONTACT = 1;
    private ArrayList<ContactsModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED  )
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQ_PER_READ_CONTACT);
        }else {
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();

        }


    }




    private void readContacts(){
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME+" ASC";
        Cursor cursor = getContentResolver().query(uri,null,null,null,sortOrder);

        if (cursor.moveToFirst())
        {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("_ID"));
                Uri uri2 = ContactsContract.Data.CONTENT_URI;
                String selection = ContactsContract.Data.CONTACT_ID+"=?";
                String[] selectionArgs =new String[]{String.valueOf(id)};
                Cursor cursor2 = getContentResolver().query(uri2,null,selection,selectionArgs,null);

                String displayName = "";
                String nickName = "";
                String homePhone = "";
                String mobilePhone = "";
                String workPhone = "";
                byte[] image = null;
                String homeEmail = "";
                String workEmail = "";
                String companyName = "";
                String title = "";
                String contactNumbers = "";
                String contactEmailAddresses = "";
                String contactOtherDetails = "";

                if (cursor2.moveToFirst())
                {
                    displayName = cursor2.getString(cursor2.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    do {
                        if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE))
                        {
                            nickName = cursor2.getString(cursor2.getColumnIndex("data1"));
                            contactOtherDetails += nickName + "\n";
                        }
                        if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
                        {
                            switch (cursor2.getInt(cursor2.getColumnIndex("data2")))
                            {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    homePhone = cursor2.getString(cursor2.getColumnIndex("data1"));
                                    contactNumbers += "Home Phone : " + homePhone
                                            + "\n";
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    workPhone = cursor2.getString(cursor2.getColumnIndex("data1"));
                                    contactNumbers += "Work Phone : " + workPhone
                                            + "\n";
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = cursor2.getString(cursor2.getColumnIndex("data1"));
                                    contactNumbers += "Mobile Phone : "
                                            + mobilePhone + "\n";
                                    break;
                            }


                        }

                        // get email
                        if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE))
                        {
                            switch (cursor2.getInt(cursor2.getColumnIndex("data2")))
                            {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    homeEmail = cursor2.getString(cursor2.getColumnIndex("data1"));
                                    contactEmailAddresses += "Home Email : "+ homeEmail + "\n";
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                    workEmail = cursor2.getString(cursor2.getColumnIndex("data1"));
                                    contactEmailAddresses += "Work Email : "+ workEmail + "\n";
                                    break;

                            }
                        }
                        // get Company

                        if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE))
                        {
                            companyName = cursor2.getString(cursor2.getColumnIndex("data1"));
                            // name
                            contactOtherDetails += "Coompany Name : "+ companyName + "\n";
                            title = cursor2.getString(cursor2.getColumnIndex("data4"));
                            // title
                            contactOtherDetails += "Title : " + title + "\n";

                        }

                        // get Photo

                        if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE))
                        {
                            image = cursor2.getBlob(cursor2.getColumnIndex("data15"));
                        }

                    }
                    while (cursor2.moveToNext());
                    data.add(new ContactsModel(Long.toString(id), displayName, contactNumbers, contactEmailAddresses, contactOtherDetails,image));
                }

            }while (cursor.moveToNext());
        }
    }

    private void fetchDataIntoRecycler(){
        RecyclerAdapter adapter =new RecyclerAdapter(MainActivity.this,data);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false);
        binding.mainRvNames.setLayoutManager(lm);
        binding.mainRvNames.setHasFixedSize(true);
        binding.mainRvNames.setAdapter(adapter);
    }


    class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.mainPb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readContacts();
            return null;
        }



        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            binding.mainPb.setVisibility(View.GONE);
            fetchDataIntoRecycler();
        }



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_PER_READ_CONTACT)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
        }

    }
}