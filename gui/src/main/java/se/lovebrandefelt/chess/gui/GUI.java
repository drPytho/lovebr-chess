package se.lovebrandefelt.chess.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/GUI.fxml"));
    Scene scene = new Scene(root, 600, 400);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Chess");
    primaryStage.show();
  }
}