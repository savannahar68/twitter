package twitter.Interface;

import twitter.Model.Pair;
import twitter.Model.User;

import java.time.Instant;
import java.util.List;

public interface UserDao {
    User addUser(int id);
    void addPost(int userId, int postId, Instant time);
    void addfollower(int userId, int followerId);
    void addFollowing(int userId, int followingId);
    void removefollower(int userId, int followerId);
    void removeFollowing(int userId, int followingId);
    List<Integer> getFollowers(int userId);
    List<Integer> getFollowing(int userId);
    List<Integer> getPosts(int userId);
    List<Pair<Integer, Instant>> getPostAfterTimestamp(int userId, Instant time);
}
