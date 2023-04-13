package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Data;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.omg.CORBA.portable.ApplicationException;

import java.util.List;

import static org.junit.Assert.*;

public class TwitterServiceIntTest {
    private static String CONSUMER_KEY = System.getenv("API_KEY");
    private static String CONSUMER_SECRET = System.getenv("API_KEY_SECRET");
    private static String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");
    private static String TOKEN_SECRET = System.getenv("ACCESS_TOKEN_SECRET");
    private TwitterDao twitterDao;
    private TwitterService twitterService;

    @Before
    public void setUp() {
        HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
                ACCESS_TOKEN, TOKEN_SECRET);
        twitterDao = new TwitterDao(httpHelper);
        twitterService = new TwitterService(twitterDao);
    }
    @Test
    public void testPostAndDeleteTweets() {
        Tweet tweet = new Tweet();

        tweet.getData().setText("Hello World! Test Twitter service");
        Tweet response = twitterService.postTweet(tweet);
        assertEquals(tweet.getData().getText(), response.getData().getText());

        String[] ids = {response.getData().getId()};

        List<Tweet> deleteList = twitterService.deleteTweets(ids);
        assertNotNull(deleteList);
    }

    //sadly this won't work in the free-access tier anymore
    //get tweet by ID is a paid API after 2023-03-29
//    @Test
//    public void testShowTweet(){
//        String id = "1643706343755325440";
//        String expected = "Hello World!";
//
//        String[] fields = {"id", "text"};
//        Tweet response = twitterService.showTweet(id, fields);
//        assertEquals(expected, response.getData().getText());
//    }
}
