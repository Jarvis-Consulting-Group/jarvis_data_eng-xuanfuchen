package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.JsonUtil;
import com.fasterxml.jackson.core.JsonParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {
    @Mock
    TwitterService twitterService;

    @InjectMocks
    TwitterController twitterController;

    @Test
    public void testPostTweet(){
        String[] args = {"post", "Test Post Tweet"};
        Tweet mockTweet = new Tweet();
        mockTweet.getData().setText("Test Post Tweet");

        when(twitterService.postTweet(any())).thenReturn(mockTweet);
        Tweet response = twitterController.postTweet(args);
        assertNotNull(response);
        assertNotNull(response.getData().getText());
    }

    @Test
    public void testShowTweet() throws IOException {
        String id = "1212092628029698048";
        String[] args = {"show", id, "id,text"};

        final String mockTweetJson = "{\n" +
                "   \"data\":{\n" +
                "      \"text\":\"We believe the best future version of our API will come from building it with YOU. Here¡¯s to another great year with everyone who builds on the Twitter platform. We can¡¯t wait to continue working with you in the new year. https://t.co/yvxdK6aOo2\",\n" +
                "      \"id\":\"1212092628029698048\"\n" +
                "   }\n" +
                "}";

        Tweet mockTweet = JsonUtil.toObjectFromJson(mockTweetJson, Tweet.class);
        when(twitterService.showTweet(any(), any())).thenReturn(mockTweet);
        Tweet response = twitterController.showTweet(args);
        assertNotNull(response);
        //properties that should be there
        assertNotNull(response.getData().getText());
        assertNotNull(response.getData().getId());
        //properties that shouldn't be there
        assertNull(response.getData().getEntities());
        assertNull(response.getData().getCreatedAt());
        assertNull(response.getData().getPublicMetrics());
    }

    @Test
    public void testDeleteTweet() throws IOException {
        String id = "1212092628029698048";
        String[] args = {"delete", id};
        String mockJson = "{\"data\":{\"id\":\"1212092628029698048\",\"deleted\":true}}";

        Tweet mockTweet = JsonUtil.toObjectFromJson(mockJson, Tweet.class);
        List<Tweet> mockTweets = new ArrayList<>();
        mockTweets.add(mockTweet);
        when(twitterService.deleteTweets(any())).thenReturn(mockTweets);
        List<Tweet> respondTweetList = twitterController.deleteTweet(args);
        assertNotNull(respondTweetList);
        assertTrue(respondTweetList.get(0).getData().getDeleted());
    }
}
