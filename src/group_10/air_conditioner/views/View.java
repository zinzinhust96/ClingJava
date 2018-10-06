package group_10.air_conditioner.views;

import group_10.air_conditioner.Constants;
import group_10.air_conditioner.controllers.ControllerInterface;
import group_10.air_conditioner.models.AirConditioner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class View implements Initializable, ViewInterface {
    private static AirConditioner airConditioner;
    private static Boolean isDeviceOn = true;
    private ControllerInterface controller;


    @FXML
    ImageView powerBtn;
    @FXML
    ImageView upBtn;
    @FXML
    ImageView downBtn;
    @FXML
    Label tempLb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DrawButton drawButton = new DrawButton();
        drawButton.draw(powerBtn, "power.png", 80, 80);
        drawButton.draw(upBtn, "up_arrow.png", 80, 80);
        drawButton.draw(downBtn, "down_arrow.png", 80, 80);
    }

    @FXML
    public void poweroff(MouseEvent event) {
//        Platform.exit();
        DrawButton drawButton = new DrawButton();
        if (isDeviceOn) {
            drawButton.draw(powerBtn, "power_off.png", 80, 80);
            controller.setPowerStatus(false);
            isDeviceOn = false;
        } else {
            drawButton.draw(powerBtn, "power.png", 80, 80);
            isDeviceOn = true;
            controller.setPowerStatus(true);
        }
    }

    @FXML
    public void upTemperature(MouseEvent event) {
        int temp = airConditioner.getTemperture();
        if (temp < Constants.TEMPERATURE_MAX) {
            temp += 1;
            System.out.println("Current temperature: " + temp);
            airConditioner.setTemperture(temp);
            controller.increaseTemperature();
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
            System.out.println("Current temperature: " + temp);
            airConditioner.setTemperture(temp);
            controller.decreaseTemperature();
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

    @Override
    public void onPowerStatusChange(boolean status) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DrawButton drawButton = new DrawButton();
                if (status) {
                    drawButton.draw(powerBtn, "power.png", 80, 80);
                    isDeviceOn = true;
                } else {
                    drawButton.draw(powerBtn, "power_off.png", 80, 80);
                    isDeviceOn = false;
                }
            }
        });
    }

    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }


}
