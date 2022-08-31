package com.example.restwebservice;

public class Country {
    String capital;
    String countryName;

    public Country(String cap, String countryName) {
        super();
        this.capital = cap;
        this.countryName = countryName;
    }
    public String getCapital() {
        return capital;
    }
    public void setId(String cap) {
        this.capital = cap;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
