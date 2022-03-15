package twitter.Model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class User {
    int id;
    List<Integer> followers;
    List<Integer> following;
    Map<Integer, Instant> posts;
    Map<Integer, Pair<Integer, Integer>> userFollowersFollowingIndexMapping;

    public User(int id) {
        this.id = id;
        posts = new HashMap<>();
        followers = new ArrayList<>();
        following = new ArrayList<>();
    }
}
