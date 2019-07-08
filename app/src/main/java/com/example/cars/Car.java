package com.example.cars;

public class Car {
    private int id;
    private String plateNumber;
    private Location location;
    private Model model;
    private int batteryPercentage;
    private double batteryEstimatedDistance;
    private boolean isCharging;

    public int getId() {
        return id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public Location getLocation() {
        return location;
    }

    public Model getModel() {
        return model;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public double getBatteryEstimatedDistance() {
        return batteryEstimatedDistance;
    }

    public boolean isCharging() {
        return isCharging;
    }
}