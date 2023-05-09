package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {
    private Data data;
    private Includes includes;

    public Tweet() {
        this.data = new Data();
        this.includes = new Includes();
    }

    public Tweet(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Includes getIncludes() {
        return includes;
    }

    public void setIncludes(Includes includes) {
        this.includes = includes;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "data=" + data +
                '}';
    }
}
