package twitter.Factory;

import twitter.Exceptions.TwitterServiceException;
import twitter.StorageAdapter.FeedDaoImpl;
import twitter.StorageAdapter.UserDaoImpl;

import java.util.Objects;

public class DaoFactory {
    public static <T> T get(String configKey) throws TwitterServiceException {
        if (Objects.equals(configKey, "userDao")) return (T) UserDaoImpl.getInstance();
        if (Objects.equals(configKey, "feedDao")) return (T) FeedDaoImpl.getInstance();
        throw new TwitterServiceException("Config key is incorrect");
    }
}
