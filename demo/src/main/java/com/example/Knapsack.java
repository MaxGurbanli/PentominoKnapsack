package com.example;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Parcel {
    double length;
    double width;
    double height;
    int value;

    public Parcel(double length, double width, double height, int value) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.value = value;
    }
}

public class Knapsack {

    public static void main(String[] args) {
        double cargoLength = 16.5;
        double cargoWidth = 2.5;
        double cargoHeight = 4.0;

        List<Parcel> parcels = new ArrayList<>();
        parcels.add(new Parcel(1.0, 1.0, 2.0, 3));
        parcels.add(new Parcel(1.0, 1.5, 2.0, 4));
        parcels.add(new Parcel(1.5, 1.5, 1.5, 5));

        Collections.sort(parcels, (p1, p2) -> Double.compare(p2.length * p2.width * p2.height, p1.length * p1.width * p1.height));

        int maxValue = FFD(cargoLength, cargoWidth, cargoHeight, parcels);

        System.out.println(maxValue);
    }

    private static int FFD(double cargoLength, double cargoWidth, double cargoHeight, List<Parcel> parcels) {
        int totalValue = 0;
    
        while (cargoLength > 0 && cargoWidth > 0 && cargoHeight > 0) {
            boolean addedParcel = false;
    
            for (Parcel parcel : parcels) {
                if (fitsInCargo(cargoLength, cargoWidth, cargoHeight, parcel)) {
                    totalValue += parcel.value;
                    addedParcel = true;
                    System.out.println("Added parcel with value: " + parcel.value);
                }
            }
    
            if (!addedParcel) {
                
                break;
            }
        }
    
        return totalValue;
    }

    private static boolean fitsInCargo(double cargoLength, double cargoWidth, double cargoHeight, Parcel parcel) {
        double remainingLength = cargoLength - parcel.length;
        double remainingWidth = cargoWidth - parcel.width;
        double remainingHeight = cargoHeight - parcel.height;

        if (remainingLength >= 0 && remainingWidth >= 0 && remainingHeight >= 0) {
            cargoLength = remainingLength;
            cargoWidth = remainingWidth;
            cargoHeight = remainingHeight;
            return true;
        }

        return false;
    }
}