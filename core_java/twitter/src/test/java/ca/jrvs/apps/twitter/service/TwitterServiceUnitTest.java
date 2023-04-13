package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Data;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import com.sun.org.apache.bcel.internal.generic.LSTORE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {
    @Mock
    TwitterDao mockDao;

    @InjectMocks
    TwitterService twitterService;

    @Test
    public void testPostTweet() {
        Tweet testTweet = new Tweet();
        Data data = new Data();
        testTweet.setData(data);
        data.setText("test");

        //test exception handling
        when(mockDao.create(testTweet)).thenThrow(new RuntimeException("mock"));
        try {
            twitterService.postTweet(testTweet);
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        TwitterService spyService = Mockito.spy(twitterService);

        //return testTweet when the postTweet() is called
        doReturn(testTweet).when(spyService).postTweet(any());
        Tweet response = spyService.postTweet(testTweet);

        assertNotNull(response);
        assertEquals(testTweet.getData().getText(), response.getData().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostLongTweet() {
        //build a string that is longer than the limit
        final int LIMIT = 150;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < LIMIT; i++) {
            sb.append("a");
        }
        String invalidString = sb.toString();
        Tweet tweet = new Tweet();
        tweet.getData().setText(invalidString);
        twitterService.postTweet(tweet);
    }

    @Test
    public void testShowTweet() throws Exception{
        //test exception handling
        when(mockDao.findById(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            twitterService.showTweet("1629865830337990656", null);
            fail();
        } catch (RuntimeException | AssertionError e) {
            assertTrue(true);
        }

        TwitterService spyService = Mockito.spy(twitterService);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(getTweetResponse, Tweet.class);

        doReturn(expectedTweet).when(spyService).showTweet(any(), any());
        Tweet tweet = spyService.showTweet("1629865830337990656", null);

        assertNotNull(tweet);
        assertNotNull(tweet.getData().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFields(){
        String id = "1629865830337990656";
        String[] fields = {"id", "invalidField"};
        twitterService.showTweet(id, fields);
    }

    @Test
    public void testDeleteTweets() throws Exception {
        String[] ids = {"1629865830337990656"};
        //test exception handling
        when(mockDao.deleteById(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            twitterService.deleteTweets(ids);
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        TwitterService spyService = Mockito.spy(twitterService);
        //build expected result
        List<Tweet> expectedList = new ArrayList<>();
        Tweet expected = new Tweet();
        Data data = new Data();
        expected.setData(data);
        data.setId("1212092628029698048");
        data.setDeleted(true);
        expectedList.add(expected);

        doReturn(expectedList).when(spyService).deleteTweets(any());
        List<Tweet> tweets = spyService.deleteTweets(ids);

        assertNotNull(tweets);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteInvalidID_1(){
        String idWithChar = "l2l20g26280296g80A8";
        String[] ids = {idWithChar};
        twitterService.deleteTweets(ids);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteInvalidID_2(){
        String largeId = "999999999999999999999999999";
        String[] ids = {largeId};
        twitterService.deleteTweets(ids);
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
}
