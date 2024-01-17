package com.example.ok_fx;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

enum BlockType {
    
    A(2, 2, 4, Color.RED),
    B(2, 2, 2, Color.GREEN),
    C(3, 3, 3, Color.BLUE);

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
    private BlockType type;
    private static final int BOX_SIZE = 10;

    public Blocks(BlockType type) {
        // Multiply the width, height, and depth by the size of a cell
        super(BOX_SIZE, BOX_SIZE,BOX_SIZE);
        this.type = type;
        this.setMaterial(new PhongMaterial(type.color));
    }
    public int getBlockWidth(){
        return type.width;
    }

}

