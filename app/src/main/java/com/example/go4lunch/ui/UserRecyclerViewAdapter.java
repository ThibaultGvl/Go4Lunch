package com.example.go4lunch.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.databinding.FragmentUserBinding;
import com.example.go4lunch.model.User;
import com.example.go4lunch.utils.UserCRUD;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;

    private FragmentUserBinding fragmentUserBinding;

    public UserRecyclerViewAdapter(List<User> users) {
        this.users = users;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        fragmentUserBinding = (FragmentUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new ViewHolder(fragmentUserBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = users.get(position);
        holder.userEating.setText(user.getUsername());
        Glide.with(holder.userImage.getContext()).load(user.getPicture()).apply(RequestOptions.circleCropTransform()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public User mUser;

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