package com.example.pluscomputers.firestoreprofileapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pluscomputers.firestoreprofileapp.adapter.ProfileAdapter;
import com.example.pluscomputers.firestoreprofileapp.model.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTIVITY = "MainActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference profileCollection = db.collection("Profile");

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ProfileAdapter adapter;
    private List<Profile> listProfiles = new ArrayList<>();
    private String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);

        adapter = new ProfileAdapter(this);

        recyclerView.setAdapter(adapter);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView()
                            , R.string.edit_profile, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            loadNotes();
        } else{
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView()
                            , R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent intent = new Intent(MainActivity.this,AddProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadNotes() {
        profileCollection
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {

                            Profile profile = documentSnapshot.toObject(Profile.class);

                            profile.setId(documentSnapshot.getId());

                            profileId = profile.getId();
                            String firstName = profile.getFirstName();
                            String lastName = profile.getLastName();
                            String age = profile.getAge();
                            String address = profile.getAddress();
                            String state = profile.getState();
                            String town = profile.getTown();
                            String country = profile.getCountry();
                            String video = profile.getImage();
                            String dateOfBirth = profile.getDateOfBirth();

                            Profile newProfile = new Profile(firstName, lastName, age, address, state,
                                    town, country, video, dateOfBirth);

                            newProfile.setId(profileId);

                            listProfiles.add(newProfile);
                        }

                        adapter.setProfiles(listProfiles);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(MAIN_ACTIVITY, e.toString());
                    }
                });

    }

}
