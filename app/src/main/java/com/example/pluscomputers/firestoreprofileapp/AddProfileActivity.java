package com.example.pluscomputers.firestoreprofileapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pluscomputers.firestoreprofileapp.model.Profile;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CAMERA_CODE = 1001;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference noteBookRef = db.collection("Profile");
    private Profile profile;

    @BindView(R.id.imageView) ImageView imageProfileView;
    @BindView(R.id.edit_text_name) EditText editTextName;
    @BindView(R.id.edit_text_surname) EditText editTextSurname;
    @BindView(R.id.edit_text_age) EditText editTextAge;
    @BindView(R.id.edit_text_address) EditText editTextAddress;
    @BindView(R.id.edit_text_state) EditText editTextState;
    @BindView(R.id.edit_text_town) EditText editTextTown;
    @BindView(R.id.edit_text_country) EditText editTextCountry;
    @BindView(R.id.edit_text_birth) EditText editTextDateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        ButterKnife.bind(this);

        if (actionBar != null){
            actionBar.hide();
        }
    }

    public void saveProfile(View v){

        // Here we add a profile and save it in Firestore

        String firstName = editTextName.getText().toString();
        String lastName = editTextSurname.getText().toString();
        String age = editTextAge.getText().toString();
        String address = editTextAddress.getText().toString();
        String state = editTextState.getText().toString();
        String town = editTextTown.getText().toString();
        String country = editTextCountry.getText().toString();
        String image = "";
        String dateOfBirth = editTextDateOfBirth.getText().toString();

        profile = new Profile(firstName,lastName,age,address,state,town,country,image,dateOfBirth);

        noteBookRef.add(profile);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    public void addCoverProfile(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // For some android versions there are some problems
        // To fix it we can save image in firebase database

        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK) {

                Uri uri = data.getData();

//                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//                imageProfileView.setImageBitmap(bitmap);

                profile.setImage(uri.toString());

                Picasso.get().load(uri).into(imageProfileView);

            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
            }
        }

}
