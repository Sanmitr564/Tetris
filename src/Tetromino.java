public abstract class Tetromino {
    private Rotation rotation;
    private int[][] blocks;
    private int[][] center;
    public Tetromino(int[][] center, int[][] blocks){
        this.center = center;
        this.blocks = blocks;
        rotation = Rotation.up;

    }
    public abstract void rotate(Rotate r);

    public Rotation getRotation() {
        return rotation;
    }

    public int[][] getBlocks() {
        return blocks;
    }

    public int[][] getCenter() {
        return center;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public void setBlocks(int[][] blocks) {
        this.blocks = blocks;
    }

    public void setCenter(int[][] center) {
        this.center = center;
    }
}
