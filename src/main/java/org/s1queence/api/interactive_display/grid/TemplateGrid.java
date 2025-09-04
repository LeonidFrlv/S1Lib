package org.s1queence.api.interactive_display.grid;

public class TemplateGrid {
    private final String name;
    private final GridType gridType;
    private final float cellWidth;
    private final float cellHeight;
    private final double xTranslation;
    private final double yTranslation;
    private final double zTranslation;
    private final int[][] grid;
    private final boolean isHitBox;
    private final GridSize gridSize;

    public TemplateGrid(String name, GridType gridType, float cellHeight, double yTranslation, int[][] grid, boolean isHitBox, GridSize gridSize) {
        this.name = name;
        this.gridType = gridType;
        this.cellWidth = gridSize.squareSize;
        this.gridSize = gridSize;
        this.cellHeight = cellHeight;
        this.xTranslation = 0;
        this.yTranslation = yTranslation;
        this.zTranslation = 0;
        this.grid = grid;
        this.isHitBox = isHitBox;
    }

    public TemplateGrid(String name, GridType gridType, float cellWidth, float cellHeight, double yTranslation, boolean isHitBox) {
        this.name = name;
        this.gridType = gridType;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.gridSize = GridSize.X1x1;
        this.xTranslation = 0;
        this.yTranslation = yTranslation;
        this.zTranslation = 0;
        this.grid = new int[][] {{1}};
        this.isHitBox = isHitBox;
    }

    public TemplateGrid(String name, GridType gridType, float cellHeight, double xTranslation, double yTranslation, double zTranslation, int[][] grid, boolean isHitBox, GridSize gridSize) {
        this.name = name;
        this.gridType = gridType;
        this.gridSize = gridSize;
        this.cellWidth = gridSize.squareSize;
        this.cellHeight = cellHeight;
        this.xTranslation = xTranslation;
        this.yTranslation = yTranslation;
        this.zTranslation = zTranslation;
        this.grid = grid;
        this.isHitBox = isHitBox;
    }

    public TemplateGrid(String name, GridType gridType, float cellWidth, float cellHeight, double xTranslation, double yTranslation, double zTranslation, boolean isHitBox) {
        this.name = name;
        this.gridType = gridType;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.xTranslation = xTranslation;
        this.yTranslation = yTranslation;
        this.zTranslation = zTranslation;
        this.gridSize = GridSize.X1x1;
        this.grid = new int[][] {{1}};
        this.isHitBox = isHitBox;
    }

    public String getName() {
        return name;
    }

    public float getCellHeight() {
        return cellHeight;
    }

    public double getXTranslation() {
        return xTranslation;
    }

    public double getYTranslation() {
        return yTranslation;
    }

    public double getZTranslation() {
        return zTranslation;
    }

    public int[][] getGrid() {
        return grid;
    }

    public boolean isHitBox() {
        return isHitBox;
    }

    public GridType getGridType() {
        return gridType;
    }

    public float getCellWidth() {
        return cellWidth;
    }

    public GridSize getGridSize() {
        return gridSize;
    }
}
