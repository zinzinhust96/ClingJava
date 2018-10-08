package group_10.air_conditioner.controllers;

import group_10.air_conditioner.Constants;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;

public class ActionExecutor {

    private ControllerInterface controller;

    public ActionExecutor(ControllerInterface controller) {
        this.controller = controller;
    }

    synchronized public void setPowerStatus(UpnpService upnpService, Service service, boolean value) {
        ActionInvocation getTargetInvocation = new ActionInvocation(service.getAction(Constants.SET_TARGET));
        getTargetInvocation.setInput(Constants.NEW_TARGET_VALUE, value);
        upnpService.getControlPoint().execute(
                new ActionCallback(getTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action setPowerStatus");
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
    }

    synchronized public void setTemperature(UpnpService upnpService, Service service, int value) {
        ActionInvocation getTargetInvocation = new ActionInvocation(service.getAction(Constants.SET_TEMPERATURE));
        getTargetInvocation.setInput(Constants.IN, value);
        upnpService.getControlPoint().execute(
                new ActionCallback(getTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action setTemperature");
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
    }

    synchronized public void increaseTemperature(UpnpService upnpService, Service service) {
        ActionInvocation getTargetInvocation = new ActionInvocation(service.getAction(Constants.INCREASE_TEMPERATURE));
        upnpService.getControlPoint().execute(
                new ActionCallback(getTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action increaseTemperature");
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
    }

    synchronized public void decreaseTemperature(UpnpService upnpService, Service service) {
        ActionInvocation getTargetInvocation = new ActionInvocation(service.getAction(Constants.DECREASE_TEMPERATURE));
        upnpService.getControlPoint().execute(
                new ActionCallback(getTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action decreaseTemperature");
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
    }

    synchronized public void setIntensity(UpnpService upnpService, Service service, int value) {
        ActionInvocation getTargetInvocation = new ActionInvocation(service.getAction(Constants.SET_INTENSITY));
        getTargetInvocation.setInput(Constants.IN, value);
        upnpService.getControlPoint().execute(
                new ActionCallback(getTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action setIntensity");
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
    }

    synchronized public void increaseIntensity(UpnpService upnpService, Service service) {
        ActionInvocation getTargetInvocation = new ActionInvocation(service.getAction(Constants.INCREASE_INTENSITY));
        upnpService.getControlPoint().execute(
                new ActionCallback(getTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action IncreaseIntensity");
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
    }

    synchronized public void decreaseIntensity(UpnpService upnpService, Service service) {
        ActionInvocation getTargetInvocation = new ActionInvocation(service.getAction(Constants.DECREASE_INTENSITY));
        upnpService.getControlPoint().execute(
                new ActionCallback(getTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Successfully called action DecreaseIntensity");
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
    }


}
