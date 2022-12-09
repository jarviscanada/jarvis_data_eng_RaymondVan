package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.utils.TweetUtils;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntTest {
  private TwitterController controller;
  @Before
  public void setUp() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    TwitterDao twitterDao = new TwitterDao(httpHelper);
    TwitterService service = new TwitterService(twitterDao);
    controller = new TwitterController(service);
  }

  @Test
  public void postTweet() {
    String text = "@someone sometext " + " " + System.currentTimeMillis();
    String coords = "1:-1";
    String[] args = {"post", text, coords};
    Tweet tweet = controller.postTweet(args);
    System.out.println(tweet.getId()); //
    assertEquals(text, tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getCoordinates().size());
  }

  @Test
  public void showTweet() {
    String text = "@someone sometext " + " " + System.currentTimeMillis();
    String coords = "1:-1";
    String[] argsTest = {"post", text, coords};
    Tweet tweetTest = controller.postTweet(argsTest);

    String id = tweetTest.getIdStr();
    String[] args = {"show", id};
    Tweet tweet = controller.showTweet(args);

    assertNotNull(tweet);
    assertEquals(tweetTest.getId(), tweet.getId());
  }

  @Test
  public void deleteTweet() {
    String text = "@someone sometext " + " " + System.currentTimeMillis();
    String coords = "1:-1";
    String[] argsTest = {"post", text, coords};
    Tweet tweetTest = controller.postTweet(argsTest);

    String id = tweetTest.getIdStr();
    String[] args = {"delete", id};
    List<Tweet> deleted = controller.deleteTweet(args);

    assertNotNull(deleted);
    assertEquals(deleted.get(0).getId(), tweetTest.getId());
  }
}