package group_10.air_conditioner.controllers;

public interface ControllerInterface {
    boolean setPowerStatus(String type, boolean status, String serviceId);
    boolean setTemperature(int svalue);
    boolean setIntensity(int value);
    boolean increaseTemperature();
    boolean decreaseTemperature();
    boolean increaseIntensity();
    boolean decreaseIntensity();
}
