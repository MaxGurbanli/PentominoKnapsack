package com.example.ok_fx;

public class CargoMaximizer {
    private static final int TRUCK_LENGTH = 33; // 16.5 m with 0.5 m increments
    private static final int TRUCK_WIDTH = 5;   // 2.5 m with 0.5 m increments
    private static final int TRUCK_HEIGHT = 8;  // 4.0 m with 0.5 m increments

    private static final int VALUE_A = 3;
    private static final int VALUE_B = 4;
    private static final int VALUE_C = 5;

    private static final int[][] PARCEL_DIMENSIONS = {
        {2, 2, 4}, // Parcel A dimensions in 0.5 m increments
        {2, 3, 4}, // Parcel B dimensions
        {3, 3, 3}  // Parcel C dimensions
    };
    private static final int[] PARCEL_VALUES = {VALUE_A, VALUE_B, VALUE_C};

    private static int[][][] dp;
    private static char[][][] parcelPositions;

    public static void main(String[] args) {
        System.out.println("Maximum value: " + getMaxValue());
        System.out.println("Storing");
        storeParcels();
        printParcelPositions();
    }

    private static int getMaxValue() {
        dp = new int[TRUCK_LENGTH + 1][TRUCK_WIDTH + 1][TRUCK_HEIGHT + 1];
        parcelPositions = new char[TRUCK_LENGTH + 1][TRUCK_WIDTH + 1][TRUCK_HEIGHT + 1];
    
        for (int i = 0; i <= TRUCK_LENGTH; i++) {
            for (int j = 0; j <= TRUCK_WIDTH; j++) {
                for (int k = 0; k <= TRUCK_HEIGHT; k++) {
                    for (int parcelType = 0; parcelType < PARCEL_DIMENSIONS.length; parcelType++) {
                        int[] dim = PARCEL_DIMENSIONS[parcelType];
                        if (i >= dim[0] && j >= dim[1] && k >= dim[2]) {
                            int newValue = dp[i - dim[0]][j][k] + 
                                           dp[dim[0]][j - dim[1]][k] + 
                                           dp[dim[0]][dim[1]][k - dim[2]] + 
                                           PARCEL_VALUES[parcelType];
                            if (newValue > dp[i][j][k]) {
                                dp[i][j][k] = newValue;
                                // Record that this parcel was placed at these positions
                                for (int x = 0; x < dim[0]; x++) {
                                    for (int y = 0; y < dim[1]; y++) {
                                        for (int z = 0; z < dim[2]; z++) {
                                            parcelPositions[i - x][j - y][k - z] = (char) ('A' + parcelType);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return dp[TRUCK_LENGTH][TRUCK_WIDTH][TRUCK_HEIGHT];
    }

    private static void storeParcels() {
        int i = TRUCK_LENGTH, j = TRUCK_WIDTH, k = TRUCK_HEIGHT;
        while (i > 0 && j > 0 && k > 0) {
            boolean parcelPlaced = false;
            for (int parcelType = 0; parcelType < PARCEL_DIMENSIONS.length; parcelType++) {
                int[] dim = PARCEL_DIMENSIONS[parcelType];
                if (i >= dim[0] && j >= dim[1] && k >= dim[2]) {
                    int remainingValue = dp[i - dim[0]][j - dim[1]][k - dim[2]];
                    if (dp[i][j][k] == remainingValue + PARCEL_VALUES[parcelType]) {
                        // This parcel was placed at this position
                        for (int x = 0; x < dim[0]; x++) {
                            for (int y = 0; y < dim[1]; y++) {
                                for (int z = 0; z < dim[2]; z++) {
                                    parcelPositions[i - x][j - y][k - z] = (char) ('A' + parcelType);
                                }
                            }
                        }
                        // Move to the remaining space after placing this parcel
                        i -= dim[0];
                        j -= dim[1];
                        k -= dim[2];
                        parcelPlaced = true;
                        break; // Break the loop as we found the parcel
                    }
                }
            }
            if (!parcelPlaced) {
                break; // Break the loop if no parcel could be placed
            }
        }
    }

    private static void printParcelPositions() {
        for (int i = 0; i < TRUCK_LENGTH + 1; i++) {
            for (int j = 0; j < TRUCK_WIDTH + 1; j++) {
                for (int k = 0; k < TRUCK_HEIGHT + 1; k++) {
                    System.out.print(parcelPositions[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

}
