package group_10.air_conditioner.views;

public interface ViewInterface {
    void onTemperatureChange(int newValue);
    void onIntensityChange(int newValue);
    void onPowerStatusChange(String deviceType, boolean status);
}