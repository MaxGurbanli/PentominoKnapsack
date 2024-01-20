package com.example.ok_fx;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    public static final int WIDTH = 5;
    public static final int HEIGHT = 8;
    public static final int DEPTH = 33;
    private static final double BOX_SIZE = 10;
    private static Group blockGroup;
    private Point2D lastMouseCoordinates;
    // Create the 3D array
    public static int[][][] threeDArray = new int[WIDTH][HEIGHT][DEPTH];

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        Group root = new Group();
        Group roomGroup = new Group();
        blockGroup = new Group();
        root.getChildren().addAll(roomGroup, blockGroup);

        Scene scene = new Scene(root, 800, 600, true);
        scene.setFill(Color.DARKGRAY);

        Camera camera = new PerspectiveCamera();
        camera.getTransforms().addAll (
                new Rotate(-20, Rotate.Y_AXIS),
                new Rotate(-20, Rotate.X_AXIS),
                new Translate(-200, -200, 400));

        scene.setCamera(camera);
        setupZooming(scene, camera);
        double pivotX = -350;
        double pivotY = 40;
        double pivotZ = 0;
        setupRotationWithPivot(scene, camera, pivotX, pivotY, pivotZ);

        create3DGrid(roomGroup);

        primaryStage.setTitle("Phase 3 - Knapsack");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create a random number generator


        // Define the uppercase characters to randomly place in the array


        // Fill the 3D array with random uppercase characters

        //testing shit
//        int x=0;
//        do {
//            placeBlockInArray(x,0,0,'A');
//            x += charToBlockType('A').width;
//        } while(canPlaceBlock(x,0,0, 'A')) ;
//        ArrayList<Point3D> Tpoints = getTBoxCoords();
//        for (int z = 0; z < 5; z++){
//            Point3D points = Tpoints.get(z);
//            placeBlockInArray((int) points.getX(), (int) points.getY(), (int) points.getZ(), 'T');
//        }
        DLX3D.runExample();
    }

    private void setupZooming(Scene scene, Camera camera) {
        scene.setOnScroll(event -> {
            double zoomFactor = event.getDeltaY();
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            zoomTowardsMouse(camera, zoomFactor, mouseX, mouseY, scene);
        });
    }

    private void zoomTowardsMouse(Camera camera, double zoomFactor, double mouseX, double mouseY, Scene scene) {
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

    private void setupRotationWithPivot(Scene scene, Camera camera, double pivotX, double pivotY, double pivotZ) {
        scene.setOnMousePressed(event -> {
            lastMouseCoordinates = new Point2D(event.getSceneX(), event.getSceneY());
        });

        scene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - lastMouseCoordinates.getX();
            double deltaY = event.getSceneY() - lastMouseCoordinates.getY();
            lastMouseCoordinates = new Point2D(event.getSceneX(), event.getSceneY());

            double angleX = -deltaY / 10;
            double angleY = deltaX / 10;

            rotateAroundPivot(camera, pivotX, pivotY, pivotZ, angleX, angleY);
        });
    }

    private void rotateAroundPivot(Camera camera, double pivotX, double pivotY, double pivotZ, double angleX, double angleY) {
        Translate pivotToOrigin = new Translate(-pivotX, -pivotY, -pivotZ);
        Rotate rotateX = new Rotate(angleX, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(angleY, Rotate.Y_AXIS);
        Translate backToPivot = new Translate(pivotX, pivotY, pivotZ);

        camera.getTransforms().addAll(pivotToOrigin, rotateX, rotateY, backToPivot);
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

    private static void updateVisualsFromField(char[][][] given_field) {

        blockGroup.getChildren().clear();

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < DEPTH; z++) {
                    char blockTypeChar = given_field[x][y][z];
                    if (blockTypeChar == ' '){
                        //removeVisualBlock(blockGroup, x, y, z);
                    }else{
                        BlockType blockType = charToBlockType(blockTypeChar);
                        placeVisualBlock(blockGroup, x, y, z, blockType);
                    }
                }
            }
        }
    }

    public static void updateVisualsFromField(int[][][] given_field) {

        blockGroup.getChildren().clear();

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < DEPTH; z++) {
                    int blockTypeChar = given_field[x][y][z];
                    if (blockTypeChar == 0){
                        //removeVisualBlock(blockGroup, x, y, z);
                    }else{
                        BlockType blockType = null;
                        switch (blockTypeChar){
                            case 1:
                                blockType = charToBlockType('A');
                                break;
                            case 2:
                                blockType = charToBlockType('B');
                                break;
                            case 3:
                                blockType = charToBlockType('C');
                                break;
                        }

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
        switch (blockTypeChar) {
            case 'A':
                return BlockType.A;
            case 'B':
                return BlockType.B;
            case 'C':
                return BlockType.C;
            case 'L':
                return BlockType.L;
            case 'P':
                return BlockType.P;
            case 'T':
                return BlockType.T;
            default:
                throw new IllegalArgumentException("Unknown block type character: " + blockTypeChar);

        }
    }

    private void placeBlockInArray(int x, int y, int z, char blockTypeChar) {
        for (int dx = 0; dx < charToBlockType(blockTypeChar).width; dx++) {
            for (int dy = 0; dy < charToBlockType(blockTypeChar).height; dy++) {
                for (int dz = 0; dz < charToBlockType(blockTypeChar).depth; dz++) {
                    if (x + dx < WIDTH && y + dy < HEIGHT && z + dz < DEPTH) {
                        threeDArray[x + dx][y + dy][z + dz] = blockTypeChar;
                    }
                }
            }
        }
    }

    public static void placeShapeInSpace(int[][][] space, int[][][] shape, int startX, int startY, int startZ) {
        for (int x = 0; x < shape.length; x++) {
            for (int y = 0; y < shape[0].length; y++) {
                for (int z = 0; z < shape[0][0].length; z++) {
                    // Check if the current point in the shape is part of the structure (i.e., it's a 1)
                    if (shape[x][y][z] == 1) {
                        // Calculate the corresponding indices in the space
                        int spaceX = startX + x;
                        int spaceY = startY + y;
                        int spaceZ = startZ + z;

                        // Check that the indices are within the bounds of the space
                        if (spaceX >= 0 && spaceX < space.length &&
                                spaceY >= 0 && spaceY < space[0].length &&
                                spaceZ >= 0 && spaceZ < space[0][0].length) {
                            // Place the part of the shape into the space
                            space[spaceX][spaceY][spaceZ] = shape[x][y][z];
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
