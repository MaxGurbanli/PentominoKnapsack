package com.example.ok_fx;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class SimpleCubeFrame extends Application {

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(15);
    private final DoubleProperty angleY = new SimpleDoubleProperty(60);


    public Parent createContent() {

        int[][][] array = new int[][][] {
            {{1, 0, 0}, {0, 1, 1}, {0, 1, 0}}, 
            {{1, 0, 1}, {0, 1, 0}, {0, 1, 1}},
            {{1, 0, 0}, {0, 1, 1}, {0, 1, 0}}
        };

        // Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);

        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().add(camera);

        // Assuming array is a 3D boolean array
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    // Create a box
                    Box testBox = new Box(0.5, 0.5, 0.5);
                    testBox.getTransforms().add(new Translate(i*0.5, j*0.5, k*0.5));

                    // Assign color based on the value in the array
                    if (array[i][j][k] == 1) {
                        testBox.setMaterial(new PhongMaterial(Color.RED));
                    } else {
                        testBox.setMaterial(new PhongMaterial(Color.BLUE));
                    }

                    root.getChildren().add(testBox);
                }
            }
        }
        
        // Use a SubScene
        SubScene subScene = new SubScene(root, 500,500);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);

        initMouseControl(camera, root, subScene); // Enable mouse control for camera
        // initFixedCameraView(camera); // Uncomment this to disable mouse control

        Group group = new Group();
        group.getChildren().add(subScene);
        return group;
    }

    private void initMouseControl(PerspectiveCamera camera, Group group, SubScene scene) {
        Rotate xRotate;
        Rotate yRotate;
        camera.getTransforms().addAll(
                xRotate = new Rotate(60, Rotate.X_AXIS),
                yRotate = new Rotate(15, Rotate.Y_AXIS),
                new Translate(1, 0, -12)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
    
        final double sensitivity = 0.3; // Adjust this value to control sensitivity
    
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });
    
        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()) * sensitivity);
            angleY.set(anchorAngleY + (anchorX - event.getSceneX()) * sensitivity);
        });
    }

    private void initFixedCameraView(PerspectiveCamera camera) {
        camera.getTransforms().clear();
        camera.getTransforms().addAll (
                new Rotate(60, Rotate.X_AXIS),
                new Rotate(15, Rotate.Y_AXIS),
                new Translate(1, 0, -12));
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