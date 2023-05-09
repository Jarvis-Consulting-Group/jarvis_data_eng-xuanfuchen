package ca.jrvs.apps.twitter.dao.helper;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class TwitterHttpHelperTest {
    private static final Logger logger = LoggerFactory.getLogger(TwitterHttpHelperTest.class);
    private static String CONSUMER_KEY = System.getenv("API_KEY");
    private static String CONSUMER_SECRET = System.getenv("API_KEY_SECRET");
    private static String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");
    private static String TOKEN_SECRET = System.getenv("ACCESS_TOKEN_SECRET");

    @Test
    public void httpPost() throws Exception{
        HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
                ACCESS_TOKEN, TOKEN_SECRET);

        URI uri = new URI("https://api.twitter.com/2/tweets");
        String Json = "{" +
                "\"text\": \"Junit test\"" +
                "}";
        HttpResponse response = httpHelper.httpPost(uri, Json);
        logger.info(EntityUtils.toString(response.getEntity()));
    }
}
