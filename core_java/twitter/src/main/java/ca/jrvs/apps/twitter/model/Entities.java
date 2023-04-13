package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entities {
    private List<UserMention> mentions;
    private List<Hashtag> hashtags;

    public List<UserMention> getMentions() {
        return mentions;
    }

    public void setMentions(List<UserMention> mentions) {
        this.mentions = mentions;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    @Override
    public String toString() {
        return "Entities{" +
                "mentions=" + mentions +
                ", hashtags=" + hashtags +
                '}';
    }
}
