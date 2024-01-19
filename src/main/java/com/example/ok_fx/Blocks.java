package com.example.ok_fx;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

enum BlockType {
    
    A(2, 2, 4, Color.RED),
    B(2, 3, 4, Color.GREEN),
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
        super(type.width * BOX_SIZE, type.height * BOX_SIZE, type.depth * BOX_SIZE);
        this.type = type;
        this.setMaterial(new PhongMaterial(type.color));
    }

    public Blocks(int width, int depth, int height, Color color) {
        super(width * BOX_SIZE, height * BOX_SIZE, depth * BOX_SIZE);
        this.setMaterial(new PhongMaterial(color));
    }

    // public Blocks[] getOrientations() {
    //     if (type.width == type.height && type.height == type.depth) {
    //         return new Blocks[] {this};
    //     }

    //     if (type.width == type.height || type.height == type.depth || type.width == type.depth) {
    //         return new Blocks[] {
    //             this,
    //             new Blocks(type.width, type.depth, type.height, type.color),
    //             new Blocks(type.depth, type.width, type.height, type.color)
    //         };
    //     }

    //     return new Blocks[] {
    //         this,
    //         new Blocks(type.width, type.depth, type.height, type.color),
    //         new Blocks(type.height, type.width, type.depth, type.color),
    //         new Blocks(type.height, type.depth, type.width, type.color),
    //         new Blocks(type.depth, type.width, type.height, type.color),
    //         new Blocks(type.depth, type.height, type.width, type.color)
    //     };
    // }

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
        } else {
            return ' ';
        }
    }

    public Color getColor() {
        // Return the color based on the block type
        // This is a placeholder; replace it with your actual logic
        return switch (type) {
            case A -> Color.RED;
            case B -> Color.GREEN;
            case C -> Color.BLUE;
            default -> Color.TRANSPARENT;
        };
    }
    


}