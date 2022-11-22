package ca.jrvs.apps.twitter.utils;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.Arrays;

public class TweetUtils {

  public static Tweet buildTweet(String text, double lon, double lat) {
    Tweet tweet = new Tweet();
    tweet.setText(text);
    Coordinates coordinates = new Coordinates();
    coordinates.setCoordinates(Arrays.asList(lon, lat));
    tweet.setCoordinates(coordinates);
    return tweet;
  }

}
