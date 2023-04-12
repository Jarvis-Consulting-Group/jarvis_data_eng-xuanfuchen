package ca.jrvs.apps.twitter.model;

import java.util.List;

public class Includes {
    private List<Place> places;

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Includes{" +
                "places=" + places +
                '}';
    }
}
