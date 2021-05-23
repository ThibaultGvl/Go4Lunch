package com.example.go4lunch.users;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.databinding.FragmentUserListBinding;
import com.example.go4lunch.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class UserFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;

    private UserViewModel mViewModel;

    private final List<User> users = new ArrayList<>();

    private final UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter(users);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserFragment() {
    }

    @SuppressWarnings("unused")
    public static UserFragment newInstance(int columnCount) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureViewModel();
        this.getUsers();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.go4lunch.databinding.FragmentUserListBinding fragmentUserBinding = FragmentUserListBinding.inflate(inflater, container, false);
        View view = fragmentUserBinding.getRoot();

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    private void configureViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mViewModel = new ViewModelProvider(this, mViewModelFactory).get(UserViewModel.class);
        this.mViewModel.initUsers(this.getContext());
    }

    private void updateUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        adapter.notifyDataSetChanged();
    }

    private void getUsers() {
        if(this.mViewModel.getUsers() != null) {
            this.mViewModel.getUsers().observe(this, this::updateUsers);
        }
    }
}