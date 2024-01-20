package com.example.cooks;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;

enum BlockType {

    A(2, 2, 4, Color.RED),
    B(2, 3, 4, Color.GREEN),
    C(3, 3, 3, Color.BLUE),

    L(1, 1,1 , Color.YELLOW),
    P(1, 1,1 , Color.PINK),
    T(1, 1,1 , Color.PURPLE);

    int width, height, depth;
    Color color;
    BlockType(int width, int height, int depth, Color color) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.color = color;
    }
}

public class Blocks extends Box {

    private static int[][][] testPiece;
    private static BlockType type;
    private static final int BOX_SIZE = 10;

    public Blocks(BlockType type) {
        super(type.width * BOX_SIZE, type.height * BOX_SIZE, type.depth * BOX_SIZE);
        this.type = type;
        this.setMaterial(new PhongMaterial(type.color));
    }

    public Blocks(int width, int depth, int height, Color color) {
        super(width * BOX_SIZE, height * BOX_SIZE, depth * BOX_SIZE);
        this.setMaterial(new PhongMaterial(color));
    }

    public BlockType getBlockType(){
        return type;
    }
    public char getCharacter() {
        if (type == BlockType.A) {
            return 'A';
        } else if (type == BlockType.B) {
            return 'B';
        } else if (type == BlockType.C) {
            return 'C';
        } else if (type == BlockType.L) {
            return 'L';
        }else if (type == BlockType.P) {
            return 'P';
        }else if (type == BlockType.T) {
            return 'T';
        }else {
            return ' ';
        }
    }
    public static int[][][][] getOrientationsInBinaryFormat(char boxChar){
        int[][][][] result = new int[0][][][];
        //Get Orientation
        switch (boxChar){
            case 'A':
                int[][][] shape1 = new int[2][2][4];
                fillBox(shape1.length, shape1[0].length, shape1[0][0].length, shape1);
                result = build_piece(shape1);
                break;
            case 'B':
                int[][][] shape2 = new int[2][3][4];
                fillBox(shape2.length, shape2[0].length, shape2[0][0].length, shape2);
                result = build_piece(shape2);
                break;
            case 'C':
                int[][][] shape3 = new int[3][3][3];
                fillBox(shape3.length, shape3[0].length, shape3[0][0].length, shape3);
                result = build_piece(shape3);
                break;
            case 'L':
                int[][][] l_piece = new int[2][4][1];
                l_piece[0][0][0]= 1;
                l_piece[0][1][0]= 1;
                l_piece[0][2][0]= 1;
                l_piece[0][3][0]= 1;
                l_piece[1][0][0]= 1;
                result = build_piece(l_piece);
                break;
            case 'P':
                int[][][] p_piece=  new int[2][3][1];
                p_piece[0][0][0] = 1;
                p_piece[0][1][0] = 1;
                p_piece[0][2][0] = 1;
                p_piece[1][1][0] = 1;
                p_piece[1][2][0] = 1;
                result = build_piece(p_piece);
                break;
            case 'T':
                int[][][] t_piece = new int[3][3][1];
                t_piece[0][2][0] = 1;
                t_piece[1][0][0] = 1;
                t_piece[1][1][0] = 1;
                t_piece[1][2][0] = 1;
                t_piece[2][2][0] = 1;
                result = build_piece(t_piece);
                break;
        }
        return result;
    }

    public static void fillBox(int x, int y, int z, int[][][] field){
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                for(int k = 0; k < z; k++){
                    field[i][j][k] = 1;
                }
            }
        }
    }

    // clones 3D array
    public static int[][][] clone_piece(int[][][] piece){

        int[][][] clone = new int[piece.length][piece[0].length][piece[0][0].length];
        for(int x = 0; x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){
                    clone[x][y][z] = piece[x][y][z];
                }
            }
        }
        return clone;
    }

    // returns true if this rotation is already included, so we remove identical rotation matrices
    private static boolean arrayIncludes(ArrayList<int[][][]> p , int[][][] piece){

        for(int[][][] perm : p){
            int x_len = perm.length, y_len  =perm[0].length, z_len = perm[0][0].length;
            if(x_len == piece.length && y_len == piece[0].length && z_len == piece[0][0].length){
                equal: {
                    for(int x = 0; x != x_len; ++x){
                        for(int y = 0; y != y_len; ++y){
                            for(int z = 0; z != z_len; ++z){
                                if(piece[x][y][z] != perm[x][y][z]) break equal;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static int[][][][] build_piece(int[][][] piece){
        ArrayList<int[][][]> permutations = new ArrayList<int[][][]>(24);

        // first four possible way how to face a cube/ 3D array
        for(int i = 0; i != 4; ++i){ // the rotations around Y

            piece = flipAxisY(piece);
            for(int r = 0; r != 4; ++r){
                // each facing has four rotations of the other axes
                piece = rotateAroundZ(piece);
                // add this rotation ONLY if its not already included
                if(!arrayIncludes(permutations, piece)){
                    permutations.add(clone_piece(piece));
                }
            }

        }

        // fift possible way how to face a cube
        piece = flipAxisX(piece);
        for(int r = 0; r != 4; ++r){
            // each facing has four rotations of the other axes
            piece = rotateAroundZ(piece);
            if(!arrayIncludes(permutations, piece)) permutations.add(clone_piece(piece));
        }
        piece = flipAxisX(piece);

        // sixth and the last possible way how to face a cube
        piece = flipAxisX(piece);
        for(int r = 0; r != 4; ++r){
            // each facing has four rotations of the other axes
            piece = rotateAroundZ(piece);
            if(!arrayIncludes(permutations, piece)) permutations.add(clone_piece(piece));
        }

        return permutations.toArray(new int[permutations.size()][][][]);
    }

    // rotates around X axis
    private static int[][][] flipAxisX(int[][][] piece){

        int[][][] rotated_piece = new int[piece.length][piece[0][0].length][piece[0].length];

        for(int x = 0; x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){

                    rotated_piece[x][piece[0][0].length - z - 1][y] = piece[x][y][z];
                }
            }
        }
        return rotated_piece;
    }
    //rotates around Y axis
    private static int[][][] flipAxisY(int[][][] piece){

        int[][][] rotated_piece = new int[piece[0][0].length][piece[0].length][piece.length];

        for(int x = 0; x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){
                    rotated_piece[piece[0][0].length - z - 1][y][x] = piece[x][y][z];
                }
            }
        }
        return rotated_piece;
    }

    // rotates around Z axis
    private static int[][][] rotateAroundZ(int[][][] piece){

        int[][][] rotated_piece = new int[piece[0].length][piece.length][piece[0][0].length];

        for(int x = 0; x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){
                    rotated_piece[piece[0].length - y - 1][x][z] = piece[x][y][z];
                }
            }
        }
        return rotated_piece;
    }

    public Color getColor() {
        // Return the color based on the block type
        // This is a placeholder; replace it with your actual logic
        return switch (type) {
            case A -> Color.RED;
            case B -> Color.GREEN;
            case C -> Color.BLUE;
            case L -> Color.YELLOW;
            case P -> Color.PINK;
            case T -> Color.PURPLE;
            default -> Color.TRANSPARENT;
        };
    }


    public static void main(String[] args) {
        getOrientationsInBinaryFormat('C');
    }

    public int[][][] getShape() {
        return testPiece;
    }
}