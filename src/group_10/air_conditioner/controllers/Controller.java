package group_10.air_conditioner.controllers;

import group_10.air_conditioner.Constants;
import group_10.air_conditioner.models.DeviceGenerator;
import group_10.air_conditioner.services.LightControl;
import group_10.air_conditioner.services.TemperatureControl;
import group_10.air_conditioner.views.ViewInterface;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.UDADeviceTypeHeader;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDAServiceId;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller implements ControllerInterface {

    private ViewInterface view;
    private Device ac;
    private Device light;
    private UpnpService upnpService;
    private ActionExecutor actionExecutor;
    private RegistryListener registryListener = new DefaultRegistryListener() {

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice remoteDevice) {
            System.out.println("Remote device detected.");
            if (remoteDevice.getDetails().getModelDetails().getModelName().equals(Constants.AC_MODEL_DETAILS)) {
                System.out.println("Air conditioner detected.");
                ac = remoteDevice;
                upnpService.getControlPoint().execute(createTemperatureControlSubscriptionCallBack(getServiceById(ac, Constants.TEMPERATURE_CONTROL)));
            }
            if (remoteDevice.getDetails().getModelDetails().getModelName().equals(Constants.LIGHT_MODEL_DETAILS)) {
                System.out.println("light detected.");
                light = remoteDevice;
                upnpService.getControlPoint().execute(createLightControlSubscriptionCallBack(getServiceById(light, Constants.LIGHT_CONTROL)));
            }
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice remoteDevice) {
            if (remoteDevice.getDetails().getModelDetails().getModelName().equals(Constants.AC_MODEL_DETAILS)) {
                System.out.println("Air conditioner removed.");
                ac = null;
            }
            if (remoteDevice.getDetails().getModelDetails().getModelName().equals(Constants.LIGHT_MODEL_DETAILS)) {
                System.out.println("Light removed.");
                light = null;
            }
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice localDevice) {
            System.out.println("Local device detected.");
            if (localDevice.getDetails().getModelDetails().getModelName().equals(Constants.AC_MODEL_DETAILS)) {
                System.out.println("Air conditioner detected.");
                ac = localDevice;
                upnpService.getControlPoint().execute(createTemperatureControlSubscriptionCallBack(getServiceById(ac, Constants.TEMPERATURE_CONTROL)));
                Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
                    @Override
                    public void run() {
                        setPowerStatus(Constants.AC_DEVICE_TYPE, Constants.POWER_STATUS_DEFAULT, Constants.TEMPERATURE_CONTROL);
                        setTemperature(Constants.TEMPERATURE_DEFAULT);
                    }
                }, 500, TimeUnit.MILLISECONDS);
            }
            if (localDevice.getDetails().getModelDetails().getModelName().equals(Constants.LIGHT_MODEL_DETAILS)) {
                System.out.println("light detected.");
                light = localDevice;
                upnpService.getControlPoint().execute(createLightControlSubscriptionCallBack(getServiceById(light, Constants.LIGHT_CONTROL)));
                Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
                    @Override
                    public void run() {
                        setPowerStatus(Constants.LIGHT_DEVICE_TYPE, Constants.POWER_STATUS_DEFAULT, Constants.TEMPERATURE_CONTROL);
                        setIntensity(Constants.INTENSITY_DEFAULT);
                    }
                }, 500, TimeUnit.MILLISECONDS);
            }
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice localDevice) {
            if (localDevice.getDetails().getModelDetails().getModelName().equals(Constants.AC_MODEL_DETAILS)) {
                System.out.println("ac device removed.");
                ac = null;
            }
            if (localDevice.getDetails().getModelDetails().getModelName().equals(Constants.LIGHT_MODEL_DETAILS)) {
                System.out.println("Light removed.");
                light = null;
            }
        }
    };

    public Controller(ViewInterface view) {
        this.view = view;
        init();
    }

    public void init() {
        actionExecutor = new ActionExecutor(this);
        upnpService = new UpnpServiceImpl();
        upnpService.getRegistry().addListener(registryListener);
        try {
            DeviceGenerator acDevice = new DeviceGenerator(
                    Constants.AC_DEVICE_ID,
                    Constants.AC_DEVICE_TYPE,
                    Constants.AC_MODEL_DETAILS,
                    Constants.AC_DEVICE_VERSION,
                    Constants.AC_DEVICE_NAME,
                    Constants.AC_MANUFACTURER_DETAILS,
                    Constants.AC_MODEL_DESCRIPTION,
                    Constants.AC_MODEL_NUMBER,
                    Constants.AIR_CONDITIONER_IMAGE,
                    TemperatureControl.class
            );

            DeviceGenerator lightDevice = new DeviceGenerator(
                    Constants.LIGHT_DEVICE_ID,
                    Constants.LIGHT_DEVICE_TYPE,
                    Constants.LIGHT_MODEL_DETAILS,
                    Constants.LIGHT_DEVICE_VERSION,
                    Constants.LIGHT_DEVICE_NAME,
                    Constants.LIGHT_MANUFACTURER_DETAILS,
                    Constants.LIGHT_MODEL_DESCRIPTION,
                    Constants.LIGHT_MODEL_NUMBER,
                    Constants.LIGHT_IMAGE,
                    LightControl.class
            );

            acDevice.initializeDevice();
            lightDevice.initializeDevice();
            upnpService.getRegistry().addDevice(acDevice.getDevice());
            upnpService.getRegistry().addDevice(lightDevice.getDevice());
        } catch (Exception e) {
            e.printStackTrace();
        }
        UDADeviceTypeHeader acHeader = new UDADeviceTypeHeader(new UDADeviceType(Constants.AC_DEVICE_ID));
        UDADeviceTypeHeader lightHeader = new UDADeviceTypeHeader(new UDADeviceType(Constants.LIGHT_DEVICE_ID));
        upnpService.getControlPoint().search(acHeader);
        upnpService.getControlPoint().search(lightHeader);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                upnpService.shutdown();
            }
        });
    }

    private SubscriptionCallback createTemperatureControlSubscriptionCallBack(Service service) {
        return new SubscriptionCallback(service, Integer.MAX_VALUE) {
            @Override
            protected void failed(GENASubscription genaSubscription, UpnpResponse upnpResponse, Exception e, String s) {

            }

            @Override
            protected void established(GENASubscription genaSubscription) {
                System.out.println("Temperature control subscription created.");
                setTemperature(Constants.TEMPERATURE_DEFAULT);
            }

            @Override
            protected void ended(GENASubscription genaSubscription, CancelReason cancelReason, UpnpResponse upnpResponse) {

            }

            @Override
            public void eventReceived(GENASubscription sub) {
                System.out.println("Event: " + sub.getCurrentSequence().getValue());
                Map<String, StateVariableValue> values = sub.getCurrentValues();
                for (String key : values.keySet()) {
                    System.out.println(key + " changed.");
                }
                if (values.containsKey(Constants.TEMP)) {
                    int value = (int) values.get(Constants.TEMP).getValue();
                    view.onTemperatureChange(value);
                    System.out.println("New value: " + value);
                }
                if (values.containsKey(Constants.STATUS)) {
                    boolean value = (boolean) values.get(Constants.STATUS).getValue();
                    view.onPowerStatusChange(Constants.AC_DEVICE_TYPE, value);
                    System.out.println("New value: " + value);
                }
            }

            @Override
            public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                System.out.println("Missed events: " + numberOfMissedEvents);
            }
        };
    }

    private SubscriptionCallback createLightControlSubscriptionCallBack(Service service) {
        return new SubscriptionCallback(service, Integer.MAX_VALUE) {
            @Override
            protected void failed(GENASubscription genaSubscription, UpnpResponse upnpResponse, Exception e, String s) {

            }

            @Override
            protected void established(GENASubscription genaSubscription) {
                System.out.println("Light control subscription created.");
                setIntensity(Constants.INTENSITY_DEFAULT);
            }

            @Override
            protected void ended(GENASubscription genaSubscription, CancelReason cancelReason, UpnpResponse upnpResponse) {

            }

            @Override
            public void eventReceived(GENASubscription sub) {
                System.out.println("Event: " + sub.getCurrentSequence().getValue());
                Map<String, StateVariableValue> values = sub.getCurrentValues();
                for (String key : values.keySet()) {
                    System.out.println(key + " changed.");
                }
                if (values.containsKey(Constants.INTENSITY)) {
                    int value = (int) values.get(Constants.INTENSITY).getValue();
                    view.onIntensityChange(value);
                    System.out.println("New value: " + value);
                }
                if (values.containsKey(Constants.STATUS)) {
                    boolean value = (boolean) values.get(Constants.STATUS).getValue();
                    view.onPowerStatusChange(Constants.LIGHT_DEVICE_TYPE, value);
                    System.out.println("New value: " + value);
                }
            }

            @Override
            public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                System.out.println("Missed events: " + numberOfMissedEvents);
            }
        };
    }

    @Override
    public boolean setPowerStatus(String type, boolean status, String serviceId) {
        if (type.equals(Constants.AC_DEVICE_TYPE)) {
            Service service = getServiceById(ac, serviceId);
            if (service != null) {
                actionExecutor.setPowerStatus(upnpService, service, status);
            }
        } else {
            Service service = getServiceById(light, serviceId);
            if (service != null) {
                actionExecutor.setPowerStatus(upnpService, service, status);
            }
        }
        return true;
    }

    @Override
    public boolean setTemperature(int value) {
        Service service = getServiceById(ac, Constants.TEMPERATURE_CONTROL);
        if (service != null) {
            actionExecutor.setTemperature(upnpService, service, value);
        }
        return true;
    }

    @Override
    public boolean increaseTemperature() {
        Service service = getServiceById(ac, Constants.TEMPERATURE_CONTROL);
        if (service != null) {
            actionExecutor.increaseTemperature(upnpService, service);
        }
        return true;
    }

    @Override
    public boolean decreaseTemperature() {
        Service service = getServiceById(ac, Constants.TEMPERATURE_CONTROL);
        if (service != null) {
            actionExecutor.decreaseTemperature(upnpService, service);
        }
        return true;
    }

    @Override
    public boolean setIntensity(int value) {
        Service service = getServiceById(light, Constants.LIGHT_CONTROL);
        if (service != null) {
            actionExecutor.setIntensity(upnpService, service, value);
        }
        return true;
    }

    @Override
    public boolean increaseIntensity() {
        Service service = getServiceById(light, Constants.LIGHT_CONTROL);
        if (service != null) {
            actionExecutor.increaseIntensity(upnpService, service);
        }
        return true;
    }

    @Override
    public boolean decreaseIntensity() {
        Service service = getServiceById(light, Constants.LIGHT_CONTROL);
        if (service != null) {
            actionExecutor.decreaseIntensity(upnpService, service);
        }
        return true;
    }

    private Service getServiceById(Device device, String serviceId) {
        if (device == null) {
            return null;
        }
        return device.findService(new UDAServiceId(serviceId));
    }

}
