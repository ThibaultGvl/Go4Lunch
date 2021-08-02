package com.example.go4lunch.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentUserBinding;
import com.example.go4lunch.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DetailsRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;

    public DetailsRecyclerViewAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @NotNull
    @Override
    public UserRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,
                                                                 int viewType) {
        com.example.go4lunch.databinding.FragmentUserBinding fragmentUserBinding =
                (FragmentUserBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false));
        return new UserRecyclerViewAdapter.ViewHolder(fragmentUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserRecyclerViewAdapter.ViewHolder holder,
                                 int position) {
        User user = users.get(position);
        String userJoining = user.getUsername() + holder.mFragmentUserBinding.getRoot()
                .getContext().getString(R.string.is_joining);
        holder.userEating.setText(userJoining);
        Glide.with(holder.userImage).load(user.getPicture()).apply(RequestOptions
                .circleCropTransform()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
