package com.example.ok_fx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class SimpleCubeFrame extends Application {
    // Initial mouse coordinates
    private double startX, startY;

    public Parent createContent() {

        // Box
        Box testBox = new Box(5, 5, 5);
        testBox.setMaterial(new PhongMaterial(Color.RED));
        testBox.setDrawMode(DrawMode.LINE);

        // Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                new Rotate(-15, Rotate.Y_AXIS),
                new Rotate(-15, Rotate.X_AXIS),
                new Translate(0, 0, -16));

        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(testBox);

        // Use a SubScene
        SubScene subScene = new SubScene(root, 300, 300);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);

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

                // Rotate

                camera.getTransforms().add(new Rotate(deltaX, Rotate.Y_AXIS));
                camera.getTransforms().add(new Rotate(deltaY, Rotate.X_AXIS));

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