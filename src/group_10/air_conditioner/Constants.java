package group_10.air_conditioner;

public class Constants {
    // ac device
    public static final String AC_DEVICE_ID = "ac6969";
    public static final String AC_DEVICE_NAME = "AirConditioner";
    public static final int AC_DEVICE_VERSION = 1;
    public static final String AC_DEVICE_TYPE = "AC";
    public static final String AC_MANUFACTURER_DETAILS = "6969";
    public static final String AC_MODEL_DETAILS = "AC2018";
    public static final String AC_MODEL_DESCRIPTION = "Simple Air Conditioner";
    public static final String AC_MODEL_NUMBER = "v1";

    // light device
    public static final String LIGHT_DEVICE_ID = "light696969";
    public static final String LIGHT_DEVICE_NAME = "Light";
    public static final int LIGHT_DEVICE_VERSION = 1;
    public static final String LIGHT_DEVICE_TYPE = "LI";
    public static final String LIGHT_MANUFACTURER_DETAILS = "696969";
    public static final String LIGHT_MODEL_DETAILS = "LI2018";
    public static final String LIGHT_MODEL_DESCRIPTION = "Simple Light";
    public static final String LIGHT_MODEL_NUMBER = "v1";

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

    // light control service
    public static final String LIGHT_CONTROL = "LightControl";
    public static final String INTENSITY = "Intensity";
    public static final String GET_INTENSITY = "GetIntensity";
    public static final String SET_INTENSITY = "SetIntensity";
    public static final String INCREASE_INTENSITY = "IncreaseIntensity";
    public static final String DECREASE_INTENSITY = "DecreaseIntensity";
    public static final int INTENSITY_MAX = 10;
    public static final int INTENSITY_MIN = 0;
    public static final int INTENSITY_DEFAULT = 5;

    // input argument
    public static final String IN = "In";
    public static final String OUT = "Out";

    // min-max values
    public static final int TEMPERATURE_DEFAULT = 25;
    public static final boolean POWER_STATUS_DEFAULT = false;

    // resources
    public static final String AIR_CONDITIONER_IMAGE = "/resources/air_conditioner.png";
    public static final String LIGHT_IMAGE = "/resources/light.png";
}
