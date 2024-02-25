package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Hello View
        URL resourceUrl = getClass().getResource("/com/example/demo/hello-view.fxml");
        if (resourceUrl == null) {
            System.err.println("Resource not found!");
            return;
        }
        Parent helloRoot = FXMLLoader.load(resourceUrl);

        // Create a scene with the Hello View
        Scene scene = new Scene(helloRoot, 800, 600);

        // Set up the button action to open the popup window
        // Button button = (Button) helloRoot.lookup("#statButton");
        // button.setOnAction(event -> openPopupWindow());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello View");
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
