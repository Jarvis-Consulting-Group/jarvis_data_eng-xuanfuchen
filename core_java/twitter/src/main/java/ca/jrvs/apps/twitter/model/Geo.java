package ca.jrvs.apps.twitter.model;


import java.util.Arrays;

public class Geo {
    private String type;
    double[] bbox;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getBbox() {
        return bbox;
    }

    public void setBbox(double[] bbox) {
        this.bbox = bbox;
    }

    @Override
    public String toString() {
        return "Geo{" +
                "type='" + type + '\'' +
                ", bbox=" + Arrays.toString(bbox) +
                '}';
    }
}
