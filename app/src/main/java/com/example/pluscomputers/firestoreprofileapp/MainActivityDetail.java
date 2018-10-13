package com.example.pluscomputers.firestoreprofileapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pluscomputers.firestoreprofileapp.adapter.ProfileAdapter;
import com.example.pluscomputers.firestoreprofileapp.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityDetail extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String DB_COLLECTION = "Profile";

    private String profileId;

    @BindView(R.id.edit_text_name_detail)
    EditText editTextName;
    @BindView(R.id.edit_text_surname_detail)
    EditText editTextSurname;
    @BindView(R.id.edit_text_age_detail)
    EditText editTextAge;
    @BindView(R.id.edit_text_address_detail)
    EditText editTextAddress;
    @BindView(R.id.edit_text_state_detail)
    EditText editTextState;
    @BindView(R.id.edit_text_town_detail)
    EditText editTextTown;
    @BindView(R.id.edit_text_country_detail)
    EditText editTextCountry;
    @BindView(R.id.edit_text_birth_detail)
    EditText editTextDateOfBirth;
    @BindView(R.id.image_view_details)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        profileId = intent.getStringExtra("profileId");

        loadProfileById(profileId);


    }

    // We load all the information of a profile that is clicked in order to update it

    public void loadProfileById(String id){

        DocumentReference profileReference = db.collection(DB_COLLECTION).document(id);

        profileReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Profile profile1 = documentSnapshot.toObject(Profile.class);

                            String firstName = profile1.getFirstName();
                            String lastName = profile1.getLastName();
                            String age = profile1.getAge();
                            String address = profile1.getAddress();
                            String state = profile1.getState();
                            String town = profile1.getTown();
                            String country = profile1.getCountry();
                            String image = profile1.getImage();
                            String dateOfBirth = profile1.getDateOfBirth();

                            editTextName.setText(firstName);
                            editTextSurname.setText(lastName);
                            editTextAge.setText(age);
                            editTextAddress.setText(address);
                            editTextState.setText(state);
                            editTextTown.setText(town);
                            editTextCountry.setText(country);
                            editTextDateOfBirth.setText(dateOfBirth);

                            //Picasso.get().load(image).into(imageView);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // Here we can update a profile that is created earlier

    public void updateProfile(View view) {
        String firstName = editTextName.getText().toString();
        String lastName = editTextSurname.getText().toString();
        String age = editTextAge.getText().toString();
        String address = editTextAddress.getText().toString();
        String state = editTextState.getText().toString();
        String town = editTextTown.getText().toString();
        String country = editTextCountry.getText().toString();
        String image = "";
        String dateOfBirth = editTextDateOfBirth.getText().toString();

        Profile profile = new Profile(firstName,lastName,age,address,state,town,country,image,dateOfBirth);

        DocumentReference profileReference = db.collection("Profile").document(profileId);

            profileReference.update("firstName",profile.getFirstName());
            profileReference.update("lastName",profile.getLastName());
            profileReference.update("age",profile.getAge());
            profileReference.update("address",profile.getAddress());
            profileReference.update("state",profile.getState());
            profileReference.update("town",profile.getTown());
            profileReference.update("country",profile.getCountry());
            profileReference.update("dateOfBirth",profile.getDateOfBirth())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivityDetail.this, "You successfully updated data",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivityDetail.this,MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivityDetail.this, "Error on updating data",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void cancelProfile(View view) {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
