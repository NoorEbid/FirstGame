// Import statements
package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

// Class definition
public class HelloController implements Initializable {

    // FXML annotations for injected elements
    @FXML private ImageView robotImageView;
    @FXML private Arc myArc;
    @FXML private GridPane maingrid;
    // FXML annotations for injected elements
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private ProgressBar health;
    @FXML private Label pointCounter;
    @FXML private Button statButton;




    // Initial position of the arc
    private int startpositionrow = 0;
    private int startpositioncol = 0;
    private int pointsCollected = 0;

    // Define the width and height of the map
    private final int MAP_WIDTH = 800;
    private final int MAP_HEIGHT = 600;
    private Random random = new Random();

    // Timeline for circle generation
    private Timeline circleGenerationTimeline;
    private List<Circle> generatedCircles = new ArrayList<>();

    // Flags for controlling generation behavior
    private boolean generationPaused = false;
    private boolean extraSpaceMessagePrinted = false;
    private boolean noSpaceMessagePrinted = false;

    // Number of red circles allowed on the map
    private final int MAX_RED_CIRCLES = 8;
    private int redCirclesCount = 0;
    DialogController dialogpop;


    // Method to move the robot image and the arc within the grid
    private void moveRobotAndArc(int col, int row) {
        if (robotImageView != null && myArc != null) {
            GridPane.setColumnIndex(robotImageView, col);
            GridPane.setRowIndex(robotImageView, row);
            GridPane.setColumnIndex(myArc, col);
            GridPane.setRowIndex(myArc, row);
        }
    }

    public GridPane getmaingrid(){
        return maingrid;
    }


    public HelloController() throws IOException {
    }

    // Initialize method required by Initialize interface
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Set up key event handler
//        Scene = maingrid.getScene();
//        scene.setOnKeyPressed(this::handleKeyPress);
        // Check if mainAnchor is not null before setting event handler
// Initialize arc position
        updateArcPosition(0, 0);
        mainAnchor.setOnKeyPressed(this::handleKeyPress);




//        if (mainAnchor != null) {
//            mainAnchor.setOnKeyPressed(this::handleKeyPress);
//        } else {
//            System.err.println("mainAnchor is null. Make sure it is properly injected.");
//        }
        // Initialize health bar
        // Start timeline for circle generation
        startCircleGenerationTimeline();

        // Start timeline for health increase
        startHealthIncreaseTimeline();

        statButton.setOnMouseClicked(event -> {
            System.out.println(health != null);

//            showPopUpMenu(event);
            buttonPurchase();
        });
        health.setProgress(1.0);

