package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class DialogController implements Initializable {

    private ImageView robotImageView;
    private int currentRow;
    private int currentCol;
    private HelloController helloController;

    @FXML
    private Label myLabel;

    @FXML
    private ChoiceBox<Integer> myChoiceBox;

    @FXML
    private ChoiceBox<Integer> myChoiceBox1;

    @FXML
    private Button submitCords;
    GridPane mainGridDialog;

    int counter;

//    @FXML private GridPane maingrid;


    // Default constructor
    public DialogController() {
        // Initialize currentRow and currentCol with default values
        currentRow = 0;
        currentCol = 0;
    }

//    public DialogController(ImageView robotImageView) {
//        this.robotImageView = robotImageView;
//        // Initialize currentRow and currentCol with default values
//        currentRow = 0;
//        currentCol = 0;
//    }
//
//    public DialogController(ImageView robotImageView, HelloController helloController) {
//        this.robotImageView = robotImageView;
//        this.helloController = helloController;
//    }

    public DialogController(GridPane maingrid) {
        this.mainGridDialog = maingrid;
//        System.out.println("1");
        this.counter = 5;

    }

    // Method to show the dialog
    // Method to show the dialog
    public void showDialog() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(DialogController.class.getResource("popup-view.fxml"));
            Parent root = loader.load();

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Shop");
            dialogStage.setScene(new Scene(root));

            // Show the dialog and wait for it to be closed
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("2");
        myChoiceBox.getItems().addAll(1, 2, 3, 4, 5);
        myChoiceBox1.getItems().addAll(1, 2, 3, 4, 5);
//        myChoiceBox.setOnAction(this::getNumbers);
//        myChoiceBox1.setOnAction(this::getNumbers);
        // Initialize currentRow and currentCol with default values
        currentRow = 0;
        currentCol = 0;
        System.out.println(counter);
        this.counter = 10;

//        System.out.println("This is the main");
//        System.out.println(this.mainGridDialog);
    }

//    public void getNumbers(ActionEvent event) {
//        if (myChoiceBox.getValue() != null && myChoiceBox1.getValue() != null) {
//            int row = myChoiceBox.getValue();
//            int col = myChoiceBox1.getValue();
//            myLabel.setText("You want your robot to be at row " + row + " and column " + col);
//            moveRobotToRowAndCol(row, col);
//        }
//    }

    public void moveRobotToRowAndCol(int row, int col) {

        ImageView imageView = new ImageView();
        Image image = new Image("file:/C:/Users/NoorE/OneDrive/Documents/Mycode/java/first%20game/demo/src/main/assets/statRobot.png");
        imageView.setImage(image);
        GridPane.setColumnIndex(imageView, col);
        GridPane.setRowIndex(imageView, row);
//        System.out.println("Below is the girdpane");
//        System.out.println(this.mainGridDialog);
//        mainGridDialog.getChildren().add(imageView);
//        if (robotImageView != null) {
//            // Assuming the VBox is the parent of the robotImageView
//            VBox parentVBox = (VBox) robotImageView.getParent().getParent().getParent();
//            BorderPane borderPane = (BorderPane) parentVBox.getParent();
//            GridPane gridPane = (GridPane) borderPane.getCenter(); // Assuming GridPane is in the center
//
//            // Assuming the VBox containing the ImageView is at row 0 in the GridPane
//            GridPane.setColumnIndex(robotImageView, col);
//            GridPane.setRowIndex(robotImageView, row);
//
//            currentRow = row;
//            currentCol = col;
//        }
    }

    @FXML
    private void handleSubmitCordsButtonClick(ActionEvent event) {
        if (myChoiceBox.getValue() != null && myChoiceBox1.getValue() != null) {
            System.out.println(counter);
            int row = myChoiceBox.getValue();
            int col = myChoiceBox1.getValue();
            myLabel.setText("You want your robot to be at row " + row + " and column " + col);
            moveRobotToRowAndCol(row, col);
            Stage stage = (Stage) submitCords.getScene().getWindow();
            stage.close();
        }
    }

    public void setGridPaneRef(GridPane maingrid) {
        this.mainGridDialog = maingrid;
//        System.out.println(this.mainGridDialog);
//        System.out.println(mainGridDialog.getChildren());
//        System.out.println("It worked!");
    }
}
