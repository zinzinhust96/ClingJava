package group_10.simple_vibrator;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.*;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimpleVibratorClient implements Runnable {

    private static final int VIBRATING_PERIOD = 10;
    private static final int INITIAL_DELAY = 100;

    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread clientThread = new Thread(new SimpleVibratorClient());
        clientThread.setDaemon(false);
        clientThread.start();

    }

    public void run() {
        try {

            UpnpService upnpService = new UpnpServiceImpl();

            // Add a listener for device registration events
            upnpService.getRegistry().addListener(createRegistryListener(upnpService));

            // Broadcast a search message for all devices
            upnpService.getControlPoint().search(new STAllHeader());

        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            System.exit(1);
        }
    }

    RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {

            ServiceId serviceId = new UDAServiceId("SwitchPower");

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

                Service switchPower;
                if ((switchPower = device.findService(serviceId)) != null) {
                    System.out.println("Service discovered: " + switchPower);
                    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            executeAction(upnpService, switchPower);
                        }
                    }, INITIAL_DELAY, VIBRATING_PERIOD, TimeUnit.SECONDS);
                    upnpService.getControlPoint().execute(createSubscriptionCallBack(upnpService, switchPower));
                }
            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                Service switchPower;
                if ((switchPower = device.findService(serviceId)) != null) {
                    System.out.println("Service disappeared: " + switchPower);
                }
            }

            @Override
            public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {

            }


        };
    }

    private SubscriptionCallback createSubscriptionCallBack(UpnpService upnpService, Service service) {
        return new SubscriptionCallback(service, 600) {
            @Override
            protected void failed(GENASubscription genaSubscription, UpnpResponse upnpResponse, Exception e, String s) {

            }

            @Override
            protected void established(GENASubscription genaSubscription) {

            }

            @Override
            protected void ended(GENASubscription genaSubscription, CancelReason cancelReason, UpnpResponse upnpResponse) {

            }

            @Override
            public void eventReceived(GENASubscription sub) {

                System.out.println("Event: " + sub.getCurrentSequence().getValue());

                Map<String, StateVariableValue> values = sub.getCurrentValues();
                StateVariableValue status = values.get("Status");

                System.out.println("Status is: " + status.toString());

            }

            @Override
            public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                System.out.println("Missed events: " + numberOfMissedEvents);
            }
        };
    }

    void executeAction(UpnpService upnpService, Service switchPowerService) {

        ActionInvocation getTargetInvocation = new GetTargetActionInvocation(switchPowerService);

        // Executes asynchronous in the background
        upnpService.getControlPoint().execute(
                new ActionCallback(getTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called get action!");
                        ActionInvocation setTargetInvocation = new SetTargetActionInvocation(switchPowerService, !((boolean) invocation.getOutput()[0].getValue()));
                        upnpService.getControlPoint().execute(
                                new ActionCallback(setTargetInvocation) {

                                    @Override
                                    public void success(ActionInvocation actionInvocation) {

                                        assert invocation.getOutput().length == 0;
                                        System.out.println("Successfully called set action!");
                                    }

                                    @Override
                                    public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {
                                        System.err.println("Error when calling set");
                                    }
                                }
                        );
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
//        new ActionCallback.Default(getTargetInvocation, upnpService.getControlPoint()).run();

    }

    void executeGetAction(UpnpService upnpService, Service switchPowerService) {

        ActionInvocation getTargetInvocation = new GetTargetActionInvocation(switchPowerService);

        // Executes asynchronous in the background
        upnpService.getControlPoint().execute(
                new ActionCallback(getTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Current vibrating status: " + (boolean) invocation.getOutput()[0].getValue());
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );

    }

    class SetTargetActionInvocation extends ActionInvocation {

        SetTargetActionInvocation(Service service, boolean value) {
            super(service.getAction("SetTarget"));
            try {

                // Throws InvalidValueException if the value is of wrong type
                setInput("NewTargetValue", value);

            } catch (InvalidValueException ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
    }

    class GetTargetActionInvocation extends ActionInvocation {

        GetTargetActionInvocation(Service service) {
            super(service.getAction("GetTarget"));
            try {

                // Throws InvalidValueException if the value is of wrong type
                getOutput("RetTargetValue");

            } catch (InvalidValueException ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
    }
}
