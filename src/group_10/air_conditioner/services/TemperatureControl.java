package group_10.air_conditioner.services;

import group_10.air_conditioner.Constants;
import org.fourthline.cling.binding.annotations.*;

import java.beans.PropertyChangeSupport;

@UpnpService(
        serviceId = @UpnpServiceId(Constants.TEMPERATURE_CONTROL),
        serviceType = @UpnpServiceType(value = Constants.TEMPERATURE_CONTROL, version = 1)
)

public class TemperatureControl {

    private final PropertyChangeSupport propertyChangeSupport;

    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private boolean target = false;

    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;

    @UpnpStateVariable(
            defaultValue = "30",
            allowedValueMinimum = Constants.TEMPERATURE_MIN,
            allowedValueMaximum = Constants.TEMPERATURE_MAX
    )
    private int temperature;

    public TemperatureControl() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpAction
    public void setTarget(@UpnpInputArgument(name = Constants.NEW_TARGET_VALUE) boolean newTargetValue) {

        boolean targetOldValue = target;
        target = newTargetValue;
        boolean statusOldValue = status;
        status = newTargetValue;
        getPropertyChangeSupport().firePropertyChange(Constants.STATUS, null, null);
    }

    @UpnpAction(out = @UpnpOutputArgument(name = Constants.RET_TARGET_VALUE))
    public boolean getTarget() {
        return target;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = Constants.RESULT_STATUS))
    public boolean getStatus() {
        return status;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = Constants.OUT))
    public int getTemperature() {
        return temperature;
    }

    @UpnpAction
    public void setTemperature(@UpnpInputArgument(name = Constants.IN) int temperature) {
        if (temperature >= Constants.TEMPERATURE_MIN && temperature <= Constants.TEMPERATURE_MAX) {
            this.temperature = temperature;
            getPropertyChangeSupport().firePropertyChange(Constants.TEMP, null, null);
        }
    }

    @UpnpAction
    public void increaseTemperature() {
        if (temperature + 1 <= Constants.TEMPERATURE_MAX) {
            temperature += 1;
            getPropertyChangeSupport().firePropertyChange(Constants.TEMP, null, null);
        }
    }

    @UpnpAction
    public void decreaseTemperature() {
        if (temperature - 1 >= Constants.TEMPERATURE_MIN) {
            temperature -= 1;
            getPropertyChangeSupport().firePropertyChange(Constants.TEMP, null, null);
        }
    }
}
