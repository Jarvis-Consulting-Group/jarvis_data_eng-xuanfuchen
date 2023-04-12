package ca.jrvs.apps.twitter.model;

public class Tweet {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "data=" + data +
                '}';
    }
}
