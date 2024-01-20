package com.example.ok_fx;

import javafx.scene.transform.Rotate;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.stage.Stage;

public class SimpleCubeFrame extends Application {
    // Initial mouse coordinates
    private double startX, startY;

    public Parent createContent() {

        // Box
        Box testBox = new Box(100, 100, 100);
        testBox.setMaterial(new PhongMaterial(Color.RED));
        testBox.setDrawMode(DrawMode.LINE);
        testBox.setTranslateX(150);
        testBox.setTranslateY(150);

        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().add(testBox);

        // Use a SubScene
        SubScene subScene = new SubScene(root, 300, 300);
        subScene.setFill(Color.ALICEBLUE);

        // Record the initial cursor position
        subScene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                startX = event.getSceneX();
                startY = event.getSceneY();
            }
        });

        // Calculate the change in cursor position
        subScene.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double deltaX = event.getSceneX() - startX;
                double deltaY = event.getSceneY() - startY;

                // Rotate the cube
                testBox.getTransforms().add(new javafx.scene.transform.Rotate(deltaX, Rotate.Y_AXIS));
                testBox.getTransforms().add(new javafx.scene.transform.Rotate(deltaY, Rotate.X_AXIS));

                // Record the updated cursor position
                startX = event.getSceneX();
                startY = event.getSceneY();
            }
        });

        Group group = new Group();
        group.getChildren().add(subScene);
        return group;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Java main for when running without JavaFX launcher
     */
    public static void main(String[] args) {
        launch(args);
    }
}