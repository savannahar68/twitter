package twitter.Interface;

import twitter.Model.Pair;

import java.time.Instant;
import java.util.List;

public interface FeedDao {
    List<Integer> getNewsFeed(int userId);
    
    void updateNewsFeedOnPostByUser(int userId, int postId, Instant time);

    void updateNewsFeedOnUnfollow(int userId, List<Integer> postIds);
    
    void updateNewsFeedOnFollow(int userId, List<Pair<Integer, Instant>> posts);

    Instant getTimestampOfLastPostInUserFeed(int userId);
}
