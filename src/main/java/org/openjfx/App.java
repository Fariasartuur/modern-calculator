package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlloader = new FXMLLoader(App.class.getResource("/org/openjfx/calculator.fxml"));

        Parent root = fxmlloader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Calculadora");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }
}