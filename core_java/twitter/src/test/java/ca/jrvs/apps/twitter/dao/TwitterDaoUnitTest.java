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
            twitterDao.findById("1212092628029698048");
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
        Tweet tweet = spyDao.findById("1212092628029698048");

        assertNotNull(tweet);
        assertNotNull(tweet.getData().getText());
    }

    @Test
    public void testDeleteTweet() throws Exception {
        //Test if the exceptions are handled
        when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            twitterDao.findById("1212092628029698048");
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        //mock the httpHelper and return null when httpGet is called with any not null argument
        when(mockHelper.httpDelete(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(twitterDao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(deleteTweetResponse, Tweet.class);
        //return the expectedTweet directly when parseResponseBody is called by any arguments
        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.deleteById("1212092628029698048");

        assertNotNull(tweet);
        assertTrue(tweet.getData().getDeleted());
    }

    @Test
    public void testPostTweet() throws Exception {
        Tweet tweet = new Tweet();
        tweet.getData().setText("Hello World!");

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

    private static final String getTweetResponse = "{\n" +
            "  \"data\": {\n" +
            "    \"edit_history_tweet_ids\": [\n" +
            "      \"1212092628029698048\"\n" +
            "    ],\n" +
            "    \"text\": \"We believe the best future version of our API will come from building it with YOU. Here¡¯s to another great year with everyone who builds on the Twitter platform. We can¡¯t wait to continue working with you in the new year. https://t.co/yvxdK6aOo2\",\n" +
            "    \"id\": \"1212092628029698048\",\n" +
            "    \"public_metrics\": {\n" +
            "      \"retweet_count\": 7,\n" +
            "      \"reply_count\": 3,\n" +
            "      \"like_count\": 38,\n" +
            "      \"quote_count\": 1\n" +
            "    },\n" +
            "    \"created_at\": \"2019-12-31T19:26:16.000Z\"\n" +
            "  }\n" +
            "}";
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
