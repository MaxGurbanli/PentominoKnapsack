package com.example.cooks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.example.cooks.Blocks.getOrientationsInBinaryFormat;

public class DLX3D {
    static int magic_cols = 33;
    static int magic_rows = 8;
    static int magic_depth = 5;

    //Go through the boxes and their rotations
    static int[][][][] AOrientations = getOrientationsInBinaryFormat('A');
    static int[][][][] BOrientations = getOrientationsInBinaryFormat('B');
    static int[][][][] COrientations = getOrientationsInBinaryFormat('C');

    static ArrayList<int[][][][]> ok = new ArrayList<>();

    private static final ArrayList<ArrayList<Integer>> theRows = new ArrayList<>();

    public static HashMap<ArrayList<Integer>, int[]> PentominoToRow = new HashMap<>();


    static int[][] makeSparseMatrix(){
        int[][][] empty3DGrid = new int[magic_cols][magic_rows][magic_depth];

        ok.add(AOrientations);
        ok.add(BOrientations);
        ok.add(COrientations);

        char[] da_chars = new char[3];
        da_chars[0] = 'A';
        da_chars[1] = 'B';
        da_chars[2] = 'C';

        for (int[][][][] ori : ok){
            for (int i = 0; i < ori.length; i++) {
                for (int col = 0; col < magic_cols; col++){
                    for (int row = 0; row < magic_rows; row++){
                        for (int zed = 0; zed < magic_depth; zed++){
                            //fullpiece is
                            int[][][] fullpiece = ori[i];
                            if (isPlacable(empty3DGrid, fullpiece, col, row, zed)){
                                placeShapeInSpace(empty3DGrid, fullpiece, col, row, zed);
                                //idk
                                addRowToRows(empty3DGrid, col,row,zed, da_chars[ok.indexOf(ori)], ok.indexOf(ori));
                                empty3DGrid = new int[magic_cols][magic_rows][magic_depth];
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
                            if (space[spaceX][spaceY][spaceZ] != 0){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
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
                    int pentoChar = pentominoInfo[0];
                    int x = pentominoInfo[1];
                    int y = pentominoInfo[2];
                    int z = pentominoInfo[3];
                    int OrienIndex = pentominoInfo[4];
                    System.out.println(pentoChar + " at position " + x + " " + y + " " + z + " rotation" + OrienIndex);
                    // get the piece with all orientations
                    int[][][][] full_piece_oris = ok.get(pentoChar);
                    int[][][] full_piece = full_piece_oris[OrienIndex];
                    // add the piece to show to user
                    placeShapeInSpace(field, full_piece, x, y ,z);
                }
            }
        }
        
    }

    private static void runExample() throws InterruptedException {
        int[][] example = makeSparseMatrix();
        DancingLinks DLX = new DancingLinks(example);
        DLX.runSolver();
    }
}