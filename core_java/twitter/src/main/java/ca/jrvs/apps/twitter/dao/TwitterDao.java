package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TwitterDao implements CrdDao<Tweet, String> {
    //URI constants
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String V2_PATH_TWEETS = "/2/tweets";

    //URI symbols
    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";

    //Status Codes
    private static final Integer CREATE_SUCCESS = 201;
    private HttpHelper httpHelper;

    @Autowired
    public TwitterDao(HttpHelper httpHelper){
        this.httpHelper = httpHelper;
    }

    @Override
    public Tweet create(Tweet tweet) {
        Tweet validTweet = null;

        try {
            URI uri = new URI(API_BASE_URI + V2_PATH_TWEETS);
            //build JSON style string for request body: {"text": "<tweet_content>"}
            String s = "{\"text\": \"" + tweet.getData().getText() + "\"}";
            HttpResponse response = httpHelper.httpPost(uri, s);
            validTweet = parseResponseBody(response, CREATE_SUCCESS);
            return validTweet;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Exception:", e);
        }
    }

    /**
     * Check response status code and convert response entity to Tweet object
     * @param response HttpResponse
     * @param expectedStatusCode
     * @return validated Tweet object that contains values from the HttpResponse
     */
    private Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode){
        Tweet tweet = null;

        //Check response status
        int status = response.getStatusLine().getStatusCode();
        if (status != expectedStatusCode) {
            try{
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                System.out.println("Response has no entity");
            }
            throw new RuntimeException("Unexpected Status Code: " + status);
        }

        if(response.getEntity() == null) {
            throw new RuntimeException("Empty response body");
        }

        //Convert Response Entity to String
        String jsonStr = "";
        try {
            jsonStr = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert entity to String", e);
        }

        //Convert JSON String into Tweet Object
        try {
            tweet = JsonUtil.toObjectFromJson(jsonStr, Tweet.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert JSON to Object", e);
        }

        return tweet;
    }

    @Override
    public Tweet findById(String s) {
        return null;
    }

    @Override
    public Tweet deleteById(String s) {
        return null;
    }
}
