package group_10.air_conditioner.views;

import group_10.air_conditioner.Constants;
import group_10.air_conditioner.controllers.ControllerInterface;
import group_10.air_conditioner.models.AirConditioner;
import group_10.air_conditioner.models.Light;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class View implements Initializable, ViewInterface {
    private static AirConditioner airConditioner = new AirConditioner();
    private static Light light = new Light();
    private static Boolean isAcDeviceOn = true;
    private static Boolean isLightDeviceOn = true;
    private ControllerInterface controller;


    @FXML
    ImageView acPowerBtn;
    @FXML
    ImageView acUpBtn;
    @FXML
    ImageView acDownBtn;
    @FXML
    Label tempLb;
    @FXML
    ImageView lightImage;
    @FXML
    ImageView lightPowerBtn;
    @FXML
    ImageView lightUpBtn;
    @FXML
    ImageView lightDownBtn;
    @FXML
    Label intensityLb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DrawButton drawButton = new DrawButton();
        drawButton.draw(acPowerBtn, "power.png", 80, 80);
        drawButton.draw(acUpBtn, "up_arrow.png", 80, 80);
        drawButton.draw(acDownBtn, "down_arrow.png", 80, 80);
        drawButton.draw(lightPowerBtn, "power.png", 80, 80);
        drawButton.draw(lightUpBtn, "up_arrow.png", 80, 80);
        drawButton.draw(lightDownBtn, "down_arrow.png", 80, 80);
        drawButton.draw(lightImage, "brightness.png", 80, 80);
    }

    @FXML
    public void acPowerButton(MouseEvent event) {
//        Platform.exit();
        DrawButton drawButton = new DrawButton();
        if (isAcDeviceOn) {
            drawButton.draw(acPowerBtn, "power_off.png", 80, 80);
            controller.setPowerStatus(Constants.AC_DEVICE_TYPE, false, Constants.TEMPERATURE_CONTROL);
            isAcDeviceOn = false;
        } else {
            drawButton.draw(acPowerBtn, "power.png", 80, 80);
            isAcDeviceOn = true;
            controller.setPowerStatus(Constants.AC_DEVICE_TYPE,true, Constants.TEMPERATURE_CONTROL);
            int temp = airConditioner.getTemperture();
            controller.setTemperature(temp);
            System.out.println("Current temperature: " + temp);
        }
    }

    @FXML
    public void upTemperature(MouseEvent event) {
        int temp = airConditioner.getTemperture();
        if (temp < Constants.TEMPERATURE_MAX) {
            temp += 1;
            if (isAcDeviceOn) {
                System.out.println("Current temperature: " + temp);
                controller.setTemperature(temp);
            }
            airConditioner.setTemperture(temp);
        } else {
            System.out.println("At maximum value");
        }
        tempLb.setText(String.format("%d", temp) + " °C");
    }

    @FXML
    public void downTemperature(MouseEvent event) {
        int temp = airConditioner.getTemperture();
        if (temp > Constants.TEMPERATURE_MIN) {
            temp -= 1;
            if (isAcDeviceOn) {
                System.out.println("Current temperature: " + temp);
                controller.setTemperature(temp);
            }
            airConditioner.setTemperture(temp);
        } else {
            System.out.println("At minimum value");
        }
        tempLb.setText(String.format("%d", temp) + " °C");
    }

    @Override
    public void onTemperatureChange(int newValue) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (newValue <= Constants.TEMPERATURE_MAX && newValue >= Constants.TEMPERATURE_MIN) {
                    airConditioner.setTemperture(newValue);
                    tempLb.setText(String.format("%d", airConditioner.getTemperture()) + " °C");
                }
            }
        });
    }

    @FXML
    public void lightPowerButton(MouseEvent event) {
//        Platform.exit();
        DrawButton drawButton = new DrawButton();
        if (isLightDeviceOn) {
            drawButton.draw(lightPowerBtn, "power_off.png", 80, 80);
            controller.setPowerStatus(Constants.LIGHT_DEVICE_TYPE, false, Constants.LIGHT_CONTROL);
            isLightDeviceOn = false;
        } else {
            drawButton.draw(lightPowerBtn, "power.png", 80, 80);
            isLightDeviceOn = true;
            controller.setPowerStatus(Constants.LIGHT_DEVICE_TYPE,true, Constants.LIGHT_CONTROL);
            int intensity = light.getIntensity();
            controller.setIntensity(intensity);
            System.out.println("Current intensity: " + intensity);
        }
    }

    @FXML
    public void upIntensity(MouseEvent event) {
        int intensity = light.getIntensity();
        if (intensity < Constants.INTENSITY_MAX) {
            intensity += 1;
            if (isLightDeviceOn) {
                System.out.println("Current intensity: " + intensity);
                controller.setIntensity(intensity);
            }
            light.setIntensity(intensity);
        } else {
            System.out.println("At maximum value");
        }
        intensityLb.setText(String.format("%d", intensity));
    }

    @FXML
    public void downIntensity(MouseEvent event) {
        int intensity = light.getIntensity();
        if (intensity > Constants.INTENSITY_MIN) {
            intensity -= 1;
            if (isLightDeviceOn) {
                System.out.println("Current intensity: " + intensity);
                controller.setIntensity(intensity);
            }
            light.setIntensity(intensity);
        } else {
            System.out.println("At minimum value");
        }
        intensityLb.setText(String.format("%d", intensity));
    }

    @Override
    public void onIntensityChange(int newValue) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (newValue <= Constants.INTENSITY_MAX && newValue >= Constants.INTENSITY_MIN) {
                    light.setIntensity(newValue);
                    intensityLb.setText(String.format("%d", light.getIntensity()));
                }
            }
        });
    }

    @Override
    public void onPowerStatusChange(String deviceType, boolean status) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DrawButton drawButton = new DrawButton();
                if (deviceType.equals(Constants.AC_DEVICE_TYPE)) {
                    if (status) {
                        drawButton.draw(acPowerBtn, "power.png", 80, 80);
                        isAcDeviceOn = true;
                    } else {
                        drawButton.draw(acPowerBtn, "power_off.png", 80, 80);
                        isAcDeviceOn = false;
                    }
                } else {
                    if (status) {
                        drawButton.draw(lightPowerBtn, "power.png", 80, 80);
                        isLightDeviceOn = true;
                    } else {
                        drawButton.draw(lightPowerBtn, "power_off.png", 80, 80);
                        isLightDeviceOn = false;
                    }
                }
            }
        });
    }

    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }


}
