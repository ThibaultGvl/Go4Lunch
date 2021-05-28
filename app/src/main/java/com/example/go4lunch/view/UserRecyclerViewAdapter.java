package com.example.go4lunch.view;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentUserBinding;
import com.example.go4lunch.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;

    public UserRecyclerViewAdapter(List<User> users) {
        this.users = users;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        com.example.go4lunch.databinding.FragmentUserBinding fragmentUserBinding = (FragmentUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new ViewHolder(fragmentUserBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        User user = users.get(position);
        String userKnowWhatEatingText = user.getUsername() + " is eating ";
        String userDoestKnowWhatEating = user.getUsername() + " hasn't decided yet";
        if(user.getPicture() != null) {
            Glide.with(holder.userImage.getContext()).load(user.getPicture()).apply(RequestOptions.circleCropTransform()).into(holder.userImage);
        }
        else {
            holder.userImage.setColorFilter(R.color.primary_color);
        }
        if(user.getRestaurant() != null) {
            holder.userEating.setText(userKnowWhatEatingText);
        }
        else {
            holder.userEating.setText(userDoestKnowWhatEating);
            holder.userEating.setTypeface(holder.userEating.getTypeface(), Typeface.ITALIC);
            holder.userEating.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView userImage;

        public TextView userEating;

        public FragmentUserBinding mFragmentUserBinding;

        public ViewHolder(FragmentUserBinding fragmentUserBinding) {
            super(fragmentUserBinding.getRoot());
            mFragmentUserBinding = fragmentUserBinding;
            userImage = fragmentUserBinding.userImage;
            userEating = fragmentUserBinding.eatingPlace;
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString();
        }
    }
}