        System.out.println("1");
        this.dialogpop = new DialogController(maingrid);

    }

    // Method to update the position of the arc based on the provided coordinates
    public void updateArcPosition(int row, int col) {
        if (myArc != null) {
            GridPane.setColumnIndex(myArc, col);
            GridPane.setRowIndex(myArc, row);
        }
    }

    public void openPopupWindow() {
        try {
            // Load the Popup View
            // Load the Popup View
            FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("popup-view.fxml"));
            Parent popupRoot = popupLoader.load();
            HelloController controller = popupLoader.getController(); // Ensure this returns the correct instance


            // Create a new stage for the Popup View
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(popupRoot));
            popupStage.setTitle("Popup View");
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to handle key presses
    // Method to handle key presses
    private void handleKeyPress(KeyEvent event) {
        // Find the arc node in the grid
        Node child = maingrid.lookup("#myArc");

        // Handle key events based on arrow keys
        if (child != null) {
            int numColumns = maingrid.getColumnConstraints().size();
            int numRows = maingrid.getRowConstraints().size();
            double rotation = 0;

            switch (event.getCode()) {
                case UP:
                    rotation = -90;
                    if (startpositionrow > 0) {
                        moveRobotAndArc(startpositioncol, --startpositionrow);
                    }
                    break;
                case DOWN:
                    rotation = 90;
                    if (startpositionrow < numRows) {
                        moveRobotAndArc(startpositioncol, ++startpositionrow);
                    }
                    break;
                case LEFT:
                    rotation = 180;
                    if (startpositioncol > 0) {
                        moveRobotAndArc(--startpositioncol, startpositionrow);
                    }
                    break;
                case RIGHT:
                    if (startpositioncol < numColumns) {
                        moveRobotAndArc(++startpositioncol, startpositionrow);
                    }
                    break;
                default:
                    break;
            }

            myArc.setRotate(rotation);
            checkCollisions();
        } else {
            System.out.println("Arc node with ID 'myArc' not found in GridPane layout.");
        }
    }

    // Method to move the arc within the grid
    private void moveMyArc(int i, int i1) {
        Node child = maingrid.lookup("#myArc");

        if (child != null) {
            GridPane.setColumnIndex(child, i);
            GridPane.setRowIndex(child, i1);
        }
    }

    // Method to check collisions between the arc and points
    private final double MAX_HEALTH = 100.0;
    private double currentHealth = MAX_HEALTH;

    // Method to handle key presses

    // Existing code for handleKeyPress method

    private void checkCollisions() {
        Iterator<Circle> iterator = generatedCircles.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            if (isCollision(myArc, circle) && circle.isVisible()) {
                if (circle.getFill().equals(Color.RED)) {
                    // Deduct 10% of the health when colliding with a red circle
                    currentHealth -= 0.1 * MAX_HEALTH;
                    updateHealthBar();
                    // Remove the red circle
                    iterator.remove();
                    maingrid.getChildren().remove(circle);
                    redCirclesCount--;
                } else {
                    circle.setVisible(false);
                    iterator.remove();
                    maingrid.getChildren().remove(circle);
                    pointsCollected++;
                    pointCounter.setText("Points: " + pointsCollected);
                }
            }
        }
    }

    private void updateHealthBar() {
        health.setProgress(currentHealth / MAX_HEALTH);
    }

    // Method to check collision between shapes
    private boolean isCollision(Shape arc, Circle circle) {
        Shape intersect = Shape.intersect(arc, circle);
        return intersect.getBoundsInLocal().getWidth() != -1;
    }

    // Method to start the timeline for circle generation
    private void startCircleGenerationTimeline() {
        circleGenerationTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> generateCircles(1))
        );
        circleGenerationTimeline.setCycleCount(Timeline.INDEFINITE);
        circleGenerationTimeline.play();
    }

    // Method to generate points on the grid
    private void generateCircles(int numPoints) {
        if (generationPaused) {
            return;
        }

        // Calculate empty cells on the grid
        int totalCells = maingrid.getColumnConstraints().size() * maingrid.getRowConstraints().size();
        int occupiedCells = generatedCircles.size();
        int emptyCells = totalCells - occupiedCells;

        // If there are no empty cells, stop generation
        if (emptyCells == 0) {
            if (!noSpaceMessagePrinted) {
                System.out.println("No empty space left on the map. Point generation stopped.");
                noSpaceMessagePrinted = true;
                extraSpaceMessagePrinted = false;
                generationPaused = true;
            }
            return;
        }

        // If there are less than 3 empty cells, print extra space message
        if (emptyCells < 3 && !extraSpaceMessagePrinted) {
            System.out.println("Extra space available on the map. Continuing circle generation.");
            extraSpaceMessagePrinted = true;
            generationPaused = false; // Reactivate generation
        } else if (emptyCells >= 3) {
            extraSpaceMessagePrinted = false; // Reset the flag if there is enough space
        }

        // Generate Circles
        int circlesToGenerate = Math.min(numPoints, emptyCells);
        for (int i = 0; i < circlesToGenerate; i++) {
            Circle circle;
            if (random.nextInt(100) < 55) { // 55% chance to spawn a red circle
                circle = generateRedCircle();
            } else {
                circle = generateBlueCircle();
            }
            // Add circle to grid and list of generated points
            maingrid.getChildren().add(circle);
            generatedCircles.add(circle);
        }

        // Generate remaining points recursively if needed
        int remainingPoints = numPoints - circlesToGenerate;
        if (remainingPoints > 0) {
            generateCircles(remainingPoints);
        }
    }

    // Method to generate blue circle
    private Circle generateBlueCircle() {
        Circle blueCircle = new Circle(40); // Set the radius to 40
        blueCircle.setFill(Color.DODGERBLUE); // Set the fill color to dodger blue
        blueCircle.setStroke(Color.BLACK); // Set the outline color to black
        GridPane.setValignment(blueCircle, VPos.CENTER);
        GridPane.setHalignment(blueCircle, HPos.CENTER);

        int randomCol, randomRow;
        // Randomly select column and row until an empty cell is found
        do {
            randomCol = random.nextInt(5);
            randomRow = random.nextInt(5);
        } while (isPointExists(randomCol, randomRow) || (randomCol == startpositioncol && randomRow == startpositionrow));

        // Set column and row index for the blue circle
        GridPane.setColumnIndex(blueCircle, randomCol);
        GridPane.setRowIndex(blueCircle, randomRow);

        return blueCircle;
    }

    // Method to generate red circle
    private Circle generateRedCircle() {
        Circle redCircle = new Circle(40); // Set the radius to 40
        redCircle.setFill(Color.RED); // Set the fill color to red
        redCircle.setStroke(Color.BLACK); // Set the outline color to black
        GridPane.setValignment(redCircle, VPos.CENTER);
        GridPane.setHalignment(redCircle, HPos.CENTER);

        int randomCol, randomRow;
        // Randomly select column and row until an empty cell is found
        do {
            randomCol = random.nextInt(5);
            randomRow = random.nextInt(5);
        } while (isPointExists(randomCol, randomRow) || (randomCol == startpositioncol && randomRow == startpositionrow));

        // Set column and row index for the red circle
        GridPane.setColumnIndex(redCircle, randomCol);
        GridPane.setRowIndex(redCircle, randomRow);

        // Start timer for red circle removal after 5 seconds
        startRedCircleTimer(redCircle);

        return redCircle;
    }

    // Method to check if a circle already exists at the specified column and row
    private boolean isPointExists(int col, int row) {
        for (Circle circle : generatedCircles) {
            int colIndex = GridPane.getColumnIndex(circle);
            int rowIndex = GridPane.getRowIndex(circle);
            if (colIndex == col && rowIndex == row) {
                return true;
            }
        }
        return false;
    }

    private void buttonPurchase(){
        if (pointsCollected >= 1){
            pointsCollected = pointsCollected - 1;
//            System.out.println("Success");
//            System.out.println(this.dialogpop);
            dialogpop.showDialog();

//            DialogController.showDialog();
//            openPopupWindow();

        }
        else{
            System.out.println("Not Enough points");
        }

//        Dialog shop = new PopupMenu();


    }

    // Method to start timer for red circle removal
    private void startRedCircleTimer(Circle redCircle) {
        Timeline redCircleTimer = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    maingrid.getChildren().remove(redCircle);
                    generatedCircles.remove(redCircle);
                    redCirclesCount--;
                })
        );
        redCircleTimer.setCycleCount(1);
        redCircleTimer.play();
    }

    // Method to start the timeline for health increase
    private void startHealthIncreaseTimeline() {
        Timeline healthIncreaseTimeline = new Timeline(
                new KeyFrame(Duration.seconds(4), event -> increaseHealth())
        );
        healthIncreaseTimeline.setCycleCount(Timeline.INDEFINITE);
        healthIncreaseTimeline.play();
    }

    // Method to increase health by 5%
    private void increaseHealth() {
        currentHealth = Math.min(MAX_HEALTH, currentHealth + 0.05 * MAX_HEALTH);
        updateHealthBar();
    }



    // Method to handle game over
    private void gameOver() {
        circleGenerationTimeline.stop(); // Stop circle generation
        System.out.println("Game Over"); // Display game over message

        // Close the JavaFX window
        Stage stage = (Stage) maingrid.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void showPopUpMenu(MouseEvent event) {
        System.out.println("Button clicked!");

        // Create a new popup menu
//        PopupMenu popupMenu = new PopupMenu();

        // Create menu items
        MenuItem menuItem1 = new MenuItem("Option 1");
        MenuItem menuItem2 = new MenuItem("Option 2");


    }







}