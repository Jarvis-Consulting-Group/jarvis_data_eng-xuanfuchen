package ca.jrvs.apps.twitter.dao.helper;

import ch.qos.logback.core.encoder.EchoEncoder;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;

public class TwitterHttpHelper implements HttpHelper{
    private OAuthConsumer consumer;
    private HttpClient httpClient;

    public TwitterHttpHelper(String consumerKey, String consumerSecret,
                             String accessToken, String tokenSecret) {
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, tokenSecret);
        httpClient = HttpClientBuilder.create().build();
    }

    @Override
    public HttpResponse httpPost(URI uri, String s) {
        try{
            HttpPost httpPost = new HttpPost(uri);
            StringEntity stringEntity = new StringEntity(s);
            //must have a content-type of json in Twitter V2 POST API
            httpPost.setHeader("content-type", "application/json");
            httpPost.setEntity(stringEntity);

            return execute(httpPost);
        } catch (OAuthException | IOException e) {
            throw new RuntimeException("Exception occurs when executing httpPost", e);
        }
    }

    /**
     *
     * @param uri
     * @return
     */
    @Override
    public HttpResponse httpGet(URI uri) {
        try {
            HttpGet httpGet = new HttpGet(uri);
            return execute(httpGet);
        } catch (OAuthException | IOException e){
            throw new RuntimeException("Exception occurs when executing httpGet", e);
        }
    }

    @Override
    public HttpResponse httpDelete(URI uri) {
        try {
            HttpDelete httpDelete = new HttpDelete(uri);
            return execute(httpDelete);
        } catch (OAuthException | IOException e) {
            throw new RuntimeException("Exception occurs when executing httpDelete", e);
        }
    }

    /**
     * Sign the request with OAuth and execute it using HttpClient
     * @param request HttpUriRequest
     * @return HttpResponse form executing the request
     * @throws OAuthException when unable to sign OAuth
     * @throws IOException
     */
    private HttpResponse execute(HttpUriRequest request) throws OAuthException, IOException{
        consumer.sign(request);
        return httpClient.execute(request);
    }
}
