package twitter.StorageAdapter;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import twitter.Interface.FeedDao;
import twitter.Model.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FeedDaoImpl implements FeedDao {

    Map<Integer, CircularFifoQueue<Pair<Integer,Instant>>> userFeedList;
    public static volatile FeedDaoImpl INSTANCE;
    public static final Integer queueCapacity = 10;

    private FeedDaoImpl() {
        userFeedList = new ConcurrentHashMap<>();
    }

    public static FeedDaoImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (FeedDaoImpl.class) {
                if (INSTANCE == null) INSTANCE = new FeedDaoImpl();
            }
        }
        return INSTANCE;
    }

    @Override
    public List<Integer> getNewsFeed(int userId) {
        if (isUserFeedPresent(userId)) return new ArrayList(userFeedList.get(userId));
        return Collections.emptyList();
    }

    @Override
    public void updateNewsFeedOnPostByUser(int userId, int postId, Instant time) {
        if (!isUserFeedPresent(userId)) {
            createUserFeed(userId);
        }
        userFeedList.get(userId).add(new Pair<>(postId, time));
    }

    @Override
    public void updateNewsFeedOnUnfollow(int userId, List<Integer> postIds) {
        if (!isUserFeedPresent(userId)) {
            createUserFeed(userId);
        }
        userFeedList.put(userId, new CircularFifoQueue<>(userFeedList.get(userId).parallelStream().filter(it -> !postIds.contains(it.getFirst())).collect(Collectors.toList())));
    }

    @Override
    public void updateNewsFeedOnFollow(int userId, List<Pair<Integer, Instant>> posts) {
        if (!isUserFeedPresent(userId)) {
            createUserFeed(userId);
            posts.forEach( it -> {
                updateNewsFeedOnPostByUser(userId, it.getFirst(), it.getSecond());
            });
            return;
        }
        // For each post check the timestamp and put it at correct place in the queue
        List<Pair<Integer, Instant>> list = userFeedList.get(userId).stream().collect(Collectors.toList());
        int initialSize = list.size();
        int initialIndexOfQueue = 0;
        int postIndex = 0;
        for (Pair<Integer, Instant> post : posts) {
            if (initialIndexOfQueue < initialSize) postIndex++;
            else break;
            for (int i = initialIndexOfQueue; i < initialSize; i++) {
                if (list.get(i).getSecond().isBefore(post.getSecond())) {
                    list.add(i, post);
                    initialIndexOfQueue++;
                    break;
                }
            }
        }
        // cleanup - add the remaining posts in the queue if the queue is not full
        for (int i = postIndex; i < queueCapacity && postIndex < posts.size() && initialIndexOfQueue < queueCapacity; i++, initialIndexOfQueue++) {
            list.add(initialIndexOfQueue, posts.get(postIndex));
        }
    }

    @Override
    public Instant getTimestampOfLastPostInUserFeed(int userId) {
        if (isUserFeedPresent(userId)) return userFeedList.get(userId).get(userFeedList.get(userId).size() - 1).getSecond();
        return null;
    }

    private Boolean isUserFeedPresent(int userId) { return userFeedList.containsKey(userId); }

    private void createUserFeed(int userId) { userFeedList.put(userId, new CircularFifoQueue<Pair<Integer, Instant>>(queueCapacity)); }
}
