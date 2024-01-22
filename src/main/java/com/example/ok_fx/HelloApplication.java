package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class HelloApplication extends Application {
    public static final int WIDTH = 33;
    public static final int HEIGHT = 8;
    public static final int DEPTH = 5;
    private static final double BOX_SIZE = 10;
    private static Group blockGroup;
    private static int MaxValue = 0;
    public static Text myText = new Text("MaxValue: Calculating");


    // Create the 3D array
    private double mouseX;

    public static int getMaxValue() {
        return MaxValue;
    }

    public static void setMaxValue(int maxValue) {
        MaxValue = maxValue;
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Group roomGroup = new Group();
        Group roomEmpty = new Group();
        blockGroup = new Group();
        root.getChildren().addAll(roomEmpty, roomGroup, blockGroup);

        Scene scene = new Scene(root, 800, 600, true);
        scene.setFill(Color.DARKGRAY);

        Camera camera = new PerspectiveCamera();
        roomGroup.getTransforms().addAll(
                new Rotate(-20, Rotate.Y_AXIS),
                new Rotate(20, Rotate.X_AXIS),
                new Translate(140, 100, -500));
        blockGroup.getTransforms().addAll(
                new Rotate(-20, Rotate.Y_AXIS),
                new Rotate(20, Rotate.X_AXIS),
                new Translate(140, 100, -500));

        scene.setCamera(camera);
        setupZooming(scene, camera);

        create3DGrid(roomGroup);

        primaryStage.setTitle("Phase 3 - Knapsack");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Button
        Button myButton = new Button("Run ABC -> Values");
        Button myButton2 = new Button("Run LPT -> Values");
        Button myButton3 = new Button("Run LPT - fillBox");
        Button myButton4 = new Button("Run ABC - fillBox");


        myButton2.getTransforms().addAll(
                new Translate(0,50,0)
        );
        myButton3.getTransforms().addAll(
                new Translate(0,100,0)
        );
        myButton4.getTransforms().addAll(
                new Translate(0,150,0)
        );
        myText.getTransforms().addAll(
                new Translate(0,250,0)
        );

        roomEmpty.getChildren().addAll(myButton);
        roomEmpty.getChildren().addAll(myButton2);
        roomEmpty.getChildren().addAll(myButton3);
        roomEmpty.getChildren().addAll(myButton4);
        roomEmpty.getChildren().addAll(myText);


        myButton.setOnMousePressed(event -> {
            myButton.setDisable(true);
            myButton2.setDisable(true);
            myButton3.setDisable(true);
            myButton4.setDisable(true);
            Thread newThread = new Thread(() -> {
                try {
                    DLX3D.runExampleABCVal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            newThread.start();
        });

        myButton2.setOnMousePressed(event -> {
            myButton.setDisable(true);
            myButton2.setDisable(true);
            myButton3.setDisable(true);
            myButton4.setDisable(true);

            Thread newThread = new Thread(() -> {
                try {
                    DLX3D.runExampleLPTVal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            newThread.start();
        });

        myButton3.setOnMousePressed(event -> {
            myButton.setDisable(true);
            myButton2.setDisable(true);
            myButton3.setDisable(true);
            myButton4.setDisable(true);

            myText.setText("No Value for this one!");
            Thread newThread = new Thread(() -> {
                try {
                    DLX3D.runExampleLPT();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            newThread.start();
        });

        myButton4.setOnMousePressed(event -> {
            myButton.setDisable(true);
            myButton2.setDisable(true);
            myButton3.setDisable(true);
            myButton4.setDisable(true);

            myText.setText("No found solution for this!");
            Thread newThread = new Thread(() -> {
                try {
                    DLX3D.runExampleABC();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            newThread.start();
        });


        scene.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
        });

        scene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseX;

            double centerX = (double) WIDTH / 2 + 115;

            // Translate to the center of the shape
            blockGroup.getTransforms().add(new Rotate(0, centerX, 0, 0, Rotate.X_AXIS));
            blockGroup.getTransforms().add(new Rotate(1 * deltaX, centerX, 0, 0, Rotate.Y_AXIS));
            roomGroup.getTransforms().add(new Rotate(0, centerX, 0, 0, Rotate.X_AXIS));
            roomGroup.getTransforms().add(new Rotate(1 * deltaX, centerX, 0, 0, Rotate.Y_AXIS));

            mouseX = event.getSceneX();
        });
    }

    private void setupZooming(Scene scene, Camera camera) {
        scene.setOnScroll(event -> {
            double zoomFactor = event.getDeltaY();


            zoomTowardsMouse(camera, zoomFactor);
        });
    }

    private void zoomTowardsMouse(Camera camera, double zoomFactor) {
        Point3D viewDirection = camera.localToSceneTransformProperty().getValue().deltaTransform(0, 0, -1);
        Point3D zoomDirection = viewDirection.normalize();

        // Calculate new position
        double newPosX = camera.getTranslateX() + zoomFactor * zoomDirection.getX();
        double newPosY = camera.getTranslateY() + zoomFactor * zoomDirection.getY();
        double newPosZ = camera.getTranslateZ() + zoomFactor * zoomDirection.getZ();

        camera.setTranslateX(newPosX);
        camera.setTranslateY(newPosY);
        camera.setTranslateZ(newPosZ);
    }

    private void create3DGrid(Group group) {
        Point3D[] corners = getCornersOfBox();
        Color gridColor = Color.BLACK; // Set the color for the grid lines

        // Connect each corner with lines
        connectCorners(group, corners[0], corners[1], gridColor); // Bottom front edge
        connectCorners(group, corners[1], corners[4], gridColor); // Bottom right edge
        connectCorners(group, corners[4], corners[2], gridColor); // Bottom back edge
        connectCorners(group, corners[2], corners[0], gridColor); // Bottom left edge

        connectCorners(group, corners[0], corners[3], gridColor); // Front left vertical
        connectCorners(group, corners[1], corners[6], gridColor); // Front right vertical
        connectCorners(group, corners[4], corners[7], gridColor); // Back right vertical
        connectCorners(group, corners[2], corners[5], gridColor); // Back left vertical

        connectCorners(group, corners[3], corners[6], gridColor); // Top front edge
        connectCorners(group, corners[6], corners[7], gridColor); // Top right edge
        connectCorners(group, corners[7], corners[5], gridColor); // Top back edge
        connectCorners(group, corners[5], corners[3], gridColor); // Top left edge
    }

    private void connectCorners(Group roomGroup, Point3D start, Point3D end, Color lineColor) {
        double lineRadius = 0.5; // Small radius for thin line appearance
        double distance = start.distance(end);
        Point3D midPoint = start.midpoint(end);

        // Create a cylinder to represent the line
        Cylinder line = new Cylinder(lineRadius, distance);

        // Set the material and color of the cylinder
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(lineColor);
        line.setMaterial(material);

        // Positioning and rotating the cylinder
        line.setTranslateX(midPoint.getX());
        line.setTranslateY(midPoint.getY());
        line.setTranslateZ(midPoint.getZ());

        Point3D axisOfRotation = start.subtract(end).crossProduct(new Point3D(0, -1, 0)).normalize();
        double angle = Math.acos(start.subtract(end).normalize().dotProduct(new Point3D(0, -1, 0)));
        line.setRotationAxis(axisOfRotation);
        line.setRotate(Math.toDegrees(angle));

        roomGroup.getChildren().add(line);
    }

    private Point3D[] getCornersOfBox() {
        double maxX = WIDTH * BOX_SIZE;
        double maxY = HEIGHT * BOX_SIZE;
        double maxZ = DEPTH * BOX_SIZE;

        return new Point3D[] {
                new Point3D(0, 0, 0),
                new Point3D(maxX, 0, 0),
                new Point3D(0, maxY, 0),
                new Point3D(0, 0, maxZ),
                new Point3D(maxX, maxY, 0),
                new Point3D(0, maxY, maxZ),
                new Point3D(maxX, 0, maxZ),
                new Point3D(maxX, maxY, maxZ)
        };
    }

    public static void updateVisualsFromField(int[][][] given_field) {

        blockGroup.getChildren().clear();

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < DEPTH; z++) {
                    int blockTypeChar = given_field[x][y][z];
                    if (blockTypeChar != 0){
                        BlockType blockType = switch (blockTypeChar) {
                            case 1 -> charToBlockType('A');
                            case 2 -> charToBlockType('B');
                            case 3 -> charToBlockType('C');
                            case 4 -> charToBlockType('L');
                            case 5 -> charToBlockType('P');
                            case 6 -> charToBlockType('T');
                            default -> null;
                        };
                        placeVisualBlock(blockGroup, x, y, z, blockType);
                    }
                }
            }
        }
    }

    private static void placeVisualBlock(Group blockGroup, int x, int y, int z, BlockType blockType) {
        Blocks block = new Blocks(blockType);

        Box cell = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(block.getColor());
        cell.setMaterial(material);


        double translateX = x * BOX_SIZE + BOX_SIZE/2;
        double translateY = y * BOX_SIZE + BOX_SIZE/2;
        double translateZ = z * BOX_SIZE + BOX_SIZE/2;
        cell.setTranslateX(translateX);
        cell.setTranslateY(translateY);
        cell.setTranslateZ(translateZ);
        blockGroup.getChildren().add(cell);
    }

    private static BlockType charToBlockType(char blockTypeChar) {
        return switch (blockTypeChar) {
            case 'A' -> BlockType.A;
            case 'B' -> BlockType.B;
            case 'C' -> BlockType.C;
            case 'L' -> BlockType.L;
            case 'P' -> BlockType.P;
            case 'T' -> BlockType.T;
            default -> throw new IllegalArgumentException("Unknown block type character: " + blockTypeChar);
        };
    }


    public static void main(String[] args) {
        launch(args);
    }
}
