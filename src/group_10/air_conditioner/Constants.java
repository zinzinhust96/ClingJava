package group_10.air_conditioner;

public class Constants {
    // device
    public static final String DEVICE_NAME = "AirConditioner";
    public static final String MANUFACTURER_DETAILS = "6969";
    public static final String MODEL_DETAILS = "AC2018";
    public static final String MODEL_DESCRIPTION = "Simple Air Conditioner";
    public static final String MODEL_NUMBER = "v1";

    // switch power service
    public static final String SWITCH_POWER = "SwitchPower";
    public static final String STATUS = "Status";
    public static final String SET_TARGET = "SetTarget";
    public static final String GET_TARGET = "GetTarget";
    public static final String NEW_TARGET_VALUE = "NewTargetValue";
    public static final String RET_TARGET_VALUE = "RetTargetValue";
    public static final String RESULT_STATUS = "ResultStatus";

    // temperature control service
    public static final String TEMPERATURE_CONTROL = "TemperatureControl";
    public static final String TEMP = "Temperature";
    public static final String GET_TEMPERATURE = "GetTemperature";
    public static final String SET_TEMPERATURE = "SetTemperature";
    public static final String INCREASE_TEMPERATURE = "IncreaseTemperature";
    public static final String DECREASE_TEMPERATURE = "DecreaseTemperature";
    public static final int TEMPERATURE_MAX = 30;
    public static final int TEMPERATURE_MIN = 16;

    // input argument
    public static final String IN = "In";
    public static final String OUT = "Out";

    // min-max values
    public static final int TEMPERATURE_DEFAULT = 25;
    public static final boolean POWER_STATUS_DEFAULT = false;

    // resources
    public static final String AIR_CONDITIONER_IMAGE = "/resources/air_conditioner.png";
}
