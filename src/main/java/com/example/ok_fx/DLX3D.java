package com.example.ok_fx;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.ok_fx.Blocks.getOrientationsInBinaryFormat;

public class DLX3D{
    static int magic_cols = HelloApplication.WIDTH;
    static int magic_rows = HelloApplication.HEIGHT;
    static int magic_depth = HelloApplication.DEPTH;

    //Go through the boxes and their rotations
    static int[][][][] AOrientations = getOrientationsInBinaryFormat('A');
    static int[][][][] BOrientations = getOrientationsInBinaryFormat('B');
    static int[][][][] COrientations = getOrientationsInBinaryFormat('C');

    static ArrayList<int[][][][]> ok = new ArrayList<>();

    private static final ArrayList<ArrayList<Integer>> theRows = new ArrayList<>();

    public static HashMap<ArrayList<Integer>, int[]> PentominoToRow = new HashMap<>();
    private static final ArrayList<String> sols = new ArrayList<>();


    static int[][] makeSparseMatrix(){
        int[][][] empty3DGrid = new int[magic_cols][magic_rows][magic_depth];

        ok.add(AOrientations);
        ok.add(COrientations);
        ok.add(BOrientations);


        char[] da_chars = new char[3];
        da_chars[0] = 'A';
        da_chars[2] = 'B';
        da_chars[1] = 'C';

        for (int[][][][] ori : ok){
            for (int i = 0; i < ori.length; i++) {
                for (int col = 0; col < magic_cols; col++){
                    for (int row = 0; row < magic_rows; row++){
                        for (int zed = 0; zed < magic_depth; zed++){
                            empty3DGrid = new int[magic_cols][magic_rows][magic_depth];
                            //fullpiece is
                            int[][][] fullpiece = ori[i];
                            if (isPlacable(empty3DGrid, fullpiece, col, row, zed)){
                                placeShapeInSpace(empty3DGrid, fullpiece, col, row, zed);
                                //idk
                                addRowToRows(empty3DGrid, col,row,zed, da_chars[ok.indexOf(ori)], i);
                            }
                        }
                    }
                }
            }
        }
        return theRows.stream().map(u -> u.stream().mapToInt(i -> i).toArray()).toArray(int[][]::new);
    }

    private static void addRowToRows(int[][][] gridWithPiecePlaced, int x, int y, int z, int pentoChar, int OrietationIndex) {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < magic_cols; i++){
            for (int j = 0; j < magic_rows; j++){
                for (int k = 0; k < magic_depth; k++){
                    positions.add(gridWithPiecePlaced[i][j][k]);
                }
            }
        }
        //Keeps info to find solution after the dlx gives the solution
        int[] Pentomino_Position_Information = { pentoChar , x, y, z, OrietationIndex };
        PentominoToRow.put(positions, Pentomino_Position_Information);

