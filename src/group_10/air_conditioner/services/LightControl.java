package group_10.air_conditioner.services;

import group_10.air_conditioner.Constants;
import org.fourthline.cling.binding.annotations.*;

import java.beans.PropertyChangeSupport;

@UpnpService(
        serviceId = @UpnpServiceId(Constants.LIGHT_CONTROL),
        serviceType = @UpnpServiceType(value = Constants.LIGHT_CONTROL, version = 1)
)

public class LightControl {
    private final PropertyChangeSupport propertyChangeSupport;

    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private boolean target = false;

    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;

    @UpnpStateVariable(
            defaultValue = "5",
            allowedValueMinimum = Constants.INTENSITY_MIN,
            allowedValueMaximum = Constants.INTENSITY_MAX
    )
    private int intensity;

    public LightControl()  {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
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
    public int getIntensity() {
        return intensity;
    }

    @UpnpAction
    public void setIntensity(@UpnpInputArgument(name = Constants.IN) int intensity) {
        if (intensity >= Constants.INTENSITY_MIN && intensity <= Constants.INTENSITY_MAX) {
            this.intensity = intensity;
            getPropertyChangeSupport().firePropertyChange(Constants.INTENSITY, null, null);
        }
    }

    @UpnpAction
    public void increaseIntensity() {
        if (intensity + 1 <= Constants.INTENSITY_MAX) {
            intensity += 1;
            getPropertyChangeSupport().firePropertyChange(Constants.INTENSITY, null, null);
        }
    }

    @UpnpAction
    public void decreaseIntensity() {
        if (intensity - 1 >= Constants.INTENSITY_MIN) {
            intensity -= 1;
            getPropertyChangeSupport().firePropertyChange(Constants.INTENSITY, null, null);
        }
    }
}
