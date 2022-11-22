package ca.jrvs.apps.twitter.dao.helper;

import static org.junit.Assert.*;

import com.google.gdata.util.common.base.PercentEscaper;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TwitterRestHttpHelperTest {
  private String consumerKey = System.getenv("consumerKey");
  private String consumerSecret = System.getenv("consumerSecret");
  private String accessToken = System.getenv("accessToken");
  private String tokenSecret = System.getenv("tokenSecret");
  private HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
  @Test
  public void httpPost() throws Exception {
    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    // Create components
    String status = "testeststest2";
    System.out.println("Post tweet: " + status);
    HttpResponse responsePost = httpHelper.httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=" + percentEscaper.escape(status)));
    System.out.println(EntityUtils.toString((responsePost.getEntity())));
  }

  @Test
  public void httpGet() throws Exception {
    System.out.println("Get timeline");
    HttpResponse responseGet = httpHelper.httpGet(new URI(
        "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=raymondvan0"));
    System.out.println(EntityUtils.toString(responseGet.getEntity()));
  }
}