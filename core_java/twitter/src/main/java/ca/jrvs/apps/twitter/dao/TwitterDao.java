package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.utils.JsonUtils;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterDao implements CrdDao<Tweet, String> {

  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";

  private static final String QUERY_SYMBOL = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  private static final int HTTP_OK = 200;

  private final HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  @Override
  public Tweet create(Tweet tweet) {
    URI uri;
    try {
      uri = getPostUri(tweet);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);
  }

  private URI getPostUri(Tweet tweet) throws URISyntaxException {
    String uri = API_BASE_URI + POST_PATH + QUERY_SYMBOL;
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    uri += "status" + EQUAL + percentEscaper.escape(tweet.getText()) + AMPERSAND + "long" + EQUAL
        + tweet.getCoordinates().getCoordinates().get(0) + AMPERSAND + "lat" + EQUAL
        + tweet.getCoordinates().getCoordinates().get(1);
    return new URI(uri);
  }

  @Override
  public Tweet findById(String s) {
    URI uri;
    long id = Long.parseLong(s);
    try {
      uri = getFindUri(id);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("invalid id", e);
    }
    HttpResponse response = httpHelper.httpGet(uri);

    return parseResponseBody(response, HTTP_OK);
  }

  @Override
  public Tweet deleteById(String s) {
    URI uri;
    long id = Long.parseLong(s);
    try {
      uri = getDeleteUri(id);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("invalid id", e);
    }

    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);
  }

  private URI getFindUri(long id) throws URISyntaxException {
    String uri = API_BASE_URI + SHOW_PATH + QUERY_SYMBOL + "id" + EQUAL + id;
    return new URI(uri);
  }

  private URI getDeleteUri(long id) throws URISyntaxException {
    String uri = API_BASE_URI + DELETE_PATH + "/" + id + ".json";
    return new URI(uri);
  }

  Tweet parseResponseBody(HttpResponse response, int expectedStatusCode) {
    Tweet tweet;
    int status = response.getStatusLine().getStatusCode();
    if (status != expectedStatusCode) {
      try {
        System.out.println(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        System.out.println("Response has no entity!");
      }
      throw new RuntimeException("Unexpected HTTP status:" + status);
    }

    String jsonString;
    try {
      jsonString = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert entity to String", e);
    }
    try {
      tweet = JsonUtils.toObjectFromJson(jsonString, Tweet.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to make json string to tweet object", e);
    }
    return tweet;
  }


}
