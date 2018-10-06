package group_10.air_conditioner;

import group_10.air_conditioner.controllers.Controller;
import group_10.air_conditioner.controllers.ControllerInterface;
import group_10.air_conditioner.views.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MyApplication extends Application{
    View view;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/view.fxml"));
        Parent root = fxmlLoader.load();

        // controller instance to init model attribute
        view = fxmlLoader.<View>getController();
        ControllerInterface controller = new Controller(view);
        view.setController(controller);

        primaryStage.setTitle("Air Conditioner");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}