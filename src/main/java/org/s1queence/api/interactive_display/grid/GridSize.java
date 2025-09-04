package org.s1queence.api.interactive_display.grid;

public enum GridSize {
    X1x1 (1, 1),
    X2x2 (2, 0.5f),
    X4x4 (4, 0.25f),
    X8x8 (8, 0.125f),
    X16x16 (16, 0.0625f);

    final int size;
    final float squareSize;

    GridSize(int size, float squareSize) {
        this.size = size;
        this.squareSize = squareSize;
    }
}
