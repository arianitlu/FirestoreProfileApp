package com.example.pluscomputers.firestoreprofileapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pluscomputers.firestoreprofileapp.MainActivityDetail;
import com.example.pluscomputers.firestoreprofileapp.R;
import com.example.pluscomputers.firestoreprofileapp.model.Profile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    private List<Profile> profileList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address,age;

        public MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.text_view_name);
            address = view.findViewById(R.id.text_view_address);
            age = view.findViewById(R.id.text_view_item_age);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Intent intent = new Intent(mContext, MainActivityDetail.class);

                    intent.putExtra("profileId",profileList.get(position).getId());

                    mContext.startActivity(intent);
                }
            });
        }
    }

    public ProfileAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Profile profile = profileList.get(position);

        holder.name.setText(profile.getFirstName());
        holder.address.setText(profile.getAddress());
        holder.age.setText(profile.getAge());
    }

    @Override
    public int getItemCount() {
        if (profileList == null ) return 0;
        return profileList.size();
    }

    public void setProfiles(List<Profile> profiles){
        this.profileList = profiles;
        notifyDataSetChanged();
    }
}