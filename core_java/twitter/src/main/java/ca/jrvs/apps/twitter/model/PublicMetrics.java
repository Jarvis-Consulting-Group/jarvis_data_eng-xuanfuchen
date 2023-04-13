package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicMetrics {
    @JsonProperty("retweet_count")
    private Integer retweetCount;
    @JsonProperty("reply_count")
    private Integer replyCount;
    @JsonProperty("like_count")
    private Integer likeCount;
    @JsonProperty("quote_count")
    private Integer quoteCount;

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(Integer retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getQuoteCount() {
        return quoteCount;
    }

    public void setQuoteCount(Integer quoteCount) {
        this.quoteCount = quoteCount;
    }

    @Override
    public String toString() {
        return "PublicMetrics{" +
                "retweetCount=" + retweetCount +
                ", replyCount=" + replyCount +
                ", likeCount=" + likeCount +
                ", quoteCount=" + quoteCount +
                '}';
    }
}
