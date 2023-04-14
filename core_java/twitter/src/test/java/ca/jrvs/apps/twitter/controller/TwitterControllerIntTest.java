package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TwitterControllerIntTest {
    private static String CONSUMER_KEY = System.getenv("API_KEY");
    private static String CONSUMER_SECRET = System.getenv("API_KEY_SECRET");
    private static String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");
    private static String TOKEN_SECRET = System.getenv("ACCESS_TOKEN_SECRET");

    private TwitterDao twitterDao;
    private TwitterService twitterService;
    private TwitterController twitterController;

    @Before
    public void setup() {
        HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
                ACCESS_TOKEN, TOKEN_SECRET);
        twitterDao = new TwitterDao(httpHelper);
        twitterService = new TwitterService(twitterDao);
        twitterController = new TwitterController(twitterService);
    }

    @Test
    public void testPostAndDeleteTweet() {
        String[] postArgs = {"post", "Testing controller using Junit"};
        Tweet postResponse = twitterController.postTweet(postArgs);
        String id = postResponse.getData().getId();
        assertEquals(postArgs[1], postResponse.getData().getText());

        String[] deleteArgs = {"delete", id};
        List<Tweet> deleteResponse = twitterController.deleteTweet(deleteArgs);
        assertTrue(deleteResponse.get(0).getData().getDeleted());
        assertEquals(deleteResponse.get(0).getData().getId(), id);
    }

    //sadly this won't work in the free-access tier anymore
    //get tweet by ID is a paid API after 2023-03-29
//    @Test
//    public void showTweet() {
//        String id = "1643706343755325440";
//        String expectedText = "Hello World!";
//        String[] args = {"show", id, null};
//
//        Tweet response = twitterController.showTweet(args);
//        assertEquals(id, response.getData().getId());
//        assertEquals(expectedText, response.getData().getText());
//    }
}
