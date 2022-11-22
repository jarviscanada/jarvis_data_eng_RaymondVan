package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "user_mentions",
    "hashtags",
})
public class Entities {
  @JsonProperty("user_mentions")
  private List<UserMention> userMentions;
  @JsonProperty("hashtags")
  private List<Hashtag> hashtags;

  @JsonProperty("user_mentions")
  public List<UserMention> getUserMentions() {
    return userMentions;
  }

  @JsonProperty("user_mentions")
  public void setUserMentions(List<UserMention> userMentions) {
    this.userMentions = userMentions;
  }
  @JsonProperty("hashtags")
  public List<Hashtag> getHashtags() {
    return hashtags;
  }

  @JsonProperty("hashtags")
  public void setHashtags(List<Hashtag> hashtags) {
    this.hashtags = hashtags;
  }
}
