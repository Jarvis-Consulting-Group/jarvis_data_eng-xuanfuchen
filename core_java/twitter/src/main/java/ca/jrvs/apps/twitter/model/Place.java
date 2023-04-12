package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Place {
    private Geo geo;
    private String name;
    private String country;
    @JsonProperty("full_name")
    private String fullName;

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Place{" +
                "geo=" + geo +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
