package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.utils.JsonUtils;
import ca.jrvs.apps.twitter.utils.TweetUtils;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {
  String tweetJsonStr = "{\n"
      + "\"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
      + "\"id\":1097607853932564480,\n"
      + "\"text\":\"Test\",\n"
      + "\"entities\" : {\n"
      + "    \"hashtags\":[],"
      + "    \"user_mentions\":[]"
      + "    },\n"
      + "\"coordinates\":null,\n"
      + "\"retweet_count\":0,\n"
      + "\"favorite_count\":0,\n"
      + "\"favorited\":false,\n"
      + "\"retweeeted\":false\n"
      + "}";

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao dao;

  @Test
  public void postTweet() throws Exception {
    // test failed request
    String hashTag = "#abc";
    String text = "@someone sometext " + hashTag + " " + System.currentTimeMillis();
    double lat = 1d;
    double lon = -1d;
    // exception is expected here
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException(("mock")));

    try {
      dao.create(TweetUtils.buildTweet(text, lon, lat));
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }


    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = JsonUtils.toObjectFromJson(tweetJsonStr, Tweet.class);
    // mock parseResponsebody
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    Tweet tweet = spyDao.create(TweetUtils.buildTweet(text, lon, lat));
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void findById() throws IOException {
    when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      dao.findById("1097607853932564480");
      fail();
    } catch(RuntimeException e) {
      assertTrue(true);
    }

    Tweet tweet;
    when(mockHelper.httpGet(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = JsonUtils.toObjectFromJson(tweetJsonStr, Tweet.class);

    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    tweet = spyDao.findById("1097607853932564480");
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
    assertEquals(tweet, expectedTweet);
  }

  @Test
  public void deleteById() throws Exception {
    when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      dao.deleteById("1097607853932564480");
      fail();
    } catch(RuntimeException e) {
      assertTrue(true);
    }

    Tweet tweet;
    when(mockHelper.httpGet(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = JsonUtils.toObjectFromJson(tweetJsonStr, Tweet.class);

    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    tweet = spyDao.findById("1097607853932564480");
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
    assertEquals(tweet, expectedTweet);
  }

}

