package com.example.ok_fx;

public class CargoMaximizer {

    // Assuming these are the dimensions of your cargo space in terms of 0.5m units
    private static final int LENGTH = 33; // 16.5 m / 0.5 m
    private static final int WIDTH = 5;   // 2.5 m / 0.5 m
    private static final int HEIGHT = 8;  // 4.0 m / 0.5 m

    // 3D array to track the positions of A, B, and C blocks
    private char[][][] cargoSpace = new char[LENGTH][WIDTH][HEIGHT];

    public CargoMaximizer() {
        // Initialize the cargo space with empty characters
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                for (int k = 0; k < HEIGHT; k++) {
                    cargoSpace[i][j][k] = ' '; // Space character indicates an empty spot
                }
            }
        }
    }

    // Method to place a block in the cargo space
    public boolean placeBlock(char blockType, int startX, int startY, int startZ) {
        // Define the dimensions of each block type
        int[] dimensions = getBlockDimensions(blockType);
        if (dimensions == null) return false; // Invalid block type

        // Check if the block can be placed
        if (canPlaceBlock(startX, startY, startZ, dimensions)) {
            for (int x = 0; x < dimensions[0]; x++) {
                for (int y = 0; y < dimensions[1]; y++) {
                    for (int z = 0; z < dimensions[2]; z++) {
                        cargoSpace[startX + x][startY + y][startZ + z] = blockType;
                    }
                }
            }
            return true;
        }
        return false;
    }

    // Helper method to check if a block can be placed
    private boolean canPlaceBlock(int startX, int startY, int startZ, int[] dimensions) {
        for (int x = 0; x < dimensions[0]; x++) {
            for (int y = 0; y < dimensions[1]; y++) {
                for (int z = 0; z < dimensions[2]; z++) {
                    if (startX + x >= LENGTH || startY + y >= WIDTH || startZ + z >= HEIGHT || 
                        cargoSpace[startX + x][startY + y][startZ + z] != ' ') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Helper method to get the dimensions of a block
    private int[] getBlockDimensions(char blockType) {
        switch (blockType) {
            case 'A': return new int[]{2, 2, 4}; // Dimensions for block A in 0.5m units
            case 'B': return new int[]{2, 3, 4}; // Dimensions for block B
            case 'C': return new int[]{3, 3, 3}; // Dimensions for block C
            default: return null; // Invalid block type
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        CargoMaximizer maximizer = new CargoMaximizer();
        // Example placements - these would come from your packing algorithm
        maximizer.placeBlock('A', 0, 0, 0);
        maximizer.placeBlock('B', 2, 2, 0);
        maximizer.placeBlock('C', 4, 4, 0);
        
        // Print out the cargo space for verification
        maximizer.printCargoSpace();
    }

    // Helper method to print the cargo space
    public void printCargoSpace() {
        for (int z = 0; z < HEIGHT; z++) {
            for (int y = 0; y < WIDTH; y++) {
                for (int x = 0; x < LENGTH; x++) {
                    System.out.print(cargoSpace[x][y][z]);
                }
                System.out.println();
            }
            System.out.println("Layer " + (z + 1) + " above");
        }
    }
}
