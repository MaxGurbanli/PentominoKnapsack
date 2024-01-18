package com.example.ok_fx;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BoxFiller {
    private int boxWidth, boxHeight, boxDepth;
    private char[][][] field;
    private BoxUpdateListener listener;


    public BoxFiller(int boxWidth, int boxHeight, int boxDepth,  BoxUpdateListener listener) {
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.boxDepth = boxDepth;
        this.listener = listener;
        this.field = new char[boxWidth][boxHeight][boxDepth];
        initializeField();
    }

    private void initializeField() {
        for (int x = 0; x < boxWidth; x++) {
            for (int y = 0; y < boxHeight; y++) {
                for (int z = 0; z < boxDepth; z++) {
                    field[x][y][z] = ' ';
                }
            }
        }
    }

    public char[][][] getField() {
        return field;
    }

    private Blocks[] availableBlocks = new Blocks[]{
        new Blocks(charToBlockType('A')),
        new Blocks(charToBlockType('B')),
        new Blocks(charToBlockType('C'))
    };

    public boolean fillBox() {
        // Iterate over each type of block
        for (Blocks block : availableBlocks) {
            // Try placing the block in every possible position
            if (tryPlaceBlock(block)) {
                return true;
            }
        }
    
        // Return false if no solution is found
        return false;
    }
    
    private boolean tryPlaceBlock(Blocks block) {
        for (int x = 0; x < boxWidth; x++) {
            for (int y = 0; y < boxHeight; y++) {
                for (int z = 0; z < boxDepth; z++) {
                    if (canPlaceBlock(x, y, z, block)) {
                        // Place the block
                        placeBlock(block, x, y, z, true);
    
                        // Prune: If the remaining space cannot be filled by the available blocks, remove the block and continue
                        if (!canFillRemainingSpace()) {
                            placeBlock(block, x, y, z, false);
                            continue;
                        }
    
                        // Recursively try to fill the remaining space
                        if (fillBox()) {
                            return true;
                        }
    
                        // Backtrack: Remove the block if no solution is found
                        placeBlock(block, x, y, z, false);
                    }
                }
            }
        }
        return false;
    }

    private boolean canFillRemainingSpace() {
        int remainingSpace = 0;
        for (int x = 0; x < boxWidth; x++) {
            for (int y = 0; y < boxHeight; y++) {
                for (int z = 0; z < boxDepth; z++) {
                    if (field[x][y][z] == ' ') {
                        remainingSpace++;
                    }
                }
            }
        }

        int smallestBlockVolume = 2;
    
        // If the remaining space is not a multiple of the smallest block's volume, return false
        return remainingSpace % smallestBlockVolume == 0;
    }

    private boolean isBoxFull() {
        for (int x = 0; x < boxWidth; x++) {
            for (int y = 0; y < boxHeight; y++) {
                for (int z = 0; z < boxDepth; z++) {
                    if (field[x][y][z] == ' ' ){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    
    private void placeBlock(Blocks block, int x, int y, int z, boolean place) {
        BlockType blockType = block.getBlockType();
        if (place){
            placeBlockInArray(x, y, z, block.getCharacter(), blockType);
        }else{
            removeBlockFromArray(x, y, z, blockType);
        }
        // Implement this method to place or remove a block in the box
    }
    public interface BoxUpdateListener {
        void onBlockUpdated(int x, int y, int z, char blockTypeChar);
    }


    private void placeBlockInArray(int x, int y, int z, char blockTypeChar, BlockType blockType) {
        if (!isInBounds(x, y, z)) return; // Check if starting point is within bounds
    
        for (int dx = 0; dx < blockType.width; dx++) {
            for (int dy = 0; dy < blockType.height; dy++) {
                for (int dz = 0; dz < blockType.depth; dz++) {
                    if (x + dx < boxWidth && y + dy < boxHeight && z + dz < boxDepth) {
                        field[x + dx][y + dy][z + dz] = blockTypeChar;
                    }
                }
            }
        }
        listener.onBlockUpdated(x, y, z, blockTypeChar);
    }
    
    private void removeBlockFromArray(int x, int y, int z, BlockType blockType) {
        if (!isInBounds(x, y, z)) return; // Check if starting point is within bounds
    
        for (int dx = 0; dx < blockType.width; dx++) {
            for (int dy = 0; dy < blockType.height; dy++) {
                for (int dz = 0; dz < blockType.depth; dz++) {
                    if (x + dx < boxWidth && y + dy < boxHeight && z + dz < boxDepth 
                        && field[x + dx][y + dy][z + dz] != ' ') { 
                        field[x + dx][y + dy][z + dz] = ' ';
                        listener.onBlockUpdated(x + dx, y + dy, z + dz, ' '); 
                    }
                }
            }
        }
    }
    
    private boolean isInBounds(int x, int y, int z) {
        return x >= 0 && x < boxWidth && y >= 0 && y < boxHeight && z >= 0 && z < boxDepth;
    }
    
    private boolean canPlaceBlock(int x, int y, int z, Blocks block) {
        if (block == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }
    
        BlockType blockType = block.getBlockType();
        for (int dx = 0; dx < blockType.width; dx++) {
            for (int dy = 0; dy < blockType.height; dy++) {
                for (int dz = 0; dz < blockType.depth; dz++) {
                    if (x + dx >= boxWidth || y + dy >= boxHeight || z + dz >= boxDepth || 
                    field[x + dx][y + dy][z + dz] != ' ') {
                        return false; // Out of bounds or overlapping
                    }
                }
            }
        }
        return true;
    }

    private BlockType charToBlockType(char blockTypeChar) {
        switch (blockTypeChar) {
            case 'A':
                return BlockType.A;
            case 'B':
                return BlockType.B;
            case 'C':
                return BlockType.C;
            default:
                throw new IllegalArgumentException("Unknown block type character: " + blockTypeChar);
        }
    }

}
