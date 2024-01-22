package com.example.demo;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.example.demo.Blocks.getOrientationsInBinaryFormat;

public class DLX3D{
    static int magic_cols = HelloApplication.WIDTH;
    static int magic_rows = HelloApplication.HEIGHT;
    static int magic_depth = HelloApplication.DEPTH;

    static int maxValueFound = 0;
    static int[][][] solutionField;

    //Go through the boxes and their rotations
    static int[][][][] AOrientations = getOrientationsInBinaryFormat('A');
    static int[][][][] BOrientations = getOrientationsInBinaryFormat('B');
    static int[][][][] COrientations = getOrientationsInBinaryFormat('C');

    static int[][][][] LOrientations = getOrientationsInBinaryFormat('L');
    static int[][][][] POrientations = getOrientationsInBinaryFormat('P');
    static int[][][][] TOrientations = getOrientationsInBinaryFormat('T');

    static ArrayList<int[][][][]> ok = new ArrayList<>();

    private static final ArrayList<ArrayList<Integer>> theRows = new ArrayList<>();

    public static HashMap<ArrayList<Integer>, int[]> PentominoToRow = new HashMap<>();

    static int[][] makeSparseMatrix(){
        int[][][] empty3DGrid;
        ok.add(AOrientations);
        ok.add(BOrientations);
        ok.add(COrientations);
//        ok.add(LOrientations);
//        ok.add(POrientations);
//        ok.add(TOrientations);

        char[] da_chars = new char[3];
        da_chars[0] = 'A';
        da_chars[1] = 'B';
        da_chars[2] = 'C';
//        da_chars[3] = 'L';
//        da_chars[4] = 'P';
//        da_chars[5] = 'T';

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
                                addRowToRows(empty3DGrid, col,row,zed, da_chars[ok.indexOf(ori)], i, valueGetter(da_chars[ok.indexOf(ori)]));
                            }
                        }
                    }
                }
            }
        }
        return theRows.stream().map(u -> u.stream().mapToInt(i -> i).toArray()).toArray(int[][]::new);
    }

    private static int valueGetter(char daChar) {
        return switch (daChar) {
            case 'A' -> 3;
            case 'B' -> 4;
            case 'C' -> 5;
            case 'L' -> 3;
            case 'P' -> 4;
            case 'T' -> 5;
            default -> 0;
        };
    }

    private static void addRowToRows(int[][][] gridWithPiecePlaced, int x, int y, int z, int pentoChar, int OrietationIndex, int value) {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < magic_cols; i++){
            for (int j = 0; j < magic_rows; j++){
                for (int k = 0; k < magic_depth; k++){
                    positions.add(gridWithPiecePlaced[i][j][k]);
                }
            }
        }
        //Keeps info to find solution after the dlx gives the solution
        int[] Pentomino_Position_Information = { pentoChar , x, y, z, OrietationIndex, value };
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
        } else if (Z == 'L'){
            coolint = 4;
        } else if (Z == 'P'){
            coolint = 5;
        } else if (Z == 'T'){
            coolint = 6;
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
//                    System.out.println(pentoChar + " at position " + x + " " + y + " " + z + " rotation" + OrienIndex);

                    // get the piece with all orientations
                    int[][][][] full_piece_oris;
                    if (pentoChar == 'L'){
                        full_piece_oris = ok.get(0);
                    } else if (pentoChar == 'P'){
                        full_piece_oris = ok.get(1);
                    } else {
                        full_piece_oris = ok.get(2);
                    }
                    int[][][] full_piece = full_piece_oris[OrienIndex];
                    // add the piece to show to user
                    if (isPlacable(field, full_piece, x, y ,z)){
                        placeShapeInSpace(field, full_piece, x, y ,z, pentoChar);
                    }
                    Platform.runLater(() -> HelloApplication.updateVisualsFromField(field));
                }
            }
        }
        Thread.sleep(500);
        System.out.println("Done field");
    }

    public static void ReturnPentominoesUsed2(ArrayList<String> answers) throws InterruptedException{
        // take the possible answers and turn each of them into readable user form
        int[][][] field = new int[magic_cols][magic_rows][magic_depth];
        int sum = 0;
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
                    int value = pentominoInfo[5];

                    // get the piece with all orientations
                    int[][][][] full_piece_oris;
                    if (pentoChar == 'A'){
                        full_piece_oris = ok.get(0);
                    } else if (pentoChar == 'B'){
                        full_piece_oris = ok.get(1);
                    } else {
                        full_piece_oris = ok.get(2);
                    }
                    int[][][] full_piece = full_piece_oris[OrienIndex];

                    if (isPlacable(field, full_piece, x, y ,z)){
                        placeShapeInSpace(field, full_piece, x, y ,z, pentoChar);
                        sum += value;
                    }
                }
            }
        }
        if (sum > maxValueFound){
            System.out.println("Found max! Its value: " + sum);
            solutionField = copy3DArray(field);
            maxValueFound = sum;
        }
    }

    private static int[][][] copy3DArray(int[][][] originalArray) {
        int[][][] copiedArray = new int[originalArray.length][][];
        for (int i = 0; i < originalArray.length; i++) {
            copiedArray[i] = new int[originalArray[i].length][];
            for (int j = 0; j < originalArray[i].length; j++) {
                copiedArray[i][j] = new int[originalArray[i][j].length];
                for (int k = 0; k < originalArray[i][j].length; k++) {
                    copiedArray[i][j][k] = originalArray[i][j][k];
                }
            }
        }
        return copiedArray;
    }

    static void runExample() throws InterruptedException {

                int[][] example = makeSparseMatrix();
                System.out.println(Arrays.deepToString(example));
                System.out.println("Headers:" + example[0].length);
                System.out.println("Possible Rows:" + example.length);

                DancingLinks2 DLX = new DancingLinks2(example);
                DLX.runSolver();
    }

    public static int[][][] getBestSolution() {
        return solutionField;
    }
}