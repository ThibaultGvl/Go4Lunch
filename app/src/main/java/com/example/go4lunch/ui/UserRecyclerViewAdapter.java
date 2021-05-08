package com.example.go4lunch.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.go4lunch.databinding.FragmentUserBinding;
import com.example.go4lunch.model.User;
import com.example.go4lunch.ui.dummy.DummyContent.DummyItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;

    private ImageView userImage;

    private TextView eatingPlace;

    private FragmentUserBinding fragmentUserBinding;

    public UserRecyclerViewAdapter(List<User> users) {
        this.users = users;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        fragmentUserBinding = (FragmentUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mUser = users.get(position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public User mUser;

        public ViewHolder(View view) {
            super(view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}