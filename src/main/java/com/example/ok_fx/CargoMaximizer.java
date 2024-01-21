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

    public static void main(String[] args) {
        System.out.println("Maximum value: " + getMaxValue());
    }

    private static int getMaxValue() {
        int[][][] dp = new int[TRUCK_LENGTH + 1][TRUCK_WIDTH + 1][TRUCK_HEIGHT + 1];

        for (int i = 0; i <= TRUCK_LENGTH; i++) {
            for (int j = 0; j <= TRUCK_WIDTH; j++) {
                for (int k = 0; k <= TRUCK_HEIGHT; k++) {
                    for (int parcelType = 0; parcelType < PARCEL_DIMENSIONS.length; parcelType++) {
                        int[] dim = PARCEL_DIMENSIONS[parcelType];
                        if (i >= dim[0] && j >= dim[1] && k >= dim[2]) {
                            dp[i][j][k] = Math.max(dp[i][j][k], 
                                dp[i - dim[0]][j][k] + 
                                dp[dim[0]][j - dim[1]][k] + 
                                dp[dim[0]][dim[1]][k - dim[2]] + 
                                PARCEL_VALUES[parcelType]);
                        }
                    }
                }
            }
        }
        return dp[TRUCK_LENGTH][TRUCK_WIDTH][TRUCK_HEIGHT];
    }
}
