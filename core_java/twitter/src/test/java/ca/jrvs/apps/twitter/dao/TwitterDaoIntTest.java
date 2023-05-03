package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Data;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TwitterDaoIntTest {
    //API keys
    private static String CONSUMER_KEY = System.getenv("API_KEY");
    private static String CONSUMER_SECRET = System.getenv("API_KEY_SECRET");
    private static String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");
    private static String TOKEN_SECRET = System.getenv("ACCESS_TOKEN_SECRET");
    private TwitterDao twitterDao;

    @Before
    public void setUp () {
        HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
                ACCESS_TOKEN, TOKEN_SECRET);
        twitterDao = new TwitterDao(httpHelper);
    }

    @Test
    public void testCreateAndDeleteById(){
        Tweet tweet = new Tweet();
        Data data = new Data();
        data.setText("This is a tweet from JUnit test :)");
        tweet.setData(data);

        //post a tweet
        Tweet response = twitterDao.create(tweet);
        String tweetId = response.getData().getId();
        assertEquals(tweet.getData().getText(), response.getData().getText());

        //delete the tweet
        response = twitterDao.deleteById(tweetId);
        assertTrue(response.getData().getDeleted());
    }

    //sadly this won't work in the free-access tier anymore
    //get tweet by ID is a paid API after 2023-03-29
//    @Test
//    public void findById() {
//        String text = "Tweeting";
//        String id = "1645442026509402116";
//        Tweet response = twitterDao.findById(id);
//        assertEquals(text, response.getData().getText());
//        assertEquals(id, response.getData().getId());
//    }
}
