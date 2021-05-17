package com.example.go4lunch.injections;

import com.example.go4lunch.repository.UserCRUDRepository;
import com.example.go4lunch.utils.UserCRUD;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

   public static UserCRUDRepository provideUserCRUD(){
      return new UserCRUDRepository();
   }

   public static Executor provideExecutor() {
      return Executors.newSingleThreadExecutor();
   }

   public static ViewModelFactory provideViewModelFactory() {
      UserCRUDRepository userCRUDRepository = provideUserCRUD();
      Executor executor = provideExecutor();
      return new ViewModelFactory(userCRUDRepository, executor);
   }
}
