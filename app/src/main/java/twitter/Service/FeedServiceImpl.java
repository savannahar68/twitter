package twitter.Service;

import twitter.Exceptions.TwitterServiceException;
import twitter.Factory.DaoFactory;
import twitter.Interface.FeedService;
import twitter.Interface.UserDao;
import twitter.Model.Pair;
import twitter.StorageAdapter.FeedDaoImpl;

import java.time.Instant;
import java.util.List;

public class FeedServiceImpl implements FeedService {

    UserDao userDao;
    FeedDaoImpl feedDao;

    public FeedServiceImpl() throws TwitterServiceException {
        userDao = DaoFactory.get("userDao");
        feedDao = DaoFactory.get("feedDao");
    }

    @Override
    public void onNewPostId(int userId, int postId, Instant time) {
        feedDao.updateNewsFeedOnPostByUser(userId, postId, time);
        userDao.getFollowers(userId).parallelStream().forEach( it -> {
            feedDao.updateNewsFeedOnPostByUser(it, postId, time);
        });
    }

    @Override
    public void onUnfollow(int userId, int userIdUnfollowed) {
        List<Integer> postIds = userDao.getPosts(userIdUnfollowed);
        feedDao.updateNewsFeedOnUnfollow(userId, postIds);
    }

    @Override
    public void onFollow(int userId, int userIdFollow) {
        Instant timeOfLastPostInUserFeed = feedDao.getTimestampOfLastPostInUserFeed(userId);
        List<Pair<Integer, Instant>> posts = userDao.getPostAfterTimestamp(userIdFollow, timeOfLastPostInUserFeed);
        feedDao.updateNewsFeedOnFollow(userId, posts);
    }

    @Override
    public List<Integer> getFeed(int userId) {
        return feedDao.getNewsFeed(userId);
    }
}
