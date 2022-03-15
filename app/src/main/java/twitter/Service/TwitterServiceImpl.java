package twitter.Service;

import twitter.Exceptions.TwitterServiceException;
import twitter.Factory.DaoFactory;
import twitter.Interface.FeedService;
import twitter.Interface.TwitterService;
import twitter.Interface.UserDao;

import java.time.Instant;
import java.util.List;

public class TwitterServiceImpl implements TwitterService {

    UserDao userDao;
    FeedService feedService;

    public TwitterServiceImpl() throws TwitterServiceException {
        userDao = DaoFactory.get("userDao");
        feedService = new FeedServiceImpl();
    }

    @Override
    public void postTweet(int userId, int tweetId) {
        Instant time = Instant.now();
        userDao.addPost(userId, tweetId, time);
        feedService.onNewPostId(userId, tweetId, time);
    }

    @Override
    public List<Integer> getNewsFeed(int userId) {
        return feedService.getFeed(userId);
    }

    @Override
    public void follow(int followerId, int followeeId) {
        userDao.addFollowing(followerId, followeeId);
        userDao.addfollower(followeeId, followerId);
        feedService.onFollow(followeeId, followeeId);
    }

    @Override
    public void unfollow(int followerId, int followeeId) {
        userDao.removefollower(followerId, followeeId);
        feedService.onUnfollow(followerId, followeeId);
    }
}
