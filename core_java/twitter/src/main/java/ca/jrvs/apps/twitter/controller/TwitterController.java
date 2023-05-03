package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller{
    private static final String COMMA = ",";

    @Autowired
    private Service service;

    public TwitterController(Service service){
        this.service = service;
    }

    /**
     * Parse user argument and post a tweet by calling service classes
     *
     * @param args [tweet_text]
     * @return a posted tweet
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public Tweet postTweet(String[] args) {
        Tweet tweet = new Tweet();
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid number of argument. USAGE: TwitterCLIApp post \"tweet_text\"");
        }
        tweet.getData().setText(args[1]);
        return service.postTweet(tweet);
    }

    @Override
    public Tweet showTweet(String[] args) {
        if(args.length != 2 && args.length != 3){
            throw new IllegalArgumentException(
                    "Invalid number of argument. USAGE: TwitterCLIApp show \"tweet_id\" " +
                            "OPTIONAL:\"tweet_fields_separated_by_comma\"");
        }
        String id = args[1];
        String[] fields = null;
        if(args.length == 3){
            fields = args[2].split(COMMA);
        }
        return service.showTweet(id, fields);
    }

    @Override
    public List<Tweet> deleteTweet(String[] args) {
        if(args.length != 2){
            throw new IllegalArgumentException("Invalid number of argument. USAGE: TwitterCLIApp delete \"tweet_ids_separated_by_comma\"");
        }
        String[] ids = args[1].split(COMMA);
        return service.deleteTweets(ids);
    }
}