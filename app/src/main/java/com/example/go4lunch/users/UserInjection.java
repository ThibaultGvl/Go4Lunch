package com.example.go4lunch.users;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserInjection {

   public static UserCRUDRepository provideUserCRUD(){
      return new UserCRUDRepository();
   }

   public static Executor provideExecutor() {
      return Executors.newSingleThreadExecutor();
   }

   public static UserViewModelFactory provideViewModelFactory() {
      UserCRUDRepository userCRUDRepository = provideUserCRUD();
      Executor executor = provideExecutor();
      return new UserViewModelFactory(userCRUDRepository, executor);
   }
}
