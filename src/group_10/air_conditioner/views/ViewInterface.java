package group_10.air_conditioner.views;

public interface ViewInterface {
    void onTemperatureChange(int newValue);
    void onPowerStatusChange(boolean status);
}