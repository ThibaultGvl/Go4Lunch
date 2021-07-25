package com.example.go4lunch;

import com.example.go4lunch.model.User;
import com.example.go4lunch.view.adapter.RestaurantRecyclerViewAdapter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void sortUsersRVTest() {
        final User mUser1 = new User("a", "a", "a", "a", " ",
                new ArrayList<>(), "a", "a");
        final User mUser2 = new User("b", "b", "b", "b",
                "restaurantId ", new ArrayList<>(),"b","b");
        final User mUser3 = new User("c", "c", "c", "c",
                "restaurantId", new ArrayList<>(), "c","c");
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
    public void metersDistanceTest() {
        int point = (int) RestaurantRecyclerViewAdapter.meterDistanceBetweenPoints
                (48.856613,2.352222,43.604652,1.444209);

        assertEquals(587679, point,0.0);
    }

    @Test
    public void updateRankTest() {
        assertEquals(3, RestaurantRecyclerViewAdapter.setRating(5),0.0);
    }
}