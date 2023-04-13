package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Data;
import ca.jrvs.apps.twitter.model.Tweet;
import ch.qos.logback.core.encoder.EchoEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TwitterService implements Service{
    @Autowired
    private CrdDao dao;

    private static final Integer TWEET_MAX_LENGTH = 140;

    public TwitterService(CrdDao dao){
        this.dao = dao;
    }
    /**
     * Validate and post a user input Tweet
     *
     * @param tweet tweet to be created
     * @return created tweet
     *
     * @throws IllegalArgumentException if text exceed max number of allowed characters or lat/long out of range
     */
    @Override
    public Tweet postTweet(Tweet tweet) {
        //check the validation of tweet
        validatePostTweet(tweet);
        return (Tweet) dao.create(tweet);
    }

    /**
     * Throw IllegalArgumentException
     * @param tweet
     */
    private void validatePostTweet(Tweet tweet){
        if(tweet.getData().getText().length() >= TWEET_MAX_LENGTH){
            throw new IllegalArgumentException("Failed to post a tweet: Tweet text length exceeds 140 characters");
        }
    }
    /**
     * Search a tweet by ID
     *
     * @param id tweet id
     * @param fields set fields not in the list to null
     * @return Tweet object which is returned by the Twitter API
     *
     * @throws IllegalArgumentException if id or fields param is invalid
     */
    @Override
    public Tweet showTweet(String id, String[] fields) {
        //check the validation of id and fields
        validateShowTweet(id, fields);

        Tweet responseTweet = (Tweet) dao.findById(id);
        //in case of fields are specified, create a custom Tweet with those fields only
        Tweet customTweet = new Tweet();
        Data customData = new Data();
        customTweet.setData(customData);

        //If the fields are not specified, return the whole response
        if(fields == null) {
            return responseTweet;
        } else { //If the fields are specified, add those fields to customTweet and return it
            Data responseData = responseTweet.getData();
            for (String field: fields) {
                switch (field) {
                    case "id":
                        customData.setId(responseData.getId());
                        break;
                    case "text":
                        customData.setText(responseData.getText());
                        break;
                    case "entities":
                        customData.setEntities(responseData.getEntities());
                        break;
                    case "public_metrics":
                        customData.setPublicMetrics(responseData.getPublicMetrics());
                        break;
                    default:
                        break;
                }
            }
            return customTweet;
        }
    }

    /**
     * check if the id and fields are valid values
     * @param id
     * @param fields
     * @throws IllegalArgumentException id is not numerical or not a 64-bit integer
     * @throws IllegalArgumentException fields contains value that is not id, text, entities, or public_metrics
     */
    private void validateShowTweet(String id, String[] fields){
        checkId(id);
        //check if fields contains valid values
        if(fields != null){
            for (String field: fields){
                switch (field) {
                    case "id":
                        break;
                    case "text":
                        break;
                    case "entities":
                        break;
                    case "public_metrics":
                        break;
                    default:
                        throw new IllegalArgumentException("Fields can only contain id, text, entities, and public_metrics");
                }
            }
        }
    }

    /**
     * check if the id is valid
     * @param id
     * @throws IllegalArgumentException when id is not numerical or not a 64-bit integer
     */
    private void checkId(String id){
        //check if ID is numerical
        for(char c : id.toCharArray()){
            if(!Character.isDigit(c))
                throw new IllegalArgumentException("Invalid ID: Twitter ID must be numerical");
        }
        //Check if ID is a 64-bit integer.
        try {
            long l = Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID is too large: Twitter ID must be a 64-bit integer");
        }
    }

    /**
     * Delete Tweet(s) by id(s).
     *
     * @param ids tweet IDs which will be deleted
     * @return A list of Tweets
     *
     * @throws IllegalArgumentException if one of the IDs is invalid.
     */
    @Override
    public List<Tweet> deleteTweets(String[] ids) {
        List<Tweet> tweets = new ArrayList<>();
        for(String id: ids){
            //throws IllegalArgumentException if one of the IDs is invalid.
            checkId(id);

            Tweet response = (Tweet) dao.deleteById(id);
            response.getData().setId(id);
            tweets.add(response);
        }
        return tweets;
    }
}
