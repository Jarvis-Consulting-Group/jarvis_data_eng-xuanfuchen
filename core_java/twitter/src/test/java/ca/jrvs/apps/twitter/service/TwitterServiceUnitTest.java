package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.model.Data;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {
    @Mock
    TwitterDao mockDao;

    @InjectMocks
    TwitterService twitterService;

    @Test
    public void testPostTweet() {
        Tweet mockTweet = new Tweet();
        Data data = mockTweet.getData();
        data.setText("test");

        //test exception handling
        when(mockDao.create(mockTweet)).thenThrow(new RuntimeException("mock"));
        try {
            twitterService.postTweet(mockTweet);
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        TwitterService spyService = Mockito.spy(twitterService);

        //mock TwitterDao and make it return mockTweet when got called
        when(mockDao.create(any())).thenReturn(mockTweet);
        Tweet response = spyService.postTweet(mockTweet);

        assertNotNull(response);
        assertEquals(mockTweet.getData().getText(), response.getData().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostLongTweet() {
        Tweet mockTweet = new Tweet();
        Data data = mockTweet.getData();
        data.setText("test");
        //build a string that is longer than the limit
        final int LIMIT = 150;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < LIMIT; i++) {
            sb.append("a");
        }
        String invalidString = sb.toString();
        Tweet tweet = new Tweet();
        tweet.getData().setText(invalidString);
        TwitterService spyService = Mockito.spy(twitterService);
        spyService.postTweet(tweet);
    }

    @Test
    public void testShowTweet() throws Exception{
        //test exception handling
        when(mockDao.findById(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            twitterService.showTweet("1212092628029698048", null);
            fail();
        } catch (RuntimeException | AssertionError e) {
            assertTrue(true);
        }

        TwitterService spyService = Mockito.spy(twitterService);
        Tweet mockTweet = JsonUtil.toObjectFromJson(getTweetResponse, Tweet.class);

        when(mockDao.findById(any())).thenReturn(mockTweet);
        Tweet tweet = spyService.showTweet("1212092628029698048", null);

        assertNotNull(tweet);
        assertNotNull(tweet.getData().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFields() throws IOException {
        String id = "1212092628029698048";
        String[] fields = {"id", "invalidField"};
        Tweet mockTweet = JsonUtil.toObjectFromJson(getTweetResponse, Tweet.class);
        twitterService.showTweet(id, fields);
    }

    @Test
    public void testDeleteTweets() throws Exception {
        String[] ids = {"1212092628029698048"};
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

        when(mockDao.deleteById(any())).thenReturn(expected);
        List<Tweet> tweets = spyService.deleteTweets(ids);

        assertNotNull(tweets);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteInvalidID_1(){
        TwitterService spyService = Mockito.spy(twitterService);
        String idWithChar = "l2l20g26280296g80A8";
        String[] ids = {idWithChar};
        spyService.deleteTweets(ids);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteInvalidID_2(){
        TwitterService spyService = Mockito.spy(twitterService);
        String largeId = "999999999999999999999999999";
        String[] ids = {largeId};
        spyService.deleteTweets(ids);
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
