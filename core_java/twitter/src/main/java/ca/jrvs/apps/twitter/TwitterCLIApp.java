package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterCLIApp {
    public static final String USAGE = "USAGE: TwitterCLIApp post|delete [options]";

    private Controller controller;

    public TwitterCLIApp(Controller controller) {
        this.controller = controller;
    }

    /**
     * Calls different functions in TwitterController depending on the first argument.
     * @param args arguments from user input
     */
    public void run(String[] args) {
        if(args.length == 0){
            throw new IllegalArgumentException(USAGE);
        }
        switch (args[0].toLowerCase()){
            case "post":
                printTweet(controller.postTweet(args));
                break;
            case "delete":
                controller.deleteTweet(args).forEach(this::printTweet);
                break;
            default:
                throw new IllegalArgumentException(USAGE);
        }
    }

    /**
     * Prints tweet body in prettyJson format in the terminal
     * @param tweet
     */
    private void printTweet(Tweet tweet){
        try {
            System.out.println(JsonUtil.toJson(tweet, true, false));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to convert tweet object to string", e);
        }
    }

    //Comment out because of switching to Spring framework.
//        public static void main(String[] args) {
//        String CONSUMER_KEY = System.getenv("API_KEY");
//        String CONSUMER_SECRET = System.getenv("API_KEY_SECRET");
//        String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");
//        String TOKEN_SECRET = System.getenv("ACCESS_TOKEN_SECRET");
//
//        HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
//        CrdDao dao = new TwitterDao(httpHelper);
//        Service service = new TwitterService(dao);
//        Controller controller = new TwitterController(service);
//        TwitterCLIApp app = new TwitterCLIApp(controller);
//
//        app.run(args);
//    }
}
