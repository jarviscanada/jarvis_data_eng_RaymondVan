package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


public class TwitterService implements Service {

  private CrdDao dao;
  @Autowired
  public TwitterService(CrdDao dao) { this.dao = dao; }

  @Override
  public Tweet postTweet(Tweet tweet) {
    validatePostTweet(tweet);
    return (Tweet) dao.create(tweet);
  }

  private void validatePostTweet(Tweet tweet) {
    if (tweet.getText().length() > 140) {
      throw new IllegalArgumentException("Tweet more than 140 chars");
    }
    List<Double> coords = tweet.getCoordinates().getCoordinates();
    double lon = coords.get(0);
    double lat = coords.get(1);
    if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
      throw new IllegalArgumentException("Invalid latitude or longitude");
    }
  }

  @Override
  public Tweet showTweet(String id, String[] fields) {
    try {
      long idL = Long.parseLong(id);
    } catch (Exception e) {
      System.out.println("Id not valid");
      throw e;
    }
    Tweet tweet = (Tweet) dao.findById(id);

    if (fields == null || fields.length == 0) {
      return tweet;
    }

    Tweet newTweet = new Tweet();
    Arrays.stream(fields).forEach(field -> {
      switch(field) {
        case "id":
          newTweet.setId(tweet.getId());
          break;
        case "id_str":
          newTweet.setIdStr(tweet.getIdStr());
          break;
        case "text":
          newTweet.setText(tweet.getText());
          break;
        case "entities":
          newTweet.setEntities(tweet.getEntities());
          break;
        case "coordinates":
          newTweet.setCoordinates(tweet.getCoordinates());
          break;
        case "retweet_count":
          newTweet.setRetweetedCount(tweet.getRetweetedCount());
          break;
        case "favorite_count":
          newTweet.setFavoritedCount(tweet.getFavoritedCount());
          break;
        case "favorited":
          newTweet.setFavorited(tweet.getFavorited());
          break;
        case "retweeted":
          newTweet.setRetweeted(tweet.getRetweeted());
          break;
        default:
          throw new IllegalArgumentException("Invalid field");
      }
    });

    return newTweet;
  }



  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> deleted = new ArrayList<>();
    for (String id : ids) {
      Tweet tweet = (Tweet) dao.deleteById(id);
      deleted.add(tweet);
    }
    return deleted;
  }
}
