package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.utils.TweetUtils;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterServiceIntTest {

  private TwitterService service;

  @Before
  public void setUp() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    TwitterDao twitterDao = new TwitterDao(httpHelper);
    service = new TwitterService(twitterDao);
  }

  @Test
  public void postTweet() {
    String hashTag ="#abc";
    String text = "@someone sometext " + hashTag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    Tweet builtTweet = TweetUtils.buildTweet(text, lon, lat);
    Tweet tweet = service.postTweet(builtTweet);
    System.out.println(tweet.getId()); // 1600135218844909573
    assertEquals(text, tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getCoordinates().size());
    assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    assertTrue(hashTag.contains(tweet.getEntities().getHashtags().get(0).getText()));
  }

  @Test
  public void showTweet() {
    String id = "1600135218844909573";
    long idL = Long.parseLong(id);
    Tweet tweet = service.showTweet(id, null);
    assertNotNull(tweet.getId());
    assertEquals(tweet.getId(), idL);
  }

  @Test
  public void deleteTweets() {
    String id = "1600135218844909573";
    String[] ids = {"1600135218844909573"};
    long idL = Long.parseLong(id);
    List<Tweet> deletedTweets = service.deleteTweets(ids);
    Tweet tweet = deletedTweets.get(0);
    assertEquals(tweet.getId(), idL);
  }
}