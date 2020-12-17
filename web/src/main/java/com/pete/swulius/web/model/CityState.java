package com.pete.swulius.web.model;

public class CityState {

    public String city;
    public String state;
    public Integer population;
    public Integer density;
    public String timezone;

    public CityState() {

    }

    public CityState( String aCity, String aState )  {
        city = aCity;
        state = aState;
    }
}
