package group_10.air_conditioner.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DrawButton {

    public void draw (ImageView imageView, String img_name, double width, double height){
        Image image = null;
        try {
            image = new Image(getClass().getResource("/resources/images/" + img_name).toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setSmooth(true);
    }
}