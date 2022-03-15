package twitter.StorageAdapter;

import twitter.Interface.UserDao;
import twitter.Model.Pair;
import twitter.Model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserDaoImpl implements UserDao {
    List<User> userList;
    Map<Integer, Integer> userIdToIndexMapping;
    public static volatile UserDaoImpl INSTANCE;

    private UserDaoImpl() {
        userList = new ArrayList<>();
        userIdToIndexMapping = new ConcurrentHashMap<>();
    }

    public static UserDaoImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (FeedDaoImpl.class) {
                if (INSTANCE == null) INSTANCE = new UserDaoImpl();
            }
        }
        return INSTANCE;
    }

    @Override
    public User addUser(int id) {
        User user = new User(id);
        userList.add(user);
        userIdToIndexMapping.put(id, userList.size()-1);
        return user;
    }

    @Override
    public void addPost(int userId, int postId, Instant time) {
        if (!userIdToIndexMapping.containsKey(userId)) {
            addUser(userId);
        }
        userList.get(userIdToIndexMapping.get(userId)).getPosts().put(postId, time);
    }

    @Override
    public void addfollower(int userId, int followerId) {
        if (!userIdToIndexMapping.containsKey(userId)) {
            addUser(userId);
        }
        userList.get(userIdToIndexMapping.get(userId)).getFollowers().add(followerId);
    }

    @Override
    public void addFollowing(int userId, int followingId) {
        if (!userIdToIndexMapping.containsKey(userId)) {
            addUser(userId);
        }
        userList.get(userIdToIndexMapping.get(userId)).getFollowing().add(followingId);
    }

    @Override
    public void removefollower(int userId, int followerId) {
        userList.get(userIdToIndexMapping.get(userId)).getFollowers().remove(followerId);
    }

    @Override
    public void removeFollowing(int userId, int followingId) {
        userList.get(userIdToIndexMapping.get(userId)).getFollowing().remove(followingId);
    }

    @Override
    public List<Integer> getFollowers(int userId) {
        return userList.get(userIdToIndexMapping.get(userId)).getFollowers();
    }

    @Override
    public List<Integer> getFollowing(int userId) {
        return userList.get(userIdToIndexMapping.get(userId)).getFollowing();
    }

    @Override
    public List<Integer> getPosts(int userId) {
        return new ArrayList<>(userList.get(userIdToIndexMapping.get(userId)).getPosts().keySet());
    }

    @Override
    public List<Pair<Integer, Instant>> getPostAfterTimestamp(int userId, Instant time) {
        return userList.get(userIdToIndexMapping.get(userId)).getPosts().entrySet().parallelStream().filter(it -> it.getValue().isAfter(time)).map(ij -> new Pair<>(ij.getKey(), ij.getValue())).collect(Collectors.toList());
    }
}
