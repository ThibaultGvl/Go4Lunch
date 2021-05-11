package com.example.go4lunch.injections;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.utils.UserCRUD;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

   public static UserCRUD provideUserCRUDDataSource(Context context){
      FirebaseFirestore db = FirebaseFirestore.getInstance();
      return new UserCRUD();
   }

   public static Executor provideExecutor() {
      return Executors.newSingleThreadExecutor();
   }

   public static ViewModelFactory provideViewModelFactory(Context context) {
      UserCRUD userCRUD = provideUserCRUDDataSource(context);
      Executor executor = provideExecutor();
      return new ViewModelFactory(userCRUD, executor);
   }
}
