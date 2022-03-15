package twitter.Interface;

import java.time.Instant;
import java.util.List;

public interface FeedService {
    void onNewPostId(int userId, int postId, Instant time);
    void onUnfollow(int userId, int userIdUnfollowed);
    void onFollow(int userId, int userIdFollow);
    List<Integer> getFeed(int userId);
}