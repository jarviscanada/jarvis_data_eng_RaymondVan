package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.utils.JsonUtils;
import ca.jrvs.apps.twitter.utils.TweetUtils;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

  private TwitterDao dao;

  @Before
  public void setUp() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    this.dao = new TwitterDao(httpHelper);
  }

  @Test
  public void create() throws Exception {
    String hashTag ="#abc";
    String text = "@someone sometext " + hashTag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    Tweet postTweet = TweetUtils.buildTweet(text, lon, lat);
    System.out.println("posttweet" + postTweet.getCoordinates().getCoordinates().get(0));
    System.out.println("posttweet" + postTweet.getCoordinates().getCoordinates().get(1));
    System.out.println(JsonUtils.toPrettyJson(postTweet));

    Tweet tweet = dao.create(postTweet);
    System.out.println("tweet" + tweet.getText());
    System.out.println("tweet" + tweet.getCoordinates());
    System.out.println("tweet" + tweet.getCoordinates().getCoordinates().get(0));

    assertEquals(text, tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getCoordinates().size());
    assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    assertTrue(hashTag.contains(tweet.getEntities().getHashtags().get(0).getText()));
  }

  @Test
  public void findById() throws Exception {
    String id = "1595095103307235328";
    long idL = Long.parseLong(id);
    Tweet tweet = dao.findById(id);
    assertNotNull(tweet.getId());
    assertEquals(tweet.getId(), idL);
  }

  @Test
  public void deleteById() throws Exception {
    String id = "1595095103307235328";
    long idL = Long.parseLong(id);
    Tweet tweet = dao.deleteById(id);
    assertNotNull(tweet.getCoordinates());
    assertEquals(tweet.getId(), idL);
  }
}