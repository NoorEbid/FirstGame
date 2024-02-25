package com.example.demo;

import javafx.scene.layout.GridPane;

public class SharedModel {
    private static GridPane mainGrid;

    public static GridPane getMainGrid() {
        return mainGrid;
    }

    public static void setMainGrid(GridPane gridPane) {
        mainGrid = gridPane;
    }
}
