package ca.jrvs.apps.twitter.controller;

import static ca.jrvs.apps.twitter.utils.TweetUtils.buildTweet;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class TwitterController implements Controller {
  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private Service service;

  @Autowired
  public TwitterController(Service service) {
    this.service = service;
  }
  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3 || !args[0].equals("post")) {
      throw new IllegalArgumentException(
          "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    String tweet_txt = args[1];
    String coord = args[2];
    String[] coordArray = coord.split(COORD_SEP);
    if (coordArray.length != 2 || StringUtils.isEmpty(tweet_txt)) {
      throw new IllegalArgumentException(
          "Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }
    Double lat = null;
    Double lon = null;
    try {
      lat = Double.parseDouble(coordArray[0]);
      lon = Double.parseDouble(coordArray[1]);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" latitude:longitude",
          e);
    }

    Tweet postTweet = buildTweet(tweet_txt, lon, lat);
    return service.postTweet(postTweet);
  }

  @Override
  public Tweet showTweet(String[] args) {
    if (args.length != 2 || !args[0].equals("show")) {
      throw new IllegalArgumentException("Usage: TwitterCLIApp show id");
    }
    String id = args[1];
    try {
      long idL = Long.parseLong(id);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid ID: must be integer");
    }
    return service.showTweet(id, null);
  }

  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length != 2 || !args[0].equals("delete")) {
      throw new IllegalArgumentException("Usage: TwitterCLIApp delete [ids...]");
    }
    String ids = args[1];
    String[] idArr = ids.split(COMMA);

    try {
      long idL;
      for (String id : idArr) {
        idL = Long.parseLong(id);
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid ID: must be integer");
    }
    return service.deleteTweets(idArr);
  }
}
