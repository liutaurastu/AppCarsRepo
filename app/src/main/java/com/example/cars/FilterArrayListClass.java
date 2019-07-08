package com.example.cars;

import static java.lang.Math.sqrt;

public class FilterArrayListClass {
    String urls;
    String titles;
    String numberPlates;
    int battPercentages;
    double latit;
    double longit;

    public FilterArrayListClass(String urls, String titles, String numberPlates, int battPercentages, double latit, double longit)
    {
        this.urls = urls;
        this.titles = titles;
        this.numberPlates = numberPlates;
        this.battPercentages = battPercentages;
        this.latit = latit;
        this.longit = longit;
    }

    public String getUrls() {
        return urls;
    }

    public String getTitles() {
        return titles;
    }

    public String getNumberPlates() {
        return numberPlates;
    }

    public int getBattPercentages() {
        return battPercentages;
    }

    public double getDistance(double lat, double lon)
    {
        double distance;
        distance = sqrt((latit-lat)*(latit-lat)+(longit-lon)*(longit-lon));
        return distance;
    }
}
