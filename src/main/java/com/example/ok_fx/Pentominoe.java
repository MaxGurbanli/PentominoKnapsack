package com.example.ok_fx;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class Pentominoe {

    public static ArrayList<Point3D> getLBoxCoords(){
        ArrayList<Point3D> box_positions = new ArrayList<>();
        Point3D p1 = new Point3D(0,0,0);
        Point3D p2 = new Point3D(1,0,0);
        Point3D p3 = new Point3D(2,0,0);
        Point3D p4 = new Point3D(3,0,0);
        Point3D p5 = new Point3D(3,1,0);
        box_positions.add(p1);
        box_positions.add(p2);
        box_positions.add(p3);
        box_positions.add(p4);
        box_positions.add(p5);
        return box_positions;
    }
    public static ArrayList<Point3D> getPBoxCoords(){
        ArrayList<Point3D> box_positions = new ArrayList<>();
        Point3D p1 = new Point3D(0,0,0);
        Point3D p2 = new Point3D(0,1,0);
        Point3D p3 = new Point3D(1,0,0);
        Point3D p4 = new Point3D(1,1,0);
        Point3D p5 = new Point3D(2,0,0);
        box_positions.add(p1);
        box_positions.add(p2);
        box_positions.add(p3);
        box_positions.add(p4);
        box_positions.add(p5);
        return box_positions;
    }
    public static ArrayList<Point3D> getTBoxCoords(){
        ArrayList<Point3D> box_positions = new ArrayList<>();
        Point3D p1 = new Point3D(0,0,0);
        Point3D p2 = new Point3D(1,0,0);
        Point3D p3 = new Point3D(2,0,0);
        Point3D p4 = new Point3D(1,1,0);
        Point3D p5 = new Point3D(1,2,0);
        box_positions.add(p1);
        box_positions.add(p2);
        box_positions.add(p3);
        box_positions.add(p4);
        box_positions.add(p5);
        return box_positions;
    }
}
