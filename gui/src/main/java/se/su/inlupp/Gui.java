package se.su.inlupp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gui extends Application {

  public void start(Stage stage) {
    //Graph<String> graph = new ListGraph<String>();
    TravelPlannerModel model = new TravelPlannerModel();
    TravelPlannerView view = new TravelPlannerView(model,stage);
    view.getChildren();
    Scene scene = new Scene(view, 640, 480);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
