package com.example.go4lunch.view.activity.details;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.model.User;
import com.example.go4lunch.view.fragments.user.UserRecyclerViewAdapter;

import java.util.List;

public class DetailsRecyclerView extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;

    public DetailsRecyclerView(List<User> users) {this.users = users;}

    @NonNull
    @Override
    public UserRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.example.go4lunch.databinding.FragmentUserBinding fragmentUserBinding = (com.example.go4lunch.databinding.FragmentUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new UserRecyclerViewAdapter.ViewHolder(fragmentUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerViewAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        String workmatesJoining = user.getUsername() + R.string.user_is_joining;
        holder.userEating.setText(workmatesJoining);
        Glide.with(holder.userImage.getContext()).load(user.getPicture()).apply(RequestOptions.circleCropTransform()).into(holder.userImage);
    }


    @Override
    public int getItemCount() {
        return users.size();
    }
}
