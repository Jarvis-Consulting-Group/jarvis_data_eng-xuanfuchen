package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;

import java.io.IOException;

public class main {
    public static void main(String[] args) {
        String json = "{\n" +
                "    \"data\": {\n" +
                "        \"edit_history_tweet_ids\": [\n" +
                "            \"1646166813787451392\"\n" +
                "        ],\n" +
                "        \"id\": \"1646166813787451392\",\n" +
                "        \"text\": \"Tweeting with a poll\"\n" +
                "    }\n" +
                "}";

        try {
            Tweet tweet = JsonUtil.toObjectFromJson(json, Tweet.class);
            System.out.println(tweet.toString());
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert JSON to Object", e);
        }
    }
}
