package group_10.air_conditioner.controllers;

public interface ControllerInterface {
    boolean setPowerStatus(boolean status);
    boolean setTemperature(int svalue);
    boolean increaseTemperature();
    boolean decreaseTemperature();
}