        theRows.add(positions);
    }

    public static void placeShapeInSpace(int[][][] space, int[][][] shape, int startX, int startY, int startZ, char Z) {
        int coolint = 0;
        if (Z == 'A'){
            coolint = 1;
        } else if (Z == 'B'){
            coolint = 2;
        } else if (Z == 'C'){
            coolint = 3;
        }
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
                            space[spaceX][spaceY][spaceZ] = coolint;
                        }
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
    public static boolean isPlacable(int[][][] space, int[][][] shape, int startX, int startY, int startZ) {
        // Iterate over each dimension of the shape
        for (int x = 0; x < shape.length; x++) { // shape.length should be 2 for X dimension
            for (int y = 0; y < shape[0].length; y++) { // shape[0].length should be 2 for Y dimension
                for (int z = 0; z < shape[0][0].length; z++) { // shape[0][0].length should be 4 for Z dimension

                    // Check if the current point in the shape is part of the structure (i.e., it's a 1)
                    if (shape[x][y][z] == 1) {
                        // Calculate the corresponding indices in the space
                        int spaceX = startX + x;
                        int spaceY = startY + y;
                        int spaceZ = startZ + z;

                        // Check that the indices are within the bounds of the space
                        if (spaceX < 0 || spaceX >= space.length ||
                                spaceY < 0 || spaceY >= space[0].length ||
                                spaceZ < 0 || spaceZ >= space[0][0].length) {
                            return false; // Out of bounds
                        }

                        // Check if the space is already occupied
                        if (space[spaceX][spaceY][spaceZ] != 0) {
                            return false; // Cannot place as the space is occupied
                        }
                    }
                }
            }
        }
        return true; // Shape can be placed
    }

    public static void main() throws InterruptedException {
        runExample();
    }

    public static void ReturnPentominoesUsed(ArrayList<String> answers) throws InterruptedException{
        // take the possible answers and turn each of them into readable user form
        int[][][] field = new int[magic_cols][magic_rows][magic_depth];
        for (String curr_answer : answers) {
            curr_answer = curr_answer.stripTrailing();
            String[] splitArray = curr_answer.split(" ");

            ArrayList<Integer> array_list = new ArrayList<>();

            for (String s : splitArray) {
                array_list.add(Integer.parseInt(s));
            }
            // Checking the rows now
            for (ArrayList<Integer> Row : theRows) {
                int index = 0;
                for (Integer vlera : array_list) {
                    if (Row.get(vlera) == 1) {
                        index++;
                    }
                }
                // This means that the Row had all the positions and their possible size
                // confirmed
                // So we get a unique result.
                if (index == array_list.size()) {
                    int[] pentominoInfo = PentominoToRow.get(Row);
                    char pentoChar = (char) pentominoInfo[0];
                    int x = pentominoInfo[1];
                    int y = pentominoInfo[2];
                    int z = pentominoInfo[3];
                    int OrienIndex = pentominoInfo[4];
                    System.out.println(pentoChar + " at position " + x + " " + y + " " + z + " rotation" + OrienIndex);

                    sols.add(pentoChar + " at position " + x + " " + y + " " + z + " rotation" + OrienIndex);
                    //when sols change

                    // get the piece with all orientations
                    int[][][][] full_piece_oris = ok.get(pentoChar - 65);
                    int[][][] full_piece = full_piece_oris[OrienIndex];
                    // add the piece to show to user
                    if (isPlacable(field, full_piece, x, y ,z)){
                        placeShapeInSpace(field, full_piece, x, y ,z);
                    }
                }
            }
        }
//        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//        scheduler.scheduleAtFixedRate(yourRunnable, 0, 5, TimeUnit.SECONDS);
//
//        Platform.runLater(() -> {
//            // Your UI update code here
//            // For example, updating a label:
//            System.out.println(getSols());
//            //create empty box
//
//            int[][][] box = new int[33][8][5];
//
//            for (String sol : sols){
//                char[] b = sol.toCharArray();
//                char c = b[0];
//                int lenString = b.length;
//                //29 or 30
//                int x = 0;
//                int y = 0;
//                int z = 0;
//                int rot = 0;
//                if (lenString == 29){
//                    x = Integer.parseInt(String.valueOf(b[14]));
//                    y = Integer.parseInt(String.valueOf(b[16]));
//                    z = Integer.parseInt(String.valueOf(b[18]));
//                    rot = Integer.parseInt(String.valueOf(b[28]));
//                } else if (lenString == 30){
//                    x = Integer.parseInt("" + b[14] + b[15]);
//                    y = Integer.parseInt(String.valueOf(b[17]));
//                    z = Integer.parseInt(String.valueOf(b[19]));
//                    rot = Integer.parseInt(String.valueOf(b[29]));
//                } else {
//                    System.out.println("fuck off");
//                }
//
//                //c and rotation to shape int[][][]
//                int[][][][] a = getOrientationsInBinaryFormat(c);
//                int[][][] ggggg = a[rot];
//                placeShapeInSpace(box, ggggg, x, y, z, c);
//            }
//            //get instructiuions
//            //if you can place, place
//            //update thing with new field
//            HelloApplication.updateVisualsFromField(box);
//            sols.clear();
//            // myLabel.setText("Updated from another thread");
//        });
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            // Your scheduled task code
            // For example, fetching or computing game data
            // Ensure thread safety if accessing shared data

            Platform.runLater(() -> {
                // Your UI update code here
                System.out.println(getSols());
                int[][][] box = new int[HelloApplication.WIDTH][HelloApplication.HEIGHT][HelloApplication.DEPTH];

                for (String sol : sols) {

                    char[] b = sol.toCharArray();
                char c = b[0];
                int lenString = b.length;
                //29 or 30
                int x = 0;
                int y = 0;
                int z = 0;
                int rot = 0;
                if (lenString == 29){
                    x = Integer.parseInt(String.valueOf(b[14]));
                    y = Integer.parseInt(String.valueOf(b[16]));
                    z = Integer.parseInt(String.valueOf(b[18]));
                    rot = Integer.parseInt(String.valueOf(b[28]));
                } else if (lenString == 30){
                    x = Integer.parseInt("" + b[14] + b[15]);
                    y = Integer.parseInt(String.valueOf(b[17]));
                    z = Integer.parseInt(String.valueOf(b[19]));
                    rot = Integer.parseInt(String.valueOf(b[29]));
                } else {
                    System.out.println("fuck off");
                }

                //c and rotation to shape int[][][]
                int[][][][] a = getOrientationsInBinaryFormat(c);
                int[][][] ggggg = a[rot];
                if (isPlacable(box,ggggg,x,y,z)){
                    placeShapeInSpace(box, ggggg, x, y, z, c);
                    }
                }

                HelloApplication.updateVisualsFromField(box);
                sols.clear();
            });
        }, 0, 5000, TimeUnit.MILLISECONDS);
        System.out.println("Done field");
        }

    static void runExample() throws InterruptedException {

            new Thread(() -> {
                int[][] example = makeSparseMatrix();
                DancingLinks DLX = new DancingLinks(example);

                try {
                    DLX.runSolver();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }).start();
    }

    public static ArrayList<String> getSols() {
        return sols;
    }
}