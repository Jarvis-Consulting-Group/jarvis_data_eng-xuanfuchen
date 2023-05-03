package ca.jrvs.apps.twitter.dao.helper;

import java.net.URI;
import org.apache.http.HttpResponse;

public interface HttpHelper {

    /**
     * Execute a HTTP Post call
     * @param uri the API URL
     * @param s additional Json file
     * @return HttpResponse for the request
     */
    HttpResponse httpPost(URI uri, String s);

    /**
     * Execute a HTTP Get call
     * @param uri
     * @return
     */
    HttpResponse httpGet(URI uri);

    /**
     * Execute a HTTP Delete call
     * @param uri
     * @return
     */
    HttpResponse httpDelete(URI uri);
}