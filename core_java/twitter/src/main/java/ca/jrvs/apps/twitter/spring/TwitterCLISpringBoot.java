package ca.jrvs.apps.twitter.spring;

import ca.jrvs.apps.twitter.TwitterCLIApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterCLISpringBoot implements CommandLineRunner {
    @Autowired
    private TwitterCLIApp twitterCLIApp;

    private static String CONSUMER_KEY = System.getenv("API_KEY");
    private static String CONSUMER_SECRET = System.getenv("API_KEY_SECRET");
    private static String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");
    private static String TOKEN_SECRET = System.getenv("ACCESS_TOKEN_SECRET");

    public TwitterCLISpringBoot(TwitterCLIApp twitterCLIApp) {
        this.twitterCLIApp = twitterCLIApp;
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TwitterCLISpringBoot.class);

        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        twitterCLIApp.run(args);
    }
}
