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

    //HTTP Status Codes
    private static final Integer HTTP_CREATED = 201;
    private static final Integer HTTP_OK = 200;

    @Autowired
    private HttpHelper httpHelper;

    public TwitterDao(HttpHelper httpHelper){
        this.httpHelper = httpHelper;
    }

    /**
     * Post a tweet on Twitter using HTTP POST Method.
     * @param tweet entity that to be created
     * @return response from twitter server
     */
    @Override
    public Tweet create(Tweet tweet) {
        try {
            URI uri = new URI(API_BASE_URI + V2_PATH_TWEETS);
            //build JSON style string for request body: {"text": "<tweet_content>"}
            String s = "{\"text\": \"" + tweet.getData().getText() + "\"}";
            HttpResponse response = httpHelper.httpPost(uri, s);
            return parseResponseBody(response, HTTP_CREATED);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Exception occurs when creating URI for create Tweet", e);
        }
    }

    /**
     * Find tweet by ID using Tweet API V2. It is a paid API after 2023-03-29
     * Using HTTP GET Method
     * @param id tweet id
     * @return A Tweet object with specified properties.
     */
    @Override
    public Tweet findById(String id) {
        //build the API call with fields and expansions:
        //tweet.fields=created_at,entities,public_metrics&expansions=geo.place_id&place.fields=geo
        String tweetFields = "created_at,entities,public_metrics";
        String expansions = "geo.place_id";
        String placeFields = "geo";
        String apiCall = API_BASE_URI + V2_PATH_TWEETS + "/" + id.trim() + QUERY_SYM +
                "tweet.fields" + EQUAL + tweetFields + AMPERSAND +
                "expansions" + EQUAL + expansions + AMPERSAND +
                "place.fields" + EQUAL + placeFields;

        try {
            URI uri = new URI(apiCall);

            HttpResponse response = httpHelper.httpGet(uri);
            return parseResponseBody(response, HTTP_OK);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Exception occurs when creating findById URI", e);
        }
    }

    /**
     * Delete Tweet by ID, using HTTP DELETE method
     * @param id of the entity to be deleted
     * @return response from twitter server
     */
    @Override
    public Tweet deleteById(String id) {
        String apiCall = API_BASE_URI + V2_PATH_TWEETS + "/" + id.trim();
        try {
            URI uri = new URI(apiCall);
            HttpResponse response = httpHelper.httpDelete(uri);
            return parseResponseBody(response, HTTP_OK);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Exception occurs when creating deleteByID URI", e);
        }
    }

    /**
     * Check response status code and convert response entity to Tweet object
     * @param response HttpResponse
     * @param expectedStatusCode
     * @return validated Tweet object that contains values from the HttpResponse
     */
    public Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode){
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
}
