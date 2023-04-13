package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Data;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {
    @Mock
    HttpHelper mockHelper;

    @InjectMocks
    TwitterDao twitterDao;

    @Test
    public void testShowTweet() throws Exception {
        //Test if the exceptions are handled
        when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            twitterDao.findById("1629865830337990656");
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        //mock the httpHelper and return null when httpGet is called with any not null argument
        when(mockHelper.httpGet(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(twitterDao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(getTweetResponse, Tweet.class);
        //return the expectedTweet directly when parseResponseBody is called by any arguments
        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.findById("1629865830337990656");

        assertNotNull(tweet);
        assertNotNull(tweet.getData().getText());
    }

    @Test
    public void testDeleteTweet() throws Exception {
        //Test if the exceptions are handled
        when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            twitterDao.findById("1629865830337990656");
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        //mock the httpHelper and return null when httpGet is called with any not null argument
        when(mockHelper.httpGet(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(twitterDao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(deleteTweetResponse, Tweet.class);
        //return the expectedTweet directly when parseResponseBody is called by any arguments
        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.deleteById("1629865830337990656");

        assertNotNull(tweet);
        assertTrue(tweet.getData().getDeleted());
    }

    @Test
    public void testPostTweet() throws Exception {

        Tweet tweet = new Tweet();
        Data data = new Data();
        data.setText("Hello World!");
        tweet.setData(data);

        when(mockHelper.httpPost(isNotNull(), isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            twitterDao.create(tweet);
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        when(mockHelper.httpPost(isNotNull(), isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(twitterDao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(postTweetResponse, Tweet.class);

        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet response = spyDao.create(tweet);

        assertNotNull(response);
        assertNotNull(response.getData().getText());
    }

    private static final String getTweetResponse = "{\n"
            + "\"data\": {\n"
            + "     \"id\":\"1629865830337990656\",\n"
            + "     \"text\":\"@HotForMoot Hey @HotForMoot ??, we've been hard at work developing our new free &amp; basic API tiers. We'll get back to you following the launch. \\n\\nHint: it's coming very soon!\",\n"
            + "     \"created_at\":\"2023-02-26T15:27:50.000Z\",\n"
            + "     \"entities\": {\n"
            + "       \"hashtags\": [{\n"
            + "         \"start\": 8,\n"
            + "         \"end\": 13,\n"
            + "         \"tag\": \"test\"\n"
            + "       }],\n"
            + "       \"mentions\": [{\n"
            + "         \"start\": 0,\n"
            + "         \"end\": 11,\n"
            + "         \"username\": \"HotForMoot\",\n"
            + "         \"id\":\"1519594215352668160\"\n"
            + "       },\n"
            + "       {\n"
            + "         \"start\": 16,\n"
            + "         \"end\": 27,\n"
            + "         \"username\": \"HotForMoot\",\n"
            + "         \"id\":\"1519594215352668160\"\n"
            + "       }]\n"
            + "       },\n"
            + "     \"public_metrics\": {\n"
            + "       \"retweet_count\": 1,\n"
            + "       \"reply_count\": 6,\n"
            + "       \"like_count\": 10,\n"
            + "       \"quote_count\": 3,\n"
            + "       \"impression_count\": 3244\n"
            + "     }\n"
            + "   }\n"
            + "}";
    private static final String deleteTweetResponse = "{\n" +
            "  \"data\" : {\n" +
            "    \"deleted\" : true\n" +
            "  }\n" +
            "}\n";

    private static final String postTweetResponse = "{\n" +
            "  \"data\" : {\n" +
            "    \"edit_history_tweet_ids\" : [ \"1643706343755325440\" ],\n" +
            "    \"id\" : \"1643706343755325440\",\n" +
            "    \"text\" : \"Hello World!\"\n" +
            "  }\n" +
            "}\n";
}
