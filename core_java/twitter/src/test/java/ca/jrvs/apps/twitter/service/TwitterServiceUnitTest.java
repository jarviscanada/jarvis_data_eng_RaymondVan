package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.utils.JsonUtils;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {
  @Mock
  TwitterDao dao;

  @InjectMocks
  TwitterService service;

  String tweetStr = "{\n"
      + "\"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
      + "\"id\":1,\n"
      + "\"text\":\"Test\",\n"
      + "\"entities\" : {\n"
      + "    \"hashtags\":[],"
      + "    \"user_mentions\":[]"
      + "    },\n"
      + "\"coordinates\":{"
      + "    \"coordinates\": [50.0, 0.0],"
      + "    \"type\": \"point\""
      + "    },"
      + "\"retweet_count\":0,\n"
      + "\"favorite_count\":0,\n"
      + "\"favorited\":false,\n"
      + "\"retweeeted\":false\n"
      + "}";

  Tweet mockTweet;

  @Before
  public void setUp() throws Exception {
    mockTweet = JsonUtils.toObjectFromJson(tweetStr, Tweet.class);
  }

  @Test
  public void postTweet() throws IOException {
    when(dao.create(any())).thenReturn(mockTweet);
    Tweet tweet = service.postTweet(mockTweet);
    assertNotNull(tweet);
    assertEquals(mockTweet, tweet);
  }

  @Test
  public void showTweet() throws IOException {
    when(dao.findById(any())).thenReturn(mockTweet);
    Tweet tweet = service.showTweet(Long.toString(mockTweet.getId()), null);
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
    assertEquals(mockTweet, tweet);
  }

  @Test
  public void deleteTweets() {
    Tweet tweet = new Tweet();
    when(dao.deleteById(any())).thenReturn(tweet);
    List<Tweet> responseTweets = service.deleteTweets(new String[] {"hi", "bye"});
    responseTweets.forEach(deletedTweet -> {
      assertNotNull(deletedTweet);
      assertEquals(deletedTweet, tweet);
    });
  }
}
