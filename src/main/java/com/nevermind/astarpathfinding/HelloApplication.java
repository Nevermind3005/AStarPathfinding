package com.nevermind.astarpathfinding;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group group = new Group();
        Scene scene = new Scene(group, 600, 600);
        Input input = new Input(scene);
        Grid grid = new Grid(group);

        scene.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                grid.setMousePos(event.getX(), event.getY());
            }
        });
        grid.updateBoxes();
        stage.setResizable(false);
        stage.setTitle("A*PathFinding!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}