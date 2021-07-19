package com.example.go4lunch;

import android.content.Context;

import com.example.go4lunch.model.User;
import com.example.go4lunch.model.restaurant.RestaurantOutputs;
import com.example.go4lunch.viewmodel.places.NearbyRestaurantRepository;
import com.example.go4lunch.viewmodel.places.NearbyRestaurantViewModel;
import com.example.go4lunch.viewmodel.users.UserCRUD;
import com.example.go4lunch.viewmodel.users.UserCRUDRepository;
import com.example.go4lunch.viewmodel.users.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.TestOnly;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.manipulation.Ordering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    NearbyRestaurantViewModel mNearbyRestaurantViewModel;

    NearbyRestaurantRepository mNearbyRestaurantRepository;

    UserViewModel mUserViewModel;

    UserCRUDRepository mUserCRUDRepository;

    Executor mExecutor;

    Context context;

    @Before
    public void setUp() {
        mUserViewModel = new UserViewModel(mUserCRUDRepository, mExecutor);
        mNearbyRestaurantViewModel = new NearbyRestaurantViewModel(mNearbyRestaurantRepository);
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void sortUsersRV() {
        final User mUser1 = new User("a", "a", "a", "a", " ",
                new ArrayList<>(), "a", "a");
        final User mUser2 = new User("b", "b", "b", "b",
                "restaurantId ", new ArrayList<>(), "b", "b");
        final User mUser3 = new User("c", "c", "c", "c",
                "restaurantId", new ArrayList<>(), "c", "c");
        final List<User> users = new ArrayList<>();
        users.add(mUser1);
        users.add(mUser2);
        users.add(mUser3);
        Collections.sort(users, new User.UserRestaurantComparator());
        assertSame(users.get(0), mUser2);
        assertSame(users.get(1), mUser3);
        assertSame(users.get(2), mUser1);
    }

    @Test
    public void test() {

    }
}